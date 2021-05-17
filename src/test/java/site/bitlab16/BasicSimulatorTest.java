package site.bitlab16;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Before;
import org.junit.Test;
public class BasicSimulatorTest {

	private BasicSimulator basicSimulator;

	@Before
	public void setup() {
		this.basicSimulator = new BasicSimulator();
	}

	@Test
	public void simulatorType() {
		this.basicSimulator.setSimulatorType(SimulatorType.BASIC);
		assertSame(SimulatorType.BASIC, basicSimulator.getSimulatorType());
	}
}
