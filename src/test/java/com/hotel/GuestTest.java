package com.hotel;

import com.hotel.Model.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class GuestTest {

    private Guest guest;

    @BeforeEach
    void setUp() {
        guest = new Guest("G001", "Alice Guest", "alice@hotel.com",
                "0871234567", "pass123");
    }

    @Test
    @DisplayName("Constructor should create valid guest")
    void testConstructorValid() {
        assertNotNull(guest);
        assertEquals("G001", guest.getUserId());
        assertEquals("Alice Guest", guest.getName());
        assertEquals(UserRole.GUEST, guest.getRole());
    }

    @Test
    @DisplayName("New guest should have empty booking history")
    void testNewGuestEmptyHistory() {
        assertTrue(guest.getBookingHistory().isEmpty());
    }

    @Test
    @DisplayName("Add booking should add to history")
    void testAddBooking() {
        guest.addBooking("B001");

        assertEquals(1, guest.getBookingHistory().size());
        assertTrue(guest.getBookingHistory().contains("B001"));
    }

    @Test
    @DisplayName("Guest should have view bookings permission")
    void testGuestViewPermission() {
        assertTrue(guest.hasPermission("VIEW_BOOKINGS"));
        assertTrue(guest.hasPermission("REQUEST_SUPPORT"));
    }

    @Test
    @DisplayName("Guest should not have admin permissions")
    void testGuestNoAdminPermissions() {
        assertFalse(guest.hasPermission("DELETE_USER"));
        assertFalse(guest.hasPermission("CREATE_BOOKING"));
    }

    @Test
    @DisplayName("Set payment method should work")
    void testSetPaymentMethod() {
        guest.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        assertEquals(PaymentMethod.CREDIT_CARD, guest.getPaymentMethod());
    }
}