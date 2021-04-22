package site.bitlab16;

import java.util.Calendar;
import java.util.concurrent.LinkedBlockingDeque;

import site.bitlab16.datasources.BasicSource;
import site.bitlab16.datasources.profiles.CityBuildingProfile;
import site.bitlab16.datasources.profiles.IndoorProfile;
import site.bitlab16.datasources.profiles.OutdoorProfile;

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
                return null;

        }
        builder.reset();
        builder.setSimulatorType(type);
        builder.setSimulatedSource(getSources());
        return builder.build();
    }

    private BasicSource[] getSources() {
        final int[] weekdays = {Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY};
        final int[] saturdays = {Calendar.SATURDAY, Calendar.SATURDAY};
        BasicSource paolotti = new CityBuildingProfile(1, .2f, 1f, 1.8f, 3f, 1.1f);
        BasicSource pratodellavalle = new OutdoorProfile(2, 1.2f, 3f, 1.5f, 1f, 3.5f);
        BasicSource piazzagaribaldi = new OutdoorProfile(3, .8f, 1.5f, 2f, 2f, 1f);
        BasicSource supermercato = new IndoorProfile(4, .012f, .5f, 2.f, 1f, .3f, weekdays );
        BasicSource fiera = new IndoorProfile(5, .12f, .5f, 1.5f, 1.2f, 1.f, saturdays );

        return new BasicSource[] {paolotti, pratodellavalle, piazzagaribaldi, supermercato, fiera};
    }
}
