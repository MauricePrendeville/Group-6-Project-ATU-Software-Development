package com.hotel;

import com.hotel.Model.Booking;
import com.hotel.Model.Guest;
import com.hotel.Model.Room;
import com.hotel.Model.RoomType;
import com.hotel.Service.BookingInterface;
import com.hotel.Service.BookingRegister;
import com.hotel.Service.RoomInventoryImpl;

import java.time.LocalDate;


/**
 * Hello world!
 *
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to the Overlook Hotel System");


        Guest guest1 = new Guest("Jack");
        Guest guest2 = new Guest("Danny");
        Room room1 = new Room(1, RoomType.SINGLE, true, 230 );
        Room room2 = new Room(2, RoomType.DOUBLE, true, 280 );
        Room room3 = new Room(3,RoomType.SINGLE, true, 230 );
        Room room4 = new Room(4,RoomType.SINGLE, true, 230 );
        Room room5 = new Room(5,RoomType.SINGLE, true, 230 );



       // BookingRegister bookingRegister1 = new BookingRegister(LocalDate.of(2025, 1, 1));

      //  bookingRegister1.addRoomToRegister(room1);
       // bookingRegister1.addRoomToRegister(room2);

        Booking booking1 = new Booking(LocalDate.of(2025, 10, 27),
                LocalDate.of(2025, 10, 31));

        room1.getBookingRegister().addDatesToRegister(booking1);

        Booking booking5 = new Booking(LocalDate.of(2025, 11, 2),
                LocalDate.of(2025, 11, 8));

        room1.getBookingRegister().addDatesToRegister(booking5);
        room1.setNextRoomBookingCount();

        Booking booking6 = new Booking(LocalDate.of(2025, 11, 10),
                LocalDate.of(2025, 11, 13));

        room1.getBookingRegister().addDatesToRegister(booking5);
        room1.setNextRoomBookingCount();
       // room1.getBookingRegister().addDatesToRegister(booking1,room1);
        room1.getBookingRegister().showBookedDates(room1);


        Booking booking2 = new Booking(LocalDate.of(2025, 10, 29),
                LocalDate.of(2025, 10, 31));
        room2.getBookingRegister().addDatesToRegister(booking2);

        Booking booking3 = new Booking(LocalDate.of(2025, 10, 31),
                LocalDate.of(2025, 11, 1), guest2, room2);

        Booking booking4 = new Booking(LocalDate.of(2025, 10, 31),
                LocalDate.of(2025, 11, 1), guest2, room2);
        room1.getBookingRegister().addDatesToRegister(booking4);

                 if(room2.getBookingRegister().checkForBookingOverlap(booking2, room2))
                    System.out.println("Booking Unavailable");
                    else
                        System.out.println("Booking Available");



//        bookingRegister1.addBooking(booking1);
//        bookingRegister1.addBooking(booking2);
//        bookingRegister1.showBookings();
//        bookingRegister1.showRooms();
//        bookingRegister1.showBookedDates(room1);
//        bookingRegister1.showBookedDates(room2);

        System.out.println(room1.getRoomType());
        RoomType roomType = RoomType.SINGLE;
        RoomInventoryImpl roomInventory = new RoomInventoryImpl();
        //One RoomInventoryImpl must be set up to track all rooms and bookings
        roomInventory.addRoom(room1);
        roomInventory.addRoom(room2);
        roomInventory.addRoom(room3);
        roomInventory.addRoom(room4);
        roomInventory.addRoom(room5);


                roomInventory.checkRoomAvailability(booking3, roomType);

        BookingInterface bookingInterface = new BookingInterface();
        bookingInterface.getStayRequestDetails(roomInventory);

        roomInventory.showAllBookings();

    }
}
