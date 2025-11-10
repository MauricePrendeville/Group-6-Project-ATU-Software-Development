package com.hotel;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;


class ManagerTest {

    private Manager manager;

    @BeforeEach
    void setUp() {
        manager = new Manager("M001", "Jane Manager", "jane@hotel.com",
                "0871234567", "pass123", "Operations");
    }

    @Test
    @DisplayName("Constructor should create valid manager")
    void testConstructorValid() {
        assertNotNull(manager);
        assertEquals("M001", manager.getUserId());
        assertEquals("Jane Manager", manager.getName());
        assertEquals("Operations", manager.getDepartment());
        assertEquals(UserRole.MANAGER, manager.getRole());
    }

    @Test
    @DisplayName("Constructor should throw exception for null department")
    void testConstructorNullDepartment() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Manager("M002", "Test Manager", "test@hotel.com",
                    "0870000000", "pass", null);
        });
    }

    @Test
    @DisplayName("Get department should return correct department")
    void testGetDepartment() {
        assertEquals("Operations", manager.getDepartment());
    }

    @Test
    @DisplayName("Set department should update department")
    void testSetDepartment() {
        manager.setDepartment("Finance");
        assertEquals("Finance", manager.getDepartment());
    }

    @Test
    @DisplayName("Set null department should throw exception")
    void testSetNullDepartment() {
        assertThrows(IllegalArgumentException.class, () -> {
            manager.setDepartment(null);
        });
    }

    @Test
    @DisplayName("Manager should have reporting permissions")
    void testManagerReportingPermissions() {
        assertTrue(manager.hasPermission("GENERATE_REPORTS"));
        assertTrue(manager.hasPermission("VIEW_ANALYTICS"));
    }

    @Test
    @DisplayName("Manager should have view permissions")
    void testManagerViewPermissions() {
        assertTrue(manager.hasPermission("VIEW_BOOKINGS"));
        assertTrue(manager.hasPermission("VIEW_ROOMS"));
        assertTrue(manager.hasPermission("VIEW_GUESTS"));
        assertTrue(manager.hasPermission("VIEW_STAFF"));
    }

    @Test
    @DisplayName("Manager should not have modification permissions")
    void testManagerNoModificationPermissions() {
        assertFalse(manager.hasPermission("CREATE_BOOKING"));
        assertFalse(manager.hasPermission("DELETE_USER"));
        assertFalse(manager.hasPermission("MODIFY_BOOKING"));
    }

    @Test
    @DisplayName("Get permissions should describe manager role")
    void testGetPermissions() {
        String permissions = manager.getPermissions();
        assertTrue(permissions.contains("Manager"));
        assertTrue(permissions.contains("report") || permissions.contains("Report"));
    }
}
