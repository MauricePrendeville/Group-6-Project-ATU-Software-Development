package com.hotel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class BookingInterface {


    public BookingInterface() {
    }

    public static LocalDate parseDate(int year, int month, int day) {
        String dateString = String.format("%04d-%02d-%02d", year, month, day);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            return LocalDate.parse(dateString, formatter);
        } catch (DateTimeParseException e) {
            return null;  // Return null for invalid dates
        }
    }

    public LocalDate getValidDate(){
        LocalDate candidateDate = null;
        Scanner scanning = new Scanner(System.in);
        do {
            System.out.println("Day of Month: ");
            int dateDay = scanning.nextInt();
            System.out.println("Month: ");
            int dateMonth = scanning.nextInt();
            System.out.println("Year: ");
            int dateYear = scanning.nextInt();

            candidateDate = parseDate(dateYear, dateMonth, dateDay);
            if (candidateDate == null) {
                System.out.println("Invalid! Try again.");
            }

        } while (candidateDate == null);

        return candidateDate;
    }

    public void getStayRequestDetails (BookingRegister bookingRegister, Room room) {

       Scanner scanning = new Scanner(System.in);
       System.out.println("Enter Date of Arrival: ");
       LocalDate arrivalDate = getValidDate();

       System.out.println("Enter Date of Departure: ");
       LocalDate departureDate = getValidDate();



       System.out.println("Enter Name: ");
       String guestName = scanning.next();
       Guest guest = new Guest(guestName);

       System.out.println("Select Room Type: Regular Double (press 1), Superior Suite (press 2)");
       int roomType = scanning.nextInt();

//       Room room3 = new Room("Three", "Regular Double");

       Booking booking = new Booking("3", arrivalDate,
               departureDate, LocalDate.now(), guest, room);

       if(bookingRegister.checkForBookingOverlap(booking, room))
           System.out.println("Booking Unavailable");
       else {
           System.out.println("Booking Available");
           bookingRegister.addBooking(booking);
       }
   }

}
