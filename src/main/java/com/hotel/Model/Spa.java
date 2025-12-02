package com.hotel.Model;

/**
 * Represents use of the Spa Facility
 *
 */
public class Spa extends Facilities {
    private final int quantity;

    /**
     * Constructor for Spa
     * @param description description
     * @param baseCost cost
     * @param quantity number of guests
     */
    public Spa(String description, double baseCost, int quantity) {
        super(description, baseCost);
        this.quantity = quantity;
    }

    /**
     * getCharge
     * @return baseCost multiplied by quantity of guests
     */
    @Override
    public double getCharge() {
        return getBaseCost() * getQuantity();
    }

    /**
     * getQuantity gets number of guests
     * @return quantity
     */
    public int getQuantity() {
        return quantity;
    }
}



