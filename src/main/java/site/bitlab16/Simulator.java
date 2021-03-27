package site.bitlab16;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.BlockingDeque;

import site.bitlab16.model.SourceRecord;
import site.bitlab16.sources.SimulatedSource;
import site.bitlab16.sources.Source1;

public class Simulator implements Runnable {

    BlockingDeque<SourceRecord> outQueue;
    
    private SimulatedSource sources[];

    public Simulator(BlockingDeque<SourceRecord> outQueue) {

        this.outQueue = outQueue;

        sources = new SimulatedSource[] { new Source1() };
    }
    
    @Override
    public void run() {

        TimeInstant when = new TimeInstant(new GregorianCalendar(2019, Calendar.JANUARY, 1), 0);

        for (int i = 0; i < sources.length; i++) {
            int season = 0;
            for (int j = 0; j<10000; j++) {
                season = j/2500;
                int num = sources[i].getValue(when);
                if (num != -1) {
                    outQueue.add(new SourceRecord(1L, when, num, season, false, 0.0F, 0.0F, 0.0F, 0.0F));
                }
                when.advance();
            }
        }
        
      } 


}
