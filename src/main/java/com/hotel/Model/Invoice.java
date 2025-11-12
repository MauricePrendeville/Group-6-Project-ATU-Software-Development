package com.hotel.Model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an invoice for a hotel booking.
 * Generates formatted invoices with itemized charges, taxes, and totals.
 */

public class Invoice {

    private String invoiceNumber;
    private Booking booking;
    private Payment payment;
    private LocalDateTime invoiceDate;
    private double subtotal;
    private double taxRate;
    private double taxAmount;
    private double totalAmount;
    private List<InvoiceItem> items;

    // Hotel details for invoice header
    private static final String HOTEL_NAME = "The Group 6 Hotel";
    private static final String HOTEL_ADDRESS = "Shore Road, Killybegs, Co. Donegal, F94 DV52, Ireland";
    private static final String HOTEL_PHONE = "+353 74 918 6000";
    private static final String HOTEL_EMAIL = "info@groupsixhotel.com";
    private static final double DEFAULT_TAX_RATE = 0.2; // 20% tax

    /**
     * Constructor for creating an invoice from a booking and payment.
     *
     * @param booking The booking this invoice is for
     * @param payment The payment associated with this invoice
     * @throws IllegalArgumentException if booking or payment is null
     */
    public Invoice(Booking booking, Payment payment) {
        validateBooking(booking);
        validatePayment(payment);

        this.booking = booking;
        this.payment = payment;
        this.invoiceDate = LocalDateTime.now();
        this.invoiceNumber = generateInvoiceNumber();
        this.taxRate = DEFAULT_TAX_RATE;
        this.items = new ArrayList<>();

        calculateInvoice();
    }

    /**
     * Constructor with custom tax rate.
     *
     * @param booking The booking
     * @param payment The payment
     * @param taxRate Custom tax rate (0.0 to 1.0)
     */
    public Invoice(Booking booking, Payment payment, double taxRate) {
        this(booking, payment);
        validateTaxRate(taxRate);
        this.taxRate = taxRate;
        calculateInvoice(); // Recalculate with new tax rate
    }

    /**
     * Validates the booking is not null and has required data.
     */
    private void validateBooking(Booking booking) {
        if (booking == null) {
            throw new IllegalArgumentException("Booking cannot be null");
        }
        if (booking.getBookingRoom() == null) {
            throw new IllegalArgumentException("Booking must have a room assigned");
        }
        if (booking.getBookingGuest() == null) {
            throw new IllegalArgumentException("Booking must have a guest assigned");
        }
    }

    /**
     * Validates the payment is not null.
     */
    private void validatePayment(Payment payment) {
        if (payment == null) {
            throw new IllegalArgumentException("Payment cannot be null");
        }
    }

    /**
     * Validates tax rate is between 0 and 1.
     */
    private void validateTaxRate(double rate) {
        if (rate < 0.0 || rate > 1.0) {
            throw new IllegalArgumentException("Tax rate must be between 0.0 and 1.0");
        }
    }

    /**
     * Generates a unique invoice number.
     * Format: INV-YYYY-MMDD-BookingID
     */
    private String generateInvoiceNumber() {
        String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return String.format("INV-%s-%d", dateStr, booking.getBookingID());
    }

    /**
     * Calculates the invoice totals including room charges and taxes.
     */
    private void calculateInvoice() {
        items.clear();

        // Calculate room charges
        Room room = booking.getBookingRoom();
        long numberOfNights = calculateNumberOfNights();
        double roomRate = room.getPricePerNight();
        double roomTotal = roomRate * numberOfNights;

        // Add room charge as line item
        String roomDescription = String.format("%s Room - %d night(s)",
                room.getRoomType(), numberOfNights);
        items.add(new InvoiceItem(roomDescription, numberOfNights, roomRate, roomTotal));

        // Calculate subtotal
        subtotal = roomTotal;

        // Calculate tax
        taxAmount = subtotal * taxRate;

        // Calculate total
        totalAmount = subtotal + taxAmount;
    }

    /**
     * Calculates number of nights between arrival and departure dates.
     */
    private long calculateNumberOfNights() {
        return java.time.temporal.ChronoUnit.DAYS.between(
                booking.getArriveDate(),
                booking.getDepartDate()
        );
    }

    /**
     * Adds an additional charge to the invoice (e.g., room service, minibar).
     *
     * @param description Description of the charge
     * @param amount Amount to charge
     */
    public void addAdditionalCharge(String description, double amount) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be empty");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        items.add(new InvoiceItem(description, 1, amount, amount));
        subtotal += amount;
        taxAmount = subtotal * taxRate;
        totalAmount = subtotal + taxAmount;
    }

    /**
     * Generates a formatted text invoice for display or printing.
     *
     * @return Formatted invoice as a string
     */
    public String generateFormattedInvoice() {
        StringBuilder invoice = new StringBuilder();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd MMM yyyy");
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");

        // Header
        invoice.append("\n");
        invoice.append("═══════════════════════════════════════════════════════════\n");
        invoice.append("                    INVOICE                                 \n");
        invoice.append("═══════════════════════════════════════════════════════════\n");
        invoice.append("\n");

        // Hotel Details
        invoice.append(String.format("%-30s %28s\n", HOTEL_NAME, "Invoice #: " + invoiceNumber));
        invoice.append(String.format("%-30s %28s\n", HOTEL_ADDRESS,
                "Date: " + invoiceDate.format(dateFormat)));
        invoice.append(String.format("%-30s %28s\n", "Tel: " + HOTEL_PHONE,
                "Time: " + invoiceDate.format(timeFormat)));
        invoice.append(String.format("%-30s\n", "Email: " + HOTEL_EMAIL));
        invoice.append("\n");
        invoice.append("───────────────────────────────────────────────────────────\n");

        // Guest Details
        invoice.append("BILL TO:\n");
        invoice.append(String.format("Guest Name:    %s\n", booking.getBookingGuest().getName()));
        invoice.append(String.format("Room Number:   %d\n", booking.getBookingRoom().getRoomNumber()));
        invoice.append(String.format("Check-in:      %s\n", booking.getArriveDate().format(dateFormat)));
        invoice.append(String.format("Check-out:     %s\n", booking.getDepartDate().format(dateFormat)));
        invoice.append(String.format("Booking ID:    %d\n", booking.getBookingID()));
        invoice.append("\n");
        invoice.append("───────────────────────────────────────────────────────────\n");

        // Line Items
        invoice.append(String.format("%-35s %8s %10s %10s\n",
                "DESCRIPTION", "QTY", "RATE", "AMOUNT"));
        invoice.append("───────────────────────────────────────────────────────────\n");

        for (InvoiceItem item : items) {
            invoice.append(String.format("%-35s %8d €%9.2f €%9.2f\n",
                    truncate(item.description, 35),
                    item.quantity,
                    item.unitPrice,
                    item.totalPrice));
        }

        invoice.append("───────────────────────────────────────────────────────────\n");

        // Totals
        invoice.append(String.format("%55s €%9.2f\n", "Subtotal:", subtotal));
        invoice.append(String.format("%55s €%9.2f\n",
                String.format("Tax (%.0f%%):", taxRate * 100), taxAmount));
        invoice.append("═══════════════════════════════════════════════════════════\n");
        invoice.append(String.format("%55s €%9.2f\n", "TOTAL:", totalAmount));
        invoice.append("═══════════════════════════════════════════════════════════\n");
        invoice.append("\n");

        // Payment Details
        invoice.append("PAYMENT DETAILS:\n");
        invoice.append(String.format("Payment Method: %s\n", payment.getPaymentMethod()));
        invoice.append(String.format("Payment Status: %s\n", payment.getPaymentStatus()));
        invoice.append(String.format("Receipt Number: %s\n", payment.getReceiptNumber()));
        invoice.append("\n");

        // Footer
        invoice.append("───────────────────────────────────────────────────────────\n");
        invoice.append("           Thank you for staying with us!                  \n");
        invoice.append("         We hope to see you again soon.                    \n");
        invoice.append("═══════════════════════════════════════════════════════════\n");

        return invoice.toString();
    }

    /**
     * Truncates a string to specified length.
     */
    private String truncate(String str, int maxLength) {
        if (str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength - 3) + "...";
    }

    /**
     * Generates a simple summary of the invoice.
     */
    public String generateSummary() {
        return String.format("Invoice %s: Guest %s, €%.2f (VAT: €%.2f, Total: €%.2f)",
                invoiceNumber,
                booking.getBookingGuest().getName(),
                subtotal,
                taxAmount,
                totalAmount);
    }

    // Getters
    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public Booking getBooking() {
        return booking;
    }

    public Payment getPayment() {
        return payment;
    }

    public LocalDateTime getInvoiceDate() {
        return invoiceDate;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public double getTaxRate() {
        return taxRate;
    }

    public double getTaxAmount() {
        return taxAmount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public List<InvoiceItem> getItems() {
        return new ArrayList<>(items); // Return defensive copy
    }

    @Override
    public String toString() {
        return String.format("Invoice{number='%s', total=%.2f, status=%s}",
                invoiceNumber, totalAmount, payment.getPaymentStatus());
    }

    /**
     * Inner class representing a line item on the invoice.
     */
    public static class InvoiceItem {
        private String description;
        private int quantity;
        private double unitPrice;
        private double totalPrice;

        public InvoiceItem(String description, long quantity, double unitPrice, double totalPrice) {
            this.description = description;
            this.quantity = (int) quantity;
            this.unitPrice = unitPrice;
            this.totalPrice = totalPrice;
        }

        public String getDescription() {
            return description;
        }

        public int getQuantity() {
            return quantity;
        }

        public double getUnitPrice() {
            return unitPrice;
        }

        public double getTotalPrice() {
            return totalPrice;
        }
    }
}