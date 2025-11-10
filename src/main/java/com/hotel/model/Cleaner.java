package com.hotel;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a cleaner in the hotel.
 * Can view assigned rooms and update cleaning status.
 *
 * @author dev_amru
 * @version 1.0
 */
public class Cleaner extends User {
    private List<String> assignedRooms;

    public Cleaner(String userId, String name, String email, String phone, String password) {
        super(userId, name, email, phone, password, UserRole.CLEANER);
        this.assignedRooms = new ArrayList<>();
    }

    public void assignRoom(String roomNumber) {
        validateInput(roomNumber, "Room number");
        if (!assignedRooms.contains(roomNumber)) {
            assignedRooms.add(roomNumber);
        }
    }

    public void removeAssignedRoom(String roomNumber) {
        assignedRooms.remove(roomNumber);
    }

    public List<String> getAssignedRooms() {
        return new ArrayList<>(assignedRooms);
    }

    public boolean isAssignedToRoom(String roomNumber) {
        return assignedRooms.contains(roomNumber);
    }

    @Override
    public String getPermissions() {
        return "Cleaner: View assigned rooms, update cleaning status";
    }

    @Override
    public boolean hasPermission(String permission) {
        switch (permission) {
            case "VIEW_ASSIGNED_ROOMS":
            case "UPDATE_ROOM_STATUS":
                return true;
            default:
                return false;
        }
    }
}
