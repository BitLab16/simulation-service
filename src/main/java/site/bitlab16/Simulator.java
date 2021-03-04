package site.bitlab16;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.concurrent.BlockingDeque;

import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;

import site.bitlab16.model.SourceRecord;
import site.bitlab16.sources.SimulatedSource;
import site.bitlab16.sources.Source1;

public class Simulator implements Runnable {

    BlockingDeque<SourceRecord> outQueue;
    
    private SimulatedSource sources[];

    private ArrayList<TimeSeries> series;

    public Simulator(BlockingDeque<SourceRecord> outQueue) {

        this.outQueue = outQueue;

        sources = new SimulatedSource[] { new Source1() };
        series = new ArrayList<>();
    }
    
    @Override
    public void run() {

        /// INIT VARS
        TimeInstant when = new TimeInstant(new GregorianCalendar(2019, Calendar.JANUARY, 1), 0);
        TimeInstant end = new TimeInstant(new GregorianCalendar(2019, Calendar.JANUARY, 2), 0);
        if (App.BUILD_MODE == App.ApplicationScope.DEBUG) {
            for (int i = 0; i < sources.length; i++) {
                series.add( new TimeSeries("Series" + (i+1) + "_1", Minute.class) );
                series.add( new TimeSeries("Series" + (i+1) + "_2", Minute.class) );
            }
        }
        Random random = new Random();
        
        while ( ! when.equals(end) ) {

            for (int i = 0; i < sources.length; i++) {
                
                if (sources[i].shouldPublish(when)) {

                    int num = sources[i].getValue(when);
                    int offset = (int)Math.round(random.nextGaussian()*3*((num+5)/65.));
                    
                    outQueue.add(new SourceRecord(1L, when, num+offset));
                    
                }

                when.advance();
            }
        }
        
      }  


}
