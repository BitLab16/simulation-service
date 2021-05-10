package site.bitlab16.datasources.concentration_factors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.Before;
import org.junit.Test;
public class ConcentrationModifierTest {


    ConcentrationModifier cm;

	@Before
	public void setup() {
        this.cm = new ConcentrationModifier("data/festivita.csv");
	}

	@Test
	public void constructorTest() {
        assertNotNull(cm);
        assertEquals(0, cm.get("2019-12-24", 100));
        assertEquals(1, cm.get("2019-12-25", 100));
	}

	@Test
	public void addTest() {
		assertEquals(0, this.cm.get("2099-11-11", 150));
		this.cm.add("2099-11-11", 100, 200, 42);
		assertEquals(42, this.cm.get("2099-11-11", 150));
	}

}
