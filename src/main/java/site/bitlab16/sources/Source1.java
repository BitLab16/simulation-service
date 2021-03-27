package site.bitlab16.sources;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import com.workday.insights.timeseries.arima.Arima;
import com.workday.insights.timeseries.arima.struct.ArimaParams;
import com.workday.insights.timeseries.arima.struct.ForecastResult;

import site.bitlab16.TimeInstant;

public class Source1 extends SimulatedSource {

    int oldFlow = 1;
    public Source1() {
        
        //Prelevo i dati aiutandomi con una hashmap per salvarli
        Map<String, Integer> dataMap = new HashMap<>();
        try ( Stream<String> lineStream = Files.lines(new File("data/Source1_data.csv").toPath()) ) {
            String[] lines = lineStream.toArray(String[]::new);
            for(int i = 0; i < lines.length-1; i++) {
                String[] split = lines[i].split(",");
                int year = Integer.parseInt(split[0]);
                int month = Integer.parseInt(split[1]);
                int day = Integer.parseInt(split[2]);
                String mm = month + "";
                String dd = day + "";
                if (month < 10) mm = "0" + mm;
                if (day < 10) dd = "0" + dd;
                String n1 = year+"-"+mm+"-"+dd+"/";
                int instant = Integer.parseInt(split[3])*12;
                int flow = Math.round(Float.parseFloat(split[4])*500);
                flow = (int)Math.round(flow/(Math.sqrt(flow*1f)));
                int flowNext = Math.round(Float.parseFloat(lines[i+1].split(",")[4])*500);
                flowNext = (int)Math.round(flowNext/(Math.sqrt(flowNext*1f)));
                int diff = (flowNext-flow);
                float slope = diff/12f;
                // aggiungo il primo punto delle :00
                dataMap.put(n1+(instant), Math.round(flow+slope));
                // aggiungo 11 punti sulla linea (:05,:10,:15,...,:55)
                for (int j = 1; j < 12; j++)
                    dataMap.put(n1+(instant+j), Math.round(flow+slope*j)+random.nextInt()%(Math.abs(diff/6)+3));
            }
        } catch (IOException e) {
            // Probabilmente FileNotFoundException
            System.out.println(e);
        }


        // Dalla HashMap ad un Array contiguo
        TimeInstant start = new TimeInstant(new GregorianCalendar(2018, Calendar.JANUARY, 1), 0);
        TimeInstant end = new TimeInstant(new GregorianCalendar(2020, Calendar.JANUARY, 1), 0);

        for(; ! start.equals(end); start.advance()) {
            int instant = start.getInstant();
            int year = start.getDay().get(Calendar.YEAR);
            int dayOfYear = start.getDay().get(Calendar.DAY_OF_YEAR) -1;

            Integer val = dataMap.get(start.toStringAsInstant());
            if (val == null)
                val = data2018[dayOfYear*288 + instant -1];
            if (year == 2018)
                data2018[dayOfYear*288 + instant] = val;
            else if (year == 2019)
                data2019[dayOfYear*288 + instant] = val;
        }

        //predico il futuro
        int p = 7*3, d = 0, q = 7*3, P = 1, D = 1, Q = 0, m = p+q+1;
        int forecastSize = 365;
        ArimaParams params = new ArimaParams(p, d, q, P, D, Q, m);

        int[][] input1819 = new int[288][365*2];
        int[][] result20 = new int[288][366];
        for (int i = 0; i < 288; i++) {
            for (int j = 0; j < 365; j++) {
                input1819[i][j] = data2018[j*288+i];
                input1819[i][j+365] = data2019[j*288+i];
            }
        }

        for (int i = 0; i < 288; i++) {
            ForecastResult forecastResult = Arima.forecast_arima(
                Arrays.stream(input1819[i]).asDoubleStream().toArray(),
                forecastSize,
                params);
            result20[i] = Arrays.stream(forecastResult.getForecast())
                .mapToInt(num -> (int)Math.round(num)).toArray();
        }
        
        for (int i = 0; i < 288; i++) {
            for (int j = 0; j < 365; j++) {
                data2020[j*288+i] = result20[i][j];
            }
        }

    }


}