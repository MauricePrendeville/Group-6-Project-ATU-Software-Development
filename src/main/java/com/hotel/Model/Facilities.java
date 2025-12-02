package com.hotel.Model;

import java.time.LocalDateTime;

/**
 * Represents the superclass for all Facilities available at the Hotel
 */
public abstract class Facilities {
    private final String description;
    private final double baseCost;
    private final LocalDateTime usedAt;

    /**
     * constructor for Facilities
     * @param description description of Facility
     * @param baseCost base cost of Facility
     */
    protected Facilities(String description, double baseCost) {
        this.description = description;
        this.baseCost = baseCost;
        this.usedAt = LocalDateTime.now();
    }

    /**
     * getDescription
     * @return description
     */

    public String getDescription() {
        return description;
    }

    /**
     * getBaseCost
     * @return baseCost
     */
    public double getBaseCost() {
        return baseCost;
    }

    /**
     * getUsedAt
     * @return usedAt
     */
    public LocalDateTime getUsedAt() {
        return usedAt;
    }

    /**
     * getCharge abstract method made concrete in subclasses
     *
     */
    // Concrete subclasses provide the final computed charge (could include taxes, tips, surcharges)
    public abstract double getCharge();

    /**
     * applyToPayment
     * @param payment
     * @throws IllegalArgumentException if payment is null
     */
    // Link into Payment: add this facility as a line item
    public void applyToPayment(Payment payment) {
        if (payment == null) {
            throw new IllegalArgumentException("Payment cannot be null when applying facility charge");
        }
        payment.addCharge(description + " (" + this.getClass().getSimpleName() + ")", getCharge());
    }

}



