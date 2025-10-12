package com.ainigma100.customerapi.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration for production environments.
 * Uses actual JWT tokens from Azure AD for authentication.
 * 
 * This configuration is active for all profiles except local, h2, and dev.
 */
@Profile("prod")
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityProdConfig extends AbstractSecurityConfig {

    /**
     * Configures the security filter chain for production environments.
     * Delegates to the common security configuration in the abstract base class.
     * 
     * @param http the HttpSecurity to configure
     * @return the configured SecurityFilterChain
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return configureCommonSecurity(http);
    }
}