package site.bitlab16;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Before;
import org.junit.Test;

import site.bitlab16.datasources.BasicSource;

public class SimulatorDirectorTest {

    SimulatorDirector director;

    @Before
    public void setup() {
        this.director = new SimulatorDirector();
    }

    @Test
    public void constructorTest() {
        assertNotNull(director);
    }

    @Test
    public void buildKafkaTest() {
        Simulator simulator = director.build(SimulatorType.KAFKA);
        assertTrue(simulator instanceof KafkaSimulator);
        assertTrue(SimulatorType.KAFKA == simulator.getSimulatorType());
    }

    @Test
    public void buildCSVTest() {
        Simulator simulator = director.build(SimulatorType.CSV);
        assertTrue(simulator instanceof CSVSimulator);
        assertTrue(SimulatorType.CSV == simulator.getSimulatorType());
    }

    @Test
    public void buildBasicTest() {
        Simulator simulator = director.build(SimulatorType.BASIC);
        assertTrue(simulator instanceof BasicSimulator);
        assertTrue(SimulatorType.BASIC == simulator.getSimulatorType());
    }

    @Test
    public void getSourcesTest() {
        BasicSource[] sources = director.build(SimulatorType.BASIC).getSources();
        assertTrue(sources.length > 0);
    }

}
