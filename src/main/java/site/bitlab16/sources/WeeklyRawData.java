package site.bitlab16.sources;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Stream;

public class WeeklyRawData {

    private static WeeklyRawData instance = null;
    private final Random random = new Random(10203040);

    public static WeeklyRawData getInstance() throws Exception {
        if (instance == null)
            instance = new WeeklyRawData();
        return instance;
    }

    public class Week {

        // da domenica a sabato
        private int weekData[];

        Week(int a[]) {
            if (a.length != 288*7)
                throw new IllegalArgumentException("Invalid week of " + a.length + " instants, must be " + 288*7);
            weekData = a;
        }

        public int[] getWeek() {
            return weekData;
        }
        public int[] getDayOfWeek(final int dayOfWeek) {
            return Arrays.copyOfRange(weekData, 288*(dayOfWeek-1), 288*dayOfWeek);
        }
    }

    private ArrayList<Week> data = new ArrayList<>();
    
    private WeeklyRawData() throws Exception {
        int i;
        for (int filenum = 1; filenum < 101; filenum++) {
            int weekData[] = new int[288*7];
            try ( Stream<String> lineStream =  Files.lines(new File("data/week" + filenum + ".csv").toPath()) ) {
                String[] lines = lineStream.toArray(String[]::new);
                if (lines.length != 24*7)
                    throw new Exception("Data file invalid length: week" + filenum);
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
            } catch (FileNotFoundException e) {
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