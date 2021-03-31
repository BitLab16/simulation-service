package site.bitlab16.sources;

import java.util.Calendar;

public class Source1 extends SimulatedSource {

    /* CREATION */

    private static SimulatedSource instance;
    public static SimulatedSource getInstance() {
        if (instance == null)
            instance = new Source1();
        return instance;
    }



    /* METHODS */

    @Override
    protected int getSeed() { return 2; }

    // sostituisco i giorni di festa con sabati e domeniche
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

    // imposto l' effetto della pioggia
    @Override
    protected int meteoEditValue(String date, int val, int instant, float modifier) {
        return Math.round(val / (modifier*100+1)) + val/3;
    }

    //imposto l'effetto delle stagioni
    @Override
    protected int seasonEditValue(int dayOfYear, int val, int instant) {
        int shifted = Math.abs(dayOfYear-183);
        double seasonMultiplier = 1 + 0.3 * (Math.cos(shifted*Math.PI/183*2)/2 - Math.abs(shifted)/400d);
        return (int)Math.round(seasonMultiplier*val);
    }


}