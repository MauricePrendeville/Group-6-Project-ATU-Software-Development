package com.hotel;

import com.hotel.Model.*;
import com.hotel.Service.PaymentManager;
import org.junit.jupiter.api.*;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for PaymentManager class.
 * Tests payment processing, invoice generation, refunds, and statistics.
 */

class PaymentManagerTest {

    private PaymentManager paymentManager;
    private Booking booking;
    private Guest guest;
    private Room room;

    @BeforeEach
    void setUp() {
        paymentManager = new PaymentManager();

        // Create test data
        guest = new Guest("G001", "Bob Wilson", "bob@email.com", "555-0202", "pass123");
        room = new Room(201, RoomType.SUITE, true, 250.00);

        booking = new Booking(
                LocalDate.of(2025, 12, 10),
                LocalDate.of(2025, 12, 13), // 3 nights
                guest,
                room
        );
    }

    @Test
    @DisplayName("Constructor should initialize empty manager")
    void testConstructor() {
        assertNotNull(paymentManager);
        assertEquals(0, paymentManager.getTotalPaymentCount());
        assertEquals(0, paymentManager.getTotalInvoiceCount());
    }

    @Test
    @DisplayName("Process payment should create payment and invoice")
    void testProcessPaymentSuccess() {
        Invoice invoice = paymentManager.processPayment(
                booking, 750.00, PaymentMethod.CREDIT_CARD, "Bob Wilson"
        );

        assertNotNull(invoice);
        assertNotNull(invoice.getInvoiceNumber());
        assertEquals(1, paymentManager.getTotalPaymentCount());
        assertEquals(1, paymentManager.getTotalInvoiceCount());
        assertEquals(BookingStatus.PAID, booking.getBookingStatus());
    }

    @Test
    @DisplayName("Process payment with null booking should throw exception")
    void testProcessPaymentNullBooking() {
        assertThrows(IllegalArgumentException.class, () -> {
            paymentManager.processPayment(null, 100.0, PaymentMethod.CASH, "Test");
        });
    }

    @Test
    @DisplayName("Process payment with invalid amount should throw exception")
    void testProcessPaymentInvalidAmount() {
        assertThrows(IllegalArgumentException.class, () -> {
            paymentManager.processPayment(booking, -50.0, PaymentMethod.CASH, "Bob Wilson");
        });
    }

    @Test
    @DisplayName("Process payment with transaction reference should work")
    void testProcessPaymentWithReference() {
        Invoice invoice = paymentManager.processPaymentWithReference(
                booking, 750.00, PaymentMethod.ONLINE_BANKING, "Bob Wilson", "TXN-ABC123"
        );

        assertNotNull(invoice);
        Payment payment = invoice.getPayment();
        assertEquals("TXN-ABC123", payment.getTransactionReference());
    }

    @Test
    @DisplayName("Generate invoice should create invoice")
    void testGenerateInvoice() {
        Payment payment = new Payment(booking.getBookingID(), 750.00,
                PaymentMethod.CASH, "Bob Wilson");
        payment.processPayment();

        Invoice invoice = paymentManager.generateInvoice(booking, payment);

        assertNotNull(invoice);
        assertEquals(booking, invoice.getBooking());
        assertEquals(payment, invoice.getPayment());
    }

    @Test
    @DisplayName("Generate invoice with custom tax rate should use it")
    void testGenerateInvoiceCustomTaxRate() {
        Payment payment = new Payment(booking.getBookingID(), 750.00,
                PaymentMethod.CASH, "Bob Wilson");
        payment.processPayment();

        Invoice invoice = paymentManager.generateInvoice(booking, payment, 0.20);

        assertEquals(0.20, invoice.getTaxRate());
    }

    @Test
    @DisplayName("Generate invoice with null booking should throw exception")
    void testGenerateInvoiceNullBooking() {
        Payment payment = new Payment(1, 100.0, PaymentMethod.CASH, "Test");

        assertThrows(IllegalArgumentException.class, () -> {
            paymentManager.generateInvoice(null, payment);
        });
    }

    @Test
    @DisplayName("Get payment should return correct payment")
    void testGetPayment() {
        Invoice invoice = paymentManager.processPayment(
                booking, 750.00, PaymentMethod.CREDIT_CARD, "Bob Wilson"
        );

        String paymentId = invoice.getPayment().getPaymentId();
        Payment retrieved = paymentManager.getPayment(paymentId);

        assertNotNull(retrieved);
        assertEquals(paymentId, retrieved.getPaymentId());
    }

    @Test
    @DisplayName("Get payment with invalid ID should return null")
    void testGetPaymentInvalidId() {
        Payment payment = paymentManager.getPayment("INVALID-ID");
        assertNull(payment);
    }

    @Test
    @DisplayName("Get invoice should return correct invoice")
    void testGetInvoice() {
        Invoice invoice = paymentManager.processPayment(
                booking, 750.00, PaymentMethod.CREDIT_CARD, "Bob Wilson"
        );

        String invoiceNum = invoice.getInvoiceNumber();
        Invoice retrieved = paymentManager.getInvoice(invoiceNum);

        assertNotNull(retrieved);
        assertEquals(invoiceNum, retrieved.getInvoiceNumber());
    }

    @Test
    @DisplayName("Get invoice with invalid number should return null")
    void testGetInvoiceInvalidNumber() {
        Invoice invoice = paymentManager.getInvoice("INVALID-NUM");
        assertNull(invoice);
    }

    @Test
    @DisplayName("Process refund should refund completed payment")
    void testProcessRefundSuccess() {
        Invoice invoice = paymentManager.processPayment(
                booking, 750.00, PaymentMethod.CREDIT_CARD, "Bob Wilson"
        );

        String paymentId = invoice.getPayment().getPaymentId();
        boolean refunded = paymentManager.processRefund(paymentId);

        assertTrue(refunded);
        Payment payment = paymentManager.getPayment(paymentId);
        assertEquals(PaymentStatus.REFUNDED, payment.getPaymentStatus());
    }

    @Test
    @DisplayName("Process refund for non-existent payment should throw exception")
    void testProcessRefundNonExistent() {
        assertThrows(IllegalArgumentException.class, () -> {
            paymentManager.processRefund("INVALID-ID");
        });
    }

    @Test
    @DisplayName("Cancel non-existent payment should throw exception")
    void testCancelNonExistentPayment() {
        assertThrows(IllegalArgumentException.class, () -> {
            paymentManager.cancelPayment("INVALID-ID");
        });
    }

    @Test
    @DisplayName("Get payments for booking should return list")
    void testGetPaymentsForBooking() {
        paymentManager.processPayment(booking, 750.00, PaymentMethod.CASH, "Bob Wilson");

        List<Payment> payments = paymentManager.getPaymentsForBooking(booking.getBookingID());

        assertNotNull(payments);
        assertEquals(1, payments.size());
    }

    @Test
    @DisplayName("Get payments for booking with no payments should return empty list")
    void testGetPaymentsForBookingEmpty() {
        List<Payment> payments = paymentManager.getPaymentsForBooking(9999);

        assertNotNull(payments);
        assertTrue(payments.isEmpty());
    }

    @Test
    @DisplayName("Get completed payments should filter correctly")
    void testGetCompletedPayments() {
        paymentManager.processPayment(booking, 750.00, PaymentMethod.CASH, "Bob Wilson");
        paymentManager.processPayment(booking, 250.00, PaymentMethod.CASH, "Bob Wilson");

        List<Payment> completed = paymentManager.getCompletedPayments();

        assertEquals(2, completed.size());
        assertTrue(completed.stream().allMatch(Payment::isCompleted));
    }

    @Test
    @DisplayName("Get pending payments should filter correctly")
    void testGetPendingPayments() {
        paymentManager.processPayment(booking, 750.00, PaymentMethod.CASH, "Bob Wilson");

        List<Payment> pending = paymentManager.getPendingPayments();

        // All processed payments are completed, so should be 0
        assertEquals(0, pending.size());
    }

    @Test
    @DisplayName("Get refunded payments should filter correctly")
    void testGetRefundedPayments() {
        Invoice invoice = paymentManager.processPayment(
                booking, 750.00, PaymentMethod.CREDIT_CARD, "Bob Wilson"
        );

        paymentManager.processRefund(invoice.getPayment().getPaymentId());

        List<Payment> refunded = paymentManager.getRefundedPayments();

        assertEquals(1, refunded.size());
        assertEquals(PaymentStatus.REFUNDED, refunded.get(0).getPaymentStatus());
    }

    @Test
    @DisplayName("Get all payments should return all")
    void testGetAllPayments() {
        paymentManager.processPayment(booking, 750.00, PaymentMethod.CASH, "Bob");
        paymentManager.processPayment(booking, 250.00, PaymentMethod.CASH, "Bob");
        paymentManager.processPayment(booking, 150.00, PaymentMethod.CASH, "Bob");

        List<Payment> all = paymentManager.getAllPayments();

        assertEquals(3, all.size());
    }

    @Test
    @DisplayName("Get total revenue should sum completed payments")
    void testGetTotalRevenue() {
        paymentManager.processPayment(booking, 750.00, PaymentMethod.CASH, "Bob");
        paymentManager.processPayment(booking, 250.00, PaymentMethod.CASH, "Bob");
        paymentManager.processPayment(booking, 300.00, PaymentMethod.CASH, "Bob");

        double revenue = paymentManager.getTotalRevenue();

        assertEquals(1300.00, revenue, 0.01);
    }

    @Test
    @DisplayName("Get total refunds should sum refunded payments")
    void testGetTotalRefunds() {
        Invoice inv1 = paymentManager.processPayment(booking, 750.00, PaymentMethod.CASH, "Bob");
        Invoice inv2 = paymentManager.processPayment(booking, 250.00, PaymentMethod.CASH, "Bob");

        paymentManager.processRefund(inv1.getPayment().getPaymentId());

        double refunds = paymentManager.getTotalRefunds();

        assertEquals(750.00, refunds, 0.01);
    }

    @Test
    @DisplayName("Get payment statistics should return formatted string")
    void testGetPaymentStatistics() {
        paymentManager.processPayment(booking, 750.00, PaymentMethod.CASH, "Bob");
        paymentManager.processPayment(booking, 250.00, PaymentMethod.CASH, "Bob");

        String stats = paymentManager.getPaymentStatistics();

        assertNotNull(stats);
        assertTrue(stats.contains("PAYMENT STATISTICS"));
        assertTrue(stats.contains("Total Payments"));
        assertTrue(stats.contains("Total Revenue"));
    }

    @Test
    @DisplayName("Display payment summary should not throw exception")
    void testDisplayPaymentSummary() {
        Invoice invoice = paymentManager.processPayment(
                booking, 750.00, PaymentMethod.CASH, "Bob"
        );

        assertDoesNotThrow(() -> {
            paymentManager.displayPaymentSummary(invoice.getPayment().getPaymentId());
        });
    }

    @Test
    @DisplayName("Display payment summary for non-existent should print message")
    void testDisplayPaymentSummaryNonExistent() {
        assertDoesNotThrow(() -> {
            paymentManager.displayPaymentSummary("INVALID-ID");
        });
    }

    @Test
    @DisplayName("Display invoice should not throw exception")
    void testDisplayInvoice() {
        Invoice invoice = paymentManager.processPayment(
                booking, 750.00, PaymentMethod.CASH, "Bob"
        );

        assertDoesNotThrow(() -> {
            paymentManager.displayInvoice(invoice.getInvoiceNumber());
        });
    }

    @Test
    @DisplayName("Display invoice for non-existent should print message")
    void testDisplayInvoiceNonExistent() {
        assertDoesNotThrow(() -> {
            paymentManager.displayInvoice("INVALID-NUM");
        });
    }

    @Test
    @DisplayName("Clear all should empty all storage")
    void testClearAll() {
        paymentManager.processPayment(booking, 750.00, PaymentMethod.CASH, "Bob");
        paymentManager.processPayment(booking, 250.00, PaymentMethod.CASH, "Bob");

        paymentManager.clearAll();

        assertEquals(0, paymentManager.getTotalPaymentCount());
        assertEquals(0, paymentManager.getTotalInvoiceCount());
        assertEquals(0, paymentManager.getAllPayments().size());
        assertEquals(0, paymentManager.getAllInvoices().size());
    }

    @Test
    @DisplayName("Multiple bookings should track separately")
    void testMultipleBookings() {
        Booking booking2 = new Booking(
                LocalDate.of(2025, 12, 15),
                LocalDate.of(2025, 12, 18),
                guest,
                room
        );

        paymentManager.processPayment(booking, 750.00, PaymentMethod.CASH, "Bob");
        paymentManager.processPayment(booking2, 900.00, PaymentMethod.CASH, "Bob");

        List<Payment> payments1 = paymentManager.getPaymentsForBooking(booking.getBookingID());
        List<Payment> payments2 = paymentManager.getPaymentsForBooking(booking2.getBookingID());

        assertEquals(1, payments1.size());
        assertEquals(1, payments2.size());
    }
}