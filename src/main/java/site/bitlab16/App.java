package site.bitlab16;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import site.bitlab16.kafka_producer.Consumer;
import site.bitlab16.model.SourceRecord;

public class App {

    enum ApplicationScope {
        DEBUG, // Con grafici, assume server grafico
        RELEASE // deployabile in un docker, no X11
    }
    
    final public static ApplicationScope BUILD_MODE = ApplicationScope.RELEASE;

    
    public static void main( String[] args ) {
        
        
        switch (BUILD_MODE) {
        
            case RELEASE:
                BlockingDeque<SourceRecord> outQueue = new LinkedBlockingDeque<>();
                String kafkaBootstrapServers = "127.0.0.1:9091";
                Simulator simulator = new Simulator(outQueue);
                Consumer consumer = new Consumer("Simulatore 1", kafkaBootstrapServers, outQueue);
                ExecutorService executor = Executors.newCachedThreadPool();
                executor.execute(simulator);
                executor.execute(consumer);
                break;
            
            case DEBUG:
                SimulatorWithGraphs simulator_window = new SimulatorWithGraphs("---nome finestra---");
                SwingUtilities.invokeLater(() -> {
                    simulator_window.setSize(800, 400);
                    simulator_window.setLocationRelativeTo(null);
                    simulator_window.setVisible(true);
                    simulator_window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                    simulator_window.run();
                });
                break;                
        }
    }
}
