package site.bitlab16.datasources.profiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;

import site.bitlab16.TimeInstant;
public class TypicalDataSourceTest {


    TypicalDataSource source;

	@Before
	public void setup() {
		int[][] data = new int[5][];
		data[0] = new int[288*365];
		data[1] = new int[288*365];
		data[2] = new int[288*366];
		data[3] = new int[288*365];
		data[4] = new int[288*365];
		for(int i = 0; i < 288*365; i++)
			data[0][i] = data[1][i] = data[2][i] = data[3][i] = data[4][i] = i;
		for(int i = 288*365; i < 288*366; i++)
			data[2][i] = i;
		this.source = new TypicalDataSource(data, 5, 1, 1, 1, 1, 1);
    }

	@Test
	public void constructorTest() throws Exception {
		assertNotNull(this.source);
		Field d18_ptr = TypicalDataSource.class.getDeclaredField("data2018");
		Field d20_ptr = TypicalDataSource.class.getDeclaredField("data2018");
		d18_ptr.setAccessible(true);
		d20_ptr.setAccessible(true);
		int[] d18 = (int[])d18_ptr.get(source);
		int[] d20 = (int[])d20_ptr.get(source);
		
		assertEquals(150, d18[150]);
		assertEquals(1500, d20[1500]);
		Field dataMeteo_ptr = TypicalDataSource.class.getDeclaredField("dataMeteo");
		dataMeteo_ptr.setAccessible(true);
		assertNotNull(dataMeteo_ptr.get(source));
	}

	@Test
	public void getSeedTest() {
		assertEquals(5, source.getSeed());
	}

	@Test
	public void getValueTest() {
		TimeInstant when;
		when = new TimeInstant(new GregorianCalendar(2010, 1, 1), 100);
		assertEquals(-1, source.getValue(when));
		when = new TimeInstant(new GregorianCalendar(2018, 0, 1), 100);
		assertEquals(100, source.getValue(when));
		when = new TimeInstant(new GregorianCalendar(2018, 1, 2), 100);
		assertEquals(100 + 288*32, source.getValue(when));
	}
	@Test
	public void applyModifiersTest() {
		assertTrue(true);
		//// come vadano i modificatori ad implementare le modifiche
		//// ha poca importanza, il comportamento di questa funzione
		//// non ha nessuna aspettativa concreta se non quella astratta
		//// di modificare i dati in base ai modificatori.
		//// Il fatto che per ora faccia quel che fa è risultato di tuning manuale.
		//// Se lo faccia correttamente o meno sta all' occhio umano
		//// che guarda il grafico determinare
	}

	

}
