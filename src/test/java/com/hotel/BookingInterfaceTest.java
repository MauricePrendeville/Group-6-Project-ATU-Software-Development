package com.hotel;

import com.hotel.Model.Room;
import com.hotel.Model.RoomType;
import com.hotel.Service.BookingInterface;
import com.hotel.Service.RoomInventoryImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Scanner;

import static com.hotel.Service.BookingInterface.parseDate;
import static org.junit.jupiter.api.Assertions.*;

class BookingInterfaceTest {

    private int year;
    private int month;
    private int day;
    private LocalDate parseDate;
    private int year2;
    private int month2;
    private int day2;
    private final InputStream originalSystemIn = System.in;
    private BookingInterface bookingInterface;
    private LocalDate result;
    private LocalDate result2;


    @BeforeEach
    void setUp() {

        LocalDate parseDate = LocalDate.of(2025, 11,22);
        int year = 2025;
        int month = 11;
        int day = 23;

        int year2 = 2025;
        int month2 = 25;
        int day2 = 42;
        BookingInterface bookingInterface = new BookingInterface();

    }

    @AfterEach
    void tearDown() {
        System.setIn(originalSystemIn);
    }
//
//    @AfterEach
//    public void restoreSystemIn() {
//        System.setIn(originalSystemIn);
//    }

    @Test
    void testParseDate() {

        year = 2025;
        month = 11;
        day = 23;

        month2 = 28;  //invalid dates should produce null result from parseDate
        day2 = 45;
           //BookingInterface bookingInterface3 = new BookingInterface();
        result = parseDate(year,month,day);
        assertEquals(LocalDate.of(2025,11,23), result);

        result2 = parseDate(year,month2,day2);
        assertEquals(null, result2);

    }

    @Test
    void testGetValidDate() {
          BookingInterface bookingInterface = new BookingInterface();
          String input ="25\n11\n2025\n";
          System.setIn(new ByteArrayInputStream(input.getBytes()));

          LocalDate result = bookingInterface.getValidDate();
          assertEquals(LocalDate.of(2025,11,25), result);

          String input2 ="28\n11\n2025\n";
          System.setIn(new ByteArrayInputStream(input2.getBytes()));

          LocalDate result2 = bookingInterface.getValidDate(LocalDate.of(2025,11,25));
          assertEquals(LocalDate.of(2025,11,28), result2);

    }



    @Test
    void testGetStayRequestDetails() {
        RoomInventoryImpl roomInventory = new RoomInventoryImpl();
        roomInventory = new RoomInventoryImpl();
        Room room1 = new Room(101, RoomType.SINGLE, true, 199);
        roomInventory.addRoom(room1);


        BookingInterface bookingInterface = new BookingInterface();
        String input2 = new StringBuilder()
                //.append("25\n11\n2025\n")
               // .append("28\n11\n2025\n")
         .append("1\nGrady\njanitor@overlookhotel.com\n123\npass\n").append("1\n").toString();
        System.setIn(new ByteArrayInputStream(input2.getBytes()));
        bookingInterface.getStayRequestDetails(roomInventory);
    }
}