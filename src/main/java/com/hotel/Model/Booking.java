package com.hotel.Model;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * This class is used to keep track of individual Bookings for an individual Room
 * It contains the base information for a booking the arriveDate, the departDate and the bookingID
 * It also contains the objects Guest, Room, and BookingStatus.
 * The bookingIDCounter is an AtomicInteger is static and is used to ensure that
 * each new bookingID is unique.
 */

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

    /**
     * The Constructor for Booking object
     * @param arriveDate the arrival date
     * @param departDate the depart date
     */
    public Booking(LocalDate arriveDate, LocalDate departDate) {
        this.bookingID = getNext();
        this.arriveDate = arriveDate;
        this.departDate = departDate;
        this.bookingDate = LocalDate.now();
        this.bookingGuest = new Guest("ab125", "Wendy Torrance", "wendy.torrance@shriekmail.com", "01 345678", "WendysPassword");
        this.bookingRoom = null;
        this.bookingStatus = BookingStatus.UNCONFIRMED;
    }

    /**
     * The Constructor for Booking object
     * @param arriveDate the arrival date
     * @param departDate the departure date
     * @param bookingGuest the Guest object containing Guest details
     */
    public Booking(LocalDate arriveDate, LocalDate departDate, Guest bookingGuest) {
        this.bookingID = getNext();
        this.arriveDate = arriveDate;
        this.departDate = departDate;
        this.bookingDate = LocalDate.now();
        this.bookingGuest = bookingGuest;
        this.bookingRoom = null;
        this.bookingStatus = BookingStatus.UNCONFIRMED;
    }
    /**
     * The Constructor for Booking object
     * @param arriveDate the arrival date
     * @param departDate the departure date
     * @param bookingGuest the Guest object containing Guest details
     * @param bookingRoom the Room object containing Room details
     */
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

    /**
     * getNext method increments the static AtomicInteger bookingIDCounter
     * @return bookingIDCounter increased by one
     */
    public int getNext() {
        return bookingIDCounter.getAndIncrement();
    }

    /**
     * getBookingID gets the bookingID of the object.
     * @return bookingID
     */
    public int getBookingID() {
        return bookingID;
    }

    /**
     * getArriveDate gets the arriveDate of the object
     * @return arriveDate
     */
    public LocalDate getArriveDate() {
        return arriveDate;
    }

    /**
     * getDepartureDate gets the departureDate of the object
     * @return departDate
     */
    public LocalDate getDepartDate() {
        return departDate;
    }

    /**
     * getBookingDate gets bookingDate of object
     * @return bookingDate
     */
    public LocalDate getBookingDate() {
        return bookingDate;
    }

    /**
     * getBookingGuest gets the bookingGuest of the object
     * @return bookingGuest which is a Guest object
     */
    public Guest getBookingGuest() {return bookingGuest;}

    /**
     * getBookingRoom gets the bookingRoom of the object
     * @return bookingRoom which is a Room object
     */
    public Room getBookingRoom() {
        return bookingRoom;
    }

    /**
     * setBookingStatus sets the bookingStatus of the object.
     * bookingStatus references the BookingStatus Enum constants.
     * @param bookingStatus a constant from the BookingStatus Enum.
     */
    public void setBookingStatus(BookingStatus bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    /**
     * getBookingStatus gets the bookingStatus of the object
     * @return bookingStatus a constant from the BookingStatus Enum
     */
    public BookingStatus getBookingStatus() {
        return bookingStatus;
    }

    /**
     * setBookingID sets the bookingID of the object
     * @param bookingID
     */
    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
    }

    /**
     * setArriveDate sets the arriveDate of the object
     * @param arriveDate
     */
    public void setArriveDate(LocalDate arriveDate) {
        this.arriveDate = arriveDate;
    }

    /**
     * setDepartDate sets the departDate of the object
     * @param departDate
     */
    public void setDepartDate(LocalDate departDate) {
        this.departDate = departDate;
    }

    /**
     * setBookingDate sets the bookingDate of the object
     * @param bookingDate
     */
    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    /**
     * setBookingGuest sets the bookingGuest of the object
     * @param bookingGuest
     */
    public void setBookingGuest(Guest bookingGuest) {
        this.bookingGuest = bookingGuest;
    }

    /**
     * setBookingRoom sets the bookingRoom of the object
     * @param bookingRoom
     */
    public void setBookingRoom(Room bookingRoom) {
        this.bookingRoom = bookingRoom;
    }

    /**
     * showBookingDetails prints to screen details of the Booking object
     * @param booking the Booking object
     */
    public void showBookingDetails (Booking booking){

        System.out.println("Guest: " + bookingGuest.getName() + " Room Number: " + bookingRoom.getRoomNumber() + " Type: " + bookingRoom.getRoomType()
                + " Arrive: " + arriveDate + " Depart: " + departDate + " Status: " + bookingStatus + " BookingID: " + bookingID);

    }

}
