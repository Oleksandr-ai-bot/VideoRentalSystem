package com.videorental.model.user;

import com.videorental.common.Identifiable;

public abstract class User implements Identifiable {
    protected final String id;
    protected String name;
    protected String email;

    public User(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public abstract UserRole getRole();

    @Override
    public String toString() {
        return String.format("[%s] %s (%s) | %s", id, name, email, getRole());
    }
}
