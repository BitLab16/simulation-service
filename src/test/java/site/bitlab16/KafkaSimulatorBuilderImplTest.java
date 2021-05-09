package site.bitlab16;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;


public class KafkaSimulatorBuilderImplTest {

	KafkaSimulatorBuilderImpl builder;
	KafkaSimulator simulator;

	@Before
	public void setup() {
		this.builder = new KafkaSimulatorBuilderImpl();
		this.builder.reset();
		try {
			Field field = KafkaSimulatorBuilderImpl.class.getDeclaredField("kafkaSimulator");
			field.setAccessible(true);
			simulator = (KafkaSimulator)field.get(this.builder);
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
		assertTrue(simulator instanceof KafkaSimulator);
	}

	@Test
	public void buildTest() {
		KafkaSimulator simulator = builder.build();
		assertEquals(this.simulator, simulator);
	}
}
