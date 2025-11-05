package com.hotel.model;

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

    public Room(int roomNumber, RoomType roomType, boolean available, double pricePerNight) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.available = available;
        this.pricePerNight = pricePerNight;
    }

    // 🔹 Getters and Setters
    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

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

