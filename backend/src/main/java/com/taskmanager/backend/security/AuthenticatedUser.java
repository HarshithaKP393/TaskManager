package com.taskmanager.backend.security;

import java.util.UUID;

public class AuthenticatedUser {

    private final UUID id;
    private final String email;
    private final String role;

    public AuthenticatedUser(UUID id, String email, String role) {
        this.id = id;
        this.email = email;
        this.role = role;
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}