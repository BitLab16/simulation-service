package site.bitlab16.datasources.concentration_factors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class ConcentrationModifier {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConcentrationModifier.class);
    
    /**
     * Mappa che associa una data (rappresentata come stringa) ad una lista di DailyTimeFrame
     */
    private final Map<String, ArrayList<DailyTimeFrame> > modifiers = new HashMap<>();

    public ConcentrationModifier(final String filename) {
        try ( Stream<String> fileStream = Files.lines(new File(filename).toPath()) ) {
            fileStream.forEach( line -> {
                String[] s = line.split(",");
                add(s[0], 12*Integer.parseInt(s[1]), 12*Integer.parseInt(s[2]), Integer.parseInt(s[3]));
            });
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * Classe che rappresenta un arco temporale con assegnato un valore
     * es. dalle 11 alle 14 valore = 2 \
     * (e' indipendente dal giorno e valore puo' indicare qualsiasi cosa, es. intensita' di un evento, ecc.)
     */
    private class DailyTimeFrame {

        int start;
        int end;
        float val;

        DailyTimeFrame(int start, int end, float val) {
            this.start = start;
            this.end = end;
            this.val = val;
        }

    }
    
    public void add(String day, int start, int end, float val) {
        modifiers.computeIfAbsent(day, key -> new ArrayList<DailyTimeFrame>() );
        modifiers.get(day).add(new DailyTimeFrame(start, end, val));
    }

    public float get(String day, int instant) {
        float val = 0;
        if ( modifiers.containsKey(day) )
            for (DailyTimeFrame modifier : modifiers.get(day))
                if (instant >= modifier.start && instant <= modifier.end)
                    val += modifier.val;
        return val;
    }

}
