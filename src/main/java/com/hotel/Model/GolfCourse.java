package com.hotel.Model;

public class GolfCourse extends Facilities {
    private final int quantity;

    public GolfCourse(String description, double baseCost, int quantity) {
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


