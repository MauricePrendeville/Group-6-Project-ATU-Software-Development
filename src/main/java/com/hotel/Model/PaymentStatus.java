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

    /**
     * Returns a human-readable, title-cased string representation of this payment status.
     *
     * <p>This method overrides the default {@code Enum.toString()} to format the
     * constant names (e.g., {@code PENDING}) into a more presentable, title-cased format (e.g., "Pending").</p>
     *
     * @return The status name with the first letter capitalized and the rest in lowercase.
     */
    @Override
    public String toString() {
        // Format enum values like "Processing" instead of "PROCESSING"
        String name = name().toLowerCase();
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }
}