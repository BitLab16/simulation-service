package site.bitlab16.datasources.weeklyData;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Calendar;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
public class SpecificWeeklyDayIteratorTest {

	WeeklyRawData wrd;
	SpecificWeekDayIterator swdi;
    Random random;


	@Before
	public void setup() {
        this.wrd = new WeeklyRawData();
        this.random = new Random(1);
		this.swdi = new SpecificWeekDayIterator(wrd, random);
	}

	@Test
	public void constructorTest() {
        assertNotNull(swdi);
	}
	
    @Test
    public void getDayOfWeekTest() {
        int[] day1 = swdi.getDayOfWeek(Calendar.SATURDAY);
        int[] day2 = swdi.getDayOfWeek(Calendar.SUNDAY);
        int[] day3 = swdi.getDayOfWeek(Calendar.SUNDAY);
        assertEquals(288, day1.length);
        assertEquals(288, day2.length);
        assertEquals(288, day3.length);
        assertNotEquals(day1, day2);
        assertNotEquals(day2, day3);
    }

}
