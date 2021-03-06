package site.bitlab16.datasources.profiles;

import java.util.Calendar;
import java.util.Random;

import site.bitlab16.TimeInstant;
import site.bitlab16.datasources.BasicSource;
import site.bitlab16.datasources.SourceValuesEditor;
import site.bitlab16.datasources.weekly_data.WeekDayIterator;
import site.bitlab16.datasources.weekly_data.WeeklyRawData;

public class OutdoorProfile implements BasicSource, SourceValuesEditor {

    private final TypicalDataSource basicSource;
    private final WeeklyRawData wrd;

    public OutdoorProfile (
        final int seed,
        final float baseMultiplier,
        final float indiceMeteo,
        final float indiceStagione, 
        final float indiceEventi,
        final float indiceAttivita,
        final WeeklyRawData wrd) {

        this.wrd = wrd;

        var data = new int[5][];
        data[0] = new int[288*365]; // 2018
        data[1] = new int[288*365]; // 2019
        data[2] = new int[288*366]; // 2020 leap year
        data[3] = new int[288*365]; // 2021
        data[4] = new int[288*365]; // 2022
        
        var iterator = new WeekDayIterator(wrd, new Random(seed));
        for(var i = 0; i < 288*365; i++) // 2018
                data[0][i] = roundTheMiddle(iterator.getAndAdvance(), i%288);
        for(var i = 0; i < 288*365; i++) // 2019
                data[1][i] = roundTheMiddle(iterator.getAndAdvance(), i%288);
        for(var i = 0; i < 288*366; i++) // 2020
                data[2][i] = roundTheMiddle(iterator.getAndAdvance(), i%288);
        for(var i = 0; i < 288*365; i++) // 2021
                data[3][i] = roundTheMiddle(iterator.getAndAdvance(), i%288);
        for(var i = 0; i < 288*365; i++) // 2022
                data[4][i] = roundTheMiddle(iterator.getAndAdvance(), i%288);

        basicSource = new TypicalDataSource(data, seed, baseMultiplier, indiceMeteo, indiceStagione, indiceEventi, indiceAttivita);
        basicSource.applyModifiers(this);
    }

    // sostituisco i giorni di festa con sabati e domeniche
    @Override
    public int festeEditValue(String date, int val, int instant, float modifier) {

        if (modifier == 0)
            return val;
        
        int dayOfWeek;
        if (date.hashCode() % 2 == 0)
            dayOfWeek = Calendar.SATURDAY;
        else
            dayOfWeek = Calendar.SUNDAY;
        
        int week = Math.abs( date.hashCode() % wrd.size() );
        return wrd.get(week).getDayOfWeek(dayOfWeek)[instant];
    }

    @Override
    public int meteoEditValue(String date, int flow, int instant, float modifier) {
        return Math.round(flow / (modifier*110+1) + flow/getIndiceMeteo());
    }

    @Override
    public int seasonEditValue(int dayOfYear, int flow, int instant) {
        int shifted = Math.abs(dayOfYear-183);
        double seasonMultiplier = 0.8 + getIndiceStagione()/10 * (Math.cos(shifted*Math.PI/183*2)/2 - Math.abs(shifted)/400d);
        return (int)Math.round(seasonMultiplier*flow);
    }

    @Override
    public int eventiEditValue(int flow, float modifier) {
        return Math.round((modifier+getIndiceEventi())*flow/getIndiceEventi());
    }

    @Override
    public int attivitaEditValue(int flow, float modifier) {
        return Math.round((modifier+getIndiceAttivita())*flow/getIndiceAttivita());
    }

    @Override
    public int getSeed() { return basicSource.getSeed(); }
    @Override
    public int getValue(TimeInstant when) { return basicSource.getValue(when); }
    @Override
    public float getEventi(TimeInstant when) { return basicSource.getEventi(when); }
    @Override
    public float getAttivita(TimeInstant when) { return basicSource.getAttivita(when); }
    @Override
    public float getFestivita(TimeInstant when) { return basicSource.getFestivita(when); }
    @Override
    public int getModifierMeteoAsEnum(TimeInstant when) { return basicSource.getModifierMeteoAsEnum(when); }
    @Override
    public float getIndiceOrario() { return basicSource.getIndiceOrario(); }
    @Override
    public float getIndiceMeteo() { return basicSource.getIndiceMeteo(); }
    @Override
    public float getIndiceEventi() { return basicSource.getIndiceEventi(); }
    @Override
    public float getIndiceAttivita() { return basicSource.getIndiceAttivita(); }
    @Override
    public float getIndiceStagione() { return basicSource.getIndiceStagione(); }
    

    private int roundTheMiddle(int val, int i) {
        if (i>95 && i<=110)
            return val+3;
        if (i>110 && i <=120)
            return val+7;
        if (i>120 && i<=180)
            return val+11;
        if (i>180 && i <=190)
            return val+7;
        if (i>190 && i <=200)
            return val+3;
        return val;
    }
}
