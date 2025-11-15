package com.hotel;

import java.util.ArrayList;
import java.util.List;

public class ReceptionStaff extends com.hotel.User {
    private List<String> assignedShifts;

    public ReceptionStaff(String userId, String name, String email, String phone, String password) {
        super(userId, name, email, phone, password, com.hotel.UserRole.RECEPTION_STAFF);
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
