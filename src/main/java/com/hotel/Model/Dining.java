package com.hotel.Model;

/**
 * Dining class extends Facilities. It contains the cost for dinner and the quantity of diners.
 */
public class Dining extends Facilities {
    private final int quantity;

    /**
     * Constructor for Dining
     * @param description description of dinner
     * @param baseCost base cost of dinner
     * @param quantity number of diners
     */
    public Dining(String description, double baseCost, int quantity) {
        // Facilities provides a two-argument constructor; pass those two
        super(description, baseCost);
        this.quantity = quantity;
    }

    /**
     * getCharge calculates charge by multiplying the cost by the number of diners.
     * @return baseCost multiplied by quantity
     */
    @Override
    public double getCharge() {
        // total cost = baseCost * quantity
        return getBaseCost() * getQuantity();
    }

    /**
     * getQuantity gets the number of diners
     * @return quantity
     */
    public int getQuantity() {
        return quantity;
    }
}

