package com.hotel;
import com.hotel.Model.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FacilitiesTest {

    @Test
    void diningChargeReturnsBaseCost() {
        Dining dining = new Dining("A la carte", 25.00, 1);
        assertEquals(25.00, dining.getCharge(), 1e-6);
    }

    @Test
    void diningDescriptionAndBaseCostAccessors() {
        Dining dining = new Dining("Set menu", 30.50, 1);
        assertEquals("Set menu", dining.getDescription());
        assertEquals(30.50, dining.getBaseCost(), 1e-6);
    }

    @Test
    void golfCourseChargeReturnsBaseCost() {
        GolfCourse golf = new GolfCourse("18-hole", 75.00, 1);
        assertEquals(75.00, golf.getCharge(), 1e-6);
    }

    @Test
    void golfCourseDescriptionAndBaseCostAccessors() {
        GolfCourse golf = new GolfCourse("9-hole", 45.00, 1);
        assertEquals("9-hole", golf.getDescription());
        assertEquals(45.00, golf.getBaseCost(), 1e-6);
    }

    @Test
    void gymChargeReturnsBaseCost() {
        Gym gym = new Gym("Day pass", 10.00, 1);
        assertEquals(10.00, gym.getCharge(), 1e-6);
    }

    @Test
    void gymDescriptionAndBaseCostAccessors() {
        Gym gym = new Gym("Monthly membership", 50.00, 1);
        assertEquals("Monthly membership", gym.getDescription());
        assertEquals(50.00, gym.getBaseCost(), 1e-6);
    }

    @Test
    void spaChargeReturnsBaseCost() {
        Spa spa = new Spa("Massage", 60.00, 1);
        assertEquals(60.00, spa.getCharge(), 1e-6);
    }

    @Test
    void spaDescriptionAndBaseCostAccessors() {
        Spa spa = new Spa("Facial", 40.00, 1);
        assertEquals("Facial", spa.getDescription());
        assertEquals(40.00, spa.getBaseCost(), 1e-6);
    }
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

    @Test
    void swimmingPoolChargeReturnsBaseCost() {
        SwimmingPool pool = new SwimmingPool("Lap pool", 15.00, 1);
        assertEquals(15.00, pool.getCharge(), 1e-6);
    }
    @Test
    void swimmingPoolDescriptionAndBaseCostAccessors() {
        SwimmingPool pool = new SwimmingPool("Leisure pool", 20.00, 1);
        assertEquals("Leisure pool", pool.getDescription());
        assertEquals(20.00, pool.getBaseCost(), 1e-6);
    }

}
