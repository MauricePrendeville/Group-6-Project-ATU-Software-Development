package com.hotel;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for StandardRoom class.
 */
public class StandardRoomTest {

    private StandardRoom room;

    @BeforeEach
    public void setUp() {
        room = new StandardRoom("101");
    }

    // constructor tests

    @Test
    public void testRoomCreation() {
        assertEquals("101", room.getRoomNumber());
        assertEquals("STANDARD", room.getRoomType());
        assertEquals(100.0, room.getPricePerNight());
    }

    @Test
    public void testRoomStartsAvailable() {
        // New rooms should start as AVAILABLE
        assertEquals("AVAILABLE", room.getStatus());
        assertTrue(room.isAvailable());
    }

    // status tests

    @Test
    public void testSetStatusToOccupied() {
        room.setStatus("OCCUPIED");
        assertEquals("OCCUPIED", room.getStatus());
        assertFalse(room.isAvailable());
    }

    @Test
    public void testSetStatusToCleaning() {
        room.setStatus("CLEANING");
        assertEquals("CLEANING", room.getStatus());
        assertFalse(room.isAvailable());
    }

    @Test
    public void testSetStatusBackToAvailable() {
        room.setStatus("OCCUPIED");
        room.setStatus("AVAILABLE");
        assertTrue(room.isAvailable());
    }

    // isAvailable tests

    @Test
    public void testIsAvailableWhenAvailable() {
        room.setStatus("AVAILABLE");
        assertTrue(room.isAvailable());
    }

    @Test
    public void testIsAvailableWhenOccupied() {
        room.setStatus("OCCUPIED");
        assertFalse(room.isAvailable());
    }

    @Test
    public void testIsAvailableWhenCleaning() {
        room.setStatus("CLEANING");
        assertFalse(room.isAvailable());
    }

    // polymorphism test

    @Test
    public void testPolymorphism() {
        // can StandardRoom be treated like Room
        Room genericRoom = new StandardRoom("202");
        assertEquals("202", genericRoom.getRoomNumber());
        assertEquals("STANDARD", genericRoom.getRoomType());
    }
}