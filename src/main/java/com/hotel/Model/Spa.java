package com.hotel.Model;

public class Spa extends Facilities {
    private final int quantity;

    public Spa(String description, double baseCost, int quantity) {
        super(description, baseCost);
        this.quantity = quantity;
    }

    @Override
    public double getCharge() {
        return getBaseCost() * getQuantity();
    }
}

    public int getQuantity() {
        return quantity;
    }
}


