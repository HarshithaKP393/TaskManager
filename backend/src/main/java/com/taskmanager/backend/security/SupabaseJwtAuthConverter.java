package com.taskmanager.backend.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class SupabaseJwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        UUID userId = UUID.fromString(jwt.getSubject());
        String email = jwt.getClaimAsString("email");

        Map<String, Object> userMetadata = jwt.getClaimAsMap("user_metadata");
        String appRole = (userMetadata != null && userMetadata.get("role") != null)
                ? userMetadata.get("role").toString()
                : "USER";

        AuthenticatedUser principal = new AuthenticatedUser(userId, email, appRole);
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + appRole));

        return new SupabaseAuthenticationToken(principal, authorities);
    }
}