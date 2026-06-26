package com.library;

import java.util.List;

/**
 * Manages login state, authentication operations, and session persistence.
 */
public class Authentication {
    private User currentUser;
    private Admin currentAdmin;

    public Authentication() {
        this.currentUser = null;
        this.currentAdmin = null;
    }

    /**
     * Authenticates an administrator against the system's list.
     */
    public boolean loginAdmin(String username, String password, List<Admin> admins) {
        for (Admin admin : admins) {
            if (admin.getUsername().equalsIgnoreCase(username) && admin.verifyPassword(password)) {
                this.currentAdmin = admin;
                this.currentUser = null; // Clear any existing user session
                return true;
            }
        }
        return false;
    }

    /**
     * Authenticates a user against the system's list.
     */
    public boolean loginUser(String username, String password, List<User> users) {
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username) && user.verifyPassword(password)) {
                this.currentUser = user;
                this.currentAdmin = null; // Clear any existing admin session
                return true;
            }
        }
        return false;
    }

    /**
     * Clear the current session.
     */
    public void logout() {
        this.currentUser = null;
        this.currentAdmin = null;
    }

    public boolean isUserLoggedIn() {
        return currentUser != null;
    }

    public boolean isAdminLoggedIn() {
        return currentAdmin != null;
    }

    public boolean isLoggedIn() {
        return isUserLoggedIn() || isAdminLoggedIn();
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public Admin getCurrentAdmin() {
        return currentAdmin;
    }
}
