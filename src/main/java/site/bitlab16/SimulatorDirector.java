package site.bitlab16;

import java.util.concurrent.LinkedBlockingDeque;

import site.bitlab16.sources.SimulatedSource;
import site.bitlab16.sources.points.Fiera;
import site.bitlab16.sources.points.Garibaldi;
import site.bitlab16.sources.points.Paolotti;
import site.bitlab16.sources.points.Prato;
import site.bitlab16.sources.points.Supermercato;

public class SimulatorDirector {

    public BasicSimulator build(SimulatorType type) {

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

        builder.setSimulatedSource(new SimulatedSource[] {
             new Paolotti()/*,
             new Prato(),
             new Garibaldi(),
             new Supermercato(),
             new Fiera()*/
        });
        return builder.build();
    }
}
