package site.bitlab16.sources;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.stream.Stream;

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

    //////////////////////////// STATIC

    protected static final Calendar start;
    protected static final Calendar end;

    // tabelle statiche festività, meteo, ...
    protected static float[] dataMeteo;
    protected static ConcentrationModifier dataAttivita; // NON STATICO! DIPENDE DALLA SORGENTE!
    protected static ConcentrationModifier dataFestivita;
    protected static ConcentrationModifier dataEventi; // NON STATICO! DIPENDE DALLA SORGENTE!
    static {
        start = new GregorianCalendar(2018, Calendar.JANUARY, 1);
        end = new GregorianCalendar(2022, Calendar.DECEMBER, 31);

        final int len_18_19_20_21_22 = 288*365 + 288*365 + 288*366 + 288*365 + 288*365;
        dataMeteo = new float[len_18_19_20_21_22];
        dataFestivita = new ConcentrationModifier();
        try (
            Stream<String> festivitaStream = Files.lines(new File("data/festivita.csv").toPath());
            Stream<String> meteoStream = Files.lines(new File("data/meteo.csv").toPath());
            
        ) {
            festivitaStream.forEach( (day) -> {
                dataFestivita.add(day, 0, 287, 1);
            });
            String linesMeteo[] = meteoStream.toArray(String[]::new);
            for (int i = 0; i < len_18_19_20_21_22/2; i++) {
                dataMeteo[i*2] = Float.parseFloat(linesMeteo[i]);
                dataMeteo[i*2+1] = Float.parseFloat(linesMeteo[i]);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //////////////////////////// NON STATIC

    
    protected final Random random;
    protected int[] data2018 = new int[288*365];
    protected int[] data2019 = new int[288*365];
    protected int[] data2020 = new int[288*366]; // leap
    protected int[] data2021 = new int[288*365];
    protected int[] data2022 = new int[288*365];
    
    private class DayIterator {
        WeeklyRawData weeks;
        int[] week;
        int instant;
        DayIterator() {
            reset();
        }
        void reset() {
            weeks = WeeklyRawData.getInstance();
            int selectedWeek = random.nextInt(weeks.size());
            week = weeks.get(selectedWeek).getWeek();
            instant = 0;
        }
        int getAndAdvance() {
            int flow = week[instant];
            if (++instant == week.length)
                reset();
            return flow;
        }

    }
    SimulatedSource() {
        random = new Random(getSeed());
        DayIterator iterator = new DayIterator();
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

        feste();
    }
    
    protected abstract int getSeed();

    public final int getValue(TimeInstant when) {
        if (when.getDay().compareTo(start) < 0 || when.getDay().compareTo(end) > 0)
            return -1;
        int i;
        i =  getSourceSpecificExpectedValue(when);
        i = eventi(when, i);
        i = attivita(when, i);
        i = meteo(when, i);
        i = stagione(when, i);
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

    // sostituisco i giorni di festa con le domeniche
    protected abstract void feste();
    private static int eventi(TimeInstant when, int flow) {
        return flow;
    }
    private static int attivita(TimeInstant when, int flow) {
        return flow;
    }
    private static int meteo(TimeInstant when, int flow) {
        int i = dataMeteo[(when.getDay().get(Calendar.DAY_OF_YEAR)-1)*288 + when.getInstant()] == 0 ? 
            1 : -2;
        return Math.round(flow*i);
    }
    private static int stagione(TimeInstant when, int i) {
        return i;
    }


    //// qui se voglio usare ARIMA
    //in base a n dati passati predico m dati futuri
    /* !!! è un macello ma l'idea è testata funziona
    int[] predict(int input[]) {

        //predico il futuro
        int p = 7*3, d = 0, q = 7*3, P = 1, D = 1, Q = 0, m = p+q+1;
        int forecastSize = 365;
        ArimaParams params = new ArimaParams(p, d, q, P, D, Q, m);

        int[][] input1819 = new int[288][365*2];
        int[][] result20 = new int[288][366];
        for (int i = 0; i < 288; i++) {
            for (int j = 0; j < 365; j++) {
                input1819[i][j] = data2018[j*288+i];
                input1819[i][j+365] = data2019[j*288+i];
            }
        }

        for (int i = 0; i < 288; i++) {
            ForecastResult forecastResult = Arima.forecast_arima(
                Arrays.stream(input1819[i]).asDoubleStream().toArray(),
                forecastSize,
                params);
            result20[i] = Arrays.stream(forecastResult.getForecast())
                .mapToInt(num -> (int)Math.round(num)).toArray();
        }
        
        for (int i = 0; i < 288; i++) {
            for (int j = 0; j < 365; j++) {
                data2020[j*288+i] = result20[i][j];
            }
        }
    }
    */
}