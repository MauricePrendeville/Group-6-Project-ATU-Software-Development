package com.hotel;

import com.hotel.Model.*;
import com.hotel.Service.*;
import com.hotel.UI.HotelManagementUI;
import org.junit.jupiter.api.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Robust Test Suite for HotelManagementUI.
 * Features automatic "Safety Exit" padding to prevent Scanner hangs.
 */
@Timeout(5) // Fails any test that takes longer than 5 seconds
class HotelManagementUITest {

    private HotelManagementUI ui;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final java.io.InputStream originalIn = System.in;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setIn(originalIn);
        outputStream.reset();
    }

    /**
     * Helper to create input streams.
     * AUTOMATICALLY APPENDS '0's to force menu exits and prevent hangs.
     */
    private void setInput(String... lines) {
        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            sb.append(line).append(System.lineSeparator());
        }
        // Append 10 "Exit/Back" commands to force the UI to close
        // regardless of how deep in sub-menus the logic gets.
        sb.append("0\n0\n0\n0\n0\n0\n0\n0\n0\n0\n");
        System.setIn(new ByteArrayInputStream(sb.toString().getBytes()));
    }

    private String getOutput() {
        return outputStream.toString();
    }

    @Test
    @DisplayName("Root: Start and immediate exit")
    void testStartAndExit() {
        setInput("0"); // Select Exit from Login Menu
        ui = new HotelManagementUI();
        ui.start();
        assertTrue(getOutput().contains("Thank you"));
    }

    @Test
    @DisplayName("Admin: Full User Management Flow")
    void testAdminUserFlow() {
        setInput(
                "1", "admin@hotel.com", "admin123", // Login
                "1", // User Management
                "2", "Test Rec", "rec@test.com", "123", "pass", "2", // Add Reception
                "1", // User Management
                "3", "Rec", // Search 'Rec'
                "1", // User Management
                "4", "2",   // View by Role (Reception)
                "1", // User Management
                "1"         // View All
        );
        ui = new HotelManagementUI();
        ui.start();

        assertTrue(getOutput().contains("User created"));
        assertTrue(getOutput().contains("Test Rec"));
    }

    @Test
    @DisplayName("Reception: Booking Flow with New Guest")
    void testBookingFlow() {
        String arr = LocalDate.now().plusDays(1).toString();
        String dep = LocalDate.now().plusDays(3).toString();

        setInput(
                "1", "reception@hotel.com", "reception123", // Login
                "1", // Create Booking
                "new", // New Guest
                "New Guy", "new@guy.com", "555", "pass",
                arr, dep,
                "1", // Single Room
                "yes" // Confirm
        );
        ui = new HotelManagementUI();
        ui.start();

        assertTrue(getOutput().contains("Booking confirmed"));
    }

    @Test
    @DisplayName("Reception: Checkout with Facilities (Complex Flow)")
    void testCheckoutWithFacilities() {
        // Simulating the "Walk-in" checkout logic provided in your UI class
        setInput(
                "1", "reception@hotel.com", "reception123", // Login
                "4", // Checkout & Payment
                "guest@hotel.com", // Existing Guest Email
                "101", // Room
                "2", // Nights
                "100", // Price
                "yes", // Add facilities?
                "1", "2", // Spa (Qty 2)
                "2", "1", // Gym (Qty 1)
                "3", "5", // Pool (Qty 5)
                "0",      // Done adding facilities
                "1"       // Pay with Cash
        );
        ui = new HotelManagementUI();
        ui.start();

        String out = getOutput();
        assertTrue(out.contains("PAYMENT DETAILS"));
        assertTrue(out.contains("Spa"));
        assertTrue(out.contains("Pool"));
    }

    @Test
    @DisplayName("Manager: Reports and Analytics")
    void testManagerReports() {
        setInput(
                "1", "manager@hotel.com", "manager123", // Login
                "5", // Reports
                "1", // Occupancy
                "5", // Reports
                "2", // Revenue
                "5", // Reports
                "3", // Guest Stats
                "6"  // Analytics
        );
        ui = new HotelManagementUI();
        ui.start();

        assertTrue(getOutput().contains("OCCUPANCY REPORT"));
        assertTrue(getOutput().contains("REVENUE REPORT"));
    }

    @Test
    @DisplayName("Guest: Registration failure and Login")
    void testGuestRegisterAndLogin() {
        setInput(
                "2", // Register
                "Guest Two", "guest@hotel.com", "123", "pass", // Fail: Email exists [cite: 1]
                "2", // Register again
                "Guest Two", "guest2@hotel.com", "123", "pass", // Success
                "1", "guest2@hotel.com", "pass", // Login
                "5" // View Profile
        );
        ui = new HotelManagementUI();
        ui.start();

        assertTrue(getOutput().contains("Email already registered"));
        assertTrue(getOutput().contains("Registration successful"));
        assertTrue(getOutput().contains("MY PROFILE"));
    }

    @Test
    @DisplayName("Validation: Invalid Menu Inputs")
    void testInvalidInputs() {
        setInput(
                "99", // Invalid menu choice
                "abc", // Invalid number format
                "0" // Exit
        );
        ui = new HotelManagementUI();
        ui.start();

        assertTrue(getOutput().contains("Invalid choice"));
        assertTrue(getOutput().contains("Invalid number"));
    }

    @Test
    @DisplayName("Admin: Comprehensive User & Room Management")
    void testAdminPowerUser() {
        // Targets: handleUserManagement (All roles), handleRoomManagement (All ops)
        setInput(
                "1", "admin@hotel.com", "admin123", // Login

                // --- User Management ---
                "1", // User Management
                "2", // Add User
                "New Admin", "admin2@h.com", "111", "pass", "1", // Role 1: Admin

                "1", "2", // User Mgmt -> Add User
                "New Manager", "man2@h.com", "222", "pass", "3", "Sales", // Role 3: Manager (requires Dept)

                "1", "2", // User Mgmt -> Add User
                "New Cleaner", "clean2@h.com", "333", "pass", "4", // Role 4: Cleaner

                "1", "4", // User Mgmt -> By Role
                "3", // Select Manager

                // --- Room Management ---
                "2", // Room Management
                "1", "501", "3", "250.0", // Add Room: 501, Type 3 (Deluxe), Price
                "2", // Room Management
                "3", "501", "2", // Update Status: Room 501 -> Unavailable (2)
                "2", // Room Management
                "2", "501", // Remove Room: 501

                "0", // Back to Admin Menu
                "0", // Logout
                "0"  // Exit System
        );
        ui = new HotelManagementUI();
        ui.start();

        String output = getOutput();
        assertTrue(output.contains("User created"), "Should confirm user creation");
        assertTrue(output.contains("Sales"), "Should show department for manager");
        assertTrue(output.contains("Unavailable"), "Should show status update");
    }

    @Test
    @DisplayName("Admin: Payment Management & Refunds")
    void testPaymentOperations() {
        // We first need to generate a payment to refund.
        // We'll simulate a checkout first, then log in as admin to refund it.
        setInput(
                // 1. Reception creates a payment
                "1", "reception@hotel.com", "reception123",
                "4", "guest@hotel.com", "102", "1", "100", "no", "1", // Checkout, Cash
                "0", // Logout

                // 2. Admin manages payments
                "1", "admin@hotel.com", "admin123",
                "4", // Payment Management
                "1", // View All (to see the ID)
                "4", // Payment Mgmt
                "5", "INV-2023-001", // View Invoice (Invalid ID test)
                "4", // Payment Mgmt
                "2", "1", // View Details for Payment ID 1 (Assuming sequential IDs)
                "4", // Payment Mgmt
                "3", "1", "yes", // Refund Payment ID 1, Confirm "yes"
                "4", // Payment Mgmt
                "4", // View Stats

                "0", // Back
                "0", // Logout
                "0"  // Exit
        );
        ui = new HotelManagementUI();
        ui.start();

        String output = getOutput();
        // We might not catch the exact ID "1" if tests run in random order,
        // but this exercises the code paths.
        assertTrue(output.contains("PAYMENT MANAGEMENT"));
    }

    @Test
    @DisplayName("Guest: Full Booking Journey")
    void testGuestJourney() {
        // Targets: showGuestMenu, handleGuestBooking, handleViewMyBookings
        String arr = LocalDate.now().plusDays(5).toString();
        String dep = LocalDate.now().plusDays(7).toString();

        setInput(
                "1", "guest@hotel.com", "guest123", // Login
                "1", // Make Booking
                "guest@hotel.com", // Confirm email (UI asks for it)
                arr, dep, // Dates
                "1", // Room Type: Single
                "yes", // Confirm

                "2", // View My Bookings
                "3", // View Rooms
                "4", "WiFi is slow", // Request Support

                "0", // Logout
                "0"  // Exit
        );
        ui = new HotelManagementUI();
        ui.start();

        String output = getOutput();
        assertTrue(output.contains("Booking confirmed"));
        assertTrue(output.contains("Request submitted"));
    }

    @Test
    @DisplayName("Cleaner: View and Clean Rooms")
    void testCleanerOperations() {
        // Targets: showCleanerMenu, handleViewAssignedRooms
        // Note: Cleaners only see rooms assigned to them.
        // We rely on sample data 'cleaner@hotel.com'

        setInput(
                "1", "cleaner@hotel.com", "cleaner123",
                "1", // View Assigned
                "2", "101", // Update Status -> Room 101
                "3", // Profile
                "0", // Logout
                "0"  // Exit
        );
        ui = new HotelManagementUI();
        ui.start();

        String output = getOutput();
        assertTrue(output.contains("ASSIGNED ROOMS"));
    }

    @Test
    @DisplayName("Reception: Checkout with ALL Facility Types")
    void testCheckoutAllFacilities() {
        // Targets: handleCheckOutAndPayment, addFacilityChargesToPayment (Cases 1-6)
        setInput(
                "1", "reception@hotel.com", "reception123", // Login
                "4", // Option: Check-Out & Payment
                "guest@hotel.com", // Guest Email (from sample data)
                "101", // Room Number
                "2",   // Nights
                "100", // Price per night
                "yes", // "Add facilities?" -> Enters addFacilityChargesToPayment loop

                // Now inside the Facility Loop:
                "1", "2", // 1. Spa -> Qty 2
                "2", "3", // 2. Gym -> Qty 3
                "3", "4", // 3. Pool -> Qty 4
                "4", "1", // 4. Golf -> Qty 1
                "5", "2", // 5. Dining -> Qty 2
                "6", "5", // 6. Breakfast -> Qty 5
                "99",     // Invalid choice (to test default case)
                "0",      // 0. Done adding facilities

                "1", // Payment Method: Cash
                "0", // Logout
                "0"  // Exit System
        );
        ui = new HotelManagementUI();
        ui.start();

        String output = getOutput();
        // Assert we charged for everything
        assertTrue(output.contains("Gym Access"), "Should contain Gym");
        assertTrue(output.contains("Golf Course"), "Should contain Golf");
        assertTrue(output.contains("Breakfast"), "Should contain Breakfast");
        assertTrue(output.contains("Payment processed"), "Should finish payment");
    }

    @Test
    @DisplayName("Reception: Create Booking - Invalid Date & Room Type Failures")
    void testCreateBookingFailurePaths() {
        // Targets: handleCreateBooking (Date validation, Invalid room choice, Exception catch blocks)
        String today = LocalDate.now().toString();
        String yesterday = LocalDate.now().minusDays(1).toString();

        setInput(
                "1", "reception@hotel.com", "reception123", // Login
                "1", // Create Booking
                "guest@hotel.com", // Existing Guest

                // 1. Invalid date sequence (Depart before Arrive)
                today, yesterday, // Arrive=Today, Depart=Yesterday (FAIL)

                // 2. Invalid room choice (Non-number and out of range)
                today, today, // Valid dates
                "99", // Room Type 99 (FAIL)
                "abc", // Room Type 'abc' (FAIL)
                "0", // Exit
                "0", // Logout
                "0"  // Exit System
        );
        ui = new HotelManagementUI();
        ui.start();

        String output = getOutput();
        assertTrue(output.contains("Departure date must be after"), "Should fail date validation.");
        assertTrue(output.contains("Invalid choice"), "Should fail room choice validation.");
    }

    @Test
    @DisplayName("Reception: Facility Charges - Invalid/Error Inputs")
    void testFacilityChargesErrorPaths() {
        // Targets: addFacilityChargesToPayment (Invalid choice default, getIntInput catch)
        setInput(
                "1", "reception@hotel.com", "reception123", // Login
                "4", // Check-Out & Payment
                "guest@hotel.com", // Guest Email
                "101", "2", "100", // Room Details
                "yes", // Add facilities? -> Enters addFacilityChargesToPayment loop

                // Inside Facility Loop:
                "abc", // Invalid input (triggers getIntInput catch block)
                "-1",  // Invalid choice (triggers default case)
                "0",   // Done adding facilities

                "1", // Payment Method: Cash
                "0", // Logout
                "0"  // Exit System
        );
        ui = new HotelManagementUI();
        ui.start();

        String output = getOutput();
        assertTrue(output.contains("Invalid number"), "Should catch non-numeric input.");
        assertTrue(output.contains("Invalid choice"), "Should hit default switch case.");
    }

    @Test
    @DisplayName("Manager: Reports - All Options and Invalid Input")
    void testManagerReportsWithInvalidInput() {
        // Targets: showManagerReports (All cases 1-7, getIntInput catch block)
        setInput(
                "1", "manager@hotel.com", "manager123", // Login
                "5", // Reports

                // All Report Options:
                "1", // Occupancy
                "5", // Reports
                "2", // Revenue
                "5", // Reports
                "3", // Guest Stats
                "5", // Reports
                "4", // Booking Stats
                "5", // Reports

                "abc", // Invalid input (triggers getIntInput catch block)
                "0", // Back
                "7", // Payment Stats
                "6", // Analytics
                "0", // Logout
                "0"  // Exit System
        );
        ui = new HotelManagementUI();
        ui.start();

        String output = getOutput();
        assertTrue(output.contains("REVENUE REPORT"), "Should run report.");
        assertTrue(output.contains("Invalid number"), "Should catch non-numeric input.");
    }

    @Test
    @DisplayName("Reception: Checkout Failure - Guest Not Found")
    void testCheckoutGuestNotFound() {
        // Targets: handleCheckOutAndPayment (Guest not found path)
        setInput(
                "1", "reception@hotel.com", "reception123", // Login
                "4", // Check-Out & Payment
                "nonexistent@hotel.com", // Guest Email

                // Invalid inputs to ensure the menu eventually quits cleanly
                "abc",
                "0",
                "0"
        );
        ui = new HotelManagementUI();
        ui.start();

        String output = getOutput();
        assertTrue(output.contains("Guest not found."), "Should fail because guest email is invalid.");
    }
}