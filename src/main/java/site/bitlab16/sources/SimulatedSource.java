package site.bitlab16.sources;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;
import site.bitlab16.TimeInstant;

/**
 * CLASSE ASTRATTA CHE RAPPRESENTA LA SORGENTE DATI
 * QUESTA CLASSE SI OCCUPA DI APPLICARE LE OPPORTUNE
 * MODIFICHE AI DATI FORNITI DALLE SOTTOCLASSI
 * QUESTE MODIFICHE SONO:
 *  -stagioni, pioggia, eventi, ecc
 *  -randomness extra a ondine
 *  -ecc.
 */
public abstract class SimulatedSource { 

    protected Random random;
    protected final static Calendar start = new GregorianCalendar(2018, Calendar.JANUARY, 1);
    protected final static Calendar end = new GregorianCalendar(2022, Calendar.DECEMBER, 31);

    protected int[] data2018;
    protected int[] data2019;
    protected int[] data2020;
    protected int[] data2021;
    protected int[] data2022;

    protected SimulatedSource() {
        random = new Random(1);
        data2018 = new int[288*365];
        data2019 = new int[288*365];
        data2020 = new int[288*366]; // leap
        data2021 = new int[288*365];
        data2022 = new int[288*365];
    }

    public final int getValue(TimeInstant when) {
        if (when.getDay().compareTo(start) < 0 || when.getDay().compareTo(end) > 0)
            return -1;
        int i =  getSourceSpecificExpectedValue(when);
        return i;
    }

    protected int getSourceSpecificExpectedValue(TimeInstant when) {
            int year = when.getDay().get(Calendar.YEAR);
            int dayOfYear = when.getDay().get(Calendar.DAY_OF_YEAR)-1;
            int index = dayOfYear*288 + when.getInstant();
            switch (year) {
                case 2018:
                    return data2018[index];
                case 2019:
                    return data2019[index];
                case 2020:
                    return data2020[index];
                case 2021:
                    return data2021[index];
                case 2022:
                    return data2022[index];
                default:
                    return -1;
            }
    }
    protected void applySourceSpecificRandomlyGeneratedFunctions() {
        // abstract
    }

    protected void applySourceIndependentRandomlyGeneratedFunctions() {
        // non abstract
    }

    protected void applySourceIndependentHandWrittenFunctions() {
        // non abstract
    }

    protected void applyMonteCarloRandomnessToDerivative() {
        // non abstract
    }

}