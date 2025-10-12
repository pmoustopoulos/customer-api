package com.ainigma100.customerapi.security.config;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

/**
 * Development/Test security configuration using a mocked JWT token.
 *
 * This configuration is only active in non-production profiles and provides a
 * fake JwtDecoder to simplify local development and tests without a real Identity Provider,
 * for example Azure AD, Auth0, or Keycloak.
 */
@Profile({"!prod"})
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityDevMockConfig extends AbstractSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return configureCommonSecurity(http);
    }

    @Override
    protected String[] publicUrls() {
        return ArrayUtils.add(PUBLIC_URLS, "/h2-console/**");
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return token -> Jwt.withTokenValue(token)
                .header("alg", "none")
                .claim("sub", "test-user")
                .claim("preferred_username", "test.ext@ainigma100.com")
                .claim("roles", getRoles(token))
                .build();
    }

    private List<String> getRoles(String token) {
        if (token.contains("admin-token")) {
            return List.of("Admin");
        } else if (token.contains("user-token")) {
            return List.of("User");
        }
        return List.of();
    }
}
