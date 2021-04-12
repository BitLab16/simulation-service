package site.bitlab16.sources;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Stream;

public class WeeklyRawData {

    private static WeeklyRawData instance = null;
    private static final Random random = new Random(10203040);

    public static WeeklyRawData getInstance() {
        if (instance == null)
            instance = new WeeklyRawData();
        return instance;
    }

    public class Week {

        // da domenica a sabato
        private int weekData[];

        Week(int a[]) {
            weekData = a;
        }

        public int[] getWeek() {
            return weekData;
        }
        public int[] getDayOfWeek(final int dayOfWeek) {
            return Arrays.copyOfRange(weekData, 288*(dayOfWeek-1), 288*dayOfWeek);
        }
    }
    /**
     * Iteratore per andare a istanziare gli array contenenti i flow dei vari anni
     */
    public static class WeekDayIterator {
        protected Random random;
        final WeeklyRawData weeks;
        int[] week;
        int instant;
        public WeekDayIterator(Random random) {
            weeks = WeeklyRawData.getInstance();
            this.random = random;
            reset();
        }
        void reset() {
            int selectedWeek = random.nextInt(weeks.size());
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
    

    private ArrayList<Week> data = new ArrayList<>();
    
    private WeeklyRawData() {
        int i;
        for (int filenum = 1; filenum < 101; filenum++) {
            int weekData[] = new int[288*7];
            try ( Stream<String> lineStream =  Files.lines(new File("data/weeks/week" + filenum + ".csv").toPath()) ) {
                String[] lines = lineStream.toArray(String[]::new);
                for(i = 0; i < lines.length; i++) {
                    int flow = getFlow(lines, i);
                    int flowNext =  getFlow(lines, i+1);
                    float slope = (flowNext-flow)/12f;
                    // aggiungo il primo punto delle :00
                    weekData[i*12] =  flow;
                    // aggiungo 11 punti sulla linea (:05,:10,:15,...,:55)
                    int modulo = Math.abs((flowNext-flow)/6)+2;
                    for (int j = 1; j < 12; j++)
                        weekData[i*12 + j] = Math.round(flow+slope*j) + (random.nextInt() % modulo);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            data.add(new Week(weekData));
        }
    }

    private static int getFlow(final String lines[], int i) {
        if (i == lines.length )
            i--;
        float fetched = Float.parseFloat(lines[i].split(",")[1]) * 500;
        return (int)Math.round(fetched/Math.sqrt(fetched))-1;
    }

    public Week get(final int i) {
        return data.get(i);
    }
    public int size() {
        return data.size();
    }
}