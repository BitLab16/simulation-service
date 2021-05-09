package site.bitlab16;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;


public class BasicSimulatorBuilderImplTest {

	BasicSimulatorBuilderImpl builder;
	BasicSimulator simulator;

	@Before
	public void setup() {
		this.builder = new BasicSimulatorBuilderImpl();
		this.builder.reset();
		try {
			Field field = BasicSimulatorBuilderImpl.class.getDeclaredField("simulator");
			field.setAccessible(true);
			simulator = (BasicSimulator)field.get(this.builder);
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
		assertTrue(simulator instanceof BasicSimulator);
	}

	@Test
	public void buildTest() {
		BasicSimulator simulator = builder.build();
		assertEquals(this.simulator, simulator);
	}
}
