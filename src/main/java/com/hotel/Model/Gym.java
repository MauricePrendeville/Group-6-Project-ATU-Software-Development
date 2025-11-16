package com.hotel.Model;

public class Gym extends Facilities {
    public Gym(String description, double baseCost, int quantity) {
        // Facilities has a two-arg constructor; call it with the expected parameters
        super(description, baseCost);
        // quantity is accepted for compatibility but not forwarded to Facilities
    }
    // Convenience two-arg constructor that defaults quantity to 1
    public Gym(String description, double baseCost) {
        this(description, baseCost, 1);
    }

    @Override
    public double getCharge() {
        return getBaseCost();
    }
}
