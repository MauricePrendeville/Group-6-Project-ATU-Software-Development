package com.hotel.Model;


import com.hotel.Service.BookingRegister;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Represents a single room in the hotel.
 * Each Room object holds basic details such as
 * room number, type, availability, and price per night.
 *
 * @author Vijaylakshmi
 * @version 1.0
 */
public class Room {

    private int roomNumber;
    private RoomType roomType;      // e.g., "Single", "Double", "Suite"
    private boolean available;    // true if room is available for booking
    private double pricePerNight; // room cost per night

    //Booking Register additions to Room (Maurice)
    private int bookingID;
    private BookingRegister bookingRegister;
    private int roomBookingCount;

    public Room(int roomNumber, RoomType roomType, boolean available, double pricePerNight) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.available = available;
        this.pricePerNight = pricePerNight;

        //extra attributes initialised for booking register functionality (Maurice)
        //the methods to use these attributes have been added into the BookingRegister class
        //The BookingRegister objects contain the booking details for one room each
        this.bookingID = 1;
        this.bookingRegister = new BookingRegister();
        this.roomBookingCount = 0;

        //this.roomList = new ArrayList<>();
       // this.bookedDates = new ArrayList<>(); //array used to check availability of range of dates
    }

    // 🔹 Getters and Setters
    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public BookingRegister getBookingRegister() { return bookingRegister; }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public int getRoomBookingCount() {
        return roomBookingCount;
    }

    public int setNextRoomBookingCount() { return roomBookingCount++; }

    // 🔹 Utility Methods
    @Override
    public String toString() {
        return "Room{" +
                "roomNumber=" + roomNumber +
                ", roomType='" + roomType + '\'' +
                ", available=" + available +
                ", pricePerNight=" + pricePerNight +
                '}';
    }

}