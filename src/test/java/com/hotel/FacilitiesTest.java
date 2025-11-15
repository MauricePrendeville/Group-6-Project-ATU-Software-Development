package com.hotel;

import com.hotel.Model.Breakfast;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

    public class FacilitiesTest {

        @Test
        void breakfastChargeReturnsBaseCost() {
            Breakfast breakfast = new Breakfast("Continental", 9.99);
            assertEquals(9.99, breakfast.getCharge(), 1e-6);
        }

        @Test
        void breakfastDescriptionAndBaseCostAccessors() {
            Breakfast breakfast = new Breakfast("Buffet", 12.50);
            assertEquals("Buffet", breakfast.getDescription());
            assertEquals(12.50, breakfast.getBaseCost(), 1e-6);
        }

}
