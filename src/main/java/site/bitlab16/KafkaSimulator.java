package site.bitlab16;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.BlockingDeque;

import site.bitlab16.datasources.BasicSource;
import site.bitlab16.model.SourceRecord;

public class KafkaSimulator implements Simulator {

    BasicSimulator simulator;

    private BlockingDeque<SourceRecord> outQueue;

    KafkaSimulator() {
        simulator = new BasicSimulator();
    }

    @Override
    public void writeOutput() {

        TimeInstant when = new TimeInstant(new GregorianCalendar(2018, Calendar.JANUARY, 1), 0);
        TimeInstant end = new TimeInstant(new GregorianCalendar(2023, Calendar.JANUARY, 1), 0);
        final int[] seasons = new int[]{0,0,0,1,1,1,2,2,2,3,3,3};

        while ( ! when.equals(end) ) {
            for (int i = 0; i < simulator.getSources().length; i++) {
                int flow = simulator.getSources()[i].getValue(when);
                if (flow != -1) {
                    outQueue.add(new SourceRecord(
                            1L*simulator.getSources()[i].getSeed(),
                            flow,
                            new Timestamp(when.getTimeInMillis()),
                            simulator.getSources()[i].getModifierMeteoAsEnum(when),
                            seasons[when.getDay().get(Calendar.MONTH)],
                            simulator.getSources()[i].getFestivita(when)==0 ? false : true,//holiday
                            simulator.getSources()[i].getIndiceOrario(),
                            simulator.getSources()[i].getIndiceMeteo(),
                            simulator.getSources()[i].getIndiceStagione(),
                            simulator.getSources()[i].getIndiceAttivita()));
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
