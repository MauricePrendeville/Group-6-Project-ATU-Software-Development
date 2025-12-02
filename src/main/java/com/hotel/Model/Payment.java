package com.hotel.Model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Represents a payment transaction in the hotel system.
 * Handles payment processing, validation, and tracking for bookings.
 * Each payment is associated with a booking and guest.
 */
public class Payment {

    // Static counter for generating unique payment IDs
    private static AtomicInteger paymentCounter = new AtomicInteger(1000);

    // Simple line-item support so facilities can attach charges to a Payment
    /**
     * Represents a single, itemized charge associated with a {@link Payment}.
     *
     * <p>This class is used to break down the total payment amount into individual components,
     * such as the room charge and various facility charges (e.g., Spa, Gym, Dining).
     * It holds the final, cumulative charge amount for a single item.</p>
     */
    public static class LineItem {
        private final String description;
        private final double amount;

        /**
         * Constructs a new LineItem.
         *
         * @param description A brief explanation of the charge (e.g., "Spa Treatment (Spa)", "Double Room - 2 night(s)")
         * @param amount The total monetary value of the charge for this specific item (e.g., total cost for 2 Spa treatments).
         */
        public LineItem(String description, double amount) {
            this.description = description;
            this.amount = amount;
        }

        /**
         * Gets the description of the charge.
         *
         * @return The descriptive string for the line item.
         */
        public String getDescription() {
            return description;
        }

        /**
         * Gets the total monetary amount for this line item.
         *
         * @return The total cost of the charge.
         */
        public double getAmount() {
            return amount;
        }
    }

    private final List<LineItem> lineItems = new ArrayList<>();

    private String paymentId;
    private int bookingId;
    private double amount;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private LocalDateTime paymentDate;
    private LocalDateTime processedDate;
    private String transactionReference;  // For tracking external payment systems
    private String guestName;
    private String receiptNumber;

    /**
     * Payment class constructor.
     * Automatically generates payment ID and sets initial status to PENDING.
     *
     * @param bookingId The ID of the booking this payment is for
     * @param amount The payment amount (positive values)
     * @param paymentMethod The method of payment (CASH, CREDIT_CARD, etc.)
     * @param guestName The name of the guest making the payment
     * @throws IllegalArgumentException if validation fails
     */
    public Payment(int bookingId, double amount, PaymentMethod paymentMethod, String guestName) {
        validateAmount(amount);
        validatePaymentMethod(paymentMethod);
        validateGuestName(guestName);

        this.paymentId = generatePaymentId();
        this.bookingId = bookingId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.guestName = guestName;
        this.paymentStatus = PaymentStatus.PENDING;
        this.paymentDate = LocalDateTime.now();
        this.receiptNumber = generateReceiptNumber();
    }

    /**
     * Constructor with transaction reference for external payment systems.
     *
     * @param bookingId The ID of the booking
     * @param amount The payment amount
     * @param paymentMethod The payment method
     * @param guestName The guest name
     * @param transactionReference External transaction reference
     */
    public Payment(int bookingId, double amount, PaymentMethod paymentMethod,
                   String guestName, String transactionReference) {
        this(bookingId, amount, paymentMethod, guestName);
        this.transactionReference = transactionReference;
    }

    /**
     * Generates a unique payment ID.
     * Format: PAY-xxxx where xxxx is an incrementing number
     */
    private String generatePaymentId() {
        return "PAY-" + paymentCounter.getAndIncrement();
    }

    /**
     * Generates a unique receipt number.
     * Format: RCP-yyyy-xxxx where yyyy is current year
     */
    private String generateReceiptNumber() {
        int year = LocalDateTime.now().getYear();
        return "RCP-" + year + "-" + paymentCounter.get();
    }

    /**
     * Confirms that payment amount is positive.
     */
    private void validateAmount(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Payment amount must be a positive number. Number provided: " + amount);
        }
        if (Double.isNaN(amount) || Double.isInfinite(amount)) {
            throw new IllegalArgumentException("Payment amount must be a valid number");
        }
    }

    /**
     * Confirms that payment method is not null.
     */
    private void validatePaymentMethod(PaymentMethod method) {
        if (method == null) {
            throw new IllegalArgumentException("Payment method cannot be null");
        }
    }

    /**
     * Confirms that guest name is not null or empty.
     */
    private void validateGuestName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Guest name cannot be null or empty");
        }
    }

    /**
     * Records a facility or other charge as a line item on this payment.
     * The amount is added to the running payment amount so getAmount() reflects the full total.
     *
     * @param description short description for the line item
     * @param chargeAmount positive charge amount
     */
    public void addCharge(String description, double chargeAmount) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Line item description cannot be null or empty");
        }
        if (Double.isNaN(chargeAmount) || Double.isInfinite(chargeAmount) || chargeAmount < 0) {
            throw new IllegalArgumentException("Charge amount must be a valid non-negative number");
        }
        lineItems.add(new LineItem(description, chargeAmount));
        this.amount += chargeAmount;
    }

    /**
     * Returns an unmodifiable list of recorded line items.
     */
    public List<LineItem> getLineItems() {
        return Collections.unmodifiableList(lineItems);
    }

    /**
     * Returns the sum of recorded line items (does not include the original amount if it was set separately).
     */
    public double getChargesTotal() {
        return lineItems.stream().mapToDouble(LineItem::getAmount).sum();
    }

    /**
     * Processes the payment and updates status to COMPLETED.
     * Can only be processed if current status is PENDING or PROCESSING.
     *
     * @throws IllegalStateException if payment cannot be processed
     */
    public void processPayment() {
        if (paymentStatus == PaymentStatus.COMPLETED) {
            throw new IllegalStateException("Payment already completed");
        }
        if (paymentStatus == PaymentStatus.FAILED) {
            throw new IllegalStateException("Cannot process a failed payment");
        }
        if (paymentStatus == PaymentStatus.REFUNDED) {
            throw new IllegalStateException("Cannot process a refunded payment");
        }
        if (paymentStatus == PaymentStatus.CANCELLED) {
            throw new IllegalStateException("Cannot process a cancelled payment");
        }

        this.paymentStatus = PaymentStatus.COMPLETED;
        this.processedDate = LocalDateTime.now();
    }

    /**
     * Marks the payment as failed.
     * Can only fail if currently PENDING or PROCESSING.
     *
     * @throws IllegalStateException if payment cannot be failed
     */
    public void failPayment() {
        if (paymentStatus == PaymentStatus.COMPLETED) {
            throw new IllegalStateException("Cannot fail a completed payment");
        }
        if (paymentStatus == PaymentStatus.REFUNDED) {
            throw new IllegalStateException("Cannot fail a refunded payment");
        }

        this.paymentStatus = PaymentStatus.FAILED;
        this.processedDate = LocalDateTime.now();
    }

    /**
     * Refunds the payment.
     * Can only refund if currently COMPLETED.
     *
     * @throws IllegalStateException if payment cannot be refunded
     */
    public void refundPayment() {
        if (paymentStatus != PaymentStatus.COMPLETED) {
            throw new IllegalStateException(
                    "Can only refund completed payments. Current status: " + paymentStatus
            );
        }

        this.paymentStatus = PaymentStatus.REFUNDED;
        this.processedDate = LocalDateTime.now();
    }

    /**
     * Cancels the payment.
     * Can only cancel if PENDING.
     *
     * @throws IllegalStateException if payment cannot be cancelled
     */
    public void cancelPayment() {
        if (paymentStatus != PaymentStatus.PENDING) {
            throw new IllegalStateException(
                    "Can only cancel pending payments. Current status: " + paymentStatus
            );
        }

        this.paymentStatus = PaymentStatus.CANCELLED;
        this.processedDate = LocalDateTime.now();
    }

    /**
     * Checks if the payment is completed.
     */
    public boolean isCompleted() {
        return paymentStatus == PaymentStatus.COMPLETED;
    }

    /**
     * Checks if the payment can be refunded.
     */
    public boolean isRefundable() {
        return paymentStatus == PaymentStatus.COMPLETED;
    }

    // --- Getters ---

    /**
     * Gets the unique ID of the payment.
     *
     * @return The payment ID string.
     */
    public String getPaymentId() {
        return paymentId;
    }

    /**
     * Gets the ID of the booking associated with this payment.
     *
     * @return The booking ID integer.
     */
    public int getBookingId() {
        return bookingId;
    }

    /**
     * Gets the total payment amount, which includes the initial amount plus all recorded line item charges.
     *
     * @return The total monetary amount of the payment.
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Gets the method used for the payment (e.g., Credit Card, Cash).
     *
     * @return The {@link PaymentMethod}.
     */
    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    /**
     * Gets the current status of the payment.
     *
     * @return The {@link PaymentStatus} (e.g., PENDING, COMPLETED, REFUNDED).
     */
    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    /**
     * Gets the date and time when the payment object was created.
     *
     * @return The {@link LocalDateTime} the payment was initiated.
     */
    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    /**
     * Gets the date and time when the payment status was last changed (e.g., processed, failed, refunded).
     *
     * @return The {@link LocalDateTime} the payment was processed, or {@code null} if only PENDING.
     */
    public LocalDateTime getProcessedDate() {
        return processedDate;
    }

    /**
     * Gets the external transaction reference string.
     *
     * @return The transaction reference, or {@code null} if not applicable.
     */
    public String getTransactionReference() {
        return transactionReference;
    }

    /**
     * Gets the name of the guest who made the payment.
     *
     * @return The guest's name string.
     */
    public String getGuestName() {
        return guestName;
    }

    /**
     * Gets the unique receipt number for this payment.
     *
     * @return The receipt number string.
     */
    public String getReceiptNumber() {
        return receiptNumber;
    }

    // --- Setters ---

    /**
     * Sets the external transaction reference for this payment.
     *
     * @param transactionReference The new transaction reference string.
     */
    public void setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
    }

    /**
     * Sets the payment status directly.
     * **Note:** It is generally recommended to use the dedicated status methods (e.g., {@link #processPayment()})
     * to enforce business logic transitions.
     *
     * @param status The new {@link PaymentStatus}.
     * @throws IllegalArgumentException if the provided status is null.
     */
    public void setPaymentStatus(PaymentStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Payment status cannot be null");
        }
        this.paymentStatus = status;
    }

    /**
     * Determines whether this payment is equal to another object.
     * Two {@code Payment} objects are considered equal if and only if
     * they have the same {@code paymentId}. This allows different
     * instances representing the same transaction to be treated as
     * equivalent.
     *
     * @param o the object to compare with this payment
     * @return {@code true} if the specified object is also a {@code Payment}
     * and has the same {@code paymentId}; {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Payment)) return false;
        Payment payment = (Payment) o;
        return paymentId.equals(payment.paymentId);
    }

    /**
     * Generates a hash code for the Payment object.
     *
     * <p>This implementation relies solely on the unique {@code paymentId}. Since the
     * payment ID is generated once and cannot change, it provides a stable and
     * reliable hash for use in hash-based collections (like {@code HashMap} or {@code HashSet}).</p>
     *
     * @return The hash code based on the {@code paymentId}.
     */
    @Override
    public int hashCode() {
        return Objects.hash(paymentId);
    }

    /**
     * Returns a string representation of the Payment object.
     *
     * <p>This string is formatted to be concise yet informative, including the
     * payment's unique identifier, associated booking, amount (formatted to two decimal places),
     * payment method, status, guest name, and receipt number.</p>
     *
     * @return A string containing key details of the payment transaction.
     */
    @Override
    public String toString() {
        return "Payment{" +
                "paymentId='" + paymentId + '\'' +
                ", bookingId=" + bookingId +
                ", amount=" + String.format("%.2f", amount) +
                ", method=" + paymentMethod +
                ", status=" + paymentStatus +
                ", guest='" + guestName + '\'' +
                ", receipt='" + receiptNumber + '\'' +
                '}';
    }

    /**
     * Returns a formatted payment summary for display.
     */
    public String getPaymentSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("\n PAYMENT DETAILS: \n");
        summary.append(String.format("Payment ID:    %s\n", paymentId));
        summary.append(String.format("Receipt No:    %s\n", receiptNumber));
        summary.append(String.format("Booking ID:    %d\n", bookingId));
        summary.append(String.format("Guest Name:    %s\n", guestName));
        summary.append(String.format("Amount:        €%.2f\n", amount));
        summary.append(String.format("Method:        %s\n", paymentMethod));
        summary.append(String.format("Status:        %s\n", paymentStatus));
        summary.append(String.format("Payment Date:  %s\n", paymentDate));
        if (processedDate != null) {
            summary.append(String.format("Processed:     %s\n", processedDate));
        }
        if (transactionReference != null) {
            summary.append(String.format("Transaction:   %s\n", transactionReference));
        }
        if (!lineItems.isEmpty()) {
            summary.append("Line Items:\n");
            for (LineItem li : lineItems) {
                summary.append(String.format("  - %s : €%.2f\n", li.getDescription(), li.getAmount()));
            }
        }
        summary.append("====================================\n");
        return summary.toString();
    }
}
