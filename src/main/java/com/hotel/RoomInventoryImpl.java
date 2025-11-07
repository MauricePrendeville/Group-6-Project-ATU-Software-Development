package com.hotel;

import java.util.ArrayList;
import java.util.List;
import com.hotel.Room;
import com.hotel.RoomType;
/**
 * Implementation of Room Inventory Management.
 * This class provides methods to manage the collection of rooms,
 * including adding, removing, updating status, and searching rooms.
 * @author Vijaylakshmi
 * @version 1.0
 */

public class RoomInventoryImpl {

    // Attributes
    private List<Room> rooms = new ArrayList<>();  // Collection of all rooms in the hotel

    private int totalRooms; // Total number of rooms

    private List<Room> availableRooms; // Number of available rooms

    private int bookedRooms; // Number of booked rooms

    /**
     * Adds a new room to the inventory.
     * @param room
     */
    public void addRoom(Room room) {
        rooms.add(room);
        System.out.println("Room added successfully: " + room);
    }

    /**
     * Removes a room from the inventory by room number.
     * @param roomNumber
     */
    public void removeRoom(int roomNumber) {
        boolean removed = rooms.removeIf(room -> room.getRoomNumber() == roomNumber && room.isAvailable());
        if (removed) {
            System.out.println("Room " + roomNumber + " removed successfully.");
        } else {
            System.out.println("Room " + roomNumber + " not found or Unable to remove as room is booked.");
        }
    }

    /**
     * Updates the availability status of a room.
     * @param roomNumber
     * @param available
     */
    public void updateRoomStatus(int roomNumber, boolean available) {

        for (Room room : rooms) {
            if (room.getRoomNumber() == roomNumber) {
                room.setAvailable(available);
                System.out.println("Room " + roomNumber + " availability updated to: " + available);
                return;
            }
        }
        System.out.println("Room " + roomNumber + " not found in inventory.");
    }

    /**
     * Retrieves a list of all available rooms.
     * @return getAvailableRooms
     */
    public List<Room> getAvailableRooms() {
        for (Room room : availableRooms = rooms.stream().filter(Room::isAvailable).toList()) {
            
        }

        return availableRooms;
    }

    /**
     * Searches for rooms by type.
     * @param type
     * @return rooms of specified type
     */
    public List<Room> searchRoomByType(RoomType type) {
        List<Room> result = new ArrayList<>();
        for (Room room : rooms) {
            if (room.getRoomType() == type) {
                result.add(room);
            }
        }
        return result;
    }

    /**
     * Displays all rooms in the inventory.
     */
    public void displayAllRooms() {
        if (rooms.isEmpty()) {
            System.out.println("No rooms in inventory.");
            return;
        }
        System.out.println("All Rooms in Inventory:");
        for (Room room : rooms) {
            System.out.println(room);
        }
    }

    public int getTotalRooms() {
        totalRooms =  rooms.size();
        return totalRooms;
    }

    public int getBookedRooms() {
        int availableRooms = rooms.stream().filter(Room::isAvailable).toList().size(
        );
        bookedRooms = rooms.size() - availableRooms;
        System.out.println("Total booked rooms: " + bookedRooms);
        return bookedRooms;
    }



}