package com.hotel.Model;

/**
 * Represents use of the Swimming Pool Facility
 */
public class SwimmingPool extends Facilities {
    private final int quantity;

    /**
     * Constructor for SwimmingPool for a single guest
     * @param description description
     * @param baseCost cost
     */
    public SwimmingPool(String description, double baseCost) {
        this(description, baseCost, 1);
    }

    /**
     * Constructor for SwimmingPool
     * @param description description
     * @param baseCost cost
     * @param quantity number of guests
     */
    public SwimmingPool(String description, double baseCost, int quantity) {
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
