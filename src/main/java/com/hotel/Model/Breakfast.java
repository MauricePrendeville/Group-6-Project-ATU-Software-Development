package com.hotel.Model;

/**
 * Represents a Breakfast meal. The class extends Facility. It contains the charge for Breakfast
 */
    public class Breakfast extends Facilities {
    /**
     * Constructor for Breakfast
     * @param description breakfast description
     * @param baseCost cost of breakfast
     */
    public Breakfast(String description, double baseCost) {
            super(description, baseCost);
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


