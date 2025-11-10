package com.hotel;

public class Manager extends User {
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
