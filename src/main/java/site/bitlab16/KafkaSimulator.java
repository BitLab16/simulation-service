package site.bitlab16;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.BlockingDeque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.bitlab16.datasources.BasicSource;
import site.bitlab16.model.SourceRecord;

public class KafkaSimulator implements Simulator {

    BasicSimulator simulator;

    private BlockingDeque<SourceRecord> outQueue;

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaSimulator.class);

    KafkaSimulator() {
        simulator = new BasicSimulator();
    }

    @Override
    public void writeOutput() {

        var when = new TimeInstant(new GregorianCalendar(2018, Calendar.JANUARY, 1), 0);
        var end = new TimeInstant(new GregorianCalendar(2023, Calendar.JANUARY, 1), 0);
        final var seasons = new int[]{0,0,0,1,1,1,2,2,2,3,3,3};

        while ( ! when.equals(end) ) {
            for (var i = 0; i < simulator.getSources().length; i++) {
                long millisecond = System.currentTimeMillis();
                var timestamp = new Timestamp(millisecond);
                int flow = simulator.getSources()[i].getValue(when);
                if (flow != -1) {
                    var sourceRecord = new SourceRecord(
                            (long) simulator.getSources()[i].getSeed(),
                            flow,
                            new Timestamp(when.getTimeInMillis()),
                            simulator.getSources()[i].getModifierMeteoAsEnum(when),
                            seasons[when.getDay().get(Calendar.MONTH)],
                            simulator.getSources()[i].getFestivita(when) != 0,//holiday
                            simulator.getSources()[i].getIndiceOrario(),
                            simulator.getSources()[i].getIndiceMeteo(),
                            simulator.getSources()[i].getIndiceStagione(),
                            simulator.getSources()[i].getIndiceAttivita());
                    try {
                        if(sourceRecord.getDetectionTime().after(timestamp)) {
                            LOGGER.debug("last detection time: {}", sourceRecord.getDetectionTime().toLocalDateTime());
                            LOGGER.debug("Waiting for new data, thread sleep");
                            Thread.sleep(300000);
                        }
                        outQueue.add(sourceRecord);
                    } catch (InterruptedException ex) {
                        LOGGER.error(ex.getMessage());
                        Thread.currentThread().interrupt();
                    }
                }
            }
            when.advance();
        }
    }

    public BlockingDeque<SourceRecord> getOutQueue() {
        return outQueue;
    }

    public void setOutQueue(BlockingDeque<SourceRecord> outQueue) {
        this.outQueue = outQueue;
    }

	@Override
	public SimulatorType getSimulatorType() {
        return simulator.getSimulatorType();
	}

	@Override
	public void setSimulatorType(SimulatorType type) {
		simulator.setSimulatorType(type);
	}

	@Override
	public BasicSource[] getSources() {
        return simulator.getSources();
	}

	@Override
	public void setSources(BasicSource[] sources) {
        simulator.setSources(sources);
	}

}
