package com.hotel;

import com.hotel.manager.*;
import com.hotel.model.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Main system class that coordinates all hotel operations.
 * This is the entry point for the application and API layer.
 *
 * @author dev_Amru
 * @version 1.0
 */
public class HotelSystem {
    private final BookingManager bookingManager;
    private final RoomManager roomManager;
    private final UserManager userManager;
    private final PaymentManager paymentManager;
    private User currentUser;

    /**
     * Constructs a new HotelSystem and initializes all managers.
     */
    public HotelSystem() {
        this.bookingManager = new BookingManager();
        this.roomManager = new RoomManager();
        this.userManager = new UserManager();
        this.paymentManager = new PaymentManager();
        this.currentUser = null;
    }

    /**
     * Initializes the system with sample data.
     * Creates default admin, rooms, and test data.
     */
    public void initialize() {
        // Create default admin
        Admin admin = new Admin("ADM001", "System Admin", "admin@hotel.com",
                "0871234567", "admin123");
        userManager.addUser(admin);

        // Create sample rooms
        roomManager.addRoom(new StandardRoom("101", 80.0));
        roomManager.addRoom(new StandardRoom("102", 80.0));
        roomManager.addRoom(new SuperiorRoom("201", 120.0));
        roomManager.addRoom(new SuperiorRoom("202", 120.0));
        roomManager.addRoom(new SuiteRoom("301", 200.0));

        // Create sample staff
        ReceptionStaff reception = new ReceptionStaff("REC001", "John Doe",
                "john@hotel.com", "0871111111", "pass123");
        Manager manager = new Manager("MGR001", "Jane Smith", "jane@hotel.com",
                "0872222222", "pass123", "Operations");
        Cleaner cleaner = new Cleaner("CLN001", "Mary Johnson", "mary@hotel.com",
                "0873333333", "pass123");

        userManager.addUser(reception);
        userManager.addUser(manager);
        userManager.addUser(cleaner);

        System.out.println("Hotel System initialized successfully");
    }

    /**
     * Authenticates a user and logs them into the system.
     *
     * @param email user's email
     * @param password user's password
     * @return true if login successful
     * @throws IllegalArgumentException if credentials are invalid
     */
    public boolean login(String email, String password) {
        if (email == null || password == null) {
            throw new IllegalArgumentException("Email and password cannot be null");
        }

        User user = userManager.authenticateUser(email, password);
        if (user != null) {
            this.currentUser = user;
            System.out.println("Login successful: " + user.getName());
            return true;
        }
        return false;
    }

    /**
     * Logs out the current user.
     */
    public void logout() {
        if (currentUser != null) {
            System.out.println("Logout successful: " + currentUser.getName());
            this.currentUser = null;
        }
    }

    /**
     * Gets the currently logged-in user.
     *
     * @return current user or null if not logged in
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Checks if a user is currently logged in.
     *
     * @return true if user is logged in
     */
    public boolean isLoggedIn() {
        return currentUser != null;
    }

    /**
     * Registers a new guest in the system (staff only - no guest login).
     *
     * @param name guest's name
     * @param email guest's email
     * @param phone guest's phone
     * @return the created guest
     * @throws IllegalArgumentException if email already exists
     * @throws IllegalStateException if user doesn't have permission
     */
    public Guest registerGuest(String name, String email, String phone) {
        if (!isLoggedIn() || !(currentUser instanceof Admin || currentUser instanceof ReceptionStaff)) {
            throw new IllegalStateException("Only staff can register guests");
        }

        String guestId = userManager.generateUserId("G");
        // Guests don't have passwords - they are managed by staff
        Guest guest = new Guest(guestId, name, email, phone, null);
        userManager.addUser(guest);
        return guest;
    }

    /**
     * Searches for available rooms within a date range.
     *
     * @param checkIn check-in date
     * @param checkOut check-out date
     * @param roomType desired room type (null for all types)
     * @return list of available rooms
     */
    public List<Room> searchAvailableRooms(LocalDate checkIn, LocalDate checkOut, RoomType roomType) {
        return roomManager.getAvailableRooms(checkIn, checkOut, roomType, bookingManager);
    }

    /**
     * Creates a new booking for the current user.
     *
     * @param roomNumber room to book
     * @param checkIn check-in date
     * @param checkOut check-out date
     * @param includeBreakfast whether to include breakfast
     * @return the created booking
     * @throws IllegalStateException if user not logged in or not a guest
     * @throws IllegalArgumentException if room not available
     */
    public Booking createBooking(String roomNumber, LocalDate checkIn,
                                 LocalDate checkOut, boolean includeBreakfast) {
        if (!isLoggedIn()) {
            throw new IllegalStateException("Must be logged in to create booking");
        }
        if (!(currentUser instanceof Guest)) {
            throw new IllegalStateException("Only guests can create bookings");
        }

        Room room = roomManager.getRoom(roomNumber);
        if (room == null) {
            throw new IllegalArgumentException("Room not found: " + roomNumber);
        }

        long nights = java.time.temporal.ChronoUnit.DAYS.between(checkIn, checkOut);
        double totalPrice = room.calculatePrice((int) nights, includeBreakfast);

        Booking booking = bookingManager.createBooking(currentUser.getUserId(), roomNumber,
                checkIn, checkOut, totalPrice, includeBreakfast);

        ((Guest) currentUser).addBooking(booking.getBookingId());
        room.updateStatus(RoomStatus.RESERVED);

        return booking;
    }

    /**
     * Creates a booking for a specified guest (staff functionality).
     * Allows Admin or ReceptionStaff to create bookings for guests. Guests may create bookings for themselves.
     *
     * @param guestId ID of the guest the booking is for
     * @param roomNumber room to book
     * @param checkIn check-in date
     * @param checkOut check-out date
     * @param includeBreakfast whether breakfast is included
     * @return the created booking
     */
    public Booking createBooking(String guestId, String roomNumber, LocalDate checkIn,
                                 LocalDate checkOut, boolean includeBreakfast) {
        if (!isLoggedIn()) {
            throw new IllegalStateException("Must be logged in to create booking");
        }

        // Allow staff (Admin or ReceptionStaff) to create bookings for guests
        boolean isStaffCreating = (currentUser instanceof Admin) || (currentUser instanceof ReceptionStaff);

        // Guests may create bookings only for themselves
        if (!isStaffCreating) {
            if (!(currentUser instanceof Guest)) {
                throw new IllegalStateException("Only staff or the guest can create a booking for this guest");
            }
            if (!currentUser.getUserId().equals(guestId)) {
                throw new IllegalStateException("Guests may only create bookings for themselves");
            }
        }

        // Validate guest exists
        User guestUser = userManager.getUser(guestId);
        if (guestUser == null || !(guestUser instanceof Guest)) {
            throw new IllegalArgumentException("Guest not found: " + guestId);
        }

        Room room = roomManager.getRoom(roomNumber);
        if (room == null) {
            throw new IllegalArgumentException("Room not found: " + roomNumber);
        }

        long nights = java.time.temporal.ChronoUnit.DAYS.between(checkIn, checkOut);
        double totalPrice = room.calculatePrice((int) nights, includeBreakfast);

        Booking booking = bookingManager.createBooking(guestId, roomNumber,
                checkIn, checkOut, totalPrice, includeBreakfast);

        ((Guest) guestUser).addBooking(booking.getBookingId());
        room.updateStatus(RoomStatus.RESERVED);

        return booking;
    }

    /**
     * Cancels an existing booking.
     *
     * @param bookingId booking to cancel
     * @throws IllegalStateException if user doesn't have permission
     */
    public void cancelBooking(String bookingId) {
        if (!isLoggedIn()) {
            throw new IllegalStateException("Must be logged in to cancel booking");
        }

        Booking booking = bookingManager.getBooking(bookingId);
        if (booking == null) {
            throw new IllegalArgumentException("Booking not found");
        }

        // Check permissions
        boolean canCancel = currentUser instanceof Admin ||
                (currentUser instanceof Guest &&
                        booking.getGuestId().equals(currentUser.getUserId())) ||
                currentUser instanceof ReceptionStaff;

        if (!canCancel) {
            throw new IllegalStateException("No permission to cancel this booking");
        }

        bookingManager.cancelBooking(bookingId);

        // Update room status
        Room room = roomManager.getRoom(booking.getRoomNumber());
        if (room != null) {
            room.updateStatus(RoomStatus.AVAILABLE);
        }
    }

    /**
     * Checks in a guest (staff only).
     *
     * @param bookingId booking to check in
     * @throws IllegalStateException if user doesn't have permission
     */
    public void checkInGuest(String bookingId) {
        if (!hasPermission("CHECK_IN_GUEST")) {
            throw new IllegalStateException("No permission to check in guests");
        }

        Booking booking = bookingManager.getBooking(bookingId);
        if (booking == null) {
            throw new IllegalArgumentException("Booking not found");
        }

        booking.setStatus(BookingStatus.CHECKED_IN);
        Room room = roomManager.getRoom(booking.getRoomNumber());
        if (room != null) {
            room.updateStatus(RoomStatus.OCCUPIED);
        }
    }

    /**
     * Checks out a guest (staff only).
     *
     * @param bookingId booking to check out
     * @return payment object for the booking
     * @throws IllegalStateException if user doesn't have permission
     */
    public Payment checkOutGuest(String bookingId) {
        if (!hasPermission("CHECK_OUT_GUEST")) {
            throw new IllegalStateException("No permission to check out guests");
        }

        Booking booking = bookingManager.getBooking(bookingId);
        if (booking == null) {
            throw new IllegalArgumentException("Booking not found");
        }

        booking.setStatus(BookingStatus.CHECKED_OUT);
        Room room = roomManager.getRoom(booking.getRoomNumber());
        if (room != null) {
            room.updateStatus(RoomStatus.CLEANING);
        }

        // Create payment
        return paymentManager.createPayment(bookingId, booking.getTotalPrice(),
                PaymentMethod.CREDIT_CARD);
    }

    /**
     * Generates a report (manager/admin only).
     *
     * @param reportType type of report to generate
     * @return the generated report
     * @throws IllegalStateException if user doesn't have permission
     */
    public Report generateReport(String reportType) {
        if (!hasPermission("GENERATE_REPORTS")) {
            throw new IllegalStateException("No permission to generate reports");
        }

        Report report = new Report(reportType);

        switch (reportType) {
            case "OCCUPANCY":
                report.addData("totalRooms", roomManager.getAllRooms().size());
                report.addData("occupiedRooms", roomManager.getRoomsByStatus(RoomStatus.OCCUPIED).size());
                report.addData("availableRooms", roomManager.getRoomsByStatus(RoomStatus.AVAILABLE).size());
                break;
            case "BOOKINGS":
                report.addData("totalBookings", bookingManager.getAllBookings().size());
                report.addData("upcomingBookings", bookingManager.getUpcomingBookings().size());
                break;
            default:
                throw new IllegalArgumentException("Unknown report type: " + reportType);
        }

        return report;
    }

    /**
     * Checks if current user has a specific permission.
     *
     * @param permission permission to check
     * @return true if user has permission
     */
    private boolean hasPermission(String permission) {
        return currentUser != null && currentUser.hasPermission(permission);
    }

    // Getters for managers (for testing and API access)
    public BookingManager getBookingManager() { return bookingManager; }
    public RoomManager getRoomManager() { return roomManager; }
    public UserManager getUserManager() { return userManager; }
    public PaymentManager getPaymentManager() { return paymentManager; }
}
