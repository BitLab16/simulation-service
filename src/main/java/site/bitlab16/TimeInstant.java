package site.bitlab16;

import org.jfree.data.time.Minute;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimeInstant {

    private Calendar day;
    private int instant;
    
    public TimeInstant(Calendar day, int instant) {
        if (instant < 0 || instant > 287)
            throw new IllegalArgumentException("Istanti validi 0-287, inserito " + instant);
        this.day = day;
        this.instant = instant;
    }

    public void advance() {
        if (instant == 287) {
            instant = 0;
            day.add(Calendar.DATE, 1);
        }
        else
            instant ++;
    }

    @Override
    public boolean equals(Object other) {
        TimeInstant i = (TimeInstant)other;
        return (this.day.compareTo(i.day)==0) ? (this.instant == i.instant) : (false);
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        int hour = instant/12;
        int min = (instant%12)*5;
        return sdf.format(day.getTime()) + " " + hour + ":" + min;   
    }

    // QUESTO SERVE SOLO PER I GRAFICI!!
    // INFATTI USA 'Minute' DI JFREE
    public Minute getMinute() {
        int yyyy = day.get(Calendar.YEAR);
        int MM = day.get(Calendar.MONTH)+1;
        int dd = day.get(Calendar.DATE);
        int hh = instant/12;
        int mm = (instant%12)*5;
        return new Minute(mm, hh, dd, MM, yyyy);
    }

    public long getTimeInMillisecond() {
        Calendar cal = (Calendar) this.day.clone();
        cal.add(Calendar.HOUR, this.instant /12);
        cal.add(Calendar.MINUTE, (this.instant%12) * 5);
        return cal.getTimeInMillis();
    }

    public int getInstant() {
        return instant;
    }
    public Calendar getDay() {
        return day;
    }

}