package site.bitlab16;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.BlockingDeque;

import site.bitlab16.model.SourceRecord;

public class KafkaSimulator extends BasicSimulator {

    BlockingDeque<SourceRecord> outQueue;

    @Override
    public void writeOutput() {

        TimeInstant when = new TimeInstant(new GregorianCalendar(2019, Calendar.JANUARY, 1), 0);
        TimeInstant end = new TimeInstant(new GregorianCalendar(2023, Calendar.JANUARY, 1), 0);
        final int[] seasons = new int[]{0,0,0,1,1,1,2,2,2,3,3,3};

        while ( ! when.equals(end) ) {
            for (int i = 0; i < sources.length; i++) {
                int flow = sources[i].getValue(when);
                if (flow != -1) {
                    outQueue.add(new SourceRecord(1L,
                            when,
                            flow,
                            sources[i].getModifierMeteoAsEnum(when),
                            seasons[when.getDay().get(Calendar.MONTH)],
                            sources[i].getFestivita(when)==0 ? false : true,//holiday
                            sources[i].getIndiceOrario(),
                            sources[i].getIndiceMeteo(),
                            sources[i].getIndiceStagione(),
                            sources[i].getIndiceAttivita()));
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
