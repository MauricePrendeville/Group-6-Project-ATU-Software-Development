package com.hotel.Service;

import com.hotel.Model.User;
import com.hotel.Model.UserRole;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Manages all users in the hotel system.
 * Handles user authentication, registration, and CRUD operations.
 * Maintains indexes for efficient user lookup by ID and email.
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
     * Generate a unique user ID by concatenating the provided prefix with
     * an internal incrementing counter.
     *
     * @param prefix prefix to prepend to the generated id (may be null)
     * @return newly generated unique user id string
     */
    public String generateUserId(String prefix) {
        return prefix + (userCounter++);
    }

    /**
     * Register a new user in the system.
     *
     * This method validates that the provided user is non-null and that
     * the user's id and email are not already present. The email is stored
     * in a case-insensitive index.
     *
     * @param user user to add (must be non-null)
     * @throws IllegalArgumentException when user is null, when the user id
     *         already exists, or when the email is already registered
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
     * Retrieve a user by their unique id.
     *
     * @param userId id of the user to look up (must be non-null, non-empty)
     * @return the User if found, or {@code null} if no user with the id exists
     * @throws IllegalArgumentException when {@code userId} is null or empty
     */
    public User getUser(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
        return users.get(userId);
    }

    /**
     * Retrieve a user by email address (case-insensitive).
     *
     * @param email email address to look up (must be non-null, non-empty)
     * @return the User if found, or {@code null} when not found
     * @throws IllegalArgumentException when {@code email} is null or empty
     */
    public User getUserByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        return emailIndex.get(email.toLowerCase());
    }

    /**
     * Authenticate a user by their email and password.
     *
     * @param email    user's email (case-insensitive)
     * @param password plaintext password to validate
     * @return the authenticated User when credentials match, otherwise {@code null}
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
     * Update an existing user's information.
     *
     * If the user's email changes the email index is updated accordingly
     * and the new email must not already be in use by another user.
     *
     * @param user updated user object (must be non-null and exist in the system)
     * @throws IllegalArgumentException when {@code user} is null, not found,
     *         or the new email is already used by another user
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
     * Delete a user by id.
     *
     * @param userId id of the user to remove
     * @return {@code true} when a user was removed, {@code false} when no user
     *         with the given id exists
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
     * Return a list containing all registered users.
     *
     * The returned list is a shallow copy and modifications to it do not
     * affect the internal storage.
     *
     * @return modifiable list of all users
     */
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    /**
     * Retrieve users who have the specified role.
     *
     * @param role role to filter by (must be non-null)
     * @return list of users matching the role
     * @throws IllegalArgumentException when {@code role} is null
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
     * Search users by name using a case-insensitive partial match.
     *
     * @param searchTerm substring to match against user names
     * @return list of users whose names contain the search term; empty list
     *         when {@code searchTerm} is null or empty
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
     * Return the total number of registered users.
     *
     * @return number of users currently stored
     */
    public int getTotalUserCount() {
        return users.size();
    }

    /**
     * Check whether an email address is already registered (case-insensitive).
     *
     * @param email email to check
     * @return {@code true} if the email exists in the index; {@code false}
     *         when {@code email} is null or not present
     */
    public boolean emailExists(String email) {
        if (email == null) {
            return false;
        }
        return emailIndex.containsKey(email.toLowerCase());
    }
}



