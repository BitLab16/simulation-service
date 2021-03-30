package site.bitlab16.sources;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Source1 extends SimulatedSource {

    @Override
    protected int getSeed() { return 1; }

    private Source1() {
        

    }

    @Override
    protected void feste() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar when = new GregorianCalendar(2018, Calendar.JANUARY, 1);
        Calendar end = new GregorianCalendar(2023, Calendar.JANUARY, 1);
        String date;
        int year;
        int instant;
        int offset;
        for (int i = 0; i < 288*(356*4+366); i++) {
            date = dateFormat.format(when.getTime());
            year = Integer.parseInt(date.split("-")[0]);
            instant = i % 288;
            offset = (when.get(Calendar.DAY_OF_YEAR)-1)*288 + instant;
            float modifier = dataFestivita.get(date, instant);
            switch (year) {
                case 2018: data2018[offset] = festeEditValue(date, data2018[offset], instant, modifier); break;
                case 2019: data2019[offset] = festeEditValue(date, data2019[offset], instant, modifier); break;
                case 2020: data2020[offset] = festeEditValue(date, data2020[offset], instant, modifier); break;
                case 2021: data2021[offset] = festeEditValue(date, data2021[offset], instant, modifier); break;
                case 2022: data2022[offset] = festeEditValue(date, data2022[offset], instant, modifier); break;
                default: break;
            }

            if (instant==277)
                when.add(Calendar.DATE, 1);
        }

    }

    private int festeEditValue(String date, int val, int instant, float modifier) {

        if (modifier == 0)
            return val;
        
        int dayOfWeek;
        if (date.hashCode() % 2 == 0)
            dayOfWeek = Calendar.SATURDAY;
        else
            dayOfWeek = Calendar.SUNDAY;
        
        int week = Math.abs( date.hashCode() % WeeklyRawData.getInstance().size() );
        return WeeklyRawData.getInstance().get(week).getDayOfWeek(dayOfWeek)[instant];
    }

    private static SimulatedSource instance;
    public static SimulatedSource getInstance() {
        if (instance == null)
            instance = new Source1();
        return instance;
    }

}