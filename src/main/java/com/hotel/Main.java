package com.hotel;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/**
 * Main application entry point for Hotel Booking System.
 * Provides a console interface for staff to manage hotel operations.
 * This is a staff-only system - guests are managed by staff.
 *
 * @author Group 6
 * @version 1.0
 */
public class Main {
    private static HotelSystem hotelSystem;
    private static Scanner scanner;

    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        hotelSystem = new HotelSystem();

        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║   HOTEL BOOKING MANAGEMENT SYSTEM      ║");
        System.out.println("║         Staff Access Only              ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println("\nInitializing system...");
        hotelSystem.initialize();
        System.out.println("✓ System ready!\n");

        displayDefaultCredentials();

        // Main application loop
        boolean running = true;
        while (running) {
            if (!hotelSystem.isLoggedIn()) {
                running = showLoginMenu();
            } else {
                running = showMainMenu();
            }
        }

        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║  Thank you for using Hotel System!     ║");
        System.out.println("╚════════════════════════════════════════╝");
        scanner.close();
    }

    /**
     * Displays default credentials for first-time users.
     */
    private static void displayDefaultCredentials() {
        System.out.println("═══════════════════════════════════════════════");
        System.out.println("DEFAULT STAFF CREDENTIALS:");
        System.out.println("───────────────────────────────────────────────");
        System.out.println("Admin:      admin@hotel.com / admin123");
        System.out.println("Reception:  john@hotel.com  / pass123");
        System.out.println("Manager:    jane@hotel.com  / pass123");
        System.out.println("Cleaner:    mary@hotel.com  / pass123");
        System.out.println("═══════════════════════════════════════════════\n");
    }

    /**
     * Shows login menu for staff.
     */
    private static boolean showLoginMenu() {
        System.out.println("\n┌─────────────────────────┐");
        System.out.println("│     STAFF LOGIN         │");
        System.out.println("└─────────────────────────┘");
        System.out.println("1. Login");
        System.out.println("2. Exit System");
        System.out.print("\nChoose option: ");

        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1":
                performLogin();
                return true;
            case "2":
                return false;
            default:
                System.out.println("\n✗ Invalid option! Please choose 1 or 2.");
                return true;
        }
    }

    /**
     * Performs staff login.
     */
    private static void performLogin() {
        System.out.println("\n── Enter Credentials ──");
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        try {
            if (hotelSystem.login(email, password)) {
                User user = hotelSystem.getCurrentUser();
                System.out.println("\n╔════════════════════════════════════════╗");
                System.out.println("║         LOGIN SUCCESSFUL! ✓            ║");
                System.out.println("╚════════════════════════════════════════╝");
                System.out.println("Welcome: " + user.getName());
                System.out.println("Role: " + user.getRole());
                System.out.println("Access Level: " + user.getPermissions());
            } else {
                System.out.println("\n✗ Invalid credentials! Please try again.");
            }
        } catch (Exception e) {
            System.out.println("\n✗ Login failed: " + e.getMessage());
        }
    }

    /**
     * Shows main menu based on user role.
     */
    private static boolean showMainMenu() {
        User currentUser = hotelSystem.getCurrentUser();

        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║          MAIN MENU                     ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println("User: " + currentUser.getName() + " [" + currentUser.getRole() + "]");

        if (currentUser instanceof Admin) {
            return showAdminMenu();
        } else if (currentUser instanceof ReceptionStaff) {
            return showReceptionMenu();
        } else if (currentUser instanceof Manager) {
            return showManagerMenu();
        } else if (currentUser instanceof Cleaner) {
            return showCleanerMenu();
        }

        return true;
    }

    /**
     * Shows admin menu.
     */
    private static boolean showAdminMenu() {
        System.out.println("\n┌─────────────────────────────────────┐");
        System.out.println("│  ADMIN FUNCTIONS                    │");
        System.out.println("└─────────────────────────────────────┘");
        System.out.println("  GUEST MANAGEMENT");
        System.out.println("    1. Register New Guest");
        System.out.println("    2. View All Guests");
        System.out.println("    3. Search Guest");
        System.out.println("\n  BOOKING MANAGEMENT");
        System.out.println("    4. Create Booking");
        System.out.println("    5. View All Bookings");
        System.out.println("    6. Cancel Booking");
        System.out.println("    7. Search Bookings");
        System.out.println("\n  ROOM MANAGEMENT");
        System.out.println("    8. View All Rooms");
        System.out.println("    9. Search Available Rooms");
        System.out.println("   10. Update Room Status");
        System.out.println("\n  REPORTS & ANALYTICS");
        System.out.println("   11. Generate Occupancy Report");
        System.out.println("   12. Generate Booking Report");
        System.out.println("   13. View Payment Statistics");
        System.out.println("\n  SYSTEM");
        System.out.println("   14. Logout");
        System.out.println("   15. Exit System");
        System.out.println("───────────────────────────────────────");
        System.out.print("Choose option (1-15): ");

        String choice = scanner.nextLine().trim();
        return handleAdminChoice(choice);
    }

    private static boolean handleAdminChoice(String choice) {
        switch (choice) {
            case "1": registerGuest(); break;
            case "2": viewAllGuests(); break;
            case "3": searchGuest(); break;
            case "4": createBooking(); break;
            case "5": viewAllBookings(); break;
            case "6": cancelBooking(); break;
            case "7": searchBookings(); break;
            case "8": viewAllRooms(); break;
            case "9": searchAvailableRooms(); break;
            case "10": updateRoomStatus(); break;
            case "11": generateOccupancyReport(); break;
            case "12": generateBookingReport(); break;
            case "13": viewPaymentStatistics(); break;
            case "14": hotelSystem.logout(); return true;
            case "15": hotelSystem.logout(); return false;
            default: System.out.println("\n✗ Invalid option!");
        }
        return true;
    }

    /**
     * Shows reception staff menu.
     */
    private static boolean showReceptionMenu() {
        System.out.println("\n┌─────────────────────────────────────┐");
        System.out.println("│  RECEPTION FUNCTIONS                │");
        System.out.println("└─────────────────────────────────────┘");
        System.out.println("  1. Register New Guest");
        System.out.println("  2. Create Booking");
        System.out.println("  3. Search Available Rooms");
        System.out.println("  4. Check-In Guest");
        System.out.println("  5. Check-Out Guest");
        System.out.println("  6. Cancel Booking");
        System.out.println("  7. View Guest Bookings");
        System.out.println("  8. View Upcoming Check-Ins");
        System.out.println("  9. View Room Status");
        System.out.println(" 10. Logout");
        System.out.println(" 11. Exit System");
        System.out.println("───────────────────────────────────────");
        System.out.print("Choose option (1-11): ");

        String choice = scanner.nextLine().trim();
        return handleReceptionChoice(choice);
    }

    private static boolean handleReceptionChoice(String choice) {
        switch (choice) {
            case "1": registerGuest(); break;
            case "2": createBooking(); break;
            case "3": searchAvailableRooms(); break;
            case "4": checkInGuest(); break;
            case "5": checkOutGuest(); break;
            case "6": cancelBooking(); break;
            case "7": viewGuestBookings(); break;
            case "8": viewUpcomingCheckIns(); break;
            case "9": viewAllRooms(); break;
            case "10": hotelSystem.logout(); return true;
            case "11": hotelSystem.logout(); return false;
            default: System.out.println("\n✗ Invalid option!");
        }
        return true;
    }

    /**
     * Shows manager menu.
     */
    private static boolean showManagerMenu() {
        System.out.println("\n┌─────────────────────────────────────┐");
        System.out.println("│  MANAGER FUNCTIONS                  │");
        System.out.println("└─────────────────────────────────────┘");
        System.out.println("  1. Generate Occupancy Report");
        System.out.println("  2. Generate Booking Report");
        System.out.println("  3. View Payment Statistics");
        System.out.println("  4. View All Rooms");
        System.out.println("  5. View All Bookings");
        System.out.println("  6. Logout");
        System.out.println("  7. Exit System");
        System.out.println("───────────────────────────────────────");
        System.out.print("Choose option (1-7): ");

        String choice = scanner.nextLine().trim();
        return handleManagerChoice(choice);
    }

    private static boolean handleManagerChoice(String choice) {
        switch (choice) {
            case "1": generateOccupancyReport(); break;
            case "2": generateBookingReport(); break;
            case "3": viewPaymentStatistics(); break;
            case "4": viewAllRooms(); break;
            case "5": viewAllBookings(); break;
            case "6": hotelSystem.logout(); return true;
            case "7": hotelSystem.logout(); return false;
            default: System.out.println("\n✗ Invalid option!");
        }
        return true;
    }

    /**
     * Shows cleaner menu.
     */
    private static boolean showCleanerMenu() {
        System.out.println("\n┌─────────────────────────────────────┐");
        System.out.println("│  CLEANER FUNCTIONS                  │");
        System.out.println("└─────────────────────────────────────┘");
        System.out.println("  1. View Rooms Needing Cleaning");
        System.out.println("  2. Mark Room as Cleaned");
        System.out.println("  3. View All Rooms Status");
        System.out.println("  4. Logout");
        System.out.println("  5. Exit System");
        System.out.println("───────────────────────────────────────");
        System.out.print("Choose option (1-5): ");

        String choice = scanner.nextLine().trim();
        return handleCleanerChoice(choice);
    }

    private static boolean handleCleanerChoice(String choice) {
        switch (choice) {
            case "1": viewRoomsNeedingCleaning(); break;
            case "2": markRoomCleaned(); break;
            case "3": viewAllRooms(); break;
            case "4": hotelSystem.logout(); return true;
            case "5": hotelSystem.logout(); return false;
            default: System.out.println("\n✗ Invalid option!");
        }
        return true;
    }

    // ═══════════════════════════════════════════════════════════
    //                    FEATURE IMPLEMENTATIONS
    // ═══════════════════════════════════════════════════════════

    private static void registerGuest() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║       REGISTER NEW GUEST               ║");
        System.out.println("╚════════════════════════════════════════╝");

        System.out.print("Guest Full Name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Email Address: ");
        String email = scanner.nextLine().trim();

        System.out.print("Phone Number: ");
        String phone = scanner.nextLine().trim();

        try {
            Guest guest = hotelSystem.registerGuest(name, email, phone);
            System.out.println("\n✓ Guest registered successfully!");
            System.out.println("┌────────────────────────────────────┐");
            System.out.println("│ Guest ID: " + guest.getUserId());
            System.out.println("│ Name: " + guest.getName());
            System.out.println("│ Email: " + guest.getEmail());
            System.out.println("│ Phone: " + guest.getPhone());
            System.out.println("└────────────────────────────────────┘");
        } catch (Exception e) {
            System.out.println("\n✗ Registration failed: " + e.getMessage());
        }

        pressEnterToContinue();
    }

    private static void createBooking() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║         CREATE NEW BOOKING             ║");
        System.out.println("╚════════════════════════════════════════╝");

        System.out.print("Guest ID: ");
        String guestId = scanner.nextLine().trim();

        System.out.print("Room Number: ");
        String roomNumber = scanner.nextLine().trim();

        System.out.print("Check-in Date (YYYY-MM-DD): ");
        String checkInStr = scanner.nextLine().trim();

        System.out.print("Check-out Date (YYYY-MM-DD): ");
        String checkOutStr = scanner.nextLine().trim();

        System.out.print("Include Breakfast? (yes/no): ");
        boolean breakfast = scanner.nextLine().trim().equalsIgnoreCase("yes");

        try {
            LocalDate checkIn = LocalDate.parse(checkInStr);
            LocalDate checkOut = LocalDate.parse(checkOutStr);

            Booking booking = hotelSystem.createBooking(guestId, roomNumber, checkIn, checkOut, breakfast);

            System.out.println("\n✓ Booking created successfully!");
            System.out.println("┌────────────────────────────────────┐");
            System.out.println("│ Booking ID: " + booking.getBookingId());
            System.out.println("│ Guest ID: " + booking.getGuestId());
            System.out.println("│ Room: " + booking.getRoomNumber());
            System.out.println("│ Check-in: " + booking.getCheckInDate());
            System.out.println("│ Check-out: " + booking.getCheckOutDate());
            System.out.println("│ Nights: " + booking.getNumberOfNights());
            System.out.println("│ Total: €" + String.format("%.2f", booking.getTotalPrice()));
            System.out.println("│ Breakfast: " + (breakfast ? "Yes" : "No"));
            System.out.println("│ Status: " + booking.getStatus());
            System.out.println("└────────────────────────────────────┘");
        } catch (DateTimeParseException e) {
            System.out.println("\n✗ Invalid date format! Use YYYY-MM-DD");
        } catch (Exception e) {
            System.out.println("\n✗ Booking failed: " + e.getMessage());
        }

        pressEnterToContinue();
    }

    private static void searchAvailableRooms() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║      SEARCH AVAILABLE ROOMS            ║");
        System.out.println("╚════════════════════════════════════════╝");

        System.out.print("Check-in Date (YYYY-MM-DD): ");
        String checkInStr = scanner.nextLine().trim();

        System.out.print("Check-out Date (YYYY-MM-DD): ");
        String checkOutStr = scanner.nextLine().trim();

        System.out.print("Room Type (STANDARD/SUPERIOR/SUITE or press Enter for all): ");
        String typeStr = scanner.nextLine().trim();

        try {
            LocalDate checkIn = LocalDate.parse(checkInStr);
            LocalDate checkOut = LocalDate.parse(checkOutStr);
            RoomType type = typeStr.isEmpty() ? null : RoomType.valueOf(typeStr.toUpperCase());

            List<Room> rooms = hotelSystem.searchAvailableRooms(checkIn, checkOut, type);

            System.out.println("\n═══ AVAILABLE ROOMS ═══");
            if (rooms.isEmpty()) {
                System.out.println("No rooms available for selected dates.");
            } else {
                System.out.println("Found " + rooms.size() + " available room(s):\n");
                for (Room room : rooms) {
                    System.out.println("┌────────────────────────────────────┐");
                    System.out.println("│ Room: " + room.getRoomNumber());
                    System.out.println("│ Type: " + room.getRoomType());
                    System.out.println("│ Price: €" + room.getBasePrice() + "/night");
                    System.out.println("│ Capacity: " + room.getCapacity() + " guests");
                    System.out.println("│ Status: " + room.getStatus());
                    System.out.println("└────────────────────────────────────┘");
                }
            }
        } catch (DateTimeParseException e) {
            System.out.println("\n✗ Invalid date format! Use YYYY-MM-DD");
        } catch (IllegalArgumentException e) {
            System.out.println("\n✗ Invalid room type! Use STANDARD, SUPERIOR, or SUITE");
        } catch (Exception e) {
            System.out.println("\n✗ Search failed: " + e.getMessage());
        }

        pressEnterToContinue();
    }

    private static void checkInGuest() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║          CHECK-IN GUEST                ║");
        System.out.println("╚════════════════════════════════════════╝");

        System.out.print("Booking ID: ");
        String bookingId = scanner.nextLine().trim();

        try {
            hotelSystem.checkInGuest(bookingId);
            System.out.println("\n✓ Guest checked in successfully!");
            System.out.println("Room status updated to OCCUPIED");
        } catch (Exception e) {
            System.out.println("\n✗ Check-in failed: " + e.getMessage());
        }

        pressEnterToContinue();
    }

    private static void checkOutGuest() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║         CHECK-OUT GUEST                ║");
        System.out.println("╚════════════════════════════════════════╝");

        System.out.print("Booking ID: ");
        String bookingId = scanner.nextLine().trim();

        try {
            Payment payment = hotelSystem.checkOutGuest(bookingId);
            System.out.println("\n✓ Guest checked out successfully!");
            System.out.println("\n" + payment.generateInvoice());
        } catch (Exception e) {
            System.out.println("\n✗ Check-out failed: " + e.getMessage());
        }

        pressEnterToContinue();
    }

    private static void cancelBooking() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║          CANCEL BOOKING                ║");
        System.out.println("╚════════════════════════════════════════╝");

        System.out.print("Booking ID: ");
        String bookingId = scanner.nextLine().trim();

        System.out.print("Are you sure? (yes/no): ");
        String confirm = scanner.nextLine().trim();

        if (!confirm.equalsIgnoreCase("yes")) {
            System.out.println("\n✗ Cancellation aborted.");
            pressEnterToContinue();
            return;
        }

        try {
            hotelSystem.cancelBooking(bookingId);
            System.out.println("\n✓ Booking cancelled successfully!");
        } catch (Exception e) {
            System.out.println("\n✗ Cancellation failed: " + e.getMessage());
        }

        pressEnterToContinue();
    }

    private static void viewGuestBookings() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║        VIEW GUEST BOOKINGS             ║");
        System.out.println("╚════════════════════════════════════════╝");

        System.out.print("Guest ID: ");
        String guestId = scanner.nextLine().trim();

        try {
            List<Booking> bookings = hotelSystem.getBookingManager().getBookingsByGuest(guestId);

            if (bookings.isEmpty()) {
                System.out.println("\nNo bookings found for this guest.");
            } else {
                System.out.println("\n═══ GUEST BOOKINGS (" + bookings.size() + ") ═══\n");
                for (Booking booking : bookings) {
                    System.out.println("┌────────────────────────────────────┐");
                    System.out.println("│ Booking: " + booking.getBookingId());
                    System.out.println("│ Room: " + booking.getRoomNumber());
                    System.out.println("│ Check-in: " + booking.getCheckInDate());
                    System.out.println("│ Check-out: " + booking.getCheckOutDate());
                    System.out.println("│ Total: €" + String.format("%.2f", booking.getTotalPrice()));
                    System.out.println("│ Status: " + booking.getStatus());
                    System.out.println("└────────────────────────────────────┘");
                }
            }
        } catch (Exception e) {
            System.out.println("\n✗ Failed: " + e.getMessage());
        }

        pressEnterToContinue();
    }

    private static void viewAllRooms() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║          ALL ROOMS STATUS              ║");
        System.out.println("╚════════════════════════════════════════╝");

        List<Room> rooms = hotelSystem.getRoomManager().getAllRooms();

        System.out.println("\nTotal Rooms: " + rooms.size() + "\n");

        for (Room room : rooms) {
            System.out.println("┌────────────────────────────────────┐");
            System.out.println("│ Room: " + room.getRoomNumber());
            System.out.println("│ Type: " + room.getRoomType());
            System.out.println("│ Price: €" + room.getBasePrice() + "/night");
            System.out.println("│ Status: " + room.getStatus());
            System.out.println("│ Capacity: " + room.getCapacity() + " guests");
            System.out.println("└────────────────────────────────────┘");
        }

        pressEnterToContinue();
    }

    private static void viewAllBookings() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║          ALL BOOKINGS                  ║");
        System.out.println("╚════════════════════════════════════════╝");

        List<Booking> bookings = hotelSystem.getBookingManager().getAllBookings();

        if (bookings.isEmpty()) {
            System.out.println("\nNo bookings in the system.");
        } else {
            System.out.println("\nTotal Bookings: " + bookings.size() + "\n");
            for (Booking booking : bookings) {
                System.out.println("┌────────────────────────────────────┐");
                System.out.println("│ ID: " + booking.getBookingId());
                System.out.println("│ Guest: " + booking.getGuestId());
                System.out.println("│ Room: " + booking.getRoomNumber());
                System.out.println("│ Check-in: " + booking.getCheckInDate());
                System.out.println("│ Check-out: " + booking.getCheckOutDate());
                System.out.println("│ Total: €" + String.format("%.2f", booking.getTotalPrice()));
                System.out.println("│ Status: " + booking.getStatus());
                System.out.println("└────────────────────────────────────┘");
            }
        }

        pressEnterToContinue();
    }

    private static void generateOccupancyReport() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║        OCCUPANCY REPORT                ║");
        System.out.println("╚════════════════════════════════════════╝");

        try {
            Report report = hotelSystem.generateReport("OCCUPANCY");
            System.out.println("\n" + report.generateContent());
        } catch (Exception e) {
            System.out.println("\n✗ Report generation failed: " + e.getMessage());
        }

        pressEnterToContinue();
    }

    private static void generateBookingReport() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║         BOOKING REPORT                 ║");
        System.out.println("╚════════════════════════════════════════╝");

        try {
            Report report = hotelSystem.generateReport("BOOKINGS");
            System.out.println("\n" + report.generateContent());
        } catch (Exception e) {
            System.out.println("\n✗ Report generation failed: " + e.getMessage());
        }

        pressEnterToContinue();
    }

    private static void viewPaymentStatistics() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║       PAYMENT STATISTICS               ║");
        System.out.println("╚════════════════════════════════════════╝");

        try {
            var stats = hotelSystem.getPaymentManager().getPaymentStatistics();

            System.out.println("\n┌────────────────────────────────────┐");
            System.out.println("│ Total Payments: " + stats.get("totalPayments"));
            System.out.println("│ Pending: " + stats.get("pendingPayments"));
            System.out.println("│ Completed: " + stats.get("completedPayments"));
            System.out.println("│ Failed: " + stats.get("failedPayments"));
            System.out.println("├────────────────────────────────────┤");
            System.out.println("│ Total Revenue: €" + String.format("%.2f", stats.get("totalRevenue")));
            System.out.println("│ Total Refunded: €" + String.format("%.2f", stats.get("totalRefunded")));
            System.out.println("│ Net Revenue: €" + String.format("%.2f", stats.get("netRevenue")));
            System.out.println("└────────────────────────────────────┘");
        } catch (Exception e) {
            System.out.println("\n✗ Failed to retrieve statistics: " + e.getMessage());
        }

        pressEnterToContinue();
    }

    private static void viewRoomsNeedingCleaning() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║      ROOMS NEEDING CLEANING            ║");
        System.out.println("╚════════════════════════════════════════╝");

        List<Room> rooms = hotelSystem.getRoomManager().getRoomsByStatus(RoomStatus.CLEANING);

        if (rooms.isEmpty()) {
            System.out.println("\n✓ No rooms currently need cleaning.");
        } else {
            System.out.println("\nRooms to clean: " + rooms.size() + "\n");
            for (Room room : rooms) {
                System.out.println("┌────────────────────────────────────┐");
                System.out.println("│ Room: " + room.getRoomNumber());
                System.out.println("│ Type: " + room.getRoomType());
                System.out.println("│ Status: " + room.getStatus());
                System.out.println("└────────────────────────────────────┘");
            }
        }

        pressEnterToContinue();
    }

    private static void markRoomCleaned() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║       MARK ROOM AS CLEANED             ║");
        System.out.println("╚════════════════════════════════════════╝");

        System.out.print("Room Number: ");
        String roomNumber = scanner.nextLine().trim();

        try {
            hotelSystem.getRoomManager().updateRoomStatus(roomNumber, RoomStatus.AVAILABLE);
            System.out.println("\n✓ Room " + roomNumber + " marked as cleaned and available!");
        } catch (Exception e) {
            System.out.println("\n✗ Failed: " + e.getMessage());
        }

        pressEnterToContinue();
    }

    private static void updateRoomStatus() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║        UPDATE ROOM STATUS              ║");
        System.out.println("╚════════════════════════════════════════╝");

        System.out.print("Room Number: ");
        String roomNumber = scanner.nextLine().trim();

        System.out.println("\nAvailable statuses:");
        System.out.println("1. AVAILABLE");
        System.out.println("2. OCCUPIED");
        System.out.println("3. CLEANING");
        System.out.println("4. MAINTENANCE");
        System.out.println("5. RESERVED");
        System.out.print("\nChoose status (1-5): ");

        String choice = scanner.nextLine().trim();
        RoomStatus status;

        switch (choice) {
            case "1": status = RoomStatus.AVAILABLE; break;
            case "2": status = RoomStatus.OCCUPIED; break;
            case "3": status = RoomStatus.CLEANING; break;
            case "4": status = RoomStatus.MAINTENANCE; break;
            case "5": status = RoomStatus.RESERVED; break;
            default:
                System.out.println("\n✗ Invalid choice!");
                pressEnterToContinue();
                return;
        }

        try {
            hotelSystem.getRoomManager().updateRoomStatus(roomNumber, status);
            System.out.println("\n✓ Room " + roomNumber + " status updated to " + status);
        } catch (Exception e) {
            System.out.println("\n✗ Failed: " + e.getMessage());
        }

        pressEnterToContinue();
    }

    private static void viewUpcomingCheckIns() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║       UPCOMING CHECK-INS               ║");
        System.out.println("╚════════════════════════════════════════╝");

        List<Booking> upcomingBookings = hotelSystem.getBookingManager().getUpcomingBookings();

        if (upcomingBookings.isEmpty()) {
            System.out.println("\nNo upcoming check-ins.");
        } else {
            System.out.println("\nUpcoming Check-ins: " + upcomingBookings.size() + "\n");
            for (Booking booking : upcomingBookings) {
                System.out.println("┌────────────────────────────────────┐");
                System.out.println("│ Booking: " + booking.getBookingId());
                System.out.println("│ Guest: " + booking.getGuestId());
                System.out.println("│ Room: " + booking.getRoomNumber());
                System.out.println("│ Check-in: " + booking.getCheckInDate());
                System.out.println("│ Check-out: " + booking.getCheckOutDate());
                System.out.println("│ Nights: " + booking.getNumberOfNights());
                System.out.println("└────────────────────────────────────┘");
            }
        }

        pressEnterToContinue();
    }

    private static void viewAllGuests() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║           ALL GUESTS                   ║");
        System.out.println("╚════════════════════════════════════════╝");

        List<User> allGuests = hotelSystem.getUserManager().getUsersByRole(UserRole.GUEST);

        if (allGuests.isEmpty()) {
            System.out.println("\nNo guests registered in the system.");
        } else {
            System.out.println("\nTotal Guests: " + allGuests.size() + "\n");
            for (User user : allGuests) {
                Guest guest = (Guest) user;
                System.out.println("┌────────────────────────────────────┐");
                System.out.println("│ ID: " + guest.getUserId());
                System.out.println("│ Name: " + guest.getName());
                System.out.println("│ Email: " + guest.getEmail());
                System.out.println("│ Phone: " + guest.getPhone());
                System.out.println("│ Bookings: " + guest.getBookingHistory().size());
                System.out.println("│ Returning: " + (guest.isReturningGuest() ? "Yes" : "No"));
                System.out.println("└────────────────────────────────────┘");
            }
        }

        pressEnterToContinue();
    }

    private static void searchGuest() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║          SEARCH GUEST                  ║");
        System.out.println("╚════════════════════════════════════════╝");

        System.out.println("Search by:");
        System.out.println("1. Guest ID");
        System.out.println("2. Name");
        System.out.println("3. Email");
        System.out.print("\nChoose option: ");

        String choice = scanner.nextLine().trim();

        try {
            switch (choice) {
                case "1":
                    System.out.print("Enter Guest ID: ");
                    String id = scanner.nextLine().trim();
                    User guest = hotelSystem.getUserManager().getUser(id);
                    if (guest != null && guest instanceof Guest) {
                        displayGuestDetails((Guest) guest);
                    } else {
                        System.out.println("\n✗ Guest not found!");
                    }
                    break;

                case "2":
                    System.out.print("Enter Name (partial match): ");
                    String name = scanner.nextLine().trim();
                    List<User> nameResults = hotelSystem.getUserManager().searchUsersByName(name);
                    List<Guest> guests = nameResults.stream()
                            .filter(u -> u instanceof Guest)
                            .map(u -> (Guest) u)
                            .collect(java.util.stream.Collectors.toList());

                    if (guests.isEmpty()) {
                        System.out.println("\n✗ No guests found!");
                    } else {
                        System.out.println("\nFound " + guests.size() + " guest(s):\n");
                        for (Guest g : guests) {
                            displayGuestDetails(g);
                        }
                    }
                    break;

                case "3":
                    System.out.print("Enter Email: ");
                    String email = scanner.nextLine().trim();
                    User emailResult = hotelSystem.getUserManager().getUserByEmail(email);
                    if (emailResult != null && emailResult instanceof Guest) {
                        displayGuestDetails((Guest) emailResult);
                    } else {
                        System.out.println("\n✗ Guest not found!");
                    }
                    break;

                default:
                    System.out.println("\n✗ Invalid option!");
            }
        } catch (Exception e) {
            System.out.println("\n✗ Search failed: " + e.getMessage());
        }

        pressEnterToContinue();
    }

    private static void displayGuestDetails(Guest guest) {
        System.out.println("\n┌────────────────────────────────────┐");
        System.out.println("│ GUEST DETAILS");
        System.out.println("├────────────────────────────────────┤");
        System.out.println("│ ID: " + guest.getUserId());
        System.out.println("│ Name: " + guest.getName());
        System.out.println("│ Email: " + guest.getEmail());
        System.out.println("│ Phone: " + guest.getPhone());
        System.out.println("│ Total Bookings: " + guest.getBookingHistory().size());
        System.out.println("│ Returning Guest: " + (guest.isReturningGuest() ? "Yes" : "No"));
        System.out.println("│ Payment Method: " + (guest.getPaymentMethod() != null ? guest.getPaymentMethod() : "Not set"));
        System.out.println("└────────────────────────────────────┘");
    }

    private static void searchBookings() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║         SEARCH BOOKINGS                ║");
        System.out.println("╚════════════════════════════════════════╝");

        System.out.println("Search by:");
        System.out.println("1. Booking ID");
        System.out.println("2. Guest ID");
        System.out.println("3. Room Number");
        System.out.println("4. Date Range");
        System.out.print("\nChoose option: ");

        String choice = scanner.nextLine().trim();

        try {
            switch (choice) {
                case "1":
                    System.out.print("Enter Booking ID: ");
                    String bookingId = scanner.nextLine().trim();
                    Booking booking = hotelSystem.getBookingManager().getBooking(bookingId);
                    if (booking != null) {
                        displayBookingDetails(booking);
                    } else {
                        System.out.println("\n✗ Booking not found!");
                    }
                    break;

                case "2":
                    System.out.print("Enter Guest ID: ");
                    String guestId = scanner.nextLine().trim();
                    List<Booking> guestBookings = hotelSystem.getBookingManager().getBookingsByGuest(guestId);
                    displayBookingList(guestBookings, "Guest Bookings");
                    break;

                case "3":
                    System.out.print("Enter Room Number: ");
                    String roomNumber = scanner.nextLine().trim();
                    List<Booking> roomBookings = hotelSystem.getBookingManager().getBookingsByRoom(roomNumber);
                    displayBookingList(roomBookings, "Room Bookings");
                    break;

                case "4":
                    System.out.print("Start Date (YYYY-MM-DD): ");
                    LocalDate startDate = LocalDate.parse(scanner.nextLine().trim());
                    System.out.print("End Date (YYYY-MM-DD): ");
                    LocalDate endDate = LocalDate.parse(scanner.nextLine().trim());
                    List<Booking> dateBookings = hotelSystem.getBookingManager().getBookingsInRange(startDate, endDate);
                    displayBookingList(dateBookings, "Bookings in Date Range");
                    break;

                default:
                    System.out.println("\n✗ Invalid option!");
            }
        } catch (DateTimeParseException e) {
            System.out.println("\n✗ Invalid date format! Use YYYY-MM-DD");
        } catch (Exception e) {
            System.out.println("\n✗ Search failed: " + e.getMessage());
        }

        pressEnterToContinue();
    }

    private static void displayBookingDetails(Booking booking) {
        System.out.println("\n┌────────────────────────────────────┐");
        System.out.println("│ BOOKING DETAILS");
        System.out.println("├────────────────────────────────────┤");
        System.out.println("│ ID: " + booking.getBookingId());
        System.out.println("│ Guest: " + booking.getGuestId());
        System.out.println("│ Room: " + booking.getRoomNumber());
        System.out.println("│ Check-in: " + booking.getCheckInDate());
        System.out.println("│ Check-out: " + booking.getCheckOutDate());
        System.out.println("│ Nights: " + booking.getNumberOfNights());
        System.out.println("│ Total: €" + String.format("%.2f", booking.getTotalPrice()));
        System.out.println("│ Breakfast: " + (booking.isBreakfastIncluded() ? "Yes" : "No"));
        System.out.println("│ Status: " + booking.getStatus());
        System.out.println("│ Booked: " + booking.getBookingDate());
        System.out.println("└────────────────────────────────────┘");
    }

    private static void displayBookingList(List<Booking> bookings, String title) {
        System.out.println("\n═══ " + title.toUpperCase() + " ═══");

        if (bookings.isEmpty()) {
            System.out.println("\nNo bookings found.");
        } else {
            System.out.println("\nFound " + bookings.size() + " booking(s):\n");
            for (Booking booking : bookings) {
                displayBookingDetails(booking);
            }
        }
    }

    // ═══════════════════════════════════════════════════════════
    //                      UTILITY METHODS
    // ═══════════════════════════════════════════════════════════

    /**
     * Pauses execution until user presses Enter.
     */
    private static void pressEnterToContinue() {
        System.out.println("\n[Press Enter to continue...]");
        scanner.nextLine();
    }
}