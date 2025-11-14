package com.hotel.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.hotel.Model.Booking;
import com.hotel.Model.BookingStatus;
import com.hotel.Model.Room;
import com.hotel.Model.RoomType;

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
    //double check this for loop. debugger suggested it. does it work? MP
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
//-----------------------------Check availability and dates-------------------

    public RoomInventoryImpl() {
    }

    public void checkRoomAvailability (RoomType roomType){

       // List<Room> rooms1;
        for (Room room : rooms){
            if (room.getRoomType()==roomType){
            System.out.println(room.getRoomNumber());
            }
        }

//        rooms.stream()
//                .filter(Room ->Room.getRoomType()==roomType)
//                .forEach(Room -> System.out.println(Room.getRoomNumber()));
        //for (Room room : rooms = rooms.stream().filter(RoomType::roomType).toList()) {


        }

    /**
     * checkRoomAvailability - this method searches for an available room for the guest.
     * It first takes the list of all the Rooms and orders them by the roomBookingCount and then by RoomNumber (reversed).
     * The reason for reordering the Rooms is to avoid using one Room too much.
     * The method then checks each Room that matches the roomType selected and calls checkForBookingOverlap to see if the
     * Room is available on the Guest's selected dates.
     * The search loop breaks when an available room is found or if no room is available.
      * @param booking the booking object that contains the arrive and depart dates
     * @param roomType the type of room the guest is looking to book
     *
     */
    public void checkRoomAvailability (Booking booking, RoomType roomType){

        System.out.println("Checking Room Availability..." + roomType);
//        List<Room> rooms1;
//        String roomTypeText = roomType.toString();

        //for loop runs through list of all rooms in the hotel roomInventory rooms.
        // It finds the correct room type first and then checks for date overlap.
        //To make sure that we do not overuse any one room, the rooms are ordered by their booking count descending
        // and then by their room number ascending

        List<Room> sortedRoomList = new ArrayList<>(rooms);
        sortedRoomList.sort(Comparator.comparing(Room::getRoomBookingCount)
                .thenComparing(Room::getRoomNumber).reversed());


        for (Room room : sortedRoomList){
            //System.out.println(room.getRoomType());
            if (room.getRoomType()==roomType){
                System.out.println("Possible room number: " + room.getRoomNumber()); //added output for testing MP
                if(room.getBookingRegister().checkForBookingOverlap(booking, room))
                   System.out.println("Booking Unavailable");
                else {
                    System.out.println("Booking Available");
                    booking.setBookingStatus(BookingStatus.POSSIBLE); //update booking status at each phase of process
                    booking.setBookingRoom(room);
                    room.setNextRoomBookingCount();
                    break; //the search will stop at the first room that is the correct type and has available dates that match the booking

                }
            }
        }






    }

    /**
     * showAllBookings loops through each Room and calls the showBookedDates for each of them
     */
    public void showAllBookings(){
        for (Room room : rooms) {
            room.getBookingRegister().showBookedDates(room);
        }

//            TreeMap<Integer, Booking> bookingRegister = room.getBookingRegister();
//            System.out.println(room.getBookingRegister());
//            System.out.println("Yup");
//
//            for (Booking booking : bookingRegister.values()){
//                System.out.println(booking.getBookingID() + ": Arrival: " + booking.getArriveDate()
//                        + " Departure: " + booking.getDepartDate()
//                        + " Guest: " + booking.getBookingGuest().getName()
//                        + " Room: " + booking.getBookingRoom().getRoomNumber());
//            }
////            for (Map.Entry<Integer, Booking> entry : bookingRegister.entrySet()){
////                System.out.println(entry.getKey() + ": Arrival: " + entry.getValue().getArriveDate()
////                        + " Departure: " + entry.getValue().getDepartDate()
////                        + " Guest: " + entry.getValue().getBookingGuest().getName()
////                        + " Room: " + entry.getValue().getBookingRoom().getRoomNumber());
////            }
//        }

    }
}