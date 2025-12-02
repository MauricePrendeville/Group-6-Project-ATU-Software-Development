package com.hotel.Model;

/**
 * Represents Golf facility
 */
public class GolfCourse extends Facilities {
    private final int quantity;

    /**
     * Constructor for GolfCourse
     * @param description description
     * @param baseCost cost for round of golf
     * @param quantity number of guests
     */
    public GolfCourse(String description, double baseCost, int quantity) {
        super(description, baseCost);
        this.quantity = quantity;
    }

    /**
     * getCharge
     * @return baseCost multiplied by number of guests
     */
    @Override
    public double getCharge() {
        return getBaseCost() * getQuantity();
    }

    /**
     * getQuantity gets number of guests who are playing golf
     * @return quantity
     */
    public int getQuantity() {
        return quantity;
    }
}


