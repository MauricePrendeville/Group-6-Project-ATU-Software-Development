package com.hotel.Model;

/**
 * Enum representing the status of a payment in the hotel system which tracks the lifecycle of a payment from pending to completion or failure.
 */

public enum PaymentStatus {
    PENDING,       // Payment initiated but not yet processed
    PROCESSING,    // Payment is being processed
    COMPLETED,     // Payment successfully completed
    FAILED,        // Payment failed
    REFUNDED,      // Payment was refunded
    CANCELLED;     // Payment was cancelled

    @Override
    public String toString() {
        // Format enum values like "Processing" instead of "PROCESSING"
        String name = name().toLowerCase();
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }
}