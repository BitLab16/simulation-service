package site.bitlab16;

import site.bitlab16.sources.SimulatedSource;
import site.bitlab16.sources.Source1;

import java.util.Arrays;
import java.util.concurrent.LinkedBlockingDeque;

public class SimulatorDirector {

    public BasicSimulator build(SimulatorType type) {

        BasicSimulator simulator = null;

        switch (type) {
            case KAFKA: {
                var builder = new KafkaSimulatorBuilderImpl();
                builder.reset();
                builder.setSimulatorType(type);
                builder.setOutput(new LinkedBlockingDeque<>());
                builder.setSimulatedSource(new SimulatedSource[]{Source1.getInstance()});
                simulator = builder.build();
                break;
            }
            case CSV: {
                var builder = new CSVSimulatorBuilderImpl();
                builder.reset();
                builder.setSimulatorType(type);
                builder.setSimulatedSource(new SimulatedSource[]{Source1.getInstance()});
                simulator = builder.build();
                break;
            }
            case BASIC: {
                var builder = new BasicSimulatorBuilderImpl();
                builder.reset();
                builder.setSimulatorType(type);
                builder.setSimulatedSource(new SimulatedSource[]{Source1.getInstance()});
                simulator = builder.build();
                break;
            }
        }
        return simulator;
    }
}
