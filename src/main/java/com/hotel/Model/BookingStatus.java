package com.hotel.Model;

/**
 * Enum representing the status of the Booking. The Booking moves through different stages.
 * It is Unconfirmed at the checking for availability stage.
 * It changes to Possible when there is an available Room.
 * When the Guest is happy with the Booking it is set to Confirmed.
 * At the successful conclusion of the Booking the status will be updated to Paid.
 * If there is a problem with the Booking there are Cancelled and Refunded statuses.
 */
public enum BookingStatus {
    UNCONFIRMED,
    POSSIBLE,
    CONFIRMED,
    CANCELLED,
    REFUNDED,
    PAID;

    /**
     * Formats the BookingStatus into a readable format with the first letter capitalised.
     * @return BookingStatus
     */
    @Override
    public String toString() {
        // Format enum values like "Single" instead of "SINGLE"
        String name = name().toLowerCase();
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }
}