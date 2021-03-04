package site.bitlab16;

import site.bitlab16.kafka_producer.Consumer;
import site.bitlab16.model.SourceRecord;

import javax.swing.*;
import java.util.concurrent.*;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        /*
        SimulatedSource s1 = new Source1();
        TimeInstant when = new TimeInstant(new GregorianCalendar(2020, Calendar.FEBRUARY, 26), 0);
        TimeInstant end = new TimeInstant(new GregorianCalendar(2020, Calendar.MARCH, 1), 1);

        while ( ! when.equals(end) ) {


            System.out.println(when + " " + s1.getValue(when));
            when.advance();
        }

*/
        BlockingDeque<SourceRecord> outQueue = new LinkedBlockingDeque<>();

        ExecutorService executor = Executors.newCachedThreadPool();

        String kafkaBootstrapServers = "127.0.0.1:9091";

        SwingUtilities.invokeLater(() -> {
            Simulator example = new Simulator("---nome finestra---", outQueue);
            example.setSize(800, 400);
            example.setLocationRelativeTo(null);
            example.setVisible(true);
            example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        });
        Consumer consumer = new Consumer("Simulatore 1", kafkaBootstrapServers, outQueue);
        executor.execute(consumer);
    }
}
