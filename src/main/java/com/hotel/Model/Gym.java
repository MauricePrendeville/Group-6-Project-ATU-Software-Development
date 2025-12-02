package com.hotel.Model;

/**
 * Represents use of the Gym Facility
 */
public class Gym extends Facilities {
    /**
     * Constructor for Gym
     * @param description description
     * @param baseCost cost
     * @param quantity number of guests
     */
    public Gym(String description, double baseCost, int quantity) {
        // Facilities has a two-arg constructor; call it with the expected parameters
        super(description, baseCost);
        // quantity is accepted for compatibility but not forwarded to Facilities
    }

    /**
     * Constructor for Gym for a single guest
     * @param description description
     * @param baseCost cost
     */
    // Convenience two-arg constructor that defaults quantity to 1
    public Gym(String description, double baseCost) {
        this(description, baseCost, 1);
    }

    /**
     * getCharge
     * @return baseCost
     */
    @Override
    public double getCharge() {
        return getBaseCost();
    }
}
