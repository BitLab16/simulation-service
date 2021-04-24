package site.bitlab16.datasources.weeklyData;

import java.util.Random;

/**
     * Iteratore per andare a istanziare gli array contenenti i flow dei vari anni
     */
    public class SpecificWeekDayIterator {
        protected Random random;
        final WeeklyRawData weeks;
        public SpecificWeekDayIterator(final WeeklyRawData wrd, final Random random) {
            weeks = wrd;
            this.random = random;
        }
        public int[] getDayOfWeek(final int day) {
            int selectedWeek = random.nextInt(weeks.size());
            return weeks.get(selectedWeek).getDayOfWeek(day);
        }

    }