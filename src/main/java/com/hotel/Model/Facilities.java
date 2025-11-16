package com.hotel.Model;

import java.time.LocalDateTime;

public abstract class Facilities {
    private final String description;
    private final double baseCost;
    private final LocalDateTime usedAt;

    protected Facilities(String description, double baseCost) {
        this.description = description;
        this.baseCost = baseCost;
        this.usedAt = LocalDateTime.now();
    }

    public String getDescription() {
        return description;
    }

    public double getBaseCost() {
        return baseCost;
    }

    public LocalDateTime getUsedAt() {
        return usedAt;
    }

    // Concrete subclasses provide the final computed charge (could include taxes, tips, surcharges)
    public abstract double getCharge();

    // Link into Payment: add this facility as a line item
    public void applyToPayment(Payment payment) {
        if (payment == null) {
            throw new IllegalArgumentException("Payment cannot be null when applying facility charge");
        }
        payment.addCharge(description + " (" + this.getClass().getSimpleName() + ")", getCharge());
    }

}



