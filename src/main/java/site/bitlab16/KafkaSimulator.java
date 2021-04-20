package site.bitlab16;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.BlockingDeque;

import site.bitlab16.model.SourceRecord;
import site.bitlab16.sources.SimulatedSource;
import site.bitlab16.sources.Source1;

public class KafkaSimulator extends BasicSimulator {

    BlockingDeque<SourceRecord> outQueue;

    public KafkaSimulator(){}

    public KafkaSimulator(BlockingDeque<SourceRecord> outQueue) {

        this.outQueue = outQueue;

        sources = new SimulatedSource[] { new Source1() };
    }

    @Override
    public void writeOutput() {

        TimeInstant when = new TimeInstant(new GregorianCalendar(2019, Calendar.JANUARY, 1), 0);
        TimeInstant end = new TimeInstant(new GregorianCalendar(2023, Calendar.JANUARY, 1), 0);
        final int[] seasons = new int[]{0,0,0,1,1,1,2,2,2,3,3,3};

        while ( ! when.equals(end) ) {
            for (int i = 0; i < sources.length; i++) {
                float dataMeteo = sources[i].getModifierMeteo(when);
                int meteoAsEnum;
                if (dataMeteo < 0.05) meteoAsEnum = 0;
                else if(dataMeteo < 0.13) meteoAsEnum = 1;
                else if(dataMeteo < 0.2) meteoAsEnum = 2;
                else if(dataMeteo < 0.3) meteoAsEnum = 3;
                else if(dataMeteo < 0.5) meteoAsEnum = 4;
                else meteoAsEnum = 5;
                int flow = sources[i].getValue(when);
                if (flow != -1) {
                    outQueue.add(new SourceRecord(1L,
                            flow,
                            new Timestamp(when.getTimeInMillisecond()),
                            meteoAsEnum,
                            seasons[when.getDay().get(Calendar.MONTH)],
                            sources[i].getFestivita(when)==0 ? false : true,
                            3F,
                            3F,
                            4F,
                            2F));
                }
                when.advance();
            }
        }
    }

    public BlockingDeque<SourceRecord> getOutQueue() {
        return outQueue;
    }

    public void setOutQueue(BlockingDeque<SourceRecord> outQueue) {
        this.outQueue = outQueue;
    }

}
