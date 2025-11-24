package com.hotel;

import com.hotel.Model.Booking;
import com.hotel.Model.Guest;
import com.hotel.Model.Room;
import com.hotel.Model.RoomType;
import com.hotel.Service.BookingRegister;
import com.hotel.Service.RoomInventoryImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookingRegisterTest {

    private List<Room> rooms;
    private Room room1;
    private Room room2;
    private Room room3;
    private BookingRegister bookingRegister;
    private Guest guest1;
    private Guest guest2;
    private Guest guest3;
    private Booking booking1;
    private Booking booking2;

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {

        Room room1 = new Room(101, RoomType.SINGLE, true, 199);
        Room room2 = new Room(102, RoomType.SINGLE, true, 199);
        Room room3 = new Room(103, RoomType.SINGLE, true, 199);
        BookingRegister bookingRegister = new BookingRegister();
        RoomType roomType = RoomType.SINGLE;
        Guest guest1 = new Guest("1", "Jack Torrance", "jack@axemail.com", "12345", "guest1");
        Guest guest2 = new Guest("2", "Danny Torrance", "danny@redrum.com", "12345", "guest2");
        Guest guest3 = new Guest("3", "Wendy Torrance", "wendy@shinemail.com", "12345", "guest3");
        Booking booking1 = new Booking(LocalDate.of(2025, 11,23), LocalDate.of(2025,11,30), guest1, room1);
        Booking booking2 = new Booking(LocalDate.of(2025, 11,23), LocalDate.of(2025,11,30), guest2, room2);

        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {  System.setOut(originalOut);
    }

    @Test
    void getFormattedDate() {
    }

    @Test
    void testShowRooms() {

        ArrayList<Room> roomList = new ArrayList<>();

        room1 = new Room(101, RoomType.SINGLE, true, 199);
//        room2 = new Room(102, RoomType.SINGLE, true, 237);
//        room3 = new Room(201, RoomType.DOUBLE, true, 237);
        roomList.add(room1);
        room1.getBookingRegister().showRooms();
        String output = outputStream.toString();
       // assertEquals("101",output);

    }

    @Test
    void testAddBooking() {

        BookingRegister bookingRegister = new BookingRegister();
        Room room1 = new Room(101, RoomType.SINGLE, true, 199);
        Room room2 = new Room(102, RoomType.SINGLE, true, 199);
        Guest guest1 = new Guest("1", "Jack Torrance", "jack@axemail.com", "12345", "guest1");
        Guest guest2 = new Guest("2", "Danny Torrance", "danny@redrum.com", "12345", "guest2");

        Booking booking1 = new Booking(LocalDate.of(2025, 11,23), LocalDate.of(2025,11,30), guest1, room1);
        Booking booking2 = new Booking(LocalDate.of(2025, 11,23), LocalDate.of(2025,11,30), guest2, room2);

        room1.getBookingRegister().addBooking(booking1);

        String output = outputStream.toString();
        assertTrue(output.contains("Jack Torrance"));
    }

    @Test
    void testShowBookings() {

        BookingRegister bookingRegister = new BookingRegister();
        Room room1 = new Room(101, RoomType.SINGLE, true, 199);
        Room room2 = new Room(102, RoomType.SINGLE, true, 199);
        Guest guest1 = new Guest("1", "Jack Torrance", "jack@axemail.com", "12345", "guest1");
        Guest guest2 = new Guest("2", "Danny Torrance", "danny@redrum.com", "12345", "guest2");

        Booking booking1 = new Booking(LocalDate.of(2025, 11,23), LocalDate.of(2025,11,30), guest1, room1);
        Booking booking2 = new Booking(LocalDate.of(2025, 12,1), LocalDate.of(2025,12,3), guest2, room1);

        room1.getBookingRegister().addBooking(booking1);
        room1.getBookingRegister().addBooking(booking2);

        room1.getBookingRegister().showBookings();
        String output = outputStream.toString();
        assertTrue(output.contains("Jack Torrance"));
        assertTrue(output.contains("Danny Torrance"));
    }

    @Test
    void testShowGuests() {

        BookingRegister bookingRegister = new BookingRegister();
        Room room1 = new Room(101, RoomType.SINGLE, true, 199);
        Room room2 = new Room(102, RoomType.SINGLE, true, 199);
        Guest guest1 = new Guest("1", "Jack Torrance", "jack@axemail.com", "12345", "guest1");
        Guest guest2 = new Guest("2", "Danny Torrance", "danny@redrum.com", "12345", "guest2");

        Booking booking1 = new Booking(LocalDate.of(2025, 11,23), LocalDate.of(2025,11,30), guest1, room1);
        Booking booking2 = new Booking(LocalDate.of(2025, 12,1), LocalDate.of(2025,12,3), guest2, room1);

        room1.getBookingRegister().addBooking(booking1);
        room1.getBookingRegister().addBooking(booking2);

        room1.getBookingRegister().showGuests();
        String output = outputStream.toString();
        assertTrue(output.contains("Jack Torrance"));
        assertTrue(output.contains("Danny Torrance"));
    }

    @Test
    void addDatesToRegister() {
        BookingRegister bookingRegister = new BookingRegister();
        Room room1 = new Room(101, RoomType.SINGLE, true, 199);
        Room room2 = new Room(102, RoomType.SINGLE, true, 199);
        Guest guest1 = new Guest("1", "Jack Torrance", "jack@axemail.com", "12345", "guest1");
        Guest guest2 = new Guest("2", "Danny Torrance", "danny@redrum.com", "12345", "guest2");

        Booking booking1 = new Booking(LocalDate.of(2025, 11,23), LocalDate.of(2025,11,30), guest1, room1);
        Booking booking2 = new Booking(LocalDate.of(2025, 12,1), LocalDate.of(2025,12,3), guest2, room1);

        room1.getBookingRegister().addBooking(booking1);
        room1.getBookingRegister().addBooking(booking2);

        //room3.getBookingRegister().addBooking(booking5);
        room1.getBookingRegister().addDatesToRegister(booking1);
        room1.getBookingRegister().addDatesToRegister(booking2);

        room1.getBookingRegister().showBookings();
        String output = outputStream.toString();
        assertTrue(output.contains("2025-11-23"));
        assertTrue(output.contains("2025-11-30"));
        assertTrue(output.contains("2025-12-01"));
        assertTrue(output.contains("2025-12-03"));
    }

    @Test
    void testCheckForBookingOverlap() {

        Room room1 = new Room(101, RoomType.SINGLE, true, 199);
//        Room room2 = new Room(102, RoomType.SINGLE, true, 199);
//        Room room3 = new Room(103, RoomType.SINGLE, true, 199);

        //RoomType roomType = RoomType.SINGLE;
        Guest guest1 = new Guest("1", "Jack Torrance", "jack@axemail.com", "12345", "guest1");
        Guest guest2 = new Guest("2", "Danny Torrance", "danny@redrum.com", "12345", "guest2");
        Guest guest3 = new Guest("3", "Wendy Torrance", "wendy@shinemail.com", "12345", "guest3");
        Booking booking1 = new Booking(LocalDate.of(2025, 11,23), LocalDate.of(2025,11,30), guest1, room1);
        Booking booking2 = new Booking(LocalDate.of(2025, 11,23), LocalDate.of(2025,11,30), guest2);
        Booking booking3 = new Booking(LocalDate.of(2025, 12,1), LocalDate.of(2025,12,3), guest2, room1);
//        roomInventory.addRoom(room1);
//        roomInventory.addRoom(room2);
//        roomInventory.addRoom(room3);
        room1.getBookingRegister().addBooking(booking1);
        room1.getBookingRegister().addDatesToRegister(booking1);

        assertTrue(room1.getBookingRegister().checkForBookingOverlap(booking2, room1));
        assertFalse(room1.getBookingRegister().checkForBookingOverlap(booking3, room1));


    }

    @Test
    void showBookedDates() {
    }
}