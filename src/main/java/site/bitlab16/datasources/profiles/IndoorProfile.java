package site.bitlab16.datasources.profiles;

import java.util.Random;

import site.bitlab16.TimeInstant;
import site.bitlab16.datasources.BasicSource;
import site.bitlab16.datasources.SourceValuesEditor;
import site.bitlab16.datasources.weeklyData.WeeklyRawData.SpecificWeekDayIterator;

public class IndoorProfile implements BasicSource, SourceValuesEditor {

    TypicalDataSource basicSource;

    public IndoorProfile (
        final int seed,
        final float baseMultiplier,
        final float indiceMeteo,
        final float indiceStagione, 
        final float indiceEventi,
        final float indiceAttivita,
        final int[] usableDays) {

        int[][] data = new int[5][];
        data[0] = new int[288*365]; // 2018
        data[1] = new int[288*365]; // 2019
        data[2] = new int[288*366]; // 2020 leap year
        data[3] = new int[288*365]; // 2021
        data[4] = new int[288*365]; // 2022
        SpecificWeekDayIterator iterator = new SpecificWeekDayIterator(new Random(seed));
        int[] day;
        //2018
        for(int i = 0; i < 365; i++) {
            day = getInputDay(iterator, usableDays);
            for (int instant = 0; instant < 288; instant++)
                data[0][i*288+instant] = day[instant]-usableDays.length;
        }
        //2019
        for(int i = 0; i < 365; i++) {
            day = getInputDay(iterator, usableDays);
            for (int instant = 0; instant < 288; instant++)
                data[1][i*288+instant] = day[instant]-usableDays.length;
        }
        //2020
        for(int i = 0; i < 366; i++) {
            day = getInputDay(iterator, usableDays);
            for (int instant = 0; instant < 288; instant++)
                data[2][i*288+instant] = day[instant]-usableDays.length;
        }
        //2021
        for(int i = 0; i < 365; i++) {
            day = getInputDay(iterator, usableDays);
            for (int instant = 0; instant < 288; instant++)
                data[3][i*288+instant] = day[instant]-usableDays.length;
        }
        //2022
        for(int i = 0; i < 365; i++) {
            day = getInputDay(iterator, usableDays);
            for (int instant = 0; instant < 288; instant++)
                data[4][i*288+instant] = day[instant]-usableDays.length;
        }

        basicSource = new TypicalDataSource(data, seed, baseMultiplier, indiceMeteo, indiceStagione, indiceEventi, indiceAttivita);
        basicSource.applyModifiers(this);
    }

    // ingrosso un po' le festività
    @Override
    public int festeEditValue(String date, int val, int instant, float modifier) {
        return Math.round(val*1.4f);
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
    

    private int[] getInputDay(final SpecificWeekDayIterator iterator, final int[] usableDay) {
        int[] sum = new int[288];
        int[] day;
        for (int selectedDay : usableDay) {
            day = iterator.getDayOfWeek(selectedDay);
            for (int i = 0; i < 288; i++)
                sum[i] += day[i];
        }
        return sum;
    }
}
