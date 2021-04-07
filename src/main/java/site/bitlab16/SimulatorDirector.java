package site.bitlab16;

import java.util.concurrent.LinkedBlockingDeque;

import site.bitlab16.sources.SimulatedSource;
import site.bitlab16.sources.Source2;

public class SimulatorDirector {

    public BasicSimulator build(SimulatorType type) {

        BasicSimulator simulator = null;
        
        SimulatorBuilder builder;

        switch (type) {
            case KAFKA: /*TODO : SCOPPIA??? CONTROLLARE IL CAST!*/
                builder = new KafkaSimulatorBuilderImpl();
                ((KafkaSimulatorBuilderImpl)builder).setOutput(new LinkedBlockingDeque<>());
                break;
            case CSV:
                builder = new CSVSimulatorBuilderImpl();
                break;
            case BASIC:
                builder = new BasicSimulatorBuilderImpl();
                break;
            default:
                builder = new BasicSimulatorBuilderImpl();
                break;

        }
        builder.reset();
        builder.setSimulatorType(type);
        builder.setSimulatedSource(new SimulatedSource[]{Source2.getInstance()});
        return builder.build();
    }
}
