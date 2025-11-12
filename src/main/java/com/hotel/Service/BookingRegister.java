package com.hotel.Service;
import com.hotel.Model.Booking;
import com.hotel.Model.Room;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;


public class BookingRegister {

    private TreeMap<Integer, Booking> bookingRegister;
    private ArrayList<Room> roomList;
    private ArrayList<LocalDate> bookedDates;

    public BookingRegister() {
        this.bookingRegister = new TreeMap<>(); //treemap used to keep track of booking details
        this.roomList = new ArrayList<>();
        this.bookedDates = new ArrayList<>(); //array used to check availability of range of dates
    }


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

    public void showRooms() {
            ArrayList<Room> rooms = new ArrayList<>();
            rooms = roomList;
            for (Room room : rooms)
                System.out.println(room.getRoomNumber());

    }

    //this method adds the booking to the booking register for this room. The bookingID number is also added
    public void addBooking(Booking booking) {

        System.out.println(booking.getBookingGuest().getName());
        bookingRegister.put(booking.getBookingID(), booking);
        System.out.println("add booking" + booking.getBookingID());
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


    //use addDatesToRegister when the Booking is set to CONFIRMED
    public void addDatesToRegister(Booking booking) {
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
