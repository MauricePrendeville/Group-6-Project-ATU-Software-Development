package com.hotel;

import com.hotel.UI.HotelManagementUI;

/**
 * Main entry point for the Hotel Management System.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("Initializing Group 6 Hotel Management System...");

        // Launch the unified UI
        HotelManagementUI ui = new HotelManagementUI();
        ui.start();
    }
}