package com.ainigma100.customerapi.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import tools.jackson.databind.ObjectMapper;

/**
 * Security configuration for production environments.
 * Uses actual JWT tokens from Azure AD for authentication.
 *
 * This configuration is fail-closed and acts as the default: it is active for every
 * profile except the explicitly listed development/test profiles
 * ({@code dev}, {@code local}, {@code test}, {@code testcontainers}), which instead use
 * the mocked {@link SecurityDevMockConfig}. As a result, any unknown or missing profile
 * gets real security rather than the mock decoder.
 */
@Profile("!dev & !local & !test & !testcontainers")
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityProdConfig extends AbstractSecurityConfig {

    /**
     * Configures the security filter chain for production environments.
     * Delegates to the common security configuration in the abstract base class.
     *
     * @param http the HttpSecurity to configure
     * @param objectMapper the Jackson ObjectMapper used by the error handlers
     * @return the configured SecurityFilterChain
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, ObjectMapper objectMapper) {
        return configureCommonSecurity(http, objectMapper);
    }
}