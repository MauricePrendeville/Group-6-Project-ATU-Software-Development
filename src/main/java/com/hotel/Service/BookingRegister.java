package com.hotel.Service;
import com.hotel.Model.Booking;
import com.hotel.Model.Room;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
* This class is used to keep track of all Bookings for an individual Room
*
* This class has a method to checkForBookingOverlap to avoid rooms being double booked.
 * Also method for addDatesToRegister that will convert an arrive and depart date into
 * a list of dates to add to the bookingRegister TreeMap
 * Each BookingRegister object is associated with a single Room*

 */

public class BookingRegister {

    private TreeMap<Integer, Booking> bookingRegister;
    private ArrayList<Room> roomList;
    private ArrayList<LocalDate> bookedDates;


    /** BookingRegister constructor
     * A BookingRegister object is created for each Room.
     * bookingRegister TreeMap contains the list of Booking objects associated with the Room.
     * bookDates ArrayList is used to convert the arrive and depart dates into a list to help check availability.
     */
    public BookingRegister() {
        this.bookingRegister = new TreeMap<>(); //treemap used to keep track of booking details
        this.roomList = new ArrayList<>();
        this.bookedDates = new ArrayList<>(); //array used to check availability of range of dates
    }

    /**
     * getFormattedDate puts dates in a pleasant readable format
     * @param date accepts date
     * @return date formatted for readability
     */
    public String getFormattedDate(LocalDate date){
        return date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM));
    }


//    public void addBooking(Booking booking){
//
////        //TreeMap<Booking, Integer> bookingRegister = new TreeMap<>();
//        int nextBookingID = bookingID++;
//        System.out.println(booking.getBookingGuest().getName());
//        bookingRegister.put(nextBookingID, booking);
//        System.out.println("add booking" + nextBookingID);
//    }


////add option to pass date to this method to get guests on a particular date
//    public void showGuests() {
//        System.out.println("List of Guests: ");
//        for (Map.Entry<Integer, Booking> entry : bookingRegister.entrySet()) {
//            System.out.println(entry.getKey() + " Guest: " + entry.getValue().getBookingGuest().getName()
//                    + " Room: " + entry.getValue().getBookingRoom().getRoomNumber());
//        }
//    }

    /**
     * showRooms - This method was created when it was thought that all the Rooms would be in one bookingRegister
     * instead of a bookingRegister for each Room and a RoomInventory for the Hotel.
     */
    public void showRooms() {
            ArrayList<Room> rooms = new ArrayList<>();
            rooms = roomList;
            for (Room room : rooms)
                System.out.println(room.getRoomNumber());

    }

    //this method adds the booking to the booking register for this room. The bookingID number is also added

    /**
     * addBooking - This method adds a Booking object to the bookingRegister list for a Room.
     * The Booking should be CONFIRMED before this method is called.
     * @param booking - the details of the Booking
     */
    public void addBooking(Booking booking) {

        System.out.println(booking.getBookingGuest().getName());
        bookingRegister.put(booking.getBookingID(), booking);
        System.out.println("add booking" + booking.getBookingID());
    }

    /**
     * showBookings - This method was used for to show each Booking including
     * details of Guest, Arrival, Departure, and Room. It was used for testing until the
     * HotelManagmentUI class was completed.
     */
    public void showBookings() {
        System.out.println("List of Bookings: ");
        for (Map.Entry<Integer, Booking> entry : bookingRegister.entrySet()) {
            System.out.println(entry.getKey() + ": Arrival: " + entry.getValue().getArriveDate()
                    + " Departure: " + entry.getValue().getDepartDate()
                    + " Guest: " + entry.getValue().getBookingGuest().getName()
                    + " Room: " + entry.getValue().getBookingRoom().getRoomNumber());

        }
    }

    /**
     * showGuests - This method was used to show the list of all Guests and their Rooms.
     * It was used for testing until the HotelManagementUI class was completed.
     */
    //add option to pass date to this method to get guests on a particular date
    public void showGuests() {
        System.out.println("List of Guests: ");
        for (Map.Entry<Integer, Booking> entry : bookingRegister.entrySet()) {
            System.out.println(entry.getKey() + " Guest: " + entry.getValue().getBookingGuest().getName()
                    + " Room: " + entry.getValue().getBookingRoom().getRoomNumber());
        }
    }

    /**
     * addDatesToRegister - This method is used to add dates to the bookingRegister List for a Room when
     * the Booking is set to CONFIRMED
     * @param booking - the details of the Guest's Booking
     */
     public void addDatesToRegister(Booking booking) {
        LocalDate arrive = booking.getArriveDate();
        LocalDate depart = booking.getDepartDate();

        LocalDate addDate = arrive;
        while (!addDate.isEqual(depart)) {
            bookedDates.add(addDate);
            addDate = addDate.plusDays(1);
        }
    }

    /**
     * checkForBookingOverlap This method creates a list of dates from arrive date
     * to the day before depart. It then cycles through the existing booking dates and
     * checks to see if any of the potential dates matches an existing booking date.
     * @param booking the booking object. data including the arrive and depart dates
     * @param room the room object. not necessary anymore!
     * @return true if there is an overlap with an existing booking. false if there is no overlap.
     */
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

    /**
     * showBookedDates a method to show the full list of booked dates for a given Room.
     * It prints a formatted list.
     * @param room the Room in question
     */
    public void showBookedDates(Room room) {
        for (LocalDate bookedDate : bookedDates) {
            System.out.println("Room: " + room.getRoomNumber() + " Type: " + room.getRoomType() + " Date: " + bookedDate);
        }

    }

}
