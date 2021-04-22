package site.bitlab16.datasources.profiles;

import site.bitlab16.datasources.SimulatedSource;
import site.bitlab16.datasources.weeklyData.WeeklyRawData.SpecificWeekDayIterator;

abstract class GenericIndoor extends SimulatedSource {
    

    protected abstract int[] getUsableDays();

    @Override
    protected void generateData() {
        
        //confronto un sabato e una domanic arandom per farne la somma.
        //considero tutti i giorni sabati-domeniche
        SpecificWeekDayIterator iterator = new SpecificWeekDayIterator(random);
        int[] day;
        int[] usableDays = getUsableDays();
        //2018
        for(int i = 0; i < 365; i++) {
            day = getInputDay(iterator, usableDays);
            for (int instant = 0; instant < 288; instant++)
                data2018[i*288+instant] = day[instant]-usableDays.length;
        }
        //2019
        for(int i = 0; i < 365; i++) {
            day = getInputDay(iterator, usableDays);
            for (int instant = 0; instant < 288; instant++)
                data2019[i*288+instant] = day[instant]-usableDays.length;
        }
        //2020
        for(int i = 0; i < 366; i++) {
            day = getInputDay(iterator, usableDays);
            for (int instant = 0; instant < 288; instant++)
                data2020[i*288+instant] = day[instant]-usableDays.length;
        }
        //2021
        for(int i = 0; i < 365; i++) {
            day = getInputDay(iterator, usableDays);
            for (int instant = 0; instant < 288; instant++)
                data2021[i*288+instant] = day[instant]-usableDays.length;
        }
        //2022
        for(int i = 0; i < 365; i++) {
            day = getInputDay(iterator, usableDays);
            for (int instant = 0; instant < 288; instant++)
                data2022[i*288+instant] = day[instant]-usableDays.length;
        }
        
    }
    private int[] getInputDay(SpecificWeekDayIterator iterator, int[] usableDay) {
        int[] sum = new int[288];
        int[] day;
        for (int selectedDay : usableDay) {
            day = iterator.getDayOfWeek(selectedDay);
            for (int i = 0; i < 288; i++)
                sum[i] += day[i];
        }
        return sum;
    }
    
    
    //imposto l'effetto delle stagioni
    @Override
    protected int seasonEditValue(int dayOfYear, int flow, int instant) {
        int shifted = Math.abs(dayOfYear-183);
        double seasonMultiplier = 0.8 + getIndiceStagione()/10 * (Math.cos(shifted*Math.PI/183*2)/2 - Math.abs(shifted)/400d);
        return (int)Math.round(seasonMultiplier*flow);
    }
    
    // ingrosso un po' le festività
    @Override
    protected int festeEditValue(String date, int val, int instant, float modifier) {
        return Math.round(val*1.4f);
    }

    // imposto l' effetto della pioggia 
    @Override
    protected int meteoEditValue(String date, int flow, int instant, float modifier) {
        return Math.round(flow / (modifier*100+1) + flow/getIndiceMeteo());
    }

}