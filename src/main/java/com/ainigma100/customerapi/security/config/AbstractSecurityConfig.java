package com.ainigma100.customerapi.security.config;

import com.ainigma100.customerapi.security.converter.AzureRoleConverter;
import com.ainigma100.customerapi.security.handler.CustomAccessDeniedHandler;
import com.ainigma100.customerapi.security.handler.CustomAuthenticationEntryPoint;
import tools.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Abstract base class for security configurations.
 * Contains common security configuration that is shared between different environments.
 */
public abstract class AbstractSecurityConfig {

    protected static final String[] PUBLIC_URLS = {
            "/api/auth/**", "/ui/**",
            "/swagger-ui-custom.html", "/swagger-ui.html",
            "/swagger-ui/**", "/v3/api-docs/**", "/webjars/**",
            "/swagger-ui/index.html", "/swagger-resources/**", "/api-docs/**",
            "/error"
    };

    // Accessor so profiles can safely extend PUBLIC_URLS (prod uses defaults).
    protected String[] publicUrls() {
        return PUBLIC_URLS;
    }

    /**
     * Configures the security filter chain with common settings.
     *
     * @param http the HttpSecurity to configure
     * @param objectMapper the Jackson ObjectMapper used by the error handlers at the end of the method
     * @return the configured SecurityFilterChain
     */
    protected SecurityFilterChain configureCommonSecurity(HttpSecurity http, ObjectMapper objectMapper) {

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new AzureRoleConverter());

        String[] permittedUrls = publicUrls();

        http
                // Enable CORS using the corsConfigurationSource bean defined below
                .cors(Customizer.withDefaults())

                // Disable CSRF for REST APIs
                // This is acceptable for stateless REST APIs using JWT tokens
                .csrf(csrf -> csrf.disable())

                // Configure authorization rules
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(permittedUrls).permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // Allow preflight requests
                        // All authenticated requests must have either Admin or User role
                        .anyRequest().hasAnyRole("ADMIN", "USER")
                )

                // Configure JWT authentication
                .oauth2ResourceServer(rsc -> rsc.jwt(jwtConfigurer ->
                        jwtConfigurer.jwtAuthenticationConverter(jwtAuthenticationConverter)))

                // Configure session management (stateless for REST APIs)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Configure security headers
                .headers(headers -> headers
                        .contentSecurityPolicy(csp -> csp
                                .policyDirectives("default-src 'self'; frame-ancestors 'self'"))
                        .frameOptions(frame -> frame.sameOrigin())
                        .referrerPolicy(referrer -> referrer
                                .policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN))
                        .xssProtection(xss -> xss.disable()) // Modern browsers use CSP instead
                );

        // Configure exception handling
        http.exceptionHandling(ex -> ex
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint(objectMapper)) // Handle 401
                .accessDeniedHandler(new CustomAccessDeniedHandler(objectMapper))           // Handle 403
        );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(
            @Value("${app.cors.allowed-origins:}") List<String> allowedOrigins) {

        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(allowedOrigins);
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}