package site.bitlab16;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.GregorianCalendar;

import org.junit.Test;

public class TimeInstantTest {

    TimeInstant instant;

    @Test
    public void TimeInstantConstructorTest() {
        new TimeInstant(new GregorianCalendar(2020,1,1), 286);
        assertThrows(
            IllegalArgumentException.class,
            () -> { new TimeInstant(new GregorianCalendar(2020,1,1), -1); },
            "Uncaught invalid instant 1"
        );
        assertThrows(
            IllegalArgumentException.class,
            () -> { new TimeInstant(new GregorianCalendar(2020,1,1), 40000); },
            "Uncaught invalid instant 2"
        );
    }
    
    @Test
    public void advanceTest() {
        TimeInstant instant = new TimeInstant(new GregorianCalendar(2020,2,28), 286);
        instant.advance();
        assertEquals("fail 1: instant advance same day", instant.getInstant(), 287);
        instant.advance();
        assertEquals("fail 2: instant reset at 287+1", instant.getInstant(), 0);
        assertEquals("fail 3: leap year", instant.getDay().compareTo(new GregorianCalendar(2020,2,29)), 0 );   
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
        assertEquals("day getter fails", instant.getDay().compareTo(cal), 0);
    }

    @Test
    public void equalsTest() {
        TimeInstant instant = new TimeInstant(new GregorianCalendar(2020,1,1), 34);
        TimeInstant instant2 = new TimeInstant(new GregorianCalendar(2020,1,1), 34);
        TimeInstant instant3 = new TimeInstant(new GregorianCalendar(2020,1,1), 35);
        TimeInstant instant4 = new TimeInstant(new GregorianCalendar(2020,1,2), 34);
        assertTrue("equality check fails", instant.equals(instant2));
        assertFalse("inequality check on instant fails", instant.equals(instant3));
        assertFalse("inequality check on date fails", instant.equals(instant4));
    }

    @Test
    public void toStringTest() {
        TimeInstant instant = new TimeInstant(new GregorianCalendar(2020,5,15), 127);
        assertEquals("Wrong Stringification", instant.toString(), "2020-06-15 10:35");

    }

}
