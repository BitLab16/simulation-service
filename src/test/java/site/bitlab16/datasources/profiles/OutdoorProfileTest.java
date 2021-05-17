package site.bitlab16.datasources.profiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;

import site.bitlab16.datasources.weekly_data.WeeklyRawData;
public class OutdoorProfileTest {


    OutdoorProfile source;

	@Before
	public void setup() {
		WeeklyRawData wrd = new WeeklyRawData();
		this.source = new OutdoorProfile(5, 1, 1, 1, 1, 1, wrd);
    }

	@Test
	public void constructorTest() throws Exception {
		assertNotNull(this.source);
	}	

	@Test
	public void festeEditValueTest() {
		int res = source.festeEditValue("2018-12-25", 10, 100, 1000);
		assertEquals(17, res);
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

	@Test
	public void roundTheMiddleTest() throws Exception {
		Method rtm_ptr = OutdoorProfile.class.getDeclaredMethod("roundTheMiddle", int.class, int.class);
		rtm_ptr.setAccessible(true);
		int res;
		res = (int)rtm_ptr.invoke(source, 145, 0);
		assertEquals(145, res);
		res = (int)rtm_ptr.invoke(source, 145, 130);
		assertEquals(156, res);
	}

}
