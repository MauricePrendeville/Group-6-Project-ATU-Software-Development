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

class RoomInventoryImplTest {

    private RoomInventoryImpl roomInventory;
    private RoomInventoryImpl roomInventory2;
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
        RoomInventoryImpl roomInventory = new RoomInventoryImpl();
        List<Room> rooms = new ArrayList<>();
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
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void testAddRoom() {
        roomInventory = new RoomInventoryImpl();
        roomInventory.addRoom(new Room(237, RoomType.SINGLE, true, 237));
        String output = outputStream.toString();
        assertTrue(output.contains("Room added successfully: "));
    }

    @Test
    void testRemoveRoom() {
        roomInventory = new RoomInventoryImpl();
        room1 = new Room(101, RoomType.SINGLE, true, 199);
        roomInventory.addRoom(room1);
        roomInventory.removeRoom(101);
        String output = outputStream.toString();
        assertTrue(output.contains("removed successfully."));
        roomInventory.removeRoom(237);
        String output2 = outputStream.toString();
        assertTrue(output2.contains("not found"));
    }

    @Test
    void testUpdateRoomStatus() {

        roomInventory = new RoomInventoryImpl();
        room1 = new Room(101, RoomType.SINGLE, true, 199);
        room2 = new Room(102, RoomType.SINGLE, true, 237);
        room3 = new Room(201, RoomType.DOUBLE, true, 237);

        roomInventory.addRoom(room1);
        roomInventory.addRoom(room2);
        roomInventory.addRoom(room3);

        roomInventory.updateRoomStatus(101,false);
        String output = outputStream.toString();
        assertTrue(output.contains("Room 101 availability updated to: false"));

        roomInventory.updateRoomStatus(237,false);
        String output2 = outputStream.toString();
        assertTrue(output2.contains("Room 237 not found in inventory."));

    }

    @Test
    void testGetAvailableRooms() {
        roomInventory = new RoomInventoryImpl();
        room1 = new Room(101, RoomType.SINGLE, true, 199);
        room2 = new Room(102, RoomType.SINGLE, true, 237);
        room3 = new Room(201, RoomType.DOUBLE, true, 237);

        roomInventory.addRoom(room1);
        roomInventory.addRoom(room2);
        roomInventory.addRoom(room3);

        List<Room> rooms = roomInventory.getAvailableRooms();
        Room expectedRoom = new Room(102, RoomType.SINGLE, true, 237);
        assertEquals(3, rooms.size());
    }

    @Test
    void testSearchRoomByType() {
        RoomInventoryImpl roomInventory = new RoomInventoryImpl();
        //BookingRegister bookingRegister = new BookingRegister();
        List<Room> rooms = new ArrayList<>();
        Room room1 = new Room(101, RoomType.SINGLE, true, 199);
        Room room2 = new Room(201, RoomType.DOUBLE, true, 250);
        Room room3 = new Room(301, RoomType.SUITE, true, 360);

        roomInventory.addRoom(room1);
        roomInventory.addRoom(room2);
        roomInventory.addRoom(room3);
        assertTrue(roomInventory.searchRoomByType(RoomType.SINGLE).contains(room1));
        assertTrue(roomInventory.searchRoomByType(RoomType.DOUBLE).contains(room2));
        assertTrue(roomInventory.searchRoomByType(RoomType.SUITE).contains(room3));

        assertEquals(0, roomInventory.searchRoomByType(RoomType.PRESIDENTIAL).size());
    }

    @Test
    void testDisplayAllRooms() {
        roomInventory = new RoomInventoryImpl();
        room1 = new Room(101, RoomType.SINGLE, true, 199);
        room2 = new Room(102, RoomType.SINGLE, true, 237);
        room3 = new Room(201, RoomType.DOUBLE, true, 237);

        roomInventory.displayAllRooms();
        String output = outputStream.toString();
        assertTrue(output.contains("No rooms in inventory"));

        roomInventory2 = new RoomInventoryImpl();
        roomInventory2.addRoom(room1);
        roomInventory2.addRoom(room2);
        roomInventory2.addRoom(room3);
        roomInventory2.displayAllRooms();
        String output2 = outputStream.toString();
        assertTrue(output2.contains("201"));
    }

    @Test
    void getTotalRooms() {
        roomInventory = new RoomInventoryImpl();
        room1 = new Room(101, RoomType.SINGLE, true, 199);
        room2 = new Room(102, RoomType.SINGLE, true, 237);
        room3 = new Room(201, RoomType.DOUBLE, true, 237);

        roomInventory.addRoom(room1);
        roomInventory.addRoom(room2);
        roomInventory.addRoom(room3);

        assertEquals(3,roomInventory.getTotalRooms());
    }

    @Test
    void getBookedRooms() {
        roomInventory = new RoomInventoryImpl();
        room1 = new Room(101, RoomType.SINGLE, true, 199);
        room2 = new Room(102, RoomType.SINGLE, false, 237);
        room3 = new Room(201, RoomType.DOUBLE, true, 237);

        roomInventory.addRoom(room1);
        roomInventory.addRoom(room2);
        roomInventory.addRoom(room3);

        assertEquals(1,roomInventory.getBookedRooms());
    }

    @Test
    void checkRoomAvailability() {
    }

    @Test
    void testCheckRoomAvailability() {
        RoomInventoryImpl roomInventory = new RoomInventoryImpl();
        //BookingRegister bookingRegister = new BookingRegister();
        List<Room> rooms = new ArrayList<>();
        Room room1 = new Room(101, RoomType.SINGLE, true, 199);
        Room room2 = new Room(102, RoomType.SINGLE, true, 199);
        Room room3 = new Room(103, RoomType.SINGLE, true, 199);

        RoomType roomType = RoomType.SINGLE;
        Guest guest1 = new Guest("1", "Jack Torrance", "jack@axemail.com", "12345", "guest1");
        Guest guest2 = new Guest("2", "Danny Torrance", "danny@redrum.com", "12345", "guest2");
        Guest guest3 = new Guest("3", "Wendy Torrance", "wendy@shinemail.com", "12345", "guest3");
        Booking booking1 = new Booking(LocalDate.of(2025, 11,23), LocalDate.of(2025,11,30), guest1, room1);
        Booking booking2 = new Booking(LocalDate.of(2025, 11,23), LocalDate.of(2025,11,30), guest2);
        //Booking booking3 = new Booking(LocalDate.of(2025, 11,23), LocalDate.of(2025,11,30), guest2, room2);
        roomInventory.addRoom(room1);
        roomInventory.addRoom(room2);
        roomInventory.addRoom(room3);
        room1.getBookingRegister().addBooking(booking1);
        room1.getBookingRegister().addDatesToRegister(booking1);

        roomInventory.checkRoomAvailability(booking2, RoomType.SINGLE);
        String output = outputStream.toString();
        assertTrue(output.contains("Booking Available"));



        Booking booking3 = new Booking(LocalDate.of(2025, 11,23), LocalDate.of(2025,11,30), guest1, room1);
        Booking booking4 = new Booking(LocalDate.of(2025, 11,23), LocalDate.of(2025,11,30), guest2, room2);
        Booking booking5 = new Booking(LocalDate.of(2025, 11,23), LocalDate.of(2025,11,30), guest3, room3);

        room1.getBookingRegister().addBooking(booking3);
        room2.getBookingRegister().addBooking(booking4);
        room3.getBookingRegister().addBooking(booking5);
        room1.getBookingRegister().addDatesToRegister(booking3);
        room2.getBookingRegister().addDatesToRegister(booking4);
        room3.getBookingRegister().addDatesToRegister(booking5);

        Booking booking6 = new Booking(LocalDate.of(2025, 11,23), LocalDate.of(2025,11,30), guest2);
        roomInventory.checkRoomAvailability(booking6, RoomType.SINGLE);
        String output2 = outputStream.toString();
        assertTrue(output2.contains("Booking Unavailable"));

        Booking booking7 = new Booking(LocalDate.of(2025, 11,23), LocalDate.of(2025,11,30), guest2);
        roomInventory.checkRoomAvailability(booking6, RoomType.SUITE);
        String output3 = outputStream.toString();
        assertTrue(output3.contains("Booking Unavailable"));

        roomInventory.checkRoomAvailability(RoomType.SUITE);
        String output4 = outputStream.toString();
        assertTrue(output4.contains("Booking Unavailable"));

        roomInventory.checkRoomAvailability(RoomType.SINGLE);
        String output5 = outputStream.toString();
        assertTrue(output5.contains("Booking Available"));

    }

    @Test
    void testShowAllBookings() {

        RoomInventoryImpl roomInventory = new RoomInventoryImpl();

        List<Room> rooms = new ArrayList<>();
        Room room1 = new Room(101, RoomType.SINGLE, true, 199);
        Room room2 = new Room(102, RoomType.SINGLE, true, 199);
        Room room3 = new Room(103, RoomType.SINGLE, true, 199);

        RoomType roomType = RoomType.SINGLE;
        Guest guest1 = new Guest("1", "Jack Torrance", "jack@axemail.com", "12345", "guest1");
        Guest guest2 = new Guest("2", "Danny Torrance", "danny@redrum.com", "12345", "guest2");
        Guest guest3 = new Guest("3", "Wendy Torrance", "wendy@shinemail.com", "12345", "guest3");
//        Booking booking1 = new Booking(LocalDate.of(2025, 11,23), LocalDate.of(2025,11,30), guest1, room1);
//        Booking booking2 = new Booking(LocalDate.of(2025, 11,23), LocalDate.of(2025,11,30), guest2);
//        //Booking booking3 = new Booking(LocalDate.of(2025, 11,23), LocalDate.of(2025,11,30), guest2, room2);
        roomInventory.addRoom(room1);
        roomInventory.addRoom(room2);
        roomInventory.addRoom(room3);

        Booking booking3 = new Booking(LocalDate.of(2025, 11,23), LocalDate.of(2025,11,30), guest1, room1);
        Booking booking4 = new Booking(LocalDate.of(2025, 11,23), LocalDate.of(2025,11,30), guest2, room2);
        Booking booking5 = new Booking(LocalDate.of(2025, 11,23), LocalDate.of(2025,11,30), guest3, room3);

        room1.getBookingRegister().addBooking(booking3);
        room2.getBookingRegister().addBooking(booking4);
        room3.getBookingRegister().addBooking(booking5);
        room1.getBookingRegister().addDatesToRegister(booking3);
        room2.getBookingRegister().addDatesToRegister(booking4);
        room3.getBookingRegister().addDatesToRegister(booking5);

        roomInventory.showAllBookings();

        String output = outputStream.toString();
        assertTrue(output.contains("Jack Torrance"));
        assertTrue(output.contains("Danny Torrance"));
        assertTrue(output.contains("Wendy Torrance"));
        //assertTrue(output.contains())
    }
}