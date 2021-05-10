package site.bitlab16.datasources.profiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.Method;
import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;

import site.bitlab16.datasources.weeklyData.WeeklyRawData;
public class IndoorProfileTest {


    IndoorProfile source;

	@Before
	public void setup() {
		WeeklyRawData wrd = new WeeklyRawData();
		this.source = new IndoorProfile(5, 1, 1, 1, 1, 1, new int[]{Calendar.SATURDAY}, wrd);
    }

	@Test
	public void constructorTest() throws Exception {
		assertNotNull(this.source);
	}

	@Test
	public void festeEditValueTest() {
		int res = source.festeEditValue("2018-12-25", 10, 100, 1000);
		assertEquals(14, res);
	}
	@Test
	public void seasonEditValueTest() {
		int res = source.seasonEditValue(10, 100, 1000);
		assertEquals(80, res);
	}
	@Test
	public void eventiEditValueTest() {
		int res = source.eventiEditValue(10, 100);
		assertEquals(1010, res);
	}
	@Test
	public void attivitaEditValueTest() {
		int res = source.attivitaEditValue(10, 100);
		assertEquals(1010, res);
	}

}
