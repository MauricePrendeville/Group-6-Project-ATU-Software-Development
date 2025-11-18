package com.hotel;

import com.hotel.Model.*;
import org.junit.jupiter.api.*;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Invoice class.
 * Tests invoice generation, calculations, and formatting.
 */

class InvoiceTest {

    private Booking booking;
    private Payment payment;
    private Invoice invoice;
    private Guest guest;
    private Room room;

    @BeforeEach
    void setUp() {
        // Create guest
        guest = new Guest("G001", "Alice Smith", "alice@email.com", "555-0101", "pass123");

        // Create room
        room = new Room(101, RoomType.DOUBLE, true, 120.00);

        // Create booking (3 nights)
        booking = new Booking(
                LocalDate.of(2025, 12, 1),
                LocalDate.of(2025, 12, 4),
                guest,
                room
        );

        // Create payment
        payment = new Payment(booking.getBookingID(), 360.00,
                PaymentMethod.CREDIT_CARD, "Alice Smith");
        payment.processPayment();

        // Create invoice
        invoice = new Invoice(booking, payment);
    }

    @Test
    @DisplayName("Constructor should create valid invoice")
    void testConstructorValid() {
        assertNotNull(invoice);
        assertNotNull(invoice.getInvoiceNumber());
        assertNotNull(invoice.getInvoiceDate());
        assertEquals(booking, invoice.getBooking());
        assertEquals(payment, invoice.getPayment());
    }

    @Test
    @DisplayName("Invoice number should be generated correctly")
    void testInvoiceNumberFormat() {
        String invoiceNum = invoice.getInvoiceNumber();
        assertTrue(invoiceNum.startsWith("INV-"));
        assertTrue(invoiceNum.contains("-" + booking.getBookingID()));
    }

    @Test
    @DisplayName("Constructor with null booking should throw exception")
    void testConstructorNullBooking() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Invoice(null, payment);
        });
    }

    @Test
    @DisplayName("Constructor with null payment should throw exception")
    void testConstructorNullPayment() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Invoice(booking, null);
        });
    }

    @Test
    @DisplayName("Constructor with booking missing room should throw exception")
    void testConstructorBookingMissingRoom() {
        Booking badBooking = new Booking(
                LocalDate.of(2025, 12, 1),
                LocalDate.of(2025, 12, 4),
                guest
        );

        assertThrows(IllegalArgumentException.class, () -> {
            new Invoice(badBooking, payment);
        });
    }

    @Test
    @DisplayName("Subtotal should be calculated correctly")
    void testSubtotalCalculation() {
        // 3 nights * €120 = €360
        assertEquals(360.00, invoice.getSubtotal(), 0.01);
    }

    @Test
    @DisplayName("VAT should be calculated correctly with default rate")
    void testDefaultTaxCalculation() {
        // 20% of €360 = €72.00
        assertEquals(0.20, invoice.getTaxRate(), 0.001);
        assertEquals(72.00, invoice.getTaxAmount(), 0.01);
    }

    @Test
    @DisplayName("Total should be calculated correctly")
    void testTotalCalculation() {
        // €360 + €72.00 = €432.00
        assertEquals(432.00, invoice.getTotalAmount(), 0.01);
    }

    @Test
    @DisplayName("Constructor with custom VAT rate should use it")
    void testCustomTaxRate() {
        Invoice customInvoice = new Invoice(booking, payment, 0.20);

        assertEquals(0.20, customInvoice.getTaxRate());
        assertEquals(72.00, customInvoice.getTaxAmount(), 0.01); // 20% of 360
        assertEquals(432.00, customInvoice.getTotalAmount(), 0.01); // 360 + 72
    }

    @Test
    @DisplayName("Constructor with invalid VAT rate should throw exception")
    void testInvalidTaxRate() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Invoice(booking, payment, -0.1);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Invoice(booking, payment, 1.5);
        });
    }

    @Test
    @DisplayName("Add additional charge should update totals")
    void testAddAdditionalCharge() {
        double initialSubtotal = invoice.getSubtotal();

        invoice.addAdditionalCharge("Room Service", 50.00);

        assertEquals(initialSubtotal + 50.00, invoice.getSubtotal(), 0.01);
        assertTrue(invoice.getTaxAmount() > 72.00); // VAT should increase
        assertTrue(invoice.getTotalAmount() > 432.00); // Total should increase
    }

    @Test
    @DisplayName("Add additional charge with null description should throw exception")
    void testAddAdditionalChargeNullDescription() {
        assertThrows(IllegalArgumentException.class, () -> {
            invoice.addAdditionalCharge(null, 50.00);
        });
    }

    @Test
    @DisplayName("Add additional charge with empty description should throw exception")
    void testAddAdditionalChargeEmptyDescription() {
        assertThrows(IllegalArgumentException.class, () -> {
            invoice.addAdditionalCharge("   ", 50.00);
        });
    }

    @Test
    @DisplayName("Add additional charge with negative amount should throw exception")
    void testAddAdditionalChargeNegativeAmount() {
        assertThrows(IllegalArgumentException.class, () -> {
            invoice.addAdditionalCharge("Room Service", -50.00);
        });
    }

    @Test
    @DisplayName("Get items should return invoice items")
    void testGetItems() {
        var items = invoice.getItems();
        assertNotNull(items);
        assertFalse(items.isEmpty());

        // Should have at least the room charge
        assertTrue(items.size() >= 1);
    }

    @Test
    @DisplayName("Get items should return defensive copy")
    void testGetItemsDefensiveCopy() {
        var items = invoice.getItems();
        int originalSize = items.size();

        // Try to modify the returned list
        items.clear();

        // Original should be unchanged
        assertEquals(originalSize, invoice.getItems().size());
    }

    @Test
    @DisplayName("Generate formatted invoice should return string")
    void testGenerateFormattedInvoice() {
        String formatted = invoice.generateFormattedInvoice();

        assertNotNull(formatted);
        assertFalse(formatted.isEmpty());

        // Check for key sections
        assertTrue(formatted.contains("INVOICE"));
        assertTrue(formatted.contains("The Group 6 Hotel"));
        assertTrue(formatted.contains(invoice.getInvoiceNumber()));
        assertTrue(formatted.contains("Alice Smith"));
        assertTrue(formatted.contains("Room Number:   101"));
        assertTrue(formatted.contains("Double Room"));
        assertTrue(formatted.contains("Thank you"));
    }

    @Test
    @DisplayName("Formatted invoice should contain payment details")
    void testFormattedInvoicePaymentDetails() {
        String formatted = invoice.generateFormattedInvoice();

        assertTrue(formatted.contains("PAYMENT DETAILS"));
        assertTrue(formatted.contains("Credit Card"));
        assertTrue(formatted.contains(payment.getReceiptNumber()));
    }

    @Test
    @DisplayName("Formatted invoice should show correct amounts")
    void testFormattedInvoiceAmounts() {
        String formatted = invoice.generateFormattedInvoice();

        assertTrue(formatted.contains("360.00")); // Subtotal
        assertTrue(formatted.contains("72.00")); // VAT (20% of 360)
        assertTrue(formatted.contains("432.00")); // Total
    }

    @Test
    @DisplayName("Generate summary should return concise string")
    void testGenerateSummary() {
        String summary = invoice.generateSummary();

        assertNotNull(summary);
        assertTrue(summary.contains(invoice.getInvoiceNumber()));
        assertTrue(summary.contains("Alice Smith"));
        assertTrue(summary.contains("360.00"));
        // VAT or Tax
        assertTrue(summary.contains("72.00"));
        assertTrue(summary.contains("432.00"));
    }

    @Test
    @DisplayName("ToString should contain invoice details")
    void testToString() {
        String str = invoice.toString();

        assertNotNull(str);
        assertTrue(str.contains("Invoice"));
        assertTrue(str.contains(invoice.getInvoiceNumber()));
        assertTrue(str.contains("432.00"));
    }

    @Test
    @DisplayName("Invoice for single night should calculate correctly")
    void testSingleNightInvoice() {
        Booking oneNight = new Booking(
                LocalDate.of(2025, 12, 1),
                LocalDate.of(2025, 12, 2),
                guest,
                room
        );

        Payment oneNightPayment = new Payment(oneNight.getBookingID(), 120.00,
                PaymentMethod.CASH, "Alice Smith");
        oneNightPayment.processPayment();

        Invoice oneNightInvoice = new Invoice(oneNight, oneNightPayment);

        assertEquals(120.00, oneNightInvoice.getSubtotal(), 0.01);
        assertEquals(24.00, oneNightInvoice.getTaxAmount(), 0.01); // 20% of 120
        assertEquals(144.00, oneNightInvoice.getTotalAmount(), 0.01);
    }

    @Test
    @DisplayName("Invoice for long stay should calculate correctly")
    void testLongStayInvoice() {
        Booking longStay = new Booking(
                LocalDate.of(2025, 12, 1),
                LocalDate.of(2025, 12, 15), // 14 nights
                guest,
                room
        );

        Payment longPayment = new Payment(longStay.getBookingID(), 1680.00,
                PaymentMethod.CREDIT_CARD, "Alice Smith");
        longPayment.processPayment();

        Invoice longInvoice = new Invoice(longStay, longPayment);

        assertEquals(1680.00, longInvoice.getSubtotal(), 0.01); // 14 * 120
        assertEquals(336.00, longInvoice.getTaxAmount(), 0.01); // 20% of 1680
        assertEquals(2016.00, longInvoice.getTotalAmount(), 0.01);
    }

    @Test
    @DisplayName("Multiple additional charges should all be included")
    void testMultipleAdditionalCharges() {
        invoice.addAdditionalCharge("Room Service", 30.00);
        invoice.addAdditionalCharge("Minibar", 20.00);
        invoice.addAdditionalCharge("Laundry", 15.00);

        // Subtotal should be 360 + 30 + 20 + 15 = 425
        assertEquals(425.00, invoice.getSubtotal(), 0.01);

        // Should have 4 items (room + 3 additional)
        assertEquals(4, invoice.getItems().size());
    }
}