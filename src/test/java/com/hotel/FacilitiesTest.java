package com.hotel;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FacilitiesTest {

    /**
     * Abstract facility to demonstrate OOP abstraction.
     */
    static abstract class Facilities {
        private final String description;
        private final double charge;

        protected Facilities(String description, double charge) {
            this.description = description;
            this.charge = charge;
        }

        String getDescription() {
            return description;
        }

        double getCharge() {
            return charge;
        }
    }

    // Concrete facility implementations
    static class DiningService extends Facilities { DiningService(double c) { super("Dining Service", c); } }
    static class Gym extends Facilities { Gym(double c) { super("Gym", c); } }
    static class SwimmingPool extends Facilities { SwimmingPool(double c) { super("Swimming Pool", c); } }
    static class Spa extends Facilities { Spa(double c) { super("Spa", c); } }
    static class GolfCourse extends Facilities { GolfCourse(double c) { super("Golf Course", c); } }
    static class Breakfast extends Facilities { Breakfast(double c) { super("Breakfast", c); } }

    /**
     * Minimal Payment class used by the tests to collect charges.
     * In real project this should link to the production Payment class.
     */
    static class Payment {
        private double total = 0.0;

        void addCharge(String description, double amount) {
            // In production this might record line items; test only needs totals.
            total += amount;
        }

        double getTotal() {
            return total;
        }
    }

    /**
     * Minimal Guest class that uses facilities which push charges into Payment.
     * In production this should integrate with the real Guest/Payment implementations.
     */
    static class Guest {
        private final Payment payment;

        Guest(Payment payment) {
            this.payment = payment;
        }

        void useFacility(Facilities facility) {
            payment.addCharge(facility.getDescription(), facility.getCharge());
        }

        Payment getPayment() {
            return payment;
        }
    }

    @Test
    void facilitiesContributeToFinalBill() {
        Payment payment = new Payment();
        Guest guest = new Guest(payment);

        guest.useFacility(new DiningService(45.00));
        guest.useFacility(new Breakfast(12.50));
        guest.useFacility(new Gym(5.00));
        guest.useFacility(new SwimmingPool(7.50));
        guest.useFacility(new Spa(60.00));
        guest.useFacility(new GolfCourse(120.00));

        double expectedTotal = 45.00 + 12.50 + 5.00 + 7.50 + 60.00 + 120.00;
        assertEquals(expectedTotal, payment.getTotal(), 1e-6);
    }
}