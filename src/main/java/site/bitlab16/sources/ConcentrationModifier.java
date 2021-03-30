package site.bitlab16.sources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ConcentrationModifier {

    class DailyTimeFrame {

        int start;
        int end;
        float val;

        DailyTimeFrame(int start, int end, float val) {
            this.start = start;
            this.end = end;
            this.val = val;
        }

    }

    Map<String, ArrayList<DailyTimeFrame> > modifiers = new HashMap<>();

    public void add(String day1, String day2, int start, int end) {
        //aggiungere con for loop
    }
    public void add(String day, int start, int end, float val) {
        modifiers.computeIfAbsent(day, (key) -> new ArrayList<DailyTimeFrame>() );
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