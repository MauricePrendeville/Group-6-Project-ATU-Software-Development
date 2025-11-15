package com.hotel;

/**
 * Represents an administrator user in the hotel system.
 * Minimal implementation providing admin permissions.
 *
 * @author dev_amru
 * @version 1.0
 */
public class Admin extends com.hotel.User {
	public Admin(String userId, String name, String email, String phone, String password) {
		super(userId, name, email, phone, password, com.hotel.UserRole.ADMIN);
	}

	@Override
	public String getPermissions() {
		return "Admin: Full system access";
	}

	@Override
	public boolean hasPermission(String permission) {
		// Admin has all permissions
		return true;
	}
}
