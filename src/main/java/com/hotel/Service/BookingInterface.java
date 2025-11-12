package com.hotel.Service;

import com.hotel.Model.Booking;
import com.hotel.Model.Guest;
import com.hotel.Model.RoomType;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

//import static jdk.jfr.internal.util.Utils.isAfter;

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
//getValidDate in this mode confirms that the date is a real valid date.
// There should be something added to make sure it is a date today or in the future perhaps.
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
//extra getValidDate method to check that the depart date is after the arrive date
    public LocalDate getValidDate(LocalDate arriveDate){
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

            if (candidateDate != null && arriveDate.isAfter(candidateDate)) {
                candidateDate = null;
                System.out.println("Invalid! Try again.");
            }
            if (candidateDate != null && candidateDate.isEqual(arriveDate)) {
                candidateDate = null;
                System.out.println("Invalid! Try again.");
            }

        } while (candidateDate == null);

        return candidateDate;
    }

    public void getStayRequestDetails (RoomInventoryImpl roomInventory) {

       Scanner scanning = new Scanner(System.in);
       System.out.println("Enter Date of Arrival: ");
       LocalDate arriveDate = getValidDate();

       System.out.println("Enter Date of Departure: ");
       LocalDate departDate = getValidDate(arriveDate);


        System.out.println("Enter User ID: ");
        String guestID = scanning.next();
        System.out.println("Enter Name: ");
        String guestName = scanning.next();
        System.out.println("Enter Email: ");
        String guestEmail = scanning.next();
        System.out.println("Enter Phone: ");
        String guestPhone = scanning.next();
        System.out.println("Enter Password: ");
        String guestPassword = scanning.next();
       Guest guest = new Guest(guestID, guestName, guestEmail, guestPhone, guestPassword);
       RoomType roomType = RoomType.SINGLE;

       System.out.println("Select Room Type: Single (press 1), Double (press 2), Deluxe (press 3)");
       int roomTypeNumber =0;
       switch (roomTypeNumber = scanning.nextInt()){
            case 1: roomType =  RoomType.SINGLE;
                break;
            case 2: roomType = RoomType.DOUBLE;
                break;
            case 3: roomType = RoomType.DELUXE;
                break;
            case 4: roomType = RoomType.FAMILY;
                break;
            case 5: roomType = RoomType.SUITE;
                break;
            case 6: roomType = RoomType.PRESIDENTIAL;
                break;


        }

//       Room room3 = new Room("Three", "Regular Double");

       Booking booking = new Booking(arriveDate,
               departDate, guest);

       roomInventory.checkRoomAvailability(booking, roomType);
       booking.showBookingDetails(booking);
//       if(bookingRegister.checkForBookingOverlap(booking, room))
//           System.out.println("Booking Unavailable");
//       else {
//           System.out.println("Booking Available");
//           bookingRegister.addBooking(booking);
//       }
   }

}
