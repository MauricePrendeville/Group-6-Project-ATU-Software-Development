package com.hotel;

import com.hotel.Model.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;


class ReceptionStaffTest {

    private ReceptionStaff reception;

    @BeforeEach
    void setUp() {
        reception = new ReceptionStaff("R001", "John Reception", "john@hotel.com",
                "0871234567", "pass123");
    }

    @Test
    @DisplayName("Constructor should create valid reception staff")
    void testConstructorValid() {
        assertNotNull(reception);
        assertEquals("R001", reception.getUserId());
        assertEquals("John Reception", reception.getName());
        assertEquals(UserRole.RECEPTION_STAFF, reception.getRole());
    }

    @Test
    @DisplayName("New reception staff should have empty shift list")
    void testNewReceptionEmptyShifts() {
        assertTrue(reception.getAssignedShifts().isEmpty());
    }

    @Test
    @DisplayName("Assign shift should add to list")
    void testAssignShift() {
        reception.assignShift("Morning");

        assertEquals(1, reception.getAssignedShifts().size());
        assertTrue(reception.getAssignedShifts().contains("Morning"));
    }

    @Test
    @DisplayName("Assign multiple shifts should work")
    void testAssignMultipleShifts() {
        reception.assignShift("Morning");
        reception.assignShift("Evening");
        reception.assignShift("Night");

        assertEquals(3, reception.getAssignedShifts().size());
    }

    @Test
    @DisplayName("Assign duplicate shift should not create duplicate")
    void testAssignDuplicateShift() {
        reception.assignShift("Morning");
        reception.assignShift("Morning");

        assertEquals(1, reception.getAssignedShifts().size());
    }

    @Test
    @DisplayName("Assign null shift should throw exception")
    void testAssignNullShift() {
        assertThrows(IllegalArgumentException.class, () -> {
            reception.assignShift(null);
        });
    }

    @Test
    @DisplayName("Reception should have booking management permissions")
    void testReceptionBookingPermissions() {
        assertTrue(reception.hasPermission("VIEW_BOOKINGS"));
        assertTrue(reception.hasPermission("CREATE_BOOKING"));
        assertTrue(reception.hasPermission("MODIFY_BOOKING"));
        assertTrue(reception.hasPermission("CANCEL_BOOKING"));
    }

    @Test
    @DisplayName("Reception should have guest management permissions")
    void testReceptionGuestPermissions() {
        assertTrue(reception.hasPermission("VIEW_GUESTS"));
        assertTrue(reception.hasPermission("CHECK_IN_GUEST"));
        assertTrue(reception.hasPermission("CHECK_OUT_GUEST"));
    }

    @Test
    @DisplayName("Reception should not have admin permissions")
    void testReceptionNoAdminPermissions() {
        assertFalse(reception.hasPermission("DELETE_USER"));
        assertFalse(reception.hasPermission("GENERATE_REPORTS"));
    }

    @Test
    @DisplayName("Get permissions should describe reception role")
    void testGetPermissions() {
        String permissions = reception.getPermissions();
        assertTrue(permissions.contains("Reception"));
    }
}
