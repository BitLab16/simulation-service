package site.bitlab16.datasources.profiles2;

import java.util.Calendar;
import java.util.Random;

import site.bitlab16.TimeInstant;
import site.bitlab16.datasources.BasicSource;
import site.bitlab16.datasources.SourceValuesEditor;
import site.bitlab16.datasources.weeklyData.WeeklyRawData;
import site.bitlab16.datasources.weeklyData.WeeklyRawData.WeekDayIterator;

public class CityBuildingProfile implements BasicSource, SourceValuesEditor {

    TypicalDataSource basicSource;

    public CityBuildingProfile (
        final int seed,
        final float baseMultiplier,
        final float indiceMeteo,
        final float indiceStagione, 
        final float indiceEventi,
        final float indiceAttivita) {

        int[][] data = new int[5][];
        data[0] = new int[288*365]; // 2018
        data[1] = new int[288*365]; // 2019
        data[2] = new int[288*366]; // 2020 leap year
        data[3] = new int[288*365]; // 2021
        data[4] = new int[288*365]; // 2022
        WeekDayIterator iterator = new WeekDayIterator(new Random(seed));
        for(int i = 0; i < 288*365; i++) // 2018
                data[0][i] = iterator.getAndAdvance();
        for(int i = 0; i < 288*365; i++) //2019
            data[1][i] = iterator.getAndAdvance();
        for(int i = 0; i < 288*366; i++) // 2020
                data[2][i] = iterator.getAndAdvance();
        for(int i = 0; i < 288*365; i++) //2021
            data[3][i] = iterator.getAndAdvance();
        for(int i = 0; i < 288*365; i++) // 2022
            data[4][i] = iterator.getAndAdvance();

        basicSource = new TypicalDataSource(data, seed, baseMultiplier, indiceMeteo, indiceStagione, indiceEventi, indiceAttivita);
        basicSource.applyModifiers(this);
    }

    @Override
    public int festeEditValue(String date, int val, int instant, float modifier) {
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

    @Override
    public int meteoEditValue(String date, int flow, int instant, float modifier) {
        return Math.round(flow / (modifier*100+1) + flow/getIndiceMeteo());
    }

    @Override
    public int seasonEditValue(int dayOfYear, int flow, int instant) {
        int shifted = Math.abs(dayOfYear-183);
        double seasonMultiplier = 1 + getIndiceStagione()/10 * (Math.cos(shifted*Math.PI/183*2)/2 - Math.abs(shifted)/400d);
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
    
}
