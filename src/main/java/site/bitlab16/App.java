package site.bitlab16;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import site.bitlab16.kafka_producer.Consumer;
import site.bitlab16.model.SourceRecord;
import site.bitlab16.sources.WeeklyRawData;

public class App {

    enum ApplicationScope {
        DEBUG, // Con grafici, assume server grafico
        RELEASE, // deployabile in un docker, no X11
        TMP
    }
    
    final public static ApplicationScope BUILD_MODE = ApplicationScope.DEBUG;

    
    public static void main( String[] args ) {
        
        switch (BUILD_MODE) {

            case TMP:
                break;

            case RELEASE:
                BlockingDeque<SourceRecord> outQueue = new LinkedBlockingDeque<>();
                String kafkaBootstrapServers = "kafka1:19091";
                Simulator simulator = new Simulator(outQueue);
                Consumer consumer = new Consumer("Simulatore 1", kafkaBootstrapServers, outQueue);
                ExecutorService executor = Executors.newCachedThreadPool();
                executor.execute(simulator);
                executor.execute(consumer);
                break;
            
            case DEBUG:
                SimulatorWithGraphs simulatorWindow = new SimulatorWithGraphs("---nome finestra---");
                SwingUtilities.invokeLater(() -> {
                    simulatorWindow.setSize(800, 400);
                    simulatorWindow.setLocationRelativeTo(null);
                    simulatorWindow.setVisible(true);
                    simulatorWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                    simulatorWindow.run();
                });
                break;                
        }
    }
}
