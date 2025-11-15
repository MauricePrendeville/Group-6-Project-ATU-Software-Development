package com.hotel.Model;

    public class Gym extends Facilities {
        public Gym(String description, double baseCost) {
            super(description, baseCost);
        }

        @Override
        public double getCharge() {
            return getBaseCost();
        }

}
