package site.bitlab16.sources;

import java.util.Calendar;

public class Source1 extends SimulatedSource {

    @Override
    protected int getSeed() { return 2; }


    // sostituisco i giorni di festa con le domeniche
    @Override
    protected int festeEditValue(String date, int val, int instant, float modifier) {

        if (modifier == 0)
            return val;
        
        int dayOfWeek;
        if (date.hashCode() % 2 == 0)
            dayOfWeek = Calendar.SATURDAY;
        else
            dayOfWeek = Calendar.SUNDAY;
        
        int week = Math.abs( date.hashCode() % WeeklyRawData.getInstance().size() );
        return WeeklyRawData.getInstance().get(week).getDayOfWeek(dayOfWeek)[instant];
    }

    private static SimulatedSource instance;
    public static SimulatedSource getInstance() {
        if (instance == null)
            instance = new Source1();
        return instance;
    }

}