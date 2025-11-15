package com.hotel.Model;

    public class Spa extends Facilities {
        private final double treatmentSurcharge;

        public Spa(String description, double baseCost, double treatmentSurcharge) {
            super(description, baseCost);
            this.treatmentSurcharge = treatmentSurcharge;
        }

        @Override
        public double getCharge() {
            return getBaseCost() + treatmentSurcharge;
        }
    }


