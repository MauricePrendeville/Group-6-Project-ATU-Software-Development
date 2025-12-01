package com.hotel.UI;

import com.hotel.*;
import com.hotel.Model.*;
import com.hotel.Service.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/**
 * Hotel Management System User Interface.
 * Provides a command-line interface for hotel operations including booking,
 * payments, user management, and room management with role-based access control.
 *
 * @author Group 6
 * @version 1.0
 */
public class HotelManagementUI {

    private final Scanner scanner;
    private final UserManager userManager;
    private final RoomInventoryImpl roomInventory;
    private final PaymentManager paymentManager;
    private User currentUser;
    private boolean running;

    /**
     * Constructs a HotelManagementUI with default settings.
     * Initializes all managers and loads sample data for testing.
     */
    public HotelManagementUI() {
        this.scanner = new Scanner(System.in);
        this.userManager = new UserManager();
        this.roomInventory = new RoomInventoryImpl();
        this.paymentManager = new PaymentManager();
        this.currentUser = null;
        this.running = true;
        initializeSampleData();
    }

    /**
     * Constructs a HotelManagementUI with provided managers.
     * Used for integration with existing system components.
     *
     * @param userManager the user management service
     * @param roomInventory the room inventory service
     * @param paymentManager the payment management service
     */
    public HotelManagementUI(UserManager userManager, RoomInventoryImpl roomInventory, PaymentManager paymentManager) {
        this.scanner = new Scanner(System.in);
        this.userManager = userManager;
        this.roomInventory = roomInventory;
        this.paymentManager = paymentManager;
        this.currentUser = null;
        this.running = true;
    }

    /**
     * Initializes the system with sample users and rooms for demonstration purposes.
     */
    private void initializeSampleData() {
        try {
            Admin admin = new Admin("A001", "Lusungu", "admin@hotel.com", "0871234567", "admin123");
            ReceptionStaff reception = new ReceptionStaff("R001", "Sybil Fawlty", "reception@hotel.com", "0871234568", "reception123");
            Manager manager = new Manager("M001", "Basil Fawlty", "manager@hotel.com", "0871234569", "manager123", "Operations");
            Cleaner cleaner = new Cleaner("C001", "Manuel", "cleaner@hotel.com", "0871234570", "cleaner123");
            Guest guest = new Guest("G001", "Father Ted", "guest@hotel.com", "0871234571", "guest123");

            userManager.addUser(admin);
            userManager.addUser(reception);
            userManager.addUser(manager);
            userManager.addUser(cleaner);
            userManager.addUser(guest);

            roomInventory.addRoom(new Room(101, RoomType.SINGLE, true, 120.0));
            roomInventory.addRoom(new Room(102, RoomType.SINGLE, true, 120.0));
            roomInventory.addRoom(new Room(103, RoomType.SINGLE, true, 120.0));
            roomInventory.addRoom(new Room(201, RoomType.DOUBLE, true, 180.0));
            roomInventory.addRoom(new Room(202, RoomType.DOUBLE, true, 180.0));
            roomInventory.addRoom(new Room(203, RoomType.DOUBLE, true, 180.0));
            roomInventory.addRoom(new Room(301, RoomType.DELUXE, true, 250.0));
            roomInventory.addRoom(new Room(302, RoomType.DELUXE, true, 250.0));
            roomInventory.addRoom(new Room(303, RoomType.SUITE, true, 350.0));
            roomInventory.addRoom(new Room(304, RoomType.SUITE, true, 350.0));
            roomInventory.addRoom(new Room(401, RoomType.FAMILY, true, 280.0));
            roomInventory.addRoom(new Room(402, RoomType.FAMILY, true, 280.0));
            roomInventory.addRoom(new Room(501, RoomType.PRESIDENTIAL, true, 500.0));

        } catch (Exception e) {
            System.err.println("Error initializing sample data: " + e.getMessage());
        }
    }

    /**
     * Main entry point for the UI system.
     */
    public void start() {
        displayWelcomeBanner();
        while (running) {
            if (currentUser == null) {
                showLoginMenu();
            } else {
                showMainMenu();
            }
        }
        displayGoodbyeMessage();
        scanner.close();
    }

    /**
     * Displays the welcome banner with hotel name and location.
     */
    public void displayWelcomeBanner() {
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║          THE GROUP 6 HOTEL MANAGEMENT SYSTEM               ║");
        System.out.println("║          Shore Road, Killybegs, Co. Donegal                ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
    }

    /**
     * Shows the login menu for unauthenticated users.
     * Provides options to login, register, view info, or exit.
     */
    public void showLoginMenu() {
        System.out.println("\n═══════════════════ LOGIN MENU ═══════════════════");
        System.out.println("1. Login");
        System.out.println("2. Register New Guest");
        System.out.println("3. View Hotel Information");
        System.out.println("0. Exit System");
        System.out.println("═══════════════════════════════════════════════════");

        int choice = getIntInput("Enter your choice: ");
        switch (choice) {
            case 1: handleLogin(); break;
            case 2: handleGuestRegistration(); break;
            case 3: displayHotelInformation(); break;
            case 0: running = false; break;
            default: System.out.println("Invalid choice.");
        }
    }

    /**
     * Handles user login authentication.
     * Prompts for email and password, validates credentials.
     *
     * @return true if login successful, false otherwise
     */
    public boolean handleLogin() {
        System.out.println("\n────────────── LOGIN ────────────────");
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        // Add Try-Catch block here
        try {
            User user = userManager.authenticateUser(email, password);
            if (user != null) {
                currentUser = user;
                System.out.println("\nLogin successful, welcome " + user.getName() + "!");
                return true;
            } else {
                System.out.println("\nLogin failed. Invalid email or password.");
                return false;
            }
        } catch (IllegalArgumentException e) {
            System.out.println("\nLogin failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Handles new guest registration.
     * Collects guest information and creates a new account.
     *
     * @return the created Guest object, or null if registration failed
     */
    public Guest handleGuestRegistration() {
        System.out.println("\n────────────── GUEST REGISTRATION ────────────────");
        try {
            System.out.print("Name: ");
            String name = scanner.nextLine().trim();
            System.out.print("Email: ");
            String email = scanner.nextLine().trim();

            if (userManager.emailExists(email)) {
                System.out.println("Email already registered.");
                return null;
            }

            System.out.print("Phone: ");
            String phone = scanner.nextLine().trim();
            System.out.print("Password: ");
            String password = scanner.nextLine().trim();

            String userId = userManager.generateUserId("G");
            Guest guest = new Guest(userId, name, email, phone, password);
            userManager.addUser(guest);

            System.out.println("\nRegistration successful! Your Guest ID: " + userId);
            return guest;
        } catch (IllegalArgumentException e) {
            System.out.println("Registration failed: " + e.getMessage());
            return null;
        }
    }

    /**
     * Displays hotel information including location, contact details,
     * room types, prices, and available facilities.
     */
    public void displayHotelInformation() {
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║              THE GROUP 6 HOTEL                             ║");
        System.out.println("╠════════════════════════════════════════════════════════════╣");
        System.out.println("║  Location: Shore Road, Killybegs, Co. Donegal             ║");
        System.out.println("║  Phone: +353 74 918 6000                                   ║");
        System.out.println("║  Email: info@groupsixhotel.com                             ║");
        System.out.println("║  Room Types: Single (€120), Double (€180), Deluxe (€250)  ║");
        System.out.println("║              Family (€280), Suite (€350), Presidential    ║");
        System.out.println("║  Facilities: Spa, Gym, Pool, Golf Course, Dining          ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
        System.out.println("\nTotal Rooms: " + roomInventory.getTotalRooms());
        System.out.println("Available: " + roomInventory.getAvailableRooms().size());
    }

    /**
     * Shows the main menu based on current user's role.
     * Displays role-specific options for admin, reception, manager, cleaner, or guest.
     */
    private void showMainMenu() {
        System.out.println("\n═══════════════════ MAIN MENU ═══════════════════");
        System.out.println("User: " + currentUser.getName() + " (" + currentUser.getRole() + ")");
        System.out.println("═══════════════════════════════════════════════════");

        switch (currentUser.getRole()) {
            case ADMIN: showAdminMenu(); break;
            case RECEPTION_STAFF: showReceptionMenu(); break;
            case MANAGER: showManagerMenu(); break;
            case CLEANER: showCleanerMenu(); break;
            case GUEST: showGuestMenu(); break;
        }
    }

    /**
     * Displays the admin menu with management options.
     */
    private void showAdminMenu() {
        System.out.println("1. User Management");
        System.out.println("2. Room Management");
        System.out.println("3. Booking Management");
        System.out.println("4. Payment Management");
        System.out.println("5. System Reports");
        System.out.println("6. My Profile");
        System.out.println("0. Logout");
        System.out.println("═══════════════════════════════════════════════════");

        int choice = getIntInput("Enter your choice: ");
        switch (choice) {
            case 1: handleUserManagement(); break;
            case 2: handleRoomManagement(); break;
            case 3: handleBookingManagement(); break;
            case 4: handlePaymentManagement(); break;
            case 5: handleSystemReports(); break;
            case 6: handleMyProfile(); break;
            case 0: logout(); break;
            default: System.out.println("Invalid choice.");
        }
    }

    /**
     * Displays the reception staff menu with booking and check-in options.
     */
    private void showReceptionMenu() {
        System.out.println("1. Create Booking");
        System.out.println("2. Check Availability");
        System.out.println("3. Check-In");
        System.out.println("4. Check-Out & Payment");
        System.out.println("5. View Bookings");
        System.out.println("6. Modify Booking");
        System.out.println("7. Cancel Booking");
        System.out.println("8. Add Facilities");
        System.out.println("9. Profile");
        System.out.println("0. Logout");
        System.out.println("═══════════════════════════════════════════════════");

        int choice = getIntInput("Enter your choice: ");
        switch (choice) {
            case 1: handleCreateBooking(); break;
            case 2: handleCheckAvailability(); break;
            case 3: handleCheckIn(); break;
            case 4: handleCheckOutAndPayment(); break;
            case 5: handleViewAllBookings(); break;
            case 6: handleModifyBooking(); break;
            case 7: handleCancelBooking(); break;
            case 8: handleAddFacilityCharges(); break;
            case 9: handleMyProfile(); break;
            case 0: logout(); break;
            default: System.out.println("Invalid choice.");
        }
    }

    /**
     * Displays the manager menu with reporting and analytics options.
     */
    private void showManagerMenu() {
        System.out.println("1. View Bookings");
        System.out.println("2. View Rooms");
        System.out.println("3. View Guests");
        System.out.println("4. View Staff");
        System.out.println("5. Reports");
        System.out.println("6. Analytics");
        System.out.println("7. Payment Stats");
        System.out.println("8. My Profile");
        System.out.println("0. Logout");
        System.out.println("═══════════════════════════════════════════════════");

        int choice = getIntInput("Enter your choice: ");
        switch (choice) {
            case 1: handleViewAllBookings(); break;
            case 2: handleViewAllRooms(); break;
            case 3: handleViewAllGuests(); break;
            case 4: handleViewStaff(); break;
            case 5: handleSystemReports(); break;
            case 6: handleViewAnalytics(); break;
            case 7: handlePaymentStatistics(); break;
            case 8: handleMyProfile(); break;
            case 0: logout(); break;
            default: System.out.println("Invalid choice.");
        }
    }

    /**
     * Displays the cleaner menu with room status options.
     */
    private void showCleanerMenu() {
        System.out.println("1. View Assigned Rooms");
        System.out.println("2. Update Room Status");
        System.out.println("3. My Profile");
        System.out.println("0. Logout");
        System.out.println("═══════════════════════════════════════════════════");

        int choice = getIntInput("Enter your choice: ");
        Cleaner cleaner = (Cleaner) currentUser;
        switch (choice) {
            case 1: handleViewAssignedRooms(cleaner); break;
            case 2: handleUpdateRoomStatus(cleaner); break;
            case 3: handleMyProfile(); break;
            case 0: logout(); break;
            default: System.out.println("Invalid choice.");
        }
    }

    /**
     * Displays the guest menu with booking and profile options.
     */
    private void showGuestMenu() {
        System.out.println("1. Make Booking");
        System.out.println("2. My Bookings");
        System.out.println("3. View Rooms");
        System.out.println("4. Request Support");
        System.out.println("5. My Profile");
        System.out.println("0. Logout");
        System.out.println("═══════════════════════════════════════════════════");

        int choice = getIntInput("Enter your choice: ");
        switch (choice) {
            case 1: handleGuestBooking(); break;
            case 2: handleViewMyBookings(); break;
            case 3: handleViewAvailableRooms(); break;
            case 4: handleRequestSupport(); break;
            case 5: handleMyProfile(); break;
            case 0: logout(); break;
            default: System.out.println("Invalid choice.");
        }
    }

    // BOOKING OPERATIONS
    /**
     * Handles the booking creation process.
     * Collects guest info, dates, room type, and confirms availability.
     */
    private void handleCreateBooking() {
        System.out.println("\n────────────── CREATE BOOKING ────────────────");
        try {
            System.out.print("Guest Email (or 'new'): ");
            String email = scanner.nextLine().trim();

            Guest guest;
            if (email.equalsIgnoreCase("new")) {
                guest = createNewGuestForBooking();
                if (guest == null) return;
            } else {
                User user = userManager.getUserByEmail(email);
                if (!(user instanceof Guest)) {
                    System.out.println("Guest not found.");
                    return;
                }
                guest = (Guest) user;
            }

            LocalDate arriveDate = getDateInput("Arrival (yyyy-MM-dd): ");
            LocalDate departDate = getDateInput("Departure (yyyy-MM-dd): ", arriveDate);

            if (!departDate.isAfter(arriveDate)) {
                System.out.println("Departure date must be after arrival date.");
                return;
            }

            RoomType roomType = selectRoomType();
            if (roomType == null) return;

            Booking booking = new Booking(arriveDate, departDate, guest);
            roomInventory.checkRoomAvailability(booking, roomType);

            if (booking.getBookingStatus() == BookingStatus.POSSIBLE && booking.getBookingRoom() != null) {
                long nights = java.time.temporal.ChronoUnit.DAYS.between(arriveDate, departDate);
                double total = nights * booking.getBookingRoom().getPricePerNight();

                System.out.println("\nRoom Found: " + booking.getBookingRoom().getRoomNumber());
                System.out.println("Nights: " + nights + " | Total: €" + String.format("%.2f", total));
                System.out.print("Confirm? (yes/no): ");

                if (scanner.nextLine().trim().equalsIgnoreCase("yes")) {
                    booking.setBookingStatus(BookingStatus.CONFIRMED);
                    booking.getBookingRoom().getBookingRegister().addBooking(booking);
                    booking.getBookingRoom().getBookingRegister().addDatesToRegister(booking);
                    guest.addBooking(String.valueOf(booking.getBookingID()));
                    System.out.println("Booking confirmed! ID: " + booking.getBookingID());
                }
            } else {
                System.out.println("No rooms available.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Creates a new guest for a booking.
     *
     * @return the created Guest, or null if creation failed
     */
    private Guest createNewGuestForBooking() {
        try {
            System.out.println("\n── New Guest ──");
            System.out.print("Name: ");
            String name = scanner.nextLine().trim();
            System.out.print("Email: ");
            String email = scanner.nextLine().trim();

            if (userManager.emailExists(email)) {
                System.out.println("Email already exists.");
                return null;
            }

            System.out.print("Phone: ");
            String phone = scanner.nextLine().trim();
            System.out.print("Password: ");
            String password = scanner.nextLine().trim();

            String userId = userManager.generateUserId("G");
            Guest guest = new Guest(userId, name, email, phone, password);
            userManager.addUser(guest);
            System.out.println("Guest created: " + userId);
            return guest;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    /**
     * Handles guest booking (same as create booking for guests).
     */
    private void handleGuestBooking() {
        handleCreateBooking(); // Same logic for guest
    }

    /**
     * Handles guest check-in process.
     */
    private void handleCheckIn() {
        System.out.println("\n────────────── CHECK-IN ────────────────");
        int bookingId = getIntInput("Booking ID: ");
        System.out.println("Guest checked in. Booking: " + bookingId);
    }

    /**
     * Handles guest check-out and payment processing.
     * Calculates charges, processes payment, and generates invoice.
     */
    private void handleCheckOutAndPayment() {
        System.out.println("\n────────────── CHECK-OUT & PAYMENT ────────────────");
        try {
            System.out.print("Guest Email: ");
            String email = scanner.nextLine().trim();
            User user = userManager.getUserByEmail(email);
            if (!(user instanceof Guest)) {
                System.out.println("Guest not found.");
                return;
            }
            Guest guest = (Guest) user;

            int roomNum = getIntInput("Room Number: ");
            int nights = getIntInput("Nights: ");
            double pricePerNight = getDoubleInput("Price/night: €");

            LocalDate checkOut = LocalDate.now();
            LocalDate checkIn = checkOut.minusDays(nights);
            Room room = new Room(roomNum, RoomType.DOUBLE, true, pricePerNight);
            Booking booking = new Booking(checkIn, checkOut, guest, room);

            double roomTotal = nights * pricePerNight;
            System.out.println("\n── Summary ──");
            System.out.println("Guest: " + guest.getName());
            System.out.println("Room charges: €" + String.format("%.2f", roomTotal));

            Payment payment = new Payment(booking.getBookingID(), roomTotal,
                    PaymentMethod.CREDIT_CARD, guest.getName());

            System.out.print("Add facilities? (yes/no): ");
            if (scanner.nextLine().trim().equalsIgnoreCase("yes")) {
                addFacilityChargesToPayment(payment);
            }

            PaymentMethod method = selectPaymentMethod();
            if (method == null) return;

            Invoice invoice = paymentManager.processPayment(booking, payment.getAmount(),
                    method, guest.getName());

            System.out.println("Itemizing additional charges on invoice...");
            boolean firstChargeSkipped = false;

            // We use getCharges() from the itemized Payment object
            for (Payment.LineItem charge : payment.getCharges()) {

                // The first LineItem is the Room Charge, which the Invoice constructor already handles.
                // We skip the first item to avoid double-charging the room.
                if (!firstChargeSkipped) {
                    firstChargeSkipped = true;
                    continue;
                }

                // Use the Invoice method to add the facility charge.
                // LineItem is assumed to have getDescription() and getCharge() methods.
                invoice.addAdditionalCharge(charge.getDescription(), charge.getAmount());
            }

            System.out.println("\nPayment processed!");
            System.out.println(invoice.generateFormattedInvoice());

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Adds facility charges to a payment.
     * Prompts for facility selection and quantity.
     *
     * @param payment the payment to add charges to
     */
    private void addFacilityChargesToPayment(Payment payment) {
        boolean adding = true;
        while (adding) {
            System.out.println("\n1. Spa (€50 each) 2. Gym (€15 each) 3. Pool (€10 each)");
            System.out.println("4. Golf (€60 each) 5. Dining (€25 each) 6. Breakfast (€12 each) 0. Done");
            int choice = getIntInput("Select: ");

            switch (choice) {
                case 1: // Spa
                    int spaQty = getIntInput("Quantity: ");
                    new Spa("Spa Treatment", 50.0, spaQty).applyToPayment(payment);
                    System.out.println("Added " + spaQty + " spa treatment(s): €" + (50.0 * spaQty));
                    break;

                case 2: // Gym
                    int gymQty = getIntInput("Quantity: ");
                    new Gym("Gym Access", 15.0, gymQty).applyToPayment(payment);
                    System.out.println("Added " + gymQty + " gym access(es): €" + (15.0 * gymQty));
                    break;

                case 3: // Pool
                    int poolQty = getIntInput("Quantity: ");
                    new SwimmingPool("Pool Access", 10.0, poolQty).applyToPayment(payment);
                    System.out.println("Added " + poolQty + " pool access(es): €" + (10.0 * poolQty));
                    break;

                case 4: // Golf
                    int golfQty = getIntInput("Quantity: ");
                    new GolfCourse("Golf Course", 60.0, golfQty).applyToPayment(payment);
                    System.out.println("Added " + golfQty + " golf round(s): €" + (60.0 * golfQty));
                    break;

                case 5: // Dining
                    int diningQty = getIntInput("Quantity (number of meals): ");
                    new Dining("Dining", 25.0, diningQty).applyToPayment(payment);
                    System.out.println("Added " + diningQty + " meal(s): €" + (25.0 * diningQty));
                    break;

                case 6: // Breakfast
                    int breakfastQty = getIntInput("Quantity: ");
                    // Breakfast uses 2 argument constructor and defaults to quantity of 1
                    for (int i = 0; i < breakfastQty; i++) {
                        new Breakfast("Breakfast", 12.0).applyToPayment(payment);
                    }
                    System.out.println("Added " + breakfastQty + " breakfast(s): €" + (12.0 * breakfastQty));
                    break;

                case 0:
                    adding = false;
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    /**
     * Handles adding facility charges to an existing booking.
     */
    private void handleAddFacilityCharges() {
        int bookingId = getIntInput("Booking ID: ");
        System.out.print("Guest Name: ");
        String name = scanner.nextLine().trim();
        Payment payment = new Payment(bookingId, 0, PaymentMethod.CREDIT_CARD, name);
        addFacilityChargesToPayment(payment);
        System.out.println("Charges added: €" + payment.getAmount());
    }

    /**
     * Checks room availability for given dates and type.
     */
    private void handleCheckAvailability() {
        System.out.println("\n────────────── CHECK AVAILABILITY ────────────────");
        try {
            LocalDate arrive = getDateInput("Arrival (yyyy-MM-dd): ");
            LocalDate depart = getDateInput("Departure (yyyy-MM-dd): ", arrive);
            RoomType type = selectRoomType();
            if (type == null) return;

            Guest temp = new Guest("T", "Temp", "t@t.com", "0", "t");
            Booking booking = new Booking(arrive, depart, temp);
            roomInventory.checkRoomAvailability(booking, type);

            if (booking.getBookingStatus() == BookingStatus.POSSIBLE && booking.getBookingRoom() != null) {
                System.out.println("Available: Room " + booking.getBookingRoom().getRoomNumber());
                System.out.println("Price: €" + booking.getBookingRoom().getPricePerNight() + "/night");
            } else {
                System.out.println("No rooms available.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Displays all bookings in the system.
     */
    private void handleViewAllBookings() {
        System.out.println("\n────────────── ALL BOOKINGS ────────────────");
        roomInventory.showAllBookings();
    }

    /**
     * Displays bookings for the current guest user.
     */
    private void handleViewMyBookings() {
        System.out.println("\n────────────── MY BOOKINGS ────────────────");
        Guest guest = (Guest) currentUser;
        List<String> bookings = guest.getBookingHistory();
        if (bookings.isEmpty()) {
            System.out.println("No bookings found.");
        } else {
            bookings.forEach(id -> System.out.println("Booking ID: " + id));
        }
    }

    /**
     * Handles booking modification.
     */
    private void handleModifyBooking() {
        System.out.println("\n────────────── MODIFY BOOKING ────────────────");
        int id = getIntInput("Booking ID: ");
        System.out.println("Modification functionality - Booking: " + id);
    }

    /**
     * Handles booking cancellation.
     */
    private void handleCancelBooking() {
        System.out.println("\n────────────── CANCEL BOOKING ────────────────");
        int id = getIntInput("Booking ID: ");
        System.out.print("Confirm? (yes/no): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("yes")) {
            System.out.println("Booking " + id + " cancelled.");
        }
    }

    /**
     * Handles overall booking management operations.
     */
    private void handleBookingManagement() {
        System.out.println("1. View All Bookings");
        System.out.println("2. Search Booking");
        System.out.println("0. Back");
        int choice = getIntInput("Choice: ");
        if (choice == 1) handleViewAllBookings();
    }

    // ROOM OPERATIONS
    /**
     * Displays all rooms in the inventory.
     */
    private void handleViewAllRooms() {
        System.out.println("\n────────────── ALL ROOMS ────────────────");
        roomInventory.displayAllRooms();
    }

    /**
     * Displays available rooms for booking.
     */
    private void handleViewAvailableRooms() {
        System.out.println("\n────────────── AVAILABLE ROOMS ────────────────");
        List<Room> available = roomInventory.getAvailableRooms();
        if (available.isEmpty()) {
            System.out.println("No rooms available.");
        } else {
            System.out.printf("%-10s %-15s %-10s%n", "Room#", "Type", "Price");
            System.out.println("─".repeat(40));
            available.forEach(r -> System.out.printf("%-10d %-15s €%-10.2f%n",
                    r.getRoomNumber(), r.getRoomType(), r.getPricePerNight()));
        }
    }

    /**
     * Handles room management operations (add, remove, update).
     */
    private void handleRoomManagement() {
        System.out.println("\n────────────── ROOM MANAGEMENT ────────────────");
        System.out.println("1. Add Room");
        System.out.println("2. Remove Room");
        System.out.println("3. Update Status");
        System.out.println("4. View All");
        System.out.println("0. Back");
        int choice = getIntInput("Choice: ");
        switch (choice) {
            case 1: handleAddRoom(); break;
            case 2: handleRemoveRoom(); break;
            case 3: handleUpdateRoomStatusAdmin(); break;
            case 4: handleViewAllRooms(); break;
        }
    }

    /**
     * Handles adding a new room to inventory.
     */
    private void handleAddRoom() {
        System.out.println("\n── Add Room ──");
        try {
            int num = getIntInput("Room Number: ");
            RoomType type = selectRoomType();
            if (type == null) return;
            double price = getDoubleInput("Price/night: €");
            roomInventory.addRoom(new Room(num, type, true, price));
            System.out.println("Room added!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Handles removing a room from inventory.
     */
    private void handleRemoveRoom() {
        System.out.println("\n── Remove Room ──");
        int num = getIntInput("Room Number: ");
        roomInventory.removeRoom(num);
    }

    /**
     * Handles updating room availability status.
     */
    private void handleUpdateRoomStatusAdmin() {
        System.out.println("\n── Update Room Status ──");
        int num = getIntInput("Room Number: ");
        System.out.println("1. Available");
        System.out.println("\n2. Unavailable");
        int choice = getIntInput("Status: ");
        roomInventory.updateRoomStatus(num, choice == 1);
    }

    // CLEANER OPERATIONS
    /**
     * Displays rooms assigned to a cleaner.
     *
     * @param cleaner the cleaner whose assignments to show
     */
    private void handleViewAssignedRooms(Cleaner cleaner) {
        System.out.println("\n────────────── ASSIGNED ROOMS ────────────────");
        List<String> rooms = cleaner.getAssignedRooms();
        if (rooms.isEmpty()) {
            System.out.println("No rooms assigned.");
        } else {
            rooms.forEach(r -> System.out.println("  • Room " + r));
        }
    }

    /**
     * Handles room status update by cleaner.
     *
     * @param cleaner the cleaner updating status
     */
    private void handleUpdateRoomStatus(Cleaner cleaner) {
        System.out.println("\n── Update Room Status ──");
        System.out.print("Room Number: ");
        String room = scanner.nextLine().trim();
        if (cleaner.isAssignedToRoom(room)) {
            System.out.println("Room " + room + " marked as clean.");
        } else {
            System.out.println("Not assigned to room " + room);
        }
    }

    // USER MANAGEMENT
    /**
     * Handles user management operations.
     */
    private void handleUserManagement() {
        System.out.println("\n────────────── USER MANAGEMENT ────────────────");
        System.out.println("1. View All");
        System.out.println("2. Add User");
        System.out.println("3. Search");
        System.out.println("4. By Role");
        System.out.println("0. Back");
        int choice = getIntInput("Choice: ");
        switch (choice) {
            case 1: handleViewAllUsers(); break;
            case 2: handleAddUser(); break;
            case 3: handleSearchUser(); break;
            case 4: handleViewUsersByRole(); break;
        }
    }

    /**
     * Displays all users in the system.
     */
    private void handleViewAllUsers() {
        System.out.println("\n────────────── ALL USERS ────────────────");
        List<User> users = userManager.getAllUsers();
        System.out.printf("%-10s %-20s %-25s %-15s%n", "ID", "Name", "Email", "Role");
        System.out.println("─".repeat(75));
        users.forEach(u -> System.out.printf("%-10s %-20s %-25s %-15s%n",
                u.getUserId(), u.getName(), u.getEmail(), u.getRole()));
        System.out.println("\nTotal: " + userManager.getTotalUserCount());
    }

    /**
     * Handles adding a new user to the system.
     */
    private void handleAddUser() {
        System.out.println("\n── Add User ──");
        try {
            System.out.print("Name: ");
            String name = scanner.nextLine().trim();
            System.out.print("Email: ");
            String email = scanner.nextLine().trim();
            if (userManager.emailExists(email)) {
                System.out.println("Email exists.");
                return;
            }
            System.out.print("Phone: ");
            String phone = scanner.nextLine().trim();
            System.out.print("Password: ");
            String password = scanner.nextLine().trim();

            System.out.println("\n1. Admin 2. Reception 3. Manager 4. Cleaner 5. Guest");
            int role = getIntInput("Role: ");

            User newUser = null;
            String userId;
            switch (role) {
                case 1:
                    userId = userManager.generateUserId("A");
                    newUser = new Admin(userId, name, email, phone, password);
                    break;
                case 2:
                    userId = userManager.generateUserId("R");
                    newUser = new ReceptionStaff(userId, name, email, phone, password);
                    break;
                case 3:
                    System.out.print("Department: ");
                    String dept = scanner.nextLine().trim();
                    userId = userManager.generateUserId("M");
                    newUser = new Manager(userId, name, email, phone, password, dept);
                    break;
                case 4:
                    userId = userManager.generateUserId("C");
                    newUser = new Cleaner(userId, name, email, phone, password);
                    break;
                case 5:
                    userId = userManager.generateUserId("G");
                    newUser = new Guest(userId, name, email, phone, password);
                    break;
            }

            if (newUser != null) {
                userManager.addUser(newUser);
                System.out.println("User created: " + newUser.getUserId());
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Handles searching for users by name.
     */
    private void handleSearchUser() {
        System.out.println("\n── Search User ──");
        System.out.print("Name: ");
        String search = scanner.nextLine().trim();
        List<User> results = userManager.searchUsersByName(search);
        if (results.isEmpty()) {
            System.out.println("No users found.");
        } else {
            results.forEach(u -> System.out.println(u.getUserId() + " - " + u.getName()));
        }
    }

    /**
     * Displays users filtered by role.
     */
    private void handleViewUsersByRole() {
        System.out.println("\n1. Admin 2. Reception 3. Manager 4. Cleaner 5. Guest");
        int choice = getIntInput("Role: ");
        UserRole role = null;
        switch (choice) {
            case 1: role = UserRole.ADMIN; break;
            case 2: role = UserRole.RECEPTION_STAFF; break;
            case 3: role = UserRole.MANAGER; break;
            case 4: role = UserRole.CLEANER; break;
            case 5: role = UserRole.GUEST; break;
        }
        if (role != null) {
            List<User> users = userManager.getUsersByRole(role);
            System.out.println("\n" + role + " Users:");

            users.forEach(u -> {
                String output = u.getUserId() + " - " + u.getName();

                // Check if the user is a Manager to include the department
                if (u instanceof Manager) {
                    Manager manager = (Manager) u; // Cast the User object to a Manager object
                    output += " (Dept: " + manager.getDepartment() + ")";
                }

                System.out.println(output);
            });

            System.out.println("Total: " + users.size());
        }
    }

    /**
     * Displays all guest users.
     */
    private void handleViewAllGuests() {
        System.out.println("\n────────────── ALL GUESTS ────────────────");
        List<User> guests = userManager.getUsersByRole(UserRole.GUEST);
        if (guests.isEmpty()) {
            System.out.println("No guests.");
        } else {
            System.out.printf("%-10s %-25s %-30s%n", "ID", "Name", "Email");
            System.out.println("─".repeat(70));
            guests.forEach(u -> System.out.printf("%-10s %-25s %-30s%n",
                    u.getUserId(), u.getName(), u.getEmail()));
        }
    }

    /**
     * Displays all staff users.
     */
    private void handleViewStaff() {
        System.out.println("\n────────────── STAFF ────────────────");
        System.out.println("\nReception: " + userManager.getUsersByRole(UserRole.RECEPTION_STAFF).size());
        System.out.println("Managers: " + userManager.getUsersByRole(UserRole.MANAGER).size());
        System.out.println("Cleaners: " + userManager.getUsersByRole(UserRole.CLEANER).size());
        System.out.println("Admins: " + userManager.getUsersByRole(UserRole.ADMIN).size());
    }

    // PAYMENT OPERATIONS
    /**
     * Handles payment management operations.
     */
    private void handlePaymentManagement() {
        System.out.println("\n────────────── PAYMENT MANAGEMENT ────────────────");
        System.out.println("1. View All");
        System.out.println("2. Details");
        System.out.println("3. Refund");
        System.out.println("4. Stats");
        System.out.println("5. Invoice");
        System.out.println("0. Back");
        int choice = getIntInput("Choice: ");
        switch (choice) {
            case 1: handleViewAllPayments(); break;
            case 2: handleViewPaymentDetails(); break;
            case 3: handleProcessRefund(); break;
            case 4: handlePaymentStatistics(); break;
            case 5: handleViewInvoice(); break;
        }
    }

    /**
     * Displays all payments.
     */
    private void handleViewAllPayments() {
        System.out.println("\n────────────── ALL PAYMENTS ────────────────");
        List<Payment> payments = paymentManager.getAllPayments();
        if (payments.isEmpty()) {
            System.out.println("No payments.");
        } else {
            System.out.printf("%-12s %-15s €%-10s %-12s%n", "ID", "Guest", "Amount", "Status");
            System.out.println("─".repeat(55));
            payments.forEach(p -> System.out.printf("%-12s %-15s €%-10.2f %-12s%n",
                    p.getPaymentId(), p.getGuestName(), p.getAmount(), p.getPaymentStatus()));
        }
    }

    /**
     * Displays details of a specific payment.
     */
    private void handleViewPaymentDetails() {
        System.out.print("Payment ID: ");
        String id = scanner.nextLine().trim();
        paymentManager.displayPaymentSummary(id);
    }

    /**
     * Processes a payment refund.
     */
    private void handleProcessRefund() {
        System.out.print("Payment ID: ");
        String id = scanner.nextLine().trim();
        System.out.print("Confirm? (yes/no): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("yes")) {
            try {
                paymentManager.processRefund(id);
            } catch (Exception e) {
                System.out.println("Refund failed: " + e.getMessage());
            }
        }
    }

    /**
     * Displays payment statistics and totals.
     */
    private void handlePaymentStatistics() {
        System.out.println(paymentManager.getPaymentStatistics());
    }

    /**
     * Displays a specific invoice.
     */
    private void handleViewInvoice() {
        System.out.print("Invoice Number: ");
        String num = scanner.nextLine().trim();
        paymentManager.displayInvoice(num);
    }

    // REPORTS & ANALYTICS
    /**
     * Handles system reports menu.
     */
    private void handleSystemReports() {
        System.out.println("\n────────────── REPORTS ────────────────");
        System.out.println("1. Occupancy");
        System.out.println("2. Revenue");
        System.out.println("3. Guests");
        System.out.println("4. Booking");
        System.out.println("0. Back");
        int choice = getIntInput("Choice: ");
        switch (choice) {
            case 1: handleRoomOccupancyReport(); break;
            case 2: handleRevenueReport(); break;
            case 3: handleGuestStatistics(); break;
            case 4: handleBookingStatistics(); break;
        }
    }

    /**
     * Displays room occupancy report.
     */
    private void handleRoomOccupancyReport() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║     ROOM OCCUPANCY REPORT              ║");
        System.out.println("╠════════════════════════════════════════╣");
        int total = roomInventory.getTotalRooms();
        int booked = roomInventory.getBookedRooms();
        int available = total - booked;
        double rate = total > 0 ? (booked * 100.0 / total) : 0;
        System.out.printf("║  Total:      %3d                       ║%n", total);
        System.out.printf("║  Occupied:   %3d                       ║%n", booked);
        System.out.printf("║  Available:  %3d                       ║%n", available);
        System.out.printf("║  Rate:       %5.1f%%                    ║%n", rate);
        System.out.println("╚════════════════════════════════════════╝");
    }

    /**
     * Displays revenue report.
     */
    private void handleRevenueReport() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║       REVENUE REPORT                   ║");
        System.out.println("╠════════════════════════════════════════╣");
        double revenue = paymentManager.getTotalRevenue();
        double refunds = paymentManager.getTotalRefunds();
        double net = revenue - refunds;
        System.out.printf("║  Revenue:    €%8.2f                 ║%n", revenue);
        System.out.printf("║  Refunds:    €%8.2f                 ║%n", refunds);
        System.out.printf("║  Net:        €%8.2f                 ║%n", net);
        System.out.println("╚════════════════════════════════════════╝");
    }

    /**
     * Displays guest statistics.
     */
    private void handleGuestStatistics() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║      GUEST STATISTICS                  ║");
        System.out.println("╠════════════════════════════════════════╣");
        int guests = userManager.getUsersByRole(UserRole.GUEST).size();
        System.out.printf("║  Total Guests: %4d                   ║%n", guests);
        System.out.println("╚════════════════════════════════════════╝");
    }

    /**
     * Displays booking statistics.
     */
    private void handleBookingStatistics() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║    BOOKING STATISTICS                  ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║  Under development                     ║");
        System.out.println("╚════════════════════════════════════════╝");
    }

    /**
     * Displays analytics overview.
     */
    private void handleViewAnalytics() {
        handleRoomOccupancyReport();
        System.out.println();
        handleRevenueReport();
    }

    // PROFILE & SUPPORT
    /**
     * Displays current user's profile information.
     */
    private void handleMyProfile() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║         MY PROFILE                     ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.printf("║  ID:    %-30s ║%n", currentUser.getUserId());
        System.out.printf("║  Name:  %-30s ║%n", currentUser.getName());
        System.out.printf("║  Email: %-30s ║%n", currentUser.getEmail());
        System.out.printf("║  Role:  %-30s ║%n", currentUser.getRole());
        System.out.println("╚════════════════════════════════════════╝");
    }

    /**
     * Handles guest support request.
     */
    private void handleRequestSupport() {
        System.out.println("\n────────────── SUPPORT ────────────────");
        System.out.print("Describe issue: ");
        scanner.nextLine();
        System.out.println("Request submitted. ID: SR-" + System.currentTimeMillis());
    }

    // UTILITY METHODS
    /**
     * Logs out the current user and clears session.
     */
    public void logout() {
        if (currentUser != null) {
            System.out.println("\nLogged out: " + currentUser.getName());
            currentUser = null;
        } else {
            System.out.println("\nNo user currently logged in.");
        }
    }

    /**
     * Displays goodbye message when exiting system.
     */
    public void displayGoodbyeMessage() {
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║     Thank you for using Group 6 Hotel System!             ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
    }

    /**
     * Prompts for and validates integer input.
     *
     * @param prompt the prompt message
     * @return the validated integer
     */
    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid number.");
            }
        }
    }

    /**
     * Prompts for and validates double input.
     *
     * @param prompt the prompt message
     * @return the validated double
     */
    private double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid number.");
            }
        }
    }

    /**
     * Prompts for and validates date input.
     *
     * @param prompt the prompt message
     * @return the validated date
     */
    private LocalDate getDateInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                LocalDate date = LocalDate.parse(scanner.nextLine().trim());
                if (date.isBefore(LocalDate.now())) {
                    System.out.println("Date cannot be in past.");
                    continue;
                }
                return date;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid format. Use yyyy-MM-dd");
            }
        }
    }

    /**
     * Prompts for and validates date input with minimum date constraint.
     *
     * @param prompt the prompt message
     * @param minDate the minimum allowed date
     * @return the validated date
     */
    private LocalDate getDateInput(String prompt, LocalDate minDate) {
        while (true) {
            try {
                System.out.print(prompt);
                LocalDate date = LocalDate.parse(scanner.nextLine().trim());
                if (minDate != null && date.isBefore(minDate)) {
                    System.out.println("Must be after " + minDate);
                    continue;
                }
                return date;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid format. Use yyyy-MM-dd");
            }
        }
    }

    /**
     * Displays room type selection menu and gets user choice.
     *
     * @return the selected RoomType, or null if cancelled
     */
    private RoomType selectRoomType() {
        System.out.println("\n1. Single (€120) 2. Double (€180) 3. Deluxe (€250)");
        System.out.println("4. Family (€280) 5. Suite (€350) 6. Presidential (€500)");
        System.out.println("0. Cancel");
        int choice = getIntInput("Room type: ");
        switch (choice) {
            case 1: return RoomType.SINGLE;
            case 2: return RoomType.DOUBLE;
            case 3: return RoomType.DELUXE;
            case 4: return RoomType.FAMILY;
            case 5: return RoomType.SUITE;
            case 6: return RoomType.PRESIDENTIAL;
            default: return null;
        }
    }

    /**
     * Displays payment method selection menu and gets user choice.
     *
     * @return the selected PaymentMethod, or null if cancelled
     */
    private PaymentMethod selectPaymentMethod() {
        System.out.println("\n1. Cash 2. Credit 3. Debit 4. Online 5. Mobile 0. Cancel");
        System.out.println("0. Cancel");
        int choice = getIntInput("Payment method: ");
        switch (choice) {
            case 1: return PaymentMethod.CASH;
            case 2: return PaymentMethod.CREDIT_CARD;
            case 3: return PaymentMethod.DEBIT_CARD;
            case 4: return PaymentMethod.ONLINE_BANKING;
            case 5: return PaymentMethod.MOBILE_PAYMENT;
            default: return null;
        }
    }

    /**
     * Gets the current logged-in user.
     *
     * @return the current User, or null if not logged in
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Sets the current user.
     *
     * @param user the user to set as current
     */
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    /**
     * Checks if the UI is currently running.
     *
     * @return true if running, false otherwise
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Sets the running state.
     *
     * @param running the running state
     */
    public void setRunning(boolean running) {
        this.running = running;
    }

    /**
     * Gets the user manager instance.
     *
     * @return the UserManager
     */
    public UserManager getUserManager() {
        return userManager;
    }

    /**
     * Gets the room inventory instance.
     *
     * @return the RoomInventoryImpl
     */
    public RoomInventoryImpl getRoomInventory() {
        return roomInventory;
    }

    /**
     * Gets the payment manager instance.
     *
     * @return the PaymentManager
     */
    public PaymentManager getPaymentManager() {
        return paymentManager;
    }

    /**
     * Handles the user's choice from login menu.
     *
     * @param choice the menu option selected
     */
    public void handleLoginMenuChoice(int choice) {
        switch (choice) {
            case 1: handleLogin(); break;
            case 2: handleGuestRegistration(); break;
            case 3: displayHotelInformation(); break;
            case 0: running = false; break;
            default: System.out.println("Invalid choice.");
        }
    }

    /**
     * Main entry point for the application.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        HotelManagementUI ui = new HotelManagementUI();
        ui.start();
    }
}