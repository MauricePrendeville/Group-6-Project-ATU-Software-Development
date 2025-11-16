package com.hotel.Model;

public class Dining extends Facilities {
    private final double tip; // absolute tip amount

    public Dining(String description, double baseCost, double tip) {
        super(description, baseCost);
        this.tip = tip;
    }

    @Override
    public double getCharge() {
        return getBaseCost() + tip;
    }
}


