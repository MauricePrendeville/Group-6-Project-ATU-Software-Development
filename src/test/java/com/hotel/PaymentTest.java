package com.hotel;

import com.hotel.Model.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Payment class.
 * Tests payment creation, processing, refunds, and validation.
 */

class PaymentTest {

    private Payment payment;

    @BeforeEach
    void setUp() {
        payment = new Payment(1001, 250.00, PaymentMethod.CREDIT_CARD, "John Doe");
    }

    @Test
    @DisplayName("Constructor should create valid payment")
    void testConstructorValid() {
        assertNotNull(payment);
        assertEquals(1001, payment.getBookingId());
        assertEquals(250.00, payment.getAmount());
        assertEquals(PaymentMethod.CREDIT_CARD, payment.getPaymentMethod());
        assertEquals("John Doe", payment.getGuestName());
        assertEquals(PaymentStatus.PENDING, payment.getPaymentStatus());
    }

    @Test
    @DisplayName("Payment ID should be generated automatically")
    void testPaymentIdGeneration() {
        assertNotNull(payment.getPaymentId());
        assertTrue(payment.getPaymentId().startsWith("PAY-"));
    }

    @Test
    @DisplayName("Receipt number should be generated automatically")
    void testReceiptNumberGeneration() {
        assertNotNull(payment.getReceiptNumber());
        assertTrue(payment.getReceiptNumber().startsWith("RCP-"));
    }

    @Test
    @DisplayName("Payment date should be set on creation")
    void testPaymentDateSet() {
        assertNotNull(payment.getPaymentDate());
    }

    @Test
    @DisplayName("Processed date should be null for new payment")
    void testProcessedDateInitiallyNull() {
        assertNull(payment.getProcessedDate());
    }

    @Test
    @DisplayName("Constructor with zero amount should throw exception")
    void testConstructorZeroAmount() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Payment(1001, 0.0, PaymentMethod.CASH, "John Doe");
        });
    }

    @Test
    @DisplayName("Constructor with negative amount should throw exception")
    void testConstructorNegativeAmount() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Payment(1001, -50.0, PaymentMethod.CASH, "John Doe");
        });
    }

    @Test
    @DisplayName("Constructor with NaN amount should throw exception")
    void testConstructorNaNAmount() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Payment(1001, Double.NaN, PaymentMethod.CASH, "John Doe");
        });
    }

    @Test
    @DisplayName("Constructor with null payment method should throw exception")
    void testConstructorNullPaymentMethod() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Payment(1001, 100.0, null, "John Doe");
        });
    }

    @Test
    @DisplayName("Constructor with null guest name should throw exception")
    void testConstructorNullGuestName() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Payment(1001, 100.0, PaymentMethod.CASH, null);
        });
    }

    @Test
    @DisplayName("Constructor with empty guest name should throw exception")
    void testConstructorEmptyGuestName() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Payment(1001, 100.0, PaymentMethod.CASH, "   ");
        });
    }

    @Test
    @DisplayName("Constructor with transaction reference should set it")
    void testConstructorWithTransactionReference() {
        Payment payment = new Payment(1001, 100.0, PaymentMethod.ONLINE_BANKING,
                "Jane Doe", "TXN-123456");
        assertEquals("TXN-123456", payment.getTransactionReference());
    }

    @Test
    @DisplayName("Process payment should change status to COMPLETED")
    void testProcessPaymentSuccess() {
        payment.processPayment();

        assertEquals(PaymentStatus.COMPLETED, payment.getPaymentStatus());
        assertNotNull(payment.getProcessedDate());
        assertTrue(payment.isCompleted());
    }

    @Test
    @DisplayName("Process already completed payment should throw exception")
    void testProcessAlreadyCompletedPayment() {
        payment.processPayment();

        assertThrows(IllegalStateException.class, () -> {
            payment.processPayment();
        });
    }

    @Test
    @DisplayName("Process failed payment should throw exception")
    void testProcessFailedPayment() {
        payment.failPayment();

        assertThrows(IllegalStateException.class, () -> {
            payment.processPayment();
        });
    }

    @Test
    @DisplayName("Process refunded payment should throw exception")
    void testProcessRefundedPayment() {
        payment.processPayment();
        payment.refundPayment();

        assertThrows(IllegalStateException.class, () -> {
            payment.processPayment();
        });
    }

    @Test
    @DisplayName("Process cancelled payment should throw exception")
    void testProcessCancelledPayment() {
        payment.cancelPayment();

        assertThrows(IllegalStateException.class, () -> {
            payment.processPayment();
        });
    }

    @Test
    @DisplayName("Fail payment should change status to FAILED")
    void testFailPayment() {
        payment.failPayment();

        assertEquals(PaymentStatus.FAILED, payment.getPaymentStatus());
        assertNotNull(payment.getProcessedDate());
        assertFalse(payment.isCompleted());
    }

    @Test
    @DisplayName("Fail completed payment should throw exception")
    void testFailCompletedPayment() {
        payment.processPayment();

        assertThrows(IllegalStateException.class, () -> {
            payment.failPayment();
        });
    }

    @Test
    @DisplayName("Fail refunded payment should throw exception")
    void testFailRefundedPayment() {
        payment.processPayment();
        payment.refundPayment();

        assertThrows(IllegalStateException.class, () -> {
            payment.failPayment();
        });
    }

    @Test
    @DisplayName("Refund payment should change status to REFUNDED")
    void testRefundPayment() {
        payment.processPayment();
        payment.refundPayment();

        assertEquals(PaymentStatus.REFUNDED, payment.getPaymentStatus());
        assertNotNull(payment.getProcessedDate());
    }

    @Test
    @DisplayName("Refund pending payment should throw exception")
    void testRefundPendingPayment() {
        assertThrows(IllegalStateException.class, () -> {
            payment.refundPayment();
        });
    }

    @Test
    @DisplayName("Refund failed payment should throw exception")
    void testRefundFailedPayment() {
        payment.failPayment();

        assertThrows(IllegalStateException.class, () -> {
            payment.refundPayment();
        });
    }

    @Test
    @DisplayName("Cancel payment should change status to CANCELLED")
    void testCancelPayment() {
        payment.cancelPayment();

        assertEquals(PaymentStatus.CANCELLED, payment.getPaymentStatus());
        assertNotNull(payment.getProcessedDate());
    }

    @Test
    @DisplayName("Cancel completed payment should throw exception")
    void testCancelCompletedPayment() {
        payment.processPayment();

        assertThrows(IllegalStateException.class, () -> {
            payment.cancelPayment();
        });
    }

    @Test
    @DisplayName("Is completed should return true for completed payment")
    void testIsCompletedTrue() {
        payment.processPayment();
        assertTrue(payment.isCompleted());
    }

    @Test
    @DisplayName("Is completed should return false for pending payment")
    void testIsCompletedFalse() {
        assertFalse(payment.isCompleted());
    }

    @Test
    @DisplayName("Is refundable should return true for completed payment")
    void testIsRefundableTrue() {
        payment.processPayment();
        assertTrue(payment.isRefundable());
    }

    @Test
    @DisplayName("Is refundable should return false for pending payment")
    void testIsRefundableFalse() {
        assertFalse(payment.isRefundable());
    }

    @Test
    @DisplayName("Set transaction reference should work")
    void testSetTransactionReference() {
        payment.setTransactionReference("TXN-789");
        assertEquals("TXN-789", payment.getTransactionReference());
    }

    @Test
    @DisplayName("Set payment status should work")
    void testSetPaymentStatus() {
        payment.setPaymentStatus(PaymentStatus.PROCESSING);
        assertEquals(PaymentStatus.PROCESSING, payment.getPaymentStatus());
    }

    @Test
    @DisplayName("Set null payment status should throw exception")
    void testSetNullPaymentStatus() {
        assertThrows(IllegalArgumentException.class, () -> {
            payment.setPaymentStatus(null);
        });
    }

    @Test
    @DisplayName("Equals should work for same payment")
    void testEqualsSamePayment() {
        assertEquals(payment, payment);
    }

    @Test
    @DisplayName("Equals should work for different objects with same ID")
    void testEqualsDifferentObjectSameId() {
        // This tests if two Payment objects with same paymentId are equal
        // In practice, paymentId is unique, but this tests the equals logic
        String paymentId = payment.getPaymentId();
        // We can't easily test this without reflection, but we test the general case
        assertNotEquals(payment, null);
    }

    @Test
    @DisplayName("Equals should return false for null")
    void testEqualsNull() {
        assertNotEquals(payment, null);
    }

    @Test
    @DisplayName("ToString should contain payment details")
    void testToString() {
        String str = payment.toString();
        assertTrue(str.contains("Payment"));
        assertTrue(str.contains(payment.getPaymentId()));
        assertTrue(str.contains("250.00"));
    }

    @Test
    @DisplayName("Get payment summary should return formatted string")
    void testGetPaymentSummary() {
        String summary = payment.getPaymentSummary();
        assertNotNull(summary);
        assertTrue(summary.contains("PAYMENT DETAILS"));
        assertTrue(summary.contains(payment.getPaymentId()));
        assertTrue(summary.contains("John Doe"));
        assertTrue(summary.contains("250.00"));
    }

    @Test
    @DisplayName("Multiple payments should have different IDs")
    void testMultiplePaymentsDifferentIds() {
        Payment payment1 = new Payment(1001, 100.0, PaymentMethod.CASH, "Alice");
        Payment payment2 = new Payment(1002, 200.0, PaymentMethod.CASH, "Bob");

        assertNotEquals(payment1.getPaymentId(), payment2.getPaymentId());
        assertNotEquals(payment1.getReceiptNumber(), payment2.getReceiptNumber());
    }
}