package com.hotel.Model;

public class SwimmingPool extends Facilities {
    private final int quantity;

    public SwimmingPool(String description, double baseCost) {
        this(description, baseCost, 1);
    }

    public SwimmingPool(String description, double baseCost, int quantity) {
        super(description, baseCost);
        this.quantity = quantity;
    }

    @Override
    public double getCharge() {
        return getBaseCost() * getQuantity();
    }

    public int getQuantity() {
        return quantity;
    }
}
