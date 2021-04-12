package site.bitlab16.sources;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.stream.Stream;

import com.workday.insights.timeseries.arima.Arima;
import com.workday.insights.timeseries.arima.struct.ArimaParams;
import com.workday.insights.timeseries.arima.struct.ForecastResult;

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
    // intero usato come indice che rappresenta tutti gli istanti registrati nel corso della simulazione
    static final int len_18_19_20_21_22 = 288*365*4 + 288*366;
    private static final SimpleDateFormat dateFormat;

    // tabelle statiche festività, meteo, ...
    protected static float[] modifierMeteo;
    protected static ConcentrationModifier dataFestivita;
    protected ConcentrationModifier dataAttivita; // NON STATICO! DIPENDE DALLA SORGENTE!
    protected ConcentrationModifier dataEventi; // NON STATICO! DIPENDE DALLA SORGENTE!
    static {
        start = new GregorianCalendar(2018, Calendar.JANUARY, 1);
        end = new GregorianCalendar(2022, Calendar.DECEMBER, 31);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        modifierMeteo = new float[len_18_19_20_21_22];
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
                modifierMeteo[i*2] = Float.parseFloat(linesMeteo[i]);
                modifierMeteo[i*2+1] = Float.parseFloat(linesMeteo[i]);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //////////////////////////// NON STATIC

    /**
     * Array di int che indicano il flow in un determinato istante
     * 365/366 = giorni dell'anno
     * 288 = rilevazioni in un singolo giorno
     */
    protected Random random;
    protected int[] data2018 = new int[288*365];
    protected int[] data2019 = new int[288*365];
    protected int[] data2020 = new int[288*366]; // leap
    protected int[] data2021 = new int[288*365];
    protected int[] data2022 = new int[288*365];
    
    protected SimulatedSource() {
        random = new Random(getSeed());
        
        dataEventi = new ConcentrationModifier();
        dataAttivita = new ConcentrationModifier();
        try (
            Stream<String> eventiStream = Files.lines(new File("data/eventi_source" + getSeed() + ".csv").toPath() );
            Stream<String> attivitaStream = Files.lines(new File("data/attivita_source" + getSeed() + ".csv").toPath() );
        ) {
            eventiStream.forEach( line -> {
                String[] s = line.split(",");
                dataEventi.add(s[0], 12*Integer.parseInt(s[1]), 12*Integer.parseInt(s[2]), Integer.parseInt(s[3]));
            });
            attivitaStream.forEach( line -> {
                String[] s = line.split(",");
                dataAttivita.add(s[0], 12*Integer.parseInt(s[1]), 12*Integer.parseInt(s[2]), Integer.parseInt(s[3]));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        generateData();
        applyModifiers();
    }
    
    public abstract int getSeed();
    protected float baseMultiplier = 1f;

    public final int getValue(TimeInstant when) {
        if (when.getDay().compareTo(start) < 0 || when.getDay().compareTo(end) > 0)
            return -1;
        int i;
        i =  getSourceSpecificExpectedValue(when);
        return Math.round(i*baseMultiplier);
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
    
    public float getEventi(TimeInstant when) {
        return dataEventi.get(dateFormat.format(when.getDay().getTime()), when.getInstant());
    }
    public float getAttivita(TimeInstant when) {
        return dataAttivita.get(dateFormat.format(when.getDay().getTime()), when.getInstant());
    }
    public float getFestivita(TimeInstant when) {
        return dataFestivita.get(dateFormat.format(when.getDay().getTime()), when.getInstant());
    }
    public final int getModifierMeteoAsEnum(TimeInstant when) {
        float modifier = getModifierMeteo(when);
        if (modifier < 0.05)
            return 0;
        if(modifier < 0.13)
            return 1;
        if(modifier < 0.2)
            return 2;
        if(modifier < 0.3)
            return 3;
        if(modifier < 0.5)
            return 4;
        return 5;
    }
    public float getModifierMeteo(TimeInstant when) {
        int offset = 0;
        int year = when.getDay().get(Calendar.YEAR);
        switch (year) {
            case 2022: offset += 288*365*3 + 288*366; break; //add 2021
            case 2021: offset += 288*366 + 288*365*2; break; //add 2020
            case 2020: offset += 288*365*2; break; //add 2019
            case 2019: offset += 288*365; break; //add 2018
            default: break;
        }
        offset += (when.getDay().get(Calendar.DAY_OF_YEAR)-1)*288;
        offset += when.getInstant();
        return getModifierMeteo(offset);
    }
    private float getModifierMeteo(int offset) {
        float ret = 0f;
        int x0 = Math.max(offset-50, 0);
        int x1 = Math.min(offset+50, len_18_19_20_21_22);
        float cumulative = 0f;
        for (int j = x0; j < x1; j++)
            cumulative += modifierMeteo[j];
        ret += cumulative/(x1-x0);
        x0 = Math.max(offset-100, 0);
        x1 = Math.min(offset-50, len_18_19_20_21_22);
        cumulative = 0f;
        for (int j = x0; j < x1; j++)
            cumulative += modifierMeteo[j];
        ret += cumulative/(x1-x0)/2;
        x0 = Math.max(offset+50, 0);
        x1 = Math.min(offset+100, len_18_19_20_21_22);
        cumulative = 0f;
        for (int j = x0; j < x1; j++)
            cumulative += modifierMeteo[j];
        ret += cumulative/(x1-x0)/2;
        return ret;
    }
    protected abstract void generateData();
    private void applyModifiers() {
        Calendar when = (Calendar)start.clone();
        String date;
        int year;
        int instant;
        int offset;
        for (int i = 0; i < len_18_19_20_21_22; i++) {
            date = dateFormat.format(when.getTime());
            year = Integer.parseInt(date.split("-")[0]);
            instant = i % 288;
            offset = (when.get(Calendar.DAY_OF_YEAR)-1)*288 + instant;
            float modifierFeste = dataFestivita.get(date, instant);
            float modifierMeteo = getModifierMeteo(i);
            float modifierEventi = 0;
            for (int k = -8; k < 12; k++)
                modifierEventi += dataEventi.get(date, instant+k);
            modifierEventi /= 20;
            float modifierAttivita = 0;
            for (int k = -8; k < 8; k++)
                modifierAttivita += dataAttivita.get(date, instant+k);
            modifierAttivita /= 16;
            switch (year) {
                case 2018:
                    // NON CAMBIARE L'ORDINE CON CUI VENGONO CHIAMATI I METODI ALTRIMENTI SI ROMPE !!!
                    data2018[offset] = festeEditValue(date, data2018[offset], instant, modifierFeste);
                    data2018[offset] = meteoEditValue(date, data2018[offset], instant, modifierMeteo);
                    data2018[offset] = seasonEditValue(when.get(Calendar.DAY_OF_YEAR), data2018[offset], instant);
                    data2018[offset] = eventiEditValue(data2018[offset], modifierEventi);
                    data2018[offset] = attivitaEditValue(data2018[offset], modifierAttivita);
                    break;
                case 2019:
                    data2019[offset] = festeEditValue(date, data2019[offset], instant, modifierFeste);
                    data2019[offset] = meteoEditValue(date, data2019[offset], instant, modifierMeteo);
                    data2019[offset] = seasonEditValue(when.get(Calendar.DAY_OF_YEAR), data2019[offset], instant);
                    data2019[offset] = eventiEditValue(data2019[offset], modifierEventi);
                    data2019[offset] = attivitaEditValue(data2019[offset], modifierAttivita);
                    break;
                case 2020:
                    data2020[offset] = festeEditValue(date, data2020[offset], instant, modifierFeste);
                    data2020[offset] = meteoEditValue(date, data2020[offset], instant, modifierMeteo);
                    data2020[offset] = seasonEditValue(when.get(Calendar.DAY_OF_YEAR), data2020[offset], instant);
                    data2020[offset] = eventiEditValue(data2020[offset], modifierEventi);
                    data2020[offset] = attivitaEditValue(data2020[offset], modifierAttivita);
                    break;
                case 2021:
                    data2021[offset] = festeEditValue(date, data2021[offset], instant, modifierFeste);
                    data2021[offset] = meteoEditValue(date, data2021[offset], instant, modifierMeteo);
                    data2021[offset] = seasonEditValue(when.get(Calendar.DAY_OF_YEAR), data2021[offset], instant);
                    data2021[offset] = eventiEditValue(data2021[offset], modifierEventi);
                    data2021[offset] = attivitaEditValue(data2021[offset], modifierAttivita);
                    break;
                case 2022: 
                    data2022[offset] = festeEditValue(date, data2022[offset], instant, modifierFeste);
                    data2022[offset] = meteoEditValue(date, data2022[offset], instant, modifierMeteo);
                    data2022[offset] = seasonEditValue(when.get(Calendar.DAY_OF_YEAR), data2022[offset], instant);
                    data2022[offset] = eventiEditValue(data2022[offset], modifierEventi);
                    data2022[offset] = attivitaEditValue(data2022[offset], modifierAttivita);
                    break;
                default: break;
            }

            if (instant==287)
                when.add(Calendar.DATE, 1);
        }
    
    }
    
    /*sostituisce i giorni di festa con altri profili, CHIAMARE PER PRIMA*/
    protected abstract int festeEditValue(String date, int val, int instant, float modifier);
    /*stabilisce l'effetto del meteo*/
    protected abstract int meteoEditValue(String date, int val, int instant, float modifier);
    /*stabilisce l'effetto delle stagioni*/
    protected abstract int seasonEditValue(int dayOfYear, int val, int instant);
    /*stabilisce l'effetto degli eventi*/
    protected abstract int eventiEditValue(int val, float modifier);
    /*stabilisce l'effetto delle attivita*/
    protected abstract int attivitaEditValue(int val, float modifier);

    public abstract float getIndiceOrario();
    public abstract float getIndiceAttivita();
    public abstract float getIndiceMeteo();
    public abstract float getIndiceStagione();
    public abstract float getIndiceEventi();

    // qui se voglio usare ARIMA
    // in base a input.length dati passati predico forecastSize dati futuri
    // ATTN! input.lenght dev essere multiplo di 288, perchè predice un tot di giorni
    private final int p = 7, d = 1, q = 7, P = 2, D = 1, Q = 1, m = p+q+1;
    private final ArimaParams params = new ArimaParams(p, d, q, P, D, Q, m);
    protected int[] predict(int[] inputData, int forecastSize) {

        int[][] inputSpiltData = new int[288][inputData.length/288];
        int[][] outputSplitData = new int[288][forecastSize];
        for (int day = 0; day < inputData.length/288; day++) {
            for (int instant = 0; instant < 288; instant++) {
                inputSpiltData[instant][day] = inputData[day*288+instant];
            }
        }
        for (int i = 0; i < 288; i++) {
            ForecastResult forecastResult = Arima.forecast_arima(
                Arrays.stream(inputSpiltData[i]).asDoubleStream().toArray(), // array di input
                forecastSize, // dimensione predizione
                params); // paramentri
            outputSplitData[i] = Arrays.stream(forecastResult.getForecast()) // prendo l'output
                .mapToInt(num -> (int)Math.round(num)).toArray();
        }
        
        int[] outputData = new int[288*forecastSize];
        for (int i = 0; i < forecastSize; i++)
            for (int j = 0; j < 288; j++)
                outputData[i*288+j] = outputSplitData[j][i];
        
        return outputData;
    }
    
}