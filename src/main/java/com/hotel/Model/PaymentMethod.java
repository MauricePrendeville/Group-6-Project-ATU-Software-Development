package com.hotel.Model;

/**
 * Enum representing different **payment methods** available in the hotel system,
 * allowing guests to pay for their bookings and services.
 * <p>
 * The overridden {@link #toString()} method provides a user-friendly,
 * space-separated, and capitalized representation (e.g., {@code CREDIT_CARD} becomes "Credit Card").
 * </p>
 */
public enum PaymentMethod {
    /**
     * Physical currency (paper bills and coins).
     */
    CASH,

    /**
     * Payment made using a standard credit card (e.g., Visa, Mastercard).
     */
    CREDIT_CARD,

    /**
     * Payment made using a debit card, typically drawing funds directly from a bank account.
     */
    DEBIT_CARD,

    /**
     * Payment processed through a direct transfer or interface with an online bank account.
     */
    ONLINE_BANKING,

    /**
     * Payment made using a mobile wallet or application (e.g., Apple Pay, Google Pay).
     */
    MOBILE_PAYMENT;

    /**
     * Overrides the default {@code toString()} method to provide a user-friendly,
     * space-separated, and capitalized representation of the enum value.
     * <p>
     * Example: {@code CREDIT_CARD} is returned as "Credit Card".
     * </p>
     *
     * @return A formatted, human-readable string representation of the payment method.
     */
    @Override
    public String toString() {
        // Format enum values like "credit_card" -> "credit card"
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