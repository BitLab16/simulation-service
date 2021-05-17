package site.bitlab16;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import org.junit.Before;
import org.junit.Test;

import site.bitlab16.datasources.BasicSource;
import site.bitlab16.datasources.profiles.CityBuildingProfile;
import site.bitlab16.datasources.weekly_data.WeeklyRawData;
import site.bitlab16.model.SourceRecord;
public class KafkaSimulatorTest {

	private KafkaSimulator kafkaSimulator;

	@Before
	public void setup() {
		this.kafkaSimulator = new KafkaSimulator();
	}

	@Test
	public void writeOutputTest() {
		kafkaSimulator.setOutQueue(new LinkedBlockingDeque<>());
		BasicSource paolotti = new CityBuildingProfile(1, .2f, 1f, 1.8f, 3f, 1.1f, new WeeklyRawData());
		kafkaSimulator.setSources(new BasicSource[]{paolotti});
		//kafkaSimulator.writeOutput();
		// 420768 ==> i dati del paolotti
		//assertEquals(525888, kafkaSimulator.getOutQueue().size());
		//TODO: come testare che i dati siano anche corretti?
		// non eccede lo scopo dello unit testing?
	}

	@Test
	public void outQueueTest() {
		kafkaSimulator.setOutQueue(new LinkedBlockingDeque<>());
		BlockingDeque<SourceRecord> qeque = kafkaSimulator.getOutQueue();
		assertEquals(0, qeque.size());
	}


}
