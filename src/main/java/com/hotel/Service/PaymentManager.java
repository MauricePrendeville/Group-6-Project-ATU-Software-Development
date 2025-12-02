package com.hotel.Service;

import com.hotel.Model.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service class for managing payments and invoices in the hotel system.
 * Handles payment processing, invoice generation, refunds, and payment history.
 */
public class PaymentManager {

    // Storage for payments and invoices
    private Map<String, Payment> payments;  // Key: paymentId
    private Map<String, Invoice> invoices;  // Key: invoiceNumber
    private Map<Integer, List<Payment>> paymentsByBooking;  // Key: bookingId

    /**
     * Payment manager constructor
     */
    public PaymentManager() {
        this.payments = new HashMap<>();
        this.invoices = new HashMap<>();
        this.paymentsByBooking = new HashMap<>();
    }

    /**
     * Processes a payment for a booking.
     * Creates the payment, processes it, and generates an invoice.
     *
     * @param booking The booking to process payment for
     * @param amount The payment amount
     * @param paymentMethod The method of payment
     * @param guestName The name of the guest
     * @return The generated invoice
     * @throws IllegalArgumentException if validation fails
     * @throws IllegalStateException if payment processing fails
     */
    public Invoice processPayment(Booking booking, double amount,
                                  PaymentMethod paymentMethod, String guestName) {
        // Validate inputs
        if (booking == null) {
            throw new IllegalArgumentException("Booking cannot be null");
        }

        // Create payment
        Payment payment = new Payment(booking.getBookingID(), amount, paymentMethod, guestName);

        // Process the payment
        try {
            payment.processPayment();

            // Store payment
            payments.put(payment.getPaymentId(), payment);

            // Add to booking payments list
            paymentsByBooking.computeIfAbsent(booking.getBookingID(), k -> new ArrayList<>())
                    .add(payment);

            // Update booking status to PAID
            booking.setBookingStatus(BookingStatus.PAID);

            // Generate invoice
            Invoice invoice = generateInvoice(booking, payment);

            System.out.println("✓ Payment processed successfully: " + payment.getPaymentId());
            System.out.println("✓ Invoice generated: " + invoice.getInvoiceNumber());

            return invoice;

        } catch (Exception e) {
            payment.failPayment();
            payments.put(payment.getPaymentId(), payment);
            throw new IllegalStateException("Payment processing failed: " + e.getMessage(), e);
        }
    }

    /**
     * Processes a payment with a transaction reference (for online payments).
     *
     * @param booking The booking
     * @param amount The amount
     * @param paymentMethod The payment method
     * @param guestName The guest name
     * @param transactionRef External transaction reference
     * @return The generated invoice
     */
    public Invoice processPaymentWithReference(Booking booking, double amount,
                                               PaymentMethod paymentMethod,
                                               String guestName, String transactionRef) {
        if (booking == null) {
            throw new IllegalArgumentException("Booking cannot be null");
        }

        // Create payment with transaction reference
        Payment payment = new Payment(booking.getBookingID(), amount,
                paymentMethod, guestName, transactionRef);

        try {
            payment.processPayment();
            payments.put(payment.getPaymentId(), payment);
            paymentsByBooking.computeIfAbsent(booking.getBookingID(), k -> new ArrayList<>())
                    .add(payment);

            booking.setBookingStatus(BookingStatus.PAID);

            Invoice invoice = generateInvoice(booking, payment);

            System.out.println("✓ Payment processed: " + payment.getPaymentId());
            System.out.println("✓ Transaction Ref: " + transactionRef);

            return invoice;

        } catch (Exception e) {
            payment.failPayment();
            payments.put(payment.getPaymentId(), payment);
            throw new IllegalStateException("Payment processing failed: " + e.getMessage(), e);
        }
    }

    /**
     * Generates an invoice for a booking and payment.
     *
     * @param booking The booking
     * @param payment The payment
     * @return The generated invoice
     */
    public Invoice generateInvoice(Booking booking, Payment payment) {
        if (booking == null || payment == null) {
            throw new IllegalArgumentException("Booking and payment cannot be null");
        }

        // Create invoice
        Invoice invoice = new Invoice(booking, payment);

        // Store invoice
        invoices.put(invoice.getInvoiceNumber(), invoice);

        return invoice;
    }

    /**
     * Generates an invoice with custom tax rate.
     *
     * @param booking The booking
     * @param payment The payment
     * @param taxRate Custom tax rate
     * @return The generated invoice
     */
    public Invoice generateInvoice(Booking booking, Payment payment, double taxRate) {
        if (booking == null || payment == null) {
            throw new IllegalArgumentException("Booking and payment cannot be null");
        }

        Invoice invoice = new Invoice(booking, payment, taxRate);
        invoices.put(invoice.getInvoiceNumber(), invoice);

        return invoice;
    }

    /**
     * Processes a refund for a payment.
     * Updates payment status and booking status.
     *
     * @param paymentId The payment ID to refund
     * @return true if refund successful, false otherwise
     * @throws IllegalArgumentException if payment not found
     * @throws IllegalStateException if payment cannot be refunded
     */
    public boolean processRefund(String paymentId) {
        Payment payment = payments.get(paymentId);

        if (payment == null) {
            throw new IllegalArgumentException("Payment not found: " + paymentId);
        }

        if (!payment.isRefundable()) {
            throw new IllegalStateException(
                    "Payment cannot be refunded. Current status: " + payment.getPaymentStatus()
            );
        }

        try {
            payment.refundPayment();

            // Update booking status
            int bookingId = payment.getBookingId();
            // Note: In a real system, we'd get the booking and update it
            // For now, just update the payment

            System.out.println("✓ Refund processed for payment: " + paymentId);
            System.out.println("✓ Amount refunded: €" + String.format("%.2f", payment.getAmount()));

            return true;

        } catch (Exception e) {
            System.err.println("✗ Refund failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Cancels a pending payment.
     *
     * @param paymentId The payment ID to cancel
     * @return true if cancelled successfully
     */
    public boolean cancelPayment(String paymentId) {
        Payment payment = payments.get(paymentId);

        if (payment == null) {
            throw new IllegalArgumentException("Payment not found: " + paymentId);
        }

        try {
            payment.cancelPayment();
            System.out.println("✓ Payment cancelled: " + paymentId);
            return true;
        } catch (IllegalStateException e) {
            System.err.println("✗ Cannot cancel payment: " + e.getMessage());
            return false;
        }
    }

    /**
     * Gets a payment by ID.
     *
     * @param paymentId The payment ID
     * @return The payment, or null if not found
     */
    public Payment getPayment(String paymentId) {
        return payments.get(paymentId);
    }

    /**
     * Gets an invoice by number.
     *
     * @param invoiceNumber The invoice number
     * @return The invoice, or null if not found
     */
    public Invoice getInvoice(String invoiceNumber) {
        return invoices.get(invoiceNumber);
    }

    /**
     * Gets all payments for a specific booking.
     *
     * @param bookingId The booking ID
     * @return List of payments for the booking
     */
    public List<Payment> getPaymentsForBooking(int bookingId) {
        return paymentsByBooking.getOrDefault(bookingId, new ArrayList<>());
    }

    /**
     * Gets all completed payments.
     *
     * @return List of completed payments
     */
    public List<Payment> getCompletedPayments() {
        return payments.values().stream()
                .filter(Payment::isCompleted)
                .collect(Collectors.toList());
    }

    /**
     * Gets all pending payments.
     *
     * @return List of pending payments
     */
    public List<Payment> getPendingPayments() {
        return payments.values().stream()
                .filter(p -> p.getPaymentStatus() == PaymentStatus.PENDING)
                .collect(Collectors.toList());
    }

    /**
     * Gets all refunded payments.
     *
     * @return List of refunded payments
     */
    public List<Payment> getRefundedPayments() {
        return payments.values().stream()
                .filter(p -> p.getPaymentStatus() == PaymentStatus.REFUNDED)
                .collect(Collectors.toList());
    }

    /**
     * Gets all payments.
     *
     * @return List of all payments
     */
    public List<Payment> getAllPayments() {
        return new ArrayList<>(payments.values());
    }

    /**
     * Gets all invoices.
     *
     * @return List of all invoices
     */
    public List<Invoice> getAllInvoices() {
        return new ArrayList<>(invoices.values());
    }

    /**
     * Calculates total revenue from completed payments.
     *
     * @return Total revenue
     */
    public double getTotalRevenue() {
        return payments.values().stream()
                .filter(Payment::isCompleted)
                .mapToDouble(Payment::getAmount)
                .sum();
    }

    /**
     * Calculates total refunded amount.
     *
     * @return Total refunds
     */
    public double getTotalRefunds() {
        return payments.values().stream()
                .filter(p -> p.getPaymentStatus() == PaymentStatus.REFUNDED)
                .mapToDouble(Payment::getAmount)
                .sum();
    }

    /**
     * Gets payment statistics summary.
     *
     * @return Formatted statistics string
     */
    public String getPaymentStatistics() {
        int total = payments.size();
        long completed = payments.values().stream()
                .filter(Payment::isCompleted).count();
        long pending = payments.values().stream()
                .filter(p -> p.getPaymentStatus() == PaymentStatus.PENDING).count();
        long failed = payments.values().stream()
                .filter(p -> p.getPaymentStatus() == PaymentStatus.FAILED).count();
        long refunded = payments.values().stream()
                .filter(p -> p.getPaymentStatus() == PaymentStatus.REFUNDED).count();

        double totalRevenue = getTotalRevenue();
        double totalRefunds = getTotalRefunds();

        StringBuilder stats = new StringBuilder();
        stats.append("\n╔══════════════════════════════════════╗\n");
        stats.append("║      PAYMENT STATISTICS              ║\n");
        stats.append("╠══════════════════════════════════════╣\n");
        stats.append(String.format("║ Total Payments:      %15d ║\n", total));
        stats.append(String.format("║ Completed:           %15d ║\n", completed));
        stats.append(String.format("║ Pending:             %15d ║\n", pending));
        stats.append(String.format("║ Failed:              %15d ║\n", failed));
        stats.append(String.format("║ Refunded:            %15d ║\n", refunded));
        stats.append("╠══════════════════════════════════════╣\n");
        stats.append(String.format("║ Total Revenue:    €%15.2f ║\n", totalRevenue));
        stats.append(String.format("║ Total Refunds:    €%15.2f ║\n", totalRefunds));
        stats.append(String.format("║ Net Revenue:      €%15.2f ║\n", totalRevenue - totalRefunds));
        stats.append("╚══════════════════════════════════════╝\n");

        return stats.toString();
    }

    /**
     * Displays a payment summary for a specific payment.
     *
     * @param paymentId The payment ID
     */
    public void displayPaymentSummary(String paymentId) {
        Payment payment = payments.get(paymentId);
        if (payment == null) {
            System.out.println("Payment not found: " + paymentId);
            return;
        }

        System.out.println(payment.getPaymentSummary());
    }

    /**
     * Displays an invoice.
     *
     * @param invoiceNumber The invoice number
     */
    public void displayInvoice(String invoiceNumber) {
        Invoice invoice = invoices.get(invoiceNumber);
        if (invoice == null) {
            System.out.println("Invoice not found: " + invoiceNumber);
            return;
        }

        System.out.println(invoice.generateFormattedInvoice());
    }

    /**
     * Gets the total number of payments.
     *
     * @return Total payment count
     */
    public int getTotalPaymentCount() {
        return payments.size();
    }

    /**
     * Gets the total number of invoices.
     *
     * @return Total invoice count
     */
    public int getTotalInvoiceCount() {
        return invoices.size();
    }

    /**
     * Clears all payment and invoice data.
     * WARNING: Use only for testing or system reset.
     */
    public void clearAll() {
        payments.clear();
        invoices.clear();
        paymentsByBooking.clear();
        System.out.println("⚠ All payment data cleared");
    }
}