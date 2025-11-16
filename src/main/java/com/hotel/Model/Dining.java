package com.hotel.Model;

public class Dining extends Facilities {
    private final int quantity;

    public Dining(String description, double baseCost, int quantity) {
        // Facilities provides a two-argument constructor; pass those two
        super(description, baseCost);
        this.quantity = quantity;
    }

    @Override
    public double getCharge() {
        // total cost = baseCost * quantity
        return getBaseCost() * getQuantity();
    }

    public int getQuantity() {
        return quantity;
    }
}

