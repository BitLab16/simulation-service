package site.bitlab16.sources.points;

import java.util.Calendar;

import site.bitlab16.sources.SimulatedSource;
import site.bitlab16.sources.WeeklyRawData;
import site.bitlab16.sources.WeeklyRawData.WeekDayIterator;

public class Garibaldi extends SimulatedSource {

    /* CREATION */

    public Garibaldi() {
        baseMultiplier = 0.8f;
    }

    
    /* METHODS */

    @Override
    public int getSeed() { return 3; }


    private int curva1(int val, int i) {
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

    @Override
    protected void generateData() {
        
        WeekDayIterator iterator = new WeekDayIterator(random);
        //2018
        for(int i = 0; i < 288*365; i++) {
                data2018[i] = curva1(iterator.getAndAdvance(), i%288);
        }
        //2019
        for(int i = 0; i < 288*365; i++) {
                data2019[i] = curva1(iterator.getAndAdvance(), i%288);
        }
        //2020
        for(int i = 0; i < 288*366; i++) {
                data2020[i] = curva1(iterator.getAndAdvance(), i%288);
        }
        //2021
        for(int i = 0; i < 288*365; i++) {
                data2021[i] = curva1(iterator.getAndAdvance(), i%288);
        }
        //2022
        for(int i = 0; i < 288*365; i++) {
                data2022[i] = curva1(iterator.getAndAdvance(), i%288);
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

    //imposto l'effetto degli eventi
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
        return 1.5f;
    }
    @Override
    public float getIndiceStagione() {
        return 2;
    }
    @Override
    public float getIndiceAttivita() {
        return 1;
    }
    @Override
    public float getIndiceEventi() {
        return 2f;
    }

}