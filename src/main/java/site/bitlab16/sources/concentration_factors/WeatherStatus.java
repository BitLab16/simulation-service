package site.bitlab16.sources.concentration_factors;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Calendar;
import java.util.stream.Stream;

import site.bitlab16.TimeInstant;

public class WeatherStatus {
    
    private float[] dataMeteo;
    public WeatherStatus(final String filename, final int size) {
        dataMeteo = new float[size];
        try ( Stream<String> meteoStream = Files.lines(new File(filename).toPath()) ) {
            String linesMeteo[] = meteoStream.toArray(String[]::new);
            for (int i = 0; i < size/2; i++) {
                dataMeteo[i*2] = Float.parseFloat(linesMeteo[i]);
                dataMeteo[i*2+1] = Float.parseFloat(linesMeteo[i]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public float getModifierMeteo(int offset) {
        float ret = 0f;
        int x0 = Math.max(offset-50, 0);
        int x1 = Math.min(offset+50, dataMeteo.length);
        float cumulative = 0f;
        for (int j = x0; j < x1; j++)
            cumulative += dataMeteo[j];
        ret += cumulative/(x1-x0);
        x0 = Math.max(offset-100, 0);
        x1 = Math.min(offset-50, dataMeteo.length);
        cumulative = 0f;
        for (int j = x0; j < x1; j++)
            cumulative += dataMeteo[j];
        ret += cumulative/(x1-x0)/2;
        x0 = Math.max(offset+50, 0);
        x1 = Math.min(offset+100, dataMeteo.length);
        cumulative = 0f;
        for (int j = x0; j < x1; j++)
            cumulative += dataMeteo[j];
        ret += cumulative/(x1-x0)/2;
        return ret;
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
  
    public int asEnum(final TimeInstant when) {
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
}