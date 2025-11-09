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
    private LocalDate arriveDate;
    private int bookingID;
    private ArrayList<Room> roomList;
    private ArrayList<LocalDate> bookedDates;

    public BookingRegister(LocalDate arriveDate) {
        this.arriveDate = arriveDate;
        this.bookingID = 1;
        this.bookingRegister = new TreeMap<>(); //treemap used to keep track of booking details
        this.roomList = new ArrayList<>();
        this.bookedDates = new ArrayList<>(); //array used to check availability of range of dates

    }

    public void setBookingRegister(TreeMap<Integer, Booking> bookingRegister) {
        this.bookingRegister = bookingRegister;
    }

    public void setArriveDate(LocalDate arriveDate) {
        this.arriveDate = arriveDate;
    }

    public int getBookingID(){
        return bookingID;

    }

    public void addRoomToRegister(Room room){
            //boolean addroom =
                    roomList.add(room);
                    System.out.println("Room Added: "+room.getRoomNumber() +" "+room.getRoomType());
    }

    public String getFormattedDate(){
        return arriveDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM));
    }


    public void addBooking(Booking booking){

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

    public void showRooms() {
            ArrayList<Room> rooms = new ArrayList<>();
            rooms = roomList;
            for (Room room : rooms)
                System.out.println(room.getRoomNumber());

    }

    public void addDatesToRegister(Booking booking, Room room){
            LocalDate arrive = booking.getArriveDate();
            LocalDate depart = booking.getDepartDate();

            LocalDate addDate = arrive;
            while (!addDate.isEqual(depart)){
                bookedDates.add(addDate);
                addDate = addDate.plusDays(1);
            }
    }
//checkForBookingOverlap returns True if there is an overlap with an existing booking
    public boolean checkForBookingOverlap(Booking booking, Room room){
        LocalDate arrive = booking.getArriveDate();
        LocalDate depart = booking.getDepartDate();

        ArrayList<LocalDate> potentialBookingDates = new ArrayList<>();
        LocalDate addDate = arrive;
        while (!addDate.isEqual(depart)){
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


    public void showBookedDates(Room room){
            for (LocalDate bookedDate: bookedDates){
                System.out.println("Room: "+ room.getRoomNumber() + " Type: "+ room.getRoomType() +" Date: " + bookedDate);
            }


    }

//    public void checkAvailableOnBookingDate(){
//        boolean isAfter = this.assignmentSubmittedDate.isAfter(this.assignment_Due_Date);
//        if (isAfter == false )
//            System.out.println("Assignment submitted on time" + " sub:" + this.assignmentSubmittedDate + " due: " + this.assignment_Due_Date);
//        else
//            System.out.println("Assignment submitted late" + " sub:" + this.assignmentSubmittedDate + " due: " + this.assignment_Due_Date);
//
//    }

}
