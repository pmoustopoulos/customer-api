package com.ainigma100.customerapi.security.config;

import com.ainigma100.customerapi.security.converter.AzureRoleConverter;
import com.ainigma100.customerapi.security.handler.CustomAccessDeniedHandler;
import com.ainigma100.customerapi.security.handler.CustomAuthenticationEntryPoint;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;

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
     * @return the configured SecurityFilterChain
     * @throws Exception if an error occurs during configuration
     */
    protected SecurityFilterChain configureCommonSecurity(HttpSecurity http) throws Exception {
        
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new AzureRoleConverter());

        String[] permittedUrls = publicUrls();

        http
                // Configure CORS with default settings
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
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint()) // Handle 401
                .accessDeniedHandler(new CustomAccessDeniedHandler())           // Handle 403
        );

        return http.build();
    }
}