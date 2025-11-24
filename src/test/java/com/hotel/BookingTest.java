package com.hotel;

import com.hotel.Model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class BookingTest {

    private static AtomicInteger bookingIDCounter = new AtomicInteger(0);
    private int bookingID;
    private LocalDate arriveDate;
    private LocalDate departDate;
    private LocalDate bookingDate;
    private Guest bookingGuest;
    //private Receptionist checkingReceptionist;
    private Room bookingRoom;
    private BookingStatus bookingStatus;
    private Booking booking;

    @BeforeEach
    void setUp() {
        //create bookingID
        int bookingID = 123;

        //create arrive and depart dates
        LocalDate arriveDate = LocalDate.of(2025, 11,22);
        LocalDate departDate = LocalDate.of(2025,11,25);
        LocalDate bookingDate = LocalDate.now();

        //create Guest
        Guest bookingGuest = new Guest("ab125", "Wendy Torrance", "wendy.torrance@shriekmail.com", "01 345678", "WendysPassword");

        //create Room
        Room bookingRoom = new Room(1, RoomType.SINGLE,true,168.0);

        Booking booking = new Booking(LocalDate.of(2025, 11,23), LocalDate.of(2025,11,30), bookingGuest, bookingRoom);

        //create bookingStatus
        BookingStatus bookingStatus = BookingStatus.UNCONFIRMED;

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getNext() {
    }

    @Test
    void getBookingID() {
    }

    @Test
    void getArriveDate() {
    }

    @Test
    void getDepartDate() {
    }

    @Test
    void getBookingDate() {
    }

    @Test
    void getBookingGuest() {
    }

    @Test
    void getBookingRoom() {
    }

    @Test
    void setBookingStatus() {
    }

    @Test
    void getBookingStatus() {
    }

    @Test
    void testSetBookingID() {
        int bookingID = 2;
        Booking booking = new Booking(LocalDate.of(2025, 11,23), LocalDate.of(2025,11,30), bookingGuest, bookingRoom);
        booking.setBookingID(bookingID);
        assertEquals(2,booking.getBookingID());
    }

    @Test
    void setArriveDate() {
    }

    @Test
    void setDepartDate() {
    }

    @Test
    void setBookingDate() {
    }

    @Test
    void setBookingGuest() {
    }

    @Test
    void setBookingRoom() {
    }

    @Test
    void showBookingDetails() {
    }
}