package com.library;

import java.io.Serializable;

/**
 * Represents a Library Administrator.
 */
public class Admin implements Serializable {
    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
    private String name;

    public Admin(String username, String password, String name) {
        this.username = username;
        this.password = password;
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Verifies if the provided password matches the admin's password.
     */
    public boolean verifyPassword(String password) {
        return this.password.equals(password);
    }

    @Override
    public String toString() {
        return String.format("Admin [Username: %s, Name: %s]", username, name);
    }
}
