package site.bitlab16.sources.points;

import java.util.Calendar;

import site.bitlab16.sources.SimulatedSource;
import site.bitlab16.sources.WeeklyRawData;
import site.bitlab16.sources.WeeklyRawData.SpecificWeekDayIterator;

public class Supermercato extends SimulatedSource {

    /* CREATION */

    private static SimulatedSource instance;
    public static SimulatedSource getInstance() {
        if (instance == null)
            instance = new Supermercato();
        return instance;
    }
    protected Supermercato() {
        baseMultiplier = 0.05f;
    }

    /* METHODS */

    @Override
    public int getSeed() { return 4; }


    @Override
    protected void generateData() {
        
        //confronto un sabaato e una domanic arandom per farne la somma.
        //considero tutti i giorni sabati-domeniche
        SpecificWeekDayIterator iterator = new SpecificWeekDayIterator(random);
        int[] day1;
        int[] day2;
        //2018
        for(int i = 0; i < 365; i++) {
            day1 = iterator.getDayOfWeek(Calendar.SATURDAY);
            day2 = iterator.getDayOfWeek(Calendar.SUNDAY);
            for (int instant = 0; instant < 288; instant++)
                data2018[i*288+instant] = day1[instant] + day2[instant];
        }
        //2019
        for(int i = 0; i < 288*365; i++) {
            data2019[i] = 0;
        }
        //2020
        for(int i = 0; i < 288*366; i++) {
            data2020[i] = 0;
        }
        //2021
        for(int i = 0; i < 288*365; i++) {
            data2021[i] = 0;
        }
        //2022
        for(int i = 0; i < 288*365; i++) {
            data2022[i] = 0;
        }
        
    }
    

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
    protected int meteoEditValue(String date, int flow, int instant, float modifier) {
        return Math.round(flow / (modifier*100+1) + flow/getIndiceMeteo());
    }

    //imposto l'effetto delle stagioni
    @Override
    protected int seasonEditValue(int dayOfYear, int flow, int instant) {
        int shifted = Math.abs(dayOfYear-183);
        double seasonMultiplier = 0.8 + getIndiceStagione()/10 * (Math.cos(shifted*Math.PI/183*2)/2 - Math.abs(shifted)/400d);
        return (int)Math.round(seasonMultiplier*flow);
    }

    //imposto l'effetto delle stagioni
    @Override
    protected int eventiEditValue(int flow, float modifier) {
        return flow;
    }

    //imposto l'effetto delle attività
    @Override
    protected int attivitaEditValue(int flow, float modifier) {
        return Math.round((modifier+getIndiceAttivita())*flow/getIndiceAttivita());
    }

    
    @Override
    public float getIndiceOrario() {
        return 1000;
    }
    @Override
    public float getIndiceMeteo() {
        return 0.5f;
    }
    @Override
    public float getIndiceStagione() {
        return 2;
    }
    @Override
    public float getIndiceAttivita() {
        return .3f;
    }
    @Override
    public float getIndiceEventi() {
        return 1;
    }

}