package com.ainigma100.customerapi.security.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Converter that extracts roles from a JWT token and converts them to Spring Security GrantedAuthorities.
 * Specifically designed to work with Azure AD JWT tokens.
 */
public class AzureRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Object rolesObject = jwt.getClaims().get("roles");

        if (!(rolesObject instanceof List<?> roles)) {
            return new ArrayList<>();
        }

        return roles.stream()
                .filter(role -> role instanceof String)
                .map(role -> "ROLE_" + ((String) role).toUpperCase())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}