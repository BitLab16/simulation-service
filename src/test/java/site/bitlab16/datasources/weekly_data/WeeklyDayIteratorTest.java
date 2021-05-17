package site.bitlab16.datasources.weekly_data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.lang.reflect.Method;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
public class WeeklyDayIteratorTest {

	WeeklyRawData wrd;
	WeekDayIterator wdi;
    Random random;


	@Before
	public void setup() {
        this.wrd = new WeeklyRawData();
        this.random = new Random(1);
		this.wdi = new WeekDayIterator(wrd, random);
	}

	@Test
	public void constructorTest() {
        assertEquals(288*7, wdi.week.length);
	}
	
    @Test
	public void resetTest() throws Exception {
        Method reset_fptr = WeekDayIterator.class.getDeclaredMethod("reset");
        reset_fptr.setAccessible(true);
        int week_old[] = wdi.week;
        assertEquals(week_old, wdi.week);
        reset_fptr.invoke(wdi);
        assertNotEquals(week_old, wdi.week);
	}

    @Test
    public void getAndAdvanceTest() {
        
        assertEquals(0, wdi.instant);
        int data1 = wdi.getAndAdvance();
        assertEquals(wdi.week[0], data1);
        
        assertEquals(1, wdi.instant);
        int data2 = wdi.getAndAdvance();
        assertEquals(wdi.week[1], data2);
    }

    @Test
    public void getAndAdvanceTest2() {
    assertEquals(0, wdi.instant);
        for (int i = wdi.instant; i < wdi.week.length-1; i++)
        wdi.getAndAdvance();
    assertEquals(288*7-1, wdi.instant);
    wdi.getAndAdvance();
    assertEquals(0, wdi.instant);
    }
}
