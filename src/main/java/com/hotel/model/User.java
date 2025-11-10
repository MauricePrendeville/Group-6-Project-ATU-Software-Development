package com.hotel;

import java.util.Objects;

/**
 * Abstract base class representing a user in the hotel booking system.
 * This class provides common attributes and methods for all user types.
 * @author dev_amru
 * @version 1.0
 */
public abstract class User {
    private String userId;
    private String name;
    private String email;
    private String phone;
    private String password;
    private UserRole role;

    public User(String userId, String name, String email, String phone,
                String password, UserRole role) {
        validateInput(userId, "User ID");
        validateInput(name, "Name");
        validateEmail(email);
        validateInput(phone, "Phone");
        validateInput(password, "Password");

        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.role = role;
    }

    /**
     * Validates that a string input is not null or empty.
     */
    protected void validateInput(String input, String fieldName) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or empty");
        }
    }

    /**
     Validates email format.
     */
    protected void validateEmail(String email) {
        validateInput(email, "Email");
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }

    /**
     Abstract method to get user permissions.
     Each user type implements their specific permissions.
   */
    public abstract String getPermissions();

    /**
     * Checks if user has a specific permission.
     */
    public abstract boolean hasPermission(String permission);

    // Getters
    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public UserRole getRole() { return role; }

    /**
     * Validates password for authentication.
     */
    public boolean validatePassword(String password) {
        return this.password.equals(password);
    }

    // Setters
    public void setName(String name) {
        validateInput(name, "Name");
        this.name = name;
    }

    public void setEmail(String email) {
        validateEmail(email);
        this.email = email;
    }

    public void setPhone(String phone) {
        validateInput(phone, "Phone");
        this.phone = phone;
    }

    public void setPassword(String password) {
        validateInput(password, "Password");
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return userId.equals(user.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                '}';
    }
}
