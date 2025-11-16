package com.hotel;

import com.hotel.Model.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class CleanerTest {

    private Cleaner cleaner;

    @BeforeEach
    void setUp() {
        cleaner = new Cleaner("C001", "Mary Cleaner", "mary@hotel.com",
                "0871234567", "pass123");
    }

    @Test
    @DisplayName("Constructor should create valid cleaner")
    void testConstructorValid() {
        assertNotNull(cleaner);
        assertEquals("C001", cleaner.getUserId());
        assertEquals("Mary Cleaner", cleaner.getName());
        assertEquals(UserRole.CLEANER, cleaner.getRole());
    }

    @Test
    @DisplayName("New cleaner should have no assigned rooms")
    void testNewCleanerNoRooms() {
        assertTrue(cleaner.getAssignedRooms().isEmpty());
    }

    @Test
    @DisplayName("Assign room should add to list")
    void testAssignRoom() {
        cleaner.assignRoom("101");

        assertEquals(1, cleaner.getAssignedRooms().size());
        assertTrue(cleaner.getAssignedRooms().contains("101"));
    }

    @Test
    @DisplayName("Assign multiple rooms should work")
    void testAssignMultipleRooms() {
        cleaner.assignRoom("101");
        cleaner.assignRoom("102");
        cleaner.assignRoom("103");

        assertEquals(3, cleaner.getAssignedRooms().size());
    }

    @Test
    @DisplayName("Assign duplicate room should not create duplicate")
    void testAssignDuplicateRoom() {
        cleaner.assignRoom("101");
        cleaner.assignRoom("101");

        assertEquals(1, cleaner.getAssignedRooms().size());
    }

    @Test
    @DisplayName("Assign null room should throw exception")
    void testAssignNullRoom() {
        assertThrows(IllegalArgumentException.class, () -> {
            cleaner.assignRoom(null);
        });
    }

    @Test
    @DisplayName("Remove assigned room should remove from list")
    void testRemoveAssignedRoom() {
        cleaner.assignRoom("101");
        cleaner.assignRoom("102");

        cleaner.removeAssignedRoom("101");

        assertEquals(1, cleaner.getAssignedRooms().size());
        assertFalse(cleaner.getAssignedRooms().contains("101"));
        assertTrue(cleaner.getAssignedRooms().contains("102"));
    }

    @Test
    @DisplayName("Is assigned to room should return correct value")
    void testIsAssignedToRoom() {
        cleaner.assignRoom("101");

        assertTrue(cleaner.isAssignedToRoom("101"));
        assertFalse(cleaner.isAssignedToRoom("102"));
    }

    @Test
    @DisplayName("Cleaner should have room status permissions")
    void testCleanerRoomPermissions() {
        assertTrue(cleaner.hasPermission("VIEW_ASSIGNED_ROOMS"));
        assertTrue(cleaner.hasPermission("UPDATE_ROOM_STATUS"));
    }

    @Test
    @DisplayName("Cleaner should not have booking permissions")
    void testCleanerNoBookingPermissions() {
        assertFalse(cleaner.hasPermission("CREATE_BOOKING"));
        assertFalse(cleaner.hasPermission("VIEW_BOOKINGS"));
        assertFalse(cleaner.hasPermission("GENERATE_REPORTS"));
    }

    @Test
    @DisplayName("Get permissions should describe cleaner role")
    void testGetPermissions() {
        String permissions = cleaner.getPermissions();
        assertTrue(permissions.contains("Cleaner"));
    }

    @Test
    @DisplayName("Get assigned rooms should return defensive copy")
    void testGetAssignedRoomsDefensiveCopy() {
        cleaner.assignRoom("101");

        var rooms = cleaner.getAssignedRooms();
        rooms.add("999");

        assertEquals(1, cleaner.getAssignedRooms().size());
        assertFalse(cleaner.getAssignedRooms().contains("999"));
    }
}
