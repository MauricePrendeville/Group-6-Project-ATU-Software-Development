package com.hotel;

public enum BookingStatus {
    UNCONFIRMED,
    POSSIBLE,
    CONFIRMED,
    CANCELLED,
    REFUNDED,
    PAID;

    @Override
    public String toString() {
        // Format enum values like "Single" instead of "SINGLE"
        String name = name().toLowerCase();
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }
}