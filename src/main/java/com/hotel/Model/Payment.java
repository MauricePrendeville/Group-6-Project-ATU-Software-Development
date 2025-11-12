package com.hotel.Model;

import java.time.LocalDateTime;
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

    // Getters
    public String getPaymentId() {
        return paymentId;
    }

    public int getBookingId() {
        return bookingId;
    }

    public double getAmount() {
        return amount;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public LocalDateTime getProcessedDate() {
        return processedDate;
    }

    public String getTransactionReference() {
        return transactionReference;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    // Setters with validation
    public void setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
    }

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

    @Override
    public int hashCode() {
        return Objects.hash(paymentId);
    }

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
        summary.append("====================================\n");
        return summary.toString();
    }
}