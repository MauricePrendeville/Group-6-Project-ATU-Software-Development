package com.hotel;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents reception staff in the hotel.
 * Can manage bookings, check in/out guests, and handle customer service.
 * 
 * @author Group 6
 * @version 1.0
 */
public class ReceptionStaff extends User {
    private List<String> assignedShifts;

    public ReceptionStaff(String userId, String name, String email, String phone, String password) {
        super(userId, name, email, phone, password, UserRole.RECEPTION_STAFF);
        this.assignedShifts = new ArrayList<>();
    }

    public void assignShift(String shift) {
        validateInput(shift, "Shift");
        if (!assignedShifts.contains(shift)) {
            assignedShifts.add(shift);
        }
    }

    public List<String> getAssignedShifts() {
        return new ArrayList<>(assignedShifts);
    }

    @Override
    public String getPermissions() {
        return "Reception: Manage bookings, check-in/out guests, view room status, customer service";
    }

    @Override
    public boolean hasPermission(String permission) {
        switch (permission) {
            case "VIEW_BOOKINGS":
            case "CREATE_BOOKING":
            case "MODIFY_BOOKING":
            case "CANCEL_BOOKING":
            case "CHECK_IN_GUEST":
            case "CHECK_OUT_GUEST":
            case "VIEW_ROOMS":
            case "VIEW_GUESTS":
                return true;
            default:
                return false;
        }
    }
}

/**
 * Represents a manager in the hotel.
 * Can generate reports and oversee operations.
 * 
 * @author Group 6
 * @version 1.0
 */
class Manager extends User {
    private String department;

    public Manager(String userId, String name, String email, String phone, 
                   String password, String department) {
        super(userId, name, email, phone, password, UserRole.MANAGER);
        validateInput(department, "Department");
        this.department = department;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        validateInput(department, "Department");
        this.department = department;
    }

    @Override
    public String getPermissions() {
        return "Manager: Generate reports, view all bookings and rooms, manage staff";
    }

    @Override
    public boolean hasPermission(String permission) {
        switch (permission) {
            case "VIEW_BOOKINGS":
            case "VIEW_ROOMS":
            case "VIEW_GUESTS":
            case "VIEW_STAFF":
            case "GENERATE_REPORTS":
            case "VIEW_ANALYTICS":
                return true;
            default:
                return false;
        }
    }
}

/**
 * Represents a cleaner in the hotel.
 * Can view assigned rooms and update cleaning status.
 * 
 * @author Group 6
 * @version 1.0
 */
class Cleaner extends User {
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