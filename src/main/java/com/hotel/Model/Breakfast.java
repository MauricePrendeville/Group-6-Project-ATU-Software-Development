package com.hotel.Model;

    public class Breakfast extends Facilities {
        public Breakfast(String description, double baseCost) {
            super(description, baseCost);
        }

        @Override
        public double getCharge() {
            return getBaseCost();
        }
    }


