package com.hotel;

import java.time.LocalDate;

public class Booking {

    private String bookingID;
    private LocalDate arriveDate;
    private LocalDate departDate;
    private LocalDate bookingDate;
    private Guest bookingGuest;
    //private Receptionist checkingReceptionist;
    private Room bookingRoom;
    //need to add price and price calculator for this class. get price from Room

    public Booking(String bookingID, LocalDate arriveDate, LocalDate departDate,
                   LocalDate bookingDate, Guest bookingGuest, Room bookingRoom) {
        this.bookingID = bookingID;
        this.arriveDate = arriveDate;
        this.departDate = departDate;
        this.bookingDate = bookingDate;
        this.bookingGuest = bookingGuest;
        this.bookingRoom = bookingRoom;
    }

    public String getBookingID() {
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

    public Guest getBookingGuest() {
        return bookingGuest;
    }

    public Room getBookingRoom() {
        return bookingRoom;
    }

    public void setBookingID(String bookingID) {
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


}
