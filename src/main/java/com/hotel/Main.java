package com.hotel;

import java.time.LocalDate;


/**
 * Hello world!
 *
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("Hotel System");


        Guest guest1 = new Guest("Bob");
        Guest guest2 = new Guest("Boab");
        Room room1 = new Room(1,RoomType.SINGLE, true, 230 );
        Room room2 = new Room(2, RoomType.DOUBLE, true, 280 );

        BookingRegister bookingRegister1 = new BookingRegister(LocalDate.of(2025, 1, 1));

        bookingRegister1.addRoomToRegister(room1);
        bookingRegister1.addRoomToRegister(room2);

        Booking booking1 = new Booking("1", LocalDate.of(2025, 10, 27),
                LocalDate.of(2025, 10, 31), LocalDate.of(2025, 10, 20), guest1, room1);

        bookingRegister1.addDatesToRegister(booking1,room1);
        bookingRegister1.showBookedDates(room1);


        Booking booking2 = new Booking("2", LocalDate.of(2025, 10, 29),
                LocalDate.of(2025, 10, 31), LocalDate.of(2025, 10, 21), guest2, room2);

                 if(bookingRegister1.checkForBookingOverlap(booking2, room2))
                    System.out.println("Booking Unavailable");
                    else
                        System.out.println("Booking Available");


        BookingInterface bookingInterface = new BookingInterface();
        bookingInterface.getStayRequestDetails(bookingRegister1,room2);

        bookingRegister1.addBooking(booking1);
        bookingRegister1.addBooking(booking2);
        bookingRegister1.showBookings();
        bookingRegister1.showRooms();
        bookingRegister1.showBookedDates(room1);
        bookingRegister1.showBookedDates(room2);
    }
}
