package site.bitlab16;

import java.util.Calendar;
import java.util.concurrent.LinkedBlockingDeque;

import site.bitlab16.datasources.BasicSource;
import site.bitlab16.datasources.profiles.CityBuildingProfile;
import site.bitlab16.datasources.profiles.IndoorProfile;
import site.bitlab16.datasources.profiles.OutdoorProfile;
import site.bitlab16.datasources.weekly_data.WeeklyRawData;

public class SimulatorDirector {

    public Simulator build(SimulatorType type) {

        SimulatorBuilder builder;

        switch (type) {
            case KAFKA:
                builder = new KafkaSimulatorBuilderImpl();
                builder.reset();
                builder.setOutput(new LinkedBlockingDeque<>());
                break;
            case CSV:
                builder = new CSVSimulatorBuilderImpl();
                builder.reset();
                break;
            default:
                builder = new BasicSimulatorBuilderImpl();
                builder.reset();
                break;
        }
        builder.setSimulatorType(type);
        builder.setSimulatedSource(getSources());
        return builder.build();
    }

    private BasicSource[] getSources() {
        var wrd = new WeeklyRawData();
        final var weekdays = new int[]{Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY};
        final var saturdays = new int[]{Calendar.SATURDAY, Calendar.SATURDAY};
        BasicSource paolotti = new CityBuildingProfile(1, .2f, 1f, 1.8f, 3f, 1.1f, wrd);
        BasicSource pratodellavalle = new OutdoorProfile(2, 1.2f, 3f, 1.5f, 1f, 3.5f, wrd);
        BasicSource piazzagaribaldi = new OutdoorProfile(3, .8f, 1.5f, 2f, 2f, 1f, wrd);
        BasicSource supermercato = new IndoorProfile(4, .012f, .5f, 2.f, 1f, .3f, weekdays, wrd);
        BasicSource fiera = new IndoorProfile(5, .12f, .5f, 1.5f, 1.2f, 1.f, saturdays, wrd);

        return new BasicSource[] {paolotti, pratodellavalle, piazzagaribaldi, supermercato, fiera};
    }
}
