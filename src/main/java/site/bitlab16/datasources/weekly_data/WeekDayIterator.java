package site.bitlab16.datasources.weekly_data;

import java.util.Random;

/**
 * Iteratore per andare a istanziare gli array contenenti i flow dei vari anni
 */
public class WeekDayIterator {
    private Random random;
    private final WeeklyRawData weeks;
    int[] week;
    int instant;
    public WeekDayIterator(final WeeklyRawData wrd, final Random random) {
        weeks = wrd;
        this.random = random;
        reset();
    }
    private void reset() {
        var selectedWeek = random.nextInt(weeks.size());
        week = weeks.get(selectedWeek).getWeek();
        instant = 0;
    }
    public int getAndAdvance() {
        int flow = week[instant];
        if (++instant == week.length)
            reset();
        return flow;
    }

}