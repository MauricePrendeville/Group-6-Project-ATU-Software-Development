package com.hotel;

/**
 * Standard room
 * Inherits attributes and behaviours from its parent Room abstract class
 */
public class StandardRoom extends Room {

    // This room has a fixed price of €100/night
    private static final double STANDARD_PRICE = 100.0;

    /**
     * Constructor for StandardRoom.
     * @param roomNumber The room number
     */
    public StandardRoom(String roomNumber) {
        // StnadardRoom's constructor calls parent (Room) constructor with type and price parameters, status will be automatically set to Avaialable
        super(roomNumber, "STANDARD", STANDARD_PRICE);
    }
}