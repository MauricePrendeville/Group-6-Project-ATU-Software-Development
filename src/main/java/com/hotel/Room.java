package com.hotel;

/**
 * Abstract base class for specific room types.
 */
public abstract class Room {
    private final String roomNumber;
    private final String roomType;
    private final double pricePerNight;
    private String status;  // "AVAILABLE", "OCCUPIED", "CLEANING"

    /**
     * Constructor for Room.
     * @param roomNumber Room number (e.g., "101", "202")
     * @param roomType Type of room (e.g., "STANDARD", "SUPERIOR", "SUITE")
     * @param pricePerNight Price per night for this room
     */
    public Room(String roomNumber, String roomType, double pricePerNight) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.pricePerNight = pricePerNight;
        this.status = "AVAILABLE";  // Status of new rooms will be set to available when they are created
    }

    // Getters
    public String getRoomNumber() {
        return roomNumber;
    }

    public String getRoomType() {
        return roomType;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public String getStatus() {
        return status;
    }

    // Setter for status - rooms can change status
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Check if room is available for booking.
     * @return true if status is AVAILABLE, false otherwise
     */
    public boolean isAvailable() {
        return "AVAILABLE".equals(status);
    }

    @Override
    public String toString() {
        return "Room " + roomNumber + " (" + roomType + ") - " + status + " - $" + pricePerNight + "/night";
    }
}