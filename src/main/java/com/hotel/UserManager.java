package com.hotel.manager;

import com.hotel.model.User;
import com.hotel.model.UserRole;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Manages all users in the hotel system.
 * Handles user authentication, registration, and CRUD operations.
 * Maintains indexes for efficient user lookup by ID and email.
 *
 * @author dev_Amru
 * @version 1.0
 */
public class UserManager {
    // Primary storage for all users, keyed by user ID
    private Map<String, User> users;
    
    // Secondary index for email lookup
    // Maps email addresses to user objects for quick authentication
    private Map<String, User> emailIndex;
    
    // Counter for generating unique user IDs
    // Starts at 1000 and increments for each new user
    private int userCounter;

    /**
     * Constructs a new UserManager.
     */
    public UserManager() {
        this.users = new HashMap<>();
        this.emailIndex = new HashMap<>();
        this.userCounter = 1000;
    }

    /**
     * Generates a unique user ID.
     *
     * @param prefix prefix for the ID (e.g., "G" for guest, "A" for admin)
     * @return new user ID
     */
    public String generateUserId(String prefix) {
        return prefix + (userCounter++);
    }

    /**
     * Adds a user to the system.
     *
     * @param user user to add
     * @throws IllegalArgumentException if user is null or email already exists
     */
    public void addUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (users.containsKey(user.getUserId())) {
            throw new IllegalArgumentException("User ID already exists: " + user.getUserId());
        }
        if (emailIndex.containsKey(user.getEmail().toLowerCase())) {
            throw new IllegalArgumentException("Email already registered: " + user.getEmail());
        }

        users.put(user.getUserId(), user);
        emailIndex.put(user.getEmail().toLowerCase(), user);
    }

    /**
     * Gets a user by ID.
     *
     * @param userId user ID
     * @return the user or null if not found
     */
    public User getUser(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
        return users.get(userId);
    }

    /**
     * Gets a user by email.
     *
     * @param email user email
     * @return the user or null if not found
     */
    public User getUserByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        return emailIndex.get(email.toLowerCase());
    }

    /**
     * Authenticates a user with email and password.
     *
     * @param email user's email
     * @param password user's password
     * @return authenticated user or null if credentials invalid
     */
    public User authenticateUser(String email, String password) {
        if (email == null || password == null) {
            return null;
        }

        User user = getUserByEmail(email);
        if (user != null && user.validatePassword(password)) {
            return user;
        }
        return null;
    }

    /**
     * Updates user information.
     *
     * @param user user with updated information
     * @throws IllegalArgumentException if user not found
     */
    public void updateUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (!users.containsKey(user.getUserId())) {
            throw new IllegalArgumentException("User not found: " + user.getUserId());
        }

        User existingUser = users.get(user.getUserId());

        // Update email index if email changed
        if (!existingUser.getEmail().equalsIgnoreCase(user.getEmail())) {
            emailIndex.remove(existingUser.getEmail().toLowerCase());

            if (emailIndex.containsKey(user.getEmail().toLowerCase())) {
                throw new IllegalArgumentException("Email already in use: " + user.getEmail());
            }
            emailIndex.put(user.getEmail().toLowerCase(), user);
        }

        users.put(user.getUserId(), user);
    }

    /**
     * Deletes a user from the system.
     *
     * @param userId user ID to delete
     * @return true if user was deleted
     */
    public boolean deleteUser(String userId) {
        User user = users.remove(userId);
        if (user != null) {
            emailIndex.remove(user.getEmail().toLowerCase());
            return true;
        }
        return false;
    }

    /**
     * Gets all users in the system.
     *
     * @return list of all users
     */
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    /**
     * Gets users by role.
     *
     * @param role user role to filter by
     * @return list of users with the specified role
     */
    public List<User> getUsersByRole(UserRole role) {
        if (role == null) {
            throw new IllegalArgumentException("Role cannot be null");
        }
        return users.values().stream()
                .filter(user -> user.getRole() == role)
                .collect(Collectors.toList());
    }

    /**
     * Searches users by name (case-insensitive partial match).
     *
     * @param searchTerm search term
     * @return list of matching users
     */
    public List<User> searchUsersByName(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return new ArrayList<>();
        }

        String lowerSearch = searchTerm.toLowerCase();
        return users.values().stream()
                .filter(user -> user.getName().toLowerCase().contains(lowerSearch))
                .collect(Collectors.toList());
    }

    /**
     * Gets total user count.
     *
     * @return number of users
     */
    public int getTotalUserCount() {
        return users.size();
    }

    /**
     * Checks if email is already registered.
     *
     * @param email email to check
     * @return true if email exists
     */
    public boolean emailExists(String email) {
        if (email == null) {
            return false;
        }
        return emailIndex.containsKey(email.toLowerCase());
    }
}


