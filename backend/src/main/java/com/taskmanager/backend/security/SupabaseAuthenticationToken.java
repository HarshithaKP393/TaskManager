package com.taskmanager.backend.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class SupabaseAuthenticationToken extends AbstractAuthenticationToken {

    private final AuthenticatedUser principal;

    public SupabaseAuthenticationToken(AuthenticatedUser principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}