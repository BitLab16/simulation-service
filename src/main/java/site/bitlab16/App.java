package site.bitlab16;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import site.bitlab16.kafka_producer.Consumer;

public class App {

    enum ApplicationScope {
        DEBUG, // Con grafici, assume server grafico
        RELEASE, // deployabile in un docker, no X11
        CSV
    }
    
    final public static ApplicationScope BUILD_MODE = ApplicationScope.RELEASE;

    
    public static void main( String[] args ) {
        var director = new SimulatorDirector();
        Simulator simulator = null;
        switch (BUILD_MODE) {

            case CSV:
                simulator = director.build(SimulatorType.CSV);
                simulator.writeOutput();
                break;

            case RELEASE:
                simulator = director.build(SimulatorType.KAFKA);
                String kafkaBootstrapServers = "localhost:9092";
                //String kafkaBootstrapServers = "kafka1:19091";
                Consumer consumer = new Consumer("Simulatore 1", kafkaBootstrapServers, ((KafkaSimulator) simulator).getOutQueue());
                ExecutorService executor = Executors.newCachedThreadPool();
                executor.execute(consumer);
                simulator.writeOutput();
                break;
            
            case DEBUG:
                simulator = director.build(SimulatorType.BASIC);
                Graphic graphic = new Graphic(simulator, "Dati simulati");
                SwingUtilities.invokeLater(() -> {
                    graphic.setSize(800, 400);
                    graphic.setLocationRelativeTo(null);
                    graphic.setVisible(true);
                    graphic.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                    graphic.run();
                });
                break;                
        }
    }
}
