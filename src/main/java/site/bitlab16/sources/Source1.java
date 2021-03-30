package site.bitlab16.sources;

import java.util.Random;

public class Source1 extends SimulatedSource {

    int oldFlow = 1;
    public Source1() {
        random = new Random(1);

        WeeklyRawData weeks;
        try {
            weeks = WeeklyRawData.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        for(int i = 0; i < 52; i++) {
            int[] rand_week = weeks.get(random.nextInt(weeks.size())).getWeek();
            for (int j = 0; j < rand_week.length; j++)
                data2018[i*288*7 + j] = rand_week[j];
        }

        /*

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
        */

    }


}