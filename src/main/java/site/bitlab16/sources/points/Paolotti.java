package site.bitlab16.sources.points;

import java.util.Calendar;

import site.bitlab16.sources.SimulatedSource;
import site.bitlab16.sources.WeeklyRawData;
import site.bitlab16.sources.WeeklyRawData.WeekDayIterator;

public class Paolotti extends SimulatedSource { // PAOLOTTI

    /* CREATION */

    private static SimulatedSource instance;
    public static SimulatedSource getInstance() {
        if (instance == null)
            instance = new Paolotti();
        return instance;
    }
    protected Paolotti() {
        baseMultiplier = 0.2f;
    }

    /* METHODS */

    @Override
    public int getSeed() { return 1; }

    @Override
    protected void generateData() {
        
        WeekDayIterator iterator = new WeekDayIterator(random);
        //2018
        for(int i = 0; i < 288*365; i++) {
                data2018[i] = iterator.getAndAdvance();
        }
        //2019
        for(int i = 0; i < 288*365; i++) {
                data2019[i] = iterator.getAndAdvance();
        }
        //2020
        for(int i = 0; i < 288*366; i++) {
                data2020[i] = iterator.getAndAdvance();
        }
        //2021
        for(int i = 0; i < 288*365; i++) {
                data2021[i] = iterator.getAndAdvance();
        }
        //2022
        for(int i = 0; i < 288*365; i++) {
                data2022[i] = iterator.getAndAdvance();
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
        double seasonMultiplier = 1 + getIndiceStagione()/10 * (Math.cos(shifted*Math.PI/183*2)/2 - Math.abs(shifted)/400d);
        return (int)Math.round(seasonMultiplier*flow);
    }

    //imposto l'effetto delle stagioni
    @Override
    protected int eventiEditValue(int flow, float modifier) {
        return Math.round((modifier+getIndiceEventi())*flow/getIndiceEventi());
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
        return 1;
    }
    @Override
    public float getIndiceStagione() {
        return 1.8f;
    }
    @Override
    public float getIndiceAttivita() {
        return .5f;
    }
    @Override
    public float getIndiceEventi() {
        return 3;
    }


}