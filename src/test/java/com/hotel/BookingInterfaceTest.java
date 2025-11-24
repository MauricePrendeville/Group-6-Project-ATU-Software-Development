package com.hotel;

import com.hotel.Service.BookingInterface;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDate;

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

//    @AfterEach
//    void tearDown() {
//    }
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

//        String simulatedInputDateDay = "23\n";
//        ByteArrayInputStream testInputDay = new ByteArrayInputStream(simulatedInputDateDay.getBytes());
//        System.setIn(testInputDay);
//        String simulatedInputDateMonth = "11\n";
//        ByteArrayInputStream testInputMonth = new ByteArrayInputStream(simulatedInputDateMonth.getBytes());
//        System.setIn(testInputMonth);
//        String simulatedInputDateYear = "2025\n";
//        ByteArrayInputStream testInputYear = new ByteArrayInputStream(simulatedInputDateYear.getBytes());
//        System.setIn(testInputYear);
//
//        BookingInterface bookingInterface = new BookingInterface();
//        LocalDate result = bookingInterface.getValidDate();
//        assertEquals(LocalDate.of(2025,11,23), result);

//        String simulatedInputDateDay2 = "42\n";
//        ByteArrayInputStream testInputDay2 = new ByteArrayInputStream(simulatedInputDateDay2.getBytes());
//        System.setIn(testInputDay2);
//        String simulatedInputDateMonth2 = "25\n";
//        ByteArrayInputStream testInputMonth2 = new ByteArrayInputStream(simulatedInputDateMonth2.getBytes());
//        System.setIn(testInputMonth2);
//        String simulatedInputDateYear2 = "2025\n";
//        ByteArrayInputStream testInputYear2 = new ByteArrayInputStream(simulatedInputDateYear2.getBytes());
//        System.setIn(testInputYear2);
//
//        BookingInterface bookingInterface2 = new BookingInterface();
//        LocalDate result2 = bookingInterface2.getValidDate();
//        assertNull(result2);
    }



    @Test
    void getStayRequestDetails() {
    }
}