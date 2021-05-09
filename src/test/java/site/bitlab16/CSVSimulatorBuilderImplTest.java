package site.bitlab16;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;


public class CSVSimulatorBuilderImplTest {

	CSVSimulatorBuilderImpl builder;
	CSVSimulator simulator;

	@Before
	public void setup() {
		this.builder = new CSVSimulatorBuilderImpl();
		this.builder.reset();
		try {
			Field field = CSVSimulatorBuilderImpl.class.getDeclaredField("simulator");
			field.setAccessible(true);
			simulator = (CSVSimulator)field.get(this.builder);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void constructorTest() {
		assertNotNull(this.builder);
	}

	@Test
	public void resetTest() {
		assertNotNull(simulator);
		assertTrue(simulator instanceof CSVSimulator);
	}

	@Test
	public void buildTest() {
		CSVSimulator simulator = builder.build();
		assertEquals(this.simulator, simulator);
	}
}
