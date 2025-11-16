package com.hotel;

import com.hotel.Model.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class AdminTest {

    private Admin admin;

    @BeforeEach
    void setUp() {
        admin = new Admin("A001", "Admin User", "admin@hotel.com",
                "0871234567", "admin123");
    }

    @Test
    @DisplayName("Constructor should create valid admin")
    void testConstructorValid() {
        assertNotNull(admin);
        assertEquals("A001", admin.getUserId());
        assertEquals("Admin User", admin.getName());
        assertEquals(UserRole.ADMIN, admin.getRole());
    }

    @Test
    @DisplayName("Admin should have all permissions")
    void testAdminHasAllPermissions() {
        assertTrue(admin.hasPermission("DELETE_USER"));
        assertTrue(admin.hasPermission("CREATE_BOOKING"));
        assertTrue(admin.hasPermission("GENERATE_REPORTS"));
        assertTrue(admin.hasPermission("MANAGE_SYSTEM"));
        assertTrue(admin.hasPermission("ANY_PERMISSION"));
    }

    @Test
    @DisplayName("Admin permissions description should indicate full access")
    void testAdminPermissionsDescription() {
        String permissions = admin.getPermissions();
        assertTrue(permissions.contains("Admin"));
        assertTrue(permissions.contains("Full") || permissions.contains("all"));
    }

    @Test
    @DisplayName("Admin should validate password correctly")
    void testAdminPasswordValidation() {
        assertTrue(admin.validatePassword("admin123"));
        assertFalse(admin.validatePassword("wrongpassword"));
    }

    @Test
    @DisplayName("ToString should contain admin information")
    void testToString() {
        String str = admin.toString();
        assertTrue(str.contains("Admin"));
        assertTrue(str.contains("A001"));
    }
}
