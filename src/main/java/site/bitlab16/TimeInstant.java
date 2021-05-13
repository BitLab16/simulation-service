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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TimeInstant that = (TimeInstant) o;

        if (instant != that.instant) return false;
        return day.equals(that.day);
    }

    @Override
    public int hashCode() {
        int result = day.hashCode();
        result = 31 * result + instant;
        return result;
    }

    @Override
    public String toString() {
        var sdf = new SimpleDateFormat("yyyy-MM-dd");
        int hour = instant/12;
        String hh = hour + "";
        if (hour < 10)
            hh = "0" + hh;
        int min = (instant%12)*5;
        String mm = min + "";
        if (min < 10)
            mm = "0" + mm;
        return sdf.format(day.getTime()) + " " + hh + ":" + mm;   
    }
    public String toStringAsInstant() {
        return new SimpleDateFormat("yyyy-MM-dd").format(day.getTime()) + "/" + instant;    
    }

    // QUESTO SERVE SOLO PER I GRAFICI!!
    // INFATTI USA 'Minute' DI JFREE
    public Minute getMinute() {
        int yyyy = day.get(Calendar.YEAR);
        int month = day.get(Calendar.MONTH)+1;
        int dd = day.get(Calendar.DATE);
        int hh = instant/12;
        int mm = (instant%12)*5;
        return new Minute(mm, hh, dd, month, yyyy);
    }

    public long getTimeInMillis() {
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