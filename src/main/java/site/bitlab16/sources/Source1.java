package site.bitlab16.sources;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import site.bitlab16.TimeInstant;

public class Source1 extends SimulatedSource {

    static final int MAX = 60;

    // ogni mezzora
    private Map<String, Integer> data;

    int oldFlow = 1;
    public Source1() {
        data = new HashMap<>();
        try ( Stream<String> lineStream = Files.lines(new File("data/Source1_data.csv").toPath()) ) {
            String[] lines = lineStream.toArray(String[]::new);
            for(int i = 0; i < lines.length-1; i++) {
                var split = lines[i].split(",");
                int year = Integer.parseInt(split[0]);
                int month = Integer.parseInt(split[1]) -1;
                int day = Integer.parseInt(split[2]);
                String mm = month + "";
                String dd = day + "";
                if (month < 10) mm = "0" + mm;
                if (day < 10) dd = "0" + dd;
                String n1 = year+"-"+mm+"-"+dd+"/";
                int instant = Integer.parseInt(split[3])*12;
                int flow = Math.round(Float.parseFloat(split[4])*10);
                int flowNext = Math.round(Float.parseFloat(lines[i+1].split(",")[4])*10);
                int diff = (flowNext-flow);
                float slope = diff/12f;
                // aggiungo il primo punto delle 00
                data.put(n1+(instant), Math.round(flow+slope));
                // aggiungo 11 punti sulla linea (05,10,15,...,55)
                for (int j = 1; j < 12; j++)
                    data.put(n1+(instant+j), Math.round(flow+slope*j)+random.nextInt()%(Math.abs(diff/6)+3));
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    @Override
    protected Integer getSourceSpecificExpectedValue(TimeInstant when) {
        try {
            String time = new SimpleDateFormat("yyyy-MM-dd").format(when.getDay().getTime());
            time = time + "/" + when.getInstant();
            return data.get(time);
        } catch (NullPointerException e) {
            return null;
        }
    }

}