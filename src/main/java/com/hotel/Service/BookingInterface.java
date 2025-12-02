package com.hotel.Service;

import com.hotel.Model.Booking;
import com.hotel.Model.Guest;
import com.hotel.Model.RoomType;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

//import static jdk.jfr.internal.util.Utils.isAfter;

/**
 * Represents a BookingInterface that was used for testing early in the project.
 * It is a deprecated class where the functions have been superceded by HotelManagementUI.
 */
public class BookingInterface {

    /**
     * Constructor for BookingInterface
     */
    public BookingInterface() {
    }

    /**
     * this method turns the user input of three integers into a LocalDate date object.
     * @param year the year of the potential booking
     * @param month the month of the potential booking
     * @param day the day of the potential booking
     * @return LocalDate object which is the parsed date.
     */
    public static LocalDate parseDate(int year, int month, int day) {
        String dateString = String.format("%04d-%02d-%02d", year, month, day);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            return LocalDate.parse(dateString, formatter);
        } catch (DateTimeParseException e) {
            return null;  // Return null for invalid dates
        }
    }

    /**
     * getValidDate in this mode confirms that the date is a real valid date.
     * There should be something added to make sure it is a date today or in the future perhaps.
     * @param scanning this is the Scanner object that read the user input.
     *                 It is passed into the method in part to help with testing.
     * @return candidateDate the date that is being tested for validity
     */
//
    public LocalDate getValidDate(Scanner scanning){
        LocalDate candidateDate = null;
        //Scanner scanning = new Scanner(System.in);
        do {
            System.out.println("Day of Month: ");
            String dayString = scanning.nextLine(); //scanning technique had to be changed to allow testing. nextInt was causing problems
            int dateDay = Integer.parseInt(dayString);
            System.out.println("Month: ");
            String monthString = scanning.nextLine();
            int dateMonth = Integer.parseInt(monthString);
            System.out.println("Year: ");
            String yearString = scanning.nextLine();
            int dateYear = Integer.parseInt(yearString);
            //scanning.nextLine();
            candidateDate = parseDate(dateYear, dateMonth, dateDay);
            if (candidateDate == null) {
                System.out.println("Invalid! Try again.");
            }

        } while (candidateDate == null);
        //scanning.close();
        return candidateDate;
    }
    /**
     * getValidDate in this mode confirms that the departDate is after the arriveDate.
     * It also confirms that the date is a real valid date.
     * There should be something added to make sure it is a date today or in the future perhaps.
     * @param scanning this is the Scanner object that read the user input.
     *      *                 It is passed into the method in part to help with testing.
     * @param arriveDate the arrival date of the booking. In this mode the method does
     *                   not permit a depart date (departDate) that is on or before arriveDate.
     * @return candidateDate the date that is being tested for validity
     */
    //extra getValidDate method to check that the depart date is after the arrive date
    public LocalDate getValidDate(Scanner scanning, LocalDate arriveDate){
        LocalDate candidateDate = null;
        //Scanner scanning = new Scanner(System.in);
        do {
            System.out.println("Day of Month: ");
            String dayString = scanning.nextLine();
            int dateDay = Integer.parseInt(dayString);
            System.out.println("Month: ");
            String monthString = scanning.nextLine();
            int dateMonth = Integer.parseInt(monthString);
            System.out.println("Year: ");
            String yearString = scanning.nextLine();
            int dateYear = Integer.parseInt(yearString);
           // scanning.nextLine();
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
        //scanning.close();
        return candidateDate;
    }

    /**
     * getStayRequestDetails - This method was used to test a prototype UI to allow entry of
     * Booking by a user before the HotelManagementUI was developed. It is now a DEPRECATED class.
     * @param roomInventory - the list of all Rooms in the Hotel
     */
    public void getStayRequestDetails (RoomInventoryImpl roomInventory) {

       Scanner scanning = new Scanner(System.in);
       //**********************************************Temporarily removing the get date methods to figure out how to get
        //**************************************************** test class to run ************************************************
       System.out.println("Enter Date of Arrival: ");
       LocalDate arriveDate = getValidDate(scanning);
      //  LocalDate arriveDate = LocalDate.of(2025,12,01);
       System.out.println("Enter Date of Departure: ");
       LocalDate departDate = getValidDate(scanning, arriveDate);


        System.out.println("Enter User ID: ");
        String guestID = scanning.nextLine();
        System.out.println("Enter Name: ");
        String guestName = scanning.nextLine();
        System.out.println("Enter Email: ");
        String guestEmail = scanning.nextLine();
        System.out.println("Enter Phone: ");
        String guestPhone = scanning.nextLine();
        System.out.println("Enter Password: ");
        String guestPassword = scanning.nextLine();
       Guest guest = new Guest(guestID, guestName, guestEmail, guestPhone, guestPassword);
       RoomType roomType = RoomType.SINGLE;

       System.out.println("Select Room Type: Single (press 1), Double (press 2), Deluxe (press 3)");
       //int roomTypeNumber =0;
       int roomTypeNumber= scanning.nextInt();
       scanning.nextLine();
       switch (roomTypeNumber){
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

       Booking booking = new Booking(arriveDate,
               departDate, guest);

       roomInventory.checkRoomAvailability(booking, roomType);
       booking.showBookingDetails(booking);

        scanning.close();
   }

}
