package com.hotel;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Represents a single room in the hotel.
 * Each Room object holds basic details such as
 * room number, type, availability, and price per night.
 *
 * @author Vijaylakshmi
 * @version 1.0
 */
public class Room {

    private int roomNumber;
    private RoomType roomType;      // e.g., "Single", "Double", "Suite"
    private boolean available;    // true if room is available for booking
    private double pricePerNight; // room cost per night

    //Booking Register additions to Room (Maurice)
    private TreeMap<Integer, Booking> bookingRegister;
    private int bookingID;
    //private ArrayList<Room> roomList;
    private ArrayList<LocalDate> bookedDates;


    public Room(int roomNumber, RoomType roomType, boolean available, double pricePerNight) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.available = available;
        this.pricePerNight = pricePerNight;

        //extra attributes initialised for booking register functionality (Maurice)
        this.bookingID = 1;
        this.bookingRegister = new TreeMap<>(); //treemap used to keep track of booking details
        //this.roomList = new ArrayList<>();
        this.bookedDates = new ArrayList<>(); //array used to check availability of range of dates
    }

    // 🔹 Getters and Setters
    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    // 🔹 Utility Methods
    @Override
    public String toString() {
        return "Room{" +
                "roomNumber=" + roomNumber +
                ", roomType='" + roomType + '\'' +
                ", available=" + available +
                ", pricePerNight=" + pricePerNight +
                '}';
    }

    //-------------//methods added for booking register functionality //------------------------------------
    public void addBooking(Booking booking) {

//        //TreeMap<Booking, Integer> bookingRegister = new TreeMap<>();
        int nextBookingID = bookingID++;
        System.out.println(booking.getBookingGuest().getName());
        bookingRegister.put(nextBookingID, booking);
        System.out.println("add booking" + nextBookingID);
    }

    public void showBookings() {
        System.out.println("List of Bookings: ");
        for (Map.Entry<Integer, Booking> entry : bookingRegister.entrySet()) {
            System.out.println(entry.getKey() + ": Arrival: " + entry.getValue().getArriveDate()
                    + " Departure: " + entry.getValue().getDepartDate()
                    + " Guest: " + entry.getValue().getBookingGuest().getName()
                    + " Room: " + entry.getValue().getBookingRoom().getRoomNumber());

        }
    }

    //add option to pass date to this method to get guests on a particular date
    public void showGuests() {
        System.out.println("List of Guests: ");
        for (Map.Entry<Integer, Booking> entry : bookingRegister.entrySet()) {
            System.out.println(entry.getKey() + " Guest: " + entry.getValue().getBookingGuest().getName()
                    + " Room: " + entry.getValue().getBookingRoom().getRoomNumber());
        }
    }

//    public void showRooms() {
//        ArrayList<Room> rooms = new ArrayList<>();
//        rooms = roomList;
//        for (Room room : rooms)
//            System.out.println(room.getRoomNumber());
//
//    }

    public void addDatesToRegister(Booking booking, Room room) {
        LocalDate arrive = booking.getArriveDate();
        LocalDate depart = booking.getDepartDate();

        LocalDate addDate = arrive;
        while (!addDate.isEqual(depart)) {
            bookedDates.add(addDate);
            addDate = addDate.plusDays(1);
        }
    }

    //checkForBookingOverlap returns True if there is an overlap with an existing booking
    public boolean checkForBookingOverlap(Booking booking, Room room) {
        LocalDate arrive = booking.getArriveDate();
        LocalDate depart = booking.getDepartDate();

        ArrayList<LocalDate> potentialBookingDates = new ArrayList<>();
        LocalDate addDate = arrive;
        while (!addDate.isEqual(depart)) {
            potentialBookingDates.add(addDate);
            addDate = addDate.plusDays(1);
        }
        for (LocalDate checkDate : bookedDates) {
            if (potentialBookingDates.contains(checkDate)) {
                return true;
            }
        }

        return false;
    }


    public void showBookedDates(Room room) {
        for (LocalDate bookedDate : bookedDates) {
            System.out.println("Room: " + room.getRoomNumber() + " Type: " + room.getRoomType() + " Date: " + bookedDate);
        }

    }

}
////skeleton class for testing
//public class Room {
//
//    private String roomNumber;
//    private String roomClass;
//
//
//    public Room(String roomNumber, String roomClass) {
//        this.roomNumber = roomNumber;
//        this.roomClass = roomClass;
//    }
//
//    public String getRoomNumber() {
//        return roomNumber;
//    }
//
//    public String getRoomClass() {
//        return roomClass;
//    }
//
//    public void setRoomNumber(String roomNumber) {
//        this.roomNumber = roomNumber;
//    }
//
//    public void setRoomClass(String roomClass) {
//        this.roomClass = roomClass;
//    }
//}
