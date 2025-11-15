package com.hotel.Model;

    public class SwimmingPool extends Facilities {
        public SwimmingPool(String description, double baseCost) {
            super(description, baseCost);
        }

        @Override
        public double getCharge() {
            return getBaseCost();
        }
    }

