package com.hotel.Model;

    public class GolfCourse extends Facilities {
        private final double greenFee;

        public GolfCourse(String description, double baseCost, double greenFee) {
            super(description, baseCost);
            this.greenFee = greenFee;
        }

        @Override
        public double getCharge() {
            return getBaseCost() + greenFee;
        }
    }

