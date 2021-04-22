package site.bitlab16.datasources.profiles;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import site.bitlab16.TimeInstant;
import site.bitlab16.datasources.BasicSource;
import site.bitlab16.datasources.SourceValuesEditor;
import site.bitlab16.datasources.concentration_factors.ConcentrationModifier;
import site.bitlab16.datasources.concentration_factors.WeatherStatus;

class TypicalDataSource implements BasicSource {

    
    private static final int len_18_19_20_21_22 = 288*365*4 + 288*366;
    private static final Calendar start = new GregorianCalendar(2018, Calendar.JANUARY, 1);
    private static final Calendar end = new GregorianCalendar(2022, Calendar.DECEMBER, 31);
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    
    private final int seed;
    private final float baseMultiplier;
    private final WeatherStatus dataMeteo;
    private final ConcentrationModifier dataFestivita;
    private final ConcentrationModifier dataAttivita;
    private final ConcentrationModifier dataEventi;
    private final float indiceMeteo;
    private final float indiceEventi;
    private final float indiceAttivita;
    private final float indiceStagione;
    
    
    /**
     * Array di int che indicano il flow in un determinato istante
     * 365/366 = giorni dell'anno
     * 288 = rilevazioni in un singolo giorno
     */
    private final int[] data2018;
    private final int[] data2019;
    private final int[] data2020; // leap
    private final int[] data2021;
    private final int[] data2022;

    TypicalDataSource(
        final int[][] data,
        final int seed,
        final float baseMultiplier,
        final float indiceMeteo,
        final float indiceStagione, 
        final float indiceEventi,
        final float indiceAttivita) {

        this.seed = seed;
        this.baseMultiplier = baseMultiplier;
        this.indiceMeteo = indiceMeteo;
        this.indiceStagione = indiceStagione;
        this.indiceEventi = indiceEventi;
        this.indiceAttivita = indiceAttivita;

        this.data2018 = data[0];
        this.data2019 = data[1];
        this.data2020 = data[2];
        this.data2021 = data[3];
        this.data2022 = data[4];

        dataMeteo = new WeatherStatus("data/meteo.csv", len_18_19_20_21_22);
        dataFestivita = new ConcentrationModifier("data/festivita.csv");
        dataAttivita = new ConcentrationModifier("data/attivita_source" + getSeed() + ".csv");
        dataEventi = new ConcentrationModifier("data/eventi_source" + getSeed() + ".csv");
    }

    @Override
    public int getSeed() {
        return seed;
    }

    @Override
    public int getValue(TimeInstant when) {
        if (when.getDay().compareTo(start) < 0 || when.getDay().compareTo(end) > 0)
            return -1;
        int value;
        int year = when.getDay().get(Calendar.YEAR);
        int dayOfYear = when.getDay().get(Calendar.DAY_OF_YEAR)-1;
        int index = dayOfYear*288 + when.getInstant();
        switch (year) {
            case 2018: value = data2018[index]; break;
            case 2019: value = data2019[index]; break;
            case 2020: value = data2020[index]; break;
            case 2021: value = data2021[index]; break;
            case 2022: value = data2022[index]; break;
            default: return -1;
        }
        return Math.round(value*baseMultiplier);
    }

    @Override
    public float getEventi(TimeInstant when) {
        return dataEventi.get(dateFormat.format(when.getDay().getTime()), when.getInstant());
    }

    @Override
    public float getAttivita(TimeInstant when) {
        return dataAttivita.get(dateFormat.format(when.getDay().getTime()), when.getInstant());
    }

    @Override
    public float getFestivita(TimeInstant when) {
        return dataFestivita.get(dateFormat.format(when.getDay().getTime()), when.getInstant());
    }

    @Override
    public int getModifierMeteoAsEnum(TimeInstant when) {
        return dataMeteo.asEnum(when);
    }

    @Override
    public float getIndiceMeteo() { return indiceMeteo;}
    @Override
    public float getIndiceEventi() { return indiceEventi; }
    @Override
    public float getIndiceAttivita() { return indiceAttivita; }
    @Override
    public float getIndiceStagione() { return indiceStagione; }

    public void applyModifiers(final SourceValuesEditor editor) {
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
            float modifierMeteo = dataMeteo.getModifierMeteo(i);
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
                    data2018[offset] = editor.festeEditValue(date, data2018[offset], instant, modifierFeste);
                    data2018[offset] = editor.meteoEditValue(date, data2018[offset], instant, modifierMeteo);
                    data2018[offset] = editor.seasonEditValue(when.get(Calendar.DAY_OF_YEAR), data2018[offset], instant);
                    data2018[offset] = editor.eventiEditValue(data2018[offset], modifierEventi);
                    data2018[offset] = editor.attivitaEditValue(data2018[offset], modifierAttivita);
                    break;

                case 2019:
                    data2019[offset] = editor.festeEditValue(date, data2019[offset], instant, modifierFeste);
                    data2019[offset] = editor.meteoEditValue(date, data2019[offset], instant, modifierMeteo);
                    data2019[offset] = editor.seasonEditValue(when.get(Calendar.DAY_OF_YEAR), data2019[offset], instant);
                    data2019[offset] = editor.eventiEditValue(data2019[offset], modifierEventi);
                    data2019[offset] = editor.attivitaEditValue(data2019[offset], modifierAttivita);
                    break;

                case 2020:
                    data2020[offset] = editor.festeEditValue(date, data2020[offset], instant, modifierFeste);
                    data2020[offset] = editor.meteoEditValue(date, data2020[offset], instant, modifierMeteo);
                    data2020[offset] = editor.seasonEditValue(when.get(Calendar.DAY_OF_YEAR), data2020[offset], instant);
                    data2020[offset] = editor.eventiEditValue(data2020[offset], modifierEventi);
                    data2020[offset] = editor.attivitaEditValue(data2020[offset], modifierAttivita);
                    break;

                case 2021:
                    data2021[offset] = editor.festeEditValue(date, data2021[offset], instant, modifierFeste);
                    data2021[offset] = editor.meteoEditValue(date, data2021[offset], instant, modifierMeteo);
                    data2021[offset] = editor.seasonEditValue(when.get(Calendar.DAY_OF_YEAR), data2021[offset], instant);
                    data2021[offset] = editor.eventiEditValue(data2021[offset], modifierEventi);
                    data2021[offset] = editor.attivitaEditValue(data2021[offset], modifierAttivita);
                    break;

                case 2022: 
                    data2022[offset] = editor.festeEditValue(date, data2022[offset], instant, modifierFeste);
                    data2022[offset] = editor.meteoEditValue(date, data2022[offset], instant, modifierMeteo);
                    data2022[offset] = editor.seasonEditValue(when.get(Calendar.DAY_OF_YEAR), data2022[offset], instant);
                    data2022[offset] = editor.eventiEditValue(data2022[offset], modifierEventi);
                    data2022[offset] = editor.attivitaEditValue(data2022[offset], modifierAttivita);
                    break;

                default:
                    break;

            }

            if (instant==287)
                when.add(Calendar.DATE, 1);
        }
    
    }


    /***
    // qui se voglio usare ARIMA
    // in base a input.length dati passati predico forecastSize dati futuri
    // ATTN! input.lenght dev essere multiplo di 288, perchè predice un tot di giorni
    private final int p = 7, d = 1, q = 7, P = 2, D = 1, Q = 1, m = p+q+1;
    private final ArimaParams params = new ArimaParams(p, d, q, P, D, Q, m);
    public int[] predict(int[] inputData, int forecastSize) {

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
    */
}
