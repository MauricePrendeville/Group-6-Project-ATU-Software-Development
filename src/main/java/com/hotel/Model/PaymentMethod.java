package com.hotel.Model;

/**
 * Enum representing different payment methods available in the hotel system to allow guests to pay for their booking.
 */

public enum PaymentMethod {
    CASH,
    CREDIT_CARD,
    DEBIT_CARD,
    ONLINE_BANKING,
    MOBILE_PAYMENT;

    @Override
    public String toString() {
        // Format enum values like "Credit Card" instead of "CREDIT_CARD"
        String name = name().toLowerCase().replace('_', ' ');
        // Capitalize first letter of each word
        String[] words = name.split(" ");
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                result.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1))
                        .append(" ");
            }
        }
        return result.toString().trim();
    }
}