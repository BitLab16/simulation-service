package site.bitlab16;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.GregorianCalendar;

import org.jfree.data.time.Minute;
import org.junit.Test;

public class TimeInstantTest {

    @Test
    public void whenInstantIsInvalid_constructorTestFails() {
        var calendar = new GregorianCalendar(2020,1,1);
        assertThrows(
            IllegalArgumentException.class,
            () -> { new TimeInstant(calendar, -1); },
            "Uncaught invalid instant 1"
        );
    }

    @Test
    public void whenInstantIsOutOfBound_constructorTestFails() {
        var calendar = new GregorianCalendar(2020,1,1);
        assertThrows(
                IllegalArgumentException.class,
                () -> { new TimeInstant(calendar, 40000); },
                "Uncaught invalid instant 2"
        );
    }
    
    @Test
    public void advanceTest() {
        TimeInstant instant = new TimeInstant(new GregorianCalendar(2020,2,28), 286);
        instant.advance();
        assertEquals("fail 1: instant advance same day",  287, instant.getInstant());
        instant.advance();
        assertEquals("fail 2: instant reset at 287+1",  0, instant.getInstant());
        assertEquals("fail 3: leap year", 0, instant.getDay().compareTo(new GregorianCalendar(2020,2,29)) );
    }

    @Test
    public void getInstantTest() {
        final int val = 61;
        TimeInstant instant = new TimeInstant(new GregorianCalendar(2020,1,1), val);
        assertEquals("instant getter fails", instant.getInstant(), val);
    }

    @Test
    public void getDayTest() {
        GregorianCalendar cal = new GregorianCalendar(2020,1,1);
        TimeInstant instant = new TimeInstant(cal, 1);
        assertEquals("day getter fails", 0, instant.getDay().compareTo(cal));
    }

    @Test
    public void equalsTest() {
        TimeInstant instant = new TimeInstant(new GregorianCalendar(2020,1,1), 34);
        TimeInstant instant2 = new TimeInstant(new GregorianCalendar(2020,1,1), 34);
        TimeInstant instant3 = new TimeInstant(new GregorianCalendar(2020,1,1), 35);
        TimeInstant instant4 = new TimeInstant(new GregorianCalendar(2020,1,2), 34);
        assertEquals("equality check fails", instant, instant2);
        assertNotEquals("inequality check on instant fails", instant, instant3);
        assertNotEquals("inequality check on date fails", instant, instant4);
    }

    @Test
    public void toStringTest() {
        TimeInstant instant = new TimeInstant(new GregorianCalendar(2020,5,15), 127);
        assertEquals("Wrong Stringification",  "2020-06-15 10:35", instant.toString());
    }

    @Test
    public void toStringAsInstantTest() {
        TimeInstant instant = new TimeInstant(new GregorianCalendar(2020,5,15), 127);
        assertEquals("Wrong Stringification as instant", "2020-06-15/127", instant.toStringAsInstant());
    }

    @Test
    public void getTimeInMillisTest() {
        TimeInstant instant = new TimeInstant(new GregorianCalendar(2020,5,15), 127);
        assertTrue("Wrong millis since 1970 conversion",
        instant.getTimeInMillis() > 1592110100000L && instant.getTimeInMillis() < 1592310100000L);
    }

    @Test
    public void getMinuteTest() {
        TimeInstant instant = new TimeInstant(new GregorianCalendar(2020,5,15), 127);
        Minute minute = new Minute(35,10,15,5,2020);
        assertEquals(1, minute.compareTo(instant.getInstant()));
    }
}
