package com.hotel.Model;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;

public class Booking {

    private static AtomicInteger bookingIDCounter = new AtomicInteger(0);
    private int bookingID;
    private LocalDate arriveDate;
    private LocalDate departDate;
    private LocalDate bookingDate;
    private Guest bookingGuest;
    //private Receptionist checkingReceptionist;
    private Room bookingRoom;
    private BookingStatus bookingStatus;
    //need to add price and price calculator for this class. get price from Room


    public Booking(LocalDate arriveDate, LocalDate departDate) {
        this.bookingID = getNext();
        this.arriveDate = arriveDate;
        this.departDate = departDate;
        this.bookingDate = LocalDate.now();
        this.bookingGuest = new Guest("Wendy");
        this.bookingRoom = null;
        this.bookingStatus = BookingStatus.UNCONFIRMED;
    }
    public Booking(LocalDate arriveDate, LocalDate departDate, Guest bookingGuest) {
        this.bookingID = getNext();
        this.arriveDate = arriveDate;
        this.departDate = departDate;
        this.bookingDate = LocalDate.now();
        this.bookingGuest = bookingGuest;
        this.bookingRoom = null;
        this.bookingStatus = BookingStatus.UNCONFIRMED;
    }

    public Booking(LocalDate arriveDate, LocalDate departDate,
                   Guest bookingGuest, Room bookingRoom) {
        this.bookingID = getNext();
        this.arriveDate = arriveDate;
        this.departDate = departDate;
        this.bookingDate = LocalDate.now();
        this.bookingGuest = bookingGuest;
        this.bookingRoom = bookingRoom;
        this.bookingStatus = BookingStatus.UNCONFIRMED;
    }

    public int getNext() {
        return bookingIDCounter.getAndIncrement();
    }

    public int getBookingID() {
        return bookingID;
    }

    public LocalDate getArriveDate() {
        return arriveDate;
    }

    public LocalDate getDepartDate() {
        return departDate;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }


    public Guest getBookingGuest() {return bookingGuest;}

    public Room getBookingRoom() {
        return bookingRoom;
    }

    public void setBookingStatus(BookingStatus bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public BookingStatus getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
    }

    public void setArriveDate(LocalDate arriveDate) {
        this.arriveDate = arriveDate;
    }

    public void setDepartDate(LocalDate departDate) {
        this.departDate = departDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public void setBookingGuest(Guest bookingGuest) {
        this.bookingGuest = bookingGuest;
    }

    public void setBookingRoom(Room bookingRoom) {
        this.bookingRoom = bookingRoom;
    }

    public void showBookingDetails (Booking booking){

        System.out.println("Guest: " + bookingGuest.getName() + " Room Number: " + bookingRoom.getRoomNumber() + " Type: " + bookingRoom.getRoomType()
                + " Arrive: " + arriveDate + " Depart: " + departDate + " Status: " + bookingStatus + " BookingID: " + bookingID);

    }

}
