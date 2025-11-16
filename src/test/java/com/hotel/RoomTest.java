package com.hotel;

import com.hotel.Model.Room;
import com.hotel.Model.RoomType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RoomTest {

    private Room room;

    @BeforeEach
    void setUp() {
        room = new Room(101, RoomType.SINGLE, true, 120.0);
    }

    @Test
    void testRoomCreation() {
        assertEquals(101, room.getRoomNumber());
        assertEquals(RoomType.SINGLE, room.getRoomType());
        assertEquals(120.0, room.getPricePerNight());
        assertTrue(room.isAvailable());
    }

    @Test
    void testRoomToStringFormat() {
        String result = room.toString();
        assertTrue(result.contains("roomNumber=101"));
        assertTrue(result.contains("Single"));
    }

    @Test
    void testSetters() {
        room.setRoomNumber(202);
        room.setRoomType(RoomType.DOUBLE);
        room.setAvailable(false);
        room.setPricePerNight(150.0);

        assertEquals(202, room.getRoomNumber());
        assertEquals(RoomType.DOUBLE, room.getRoomType());
        assertFalse(room.isAvailable());
        assertEquals(150.0, room.getPricePerNight());
    }
}
