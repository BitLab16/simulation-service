package site.bitlab16;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import site.bitlab16.datasources.BasicSource;
import site.bitlab16.datasources.profiles2.CityBuildingProfile;
import site.bitlab16.datasources.profiles2.IndoorProfile;
import site.bitlab16.datasources.profiles2.OutdoorProfile;

public class Graphic extends JFrame implements Runnable {

    private BasicSimulator simulator;

    public Graphic(BasicSimulator simulator, String title) {
        super(title);
        this.simulator = simulator;
    }

    @Override
    public void run() {

        /// INIT VARS
        TimeInstant when = new TimeInstant(new GregorianCalendar(2018, Calendar.JANUARY, 1), 0);
        TimeInstant end = new TimeInstant(new GregorianCalendar(2023, Calendar.JANUARY, 15), 0);
        ArrayList<TimeSeries> series = new ArrayList<TimeSeries>();


        final int[] weekdays = {Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY};
        final int[] saturdays = {Calendar.SATURDAY, Calendar.SATURDAY};
        BasicSource paolotti = new CityBuildingProfile(1, .2f, 1f, 1.8f, 3f, 1.1f);
        BasicSource pratodellavalle = new OutdoorProfile(2, 1.2f, 3f, 1.5f, 1f, 3.5f);
        BasicSource piazzagaribaldi = new OutdoorProfile(3, .8f, 1.5f, 2f, 2f, 1f);
        BasicSource supermercato = new IndoorProfile(4, .012f, .5f, 2.f, 1f, .3f, weekdays );
        BasicSource fiera = new IndoorProfile(5, .12f, .5f, 1.5f, 1.2f, 1.f, saturdays );

        BasicSource[] sources = new BasicSource[] { piazzagaribaldi };
        

        for (int i = 0; i < simulator.getSources().length +2; i++) {
            series.add( new TimeSeries("Series" + (i+1), Minute.class) );
        }

        /// GET USEFUL DATA INTO VARS
        while ( ! when.equals(end) ) {

            for (int i = 0; i < simulator.getSources().length; i++) {

                int flow = simulator.getSources()[i].getValue(when);
                if (flow != -1) {
                    series.get(i).add(when.getMinute(), flow);
                }
                int flow2 = sources[i].getValue(when);
                if (flow2 != -1) {
                    series.get(i+1).add(when.getMinute(), flow2+1);
                }
            }

            when.advance();
        }

        /// DISPLAY STUFF
        TimeSeriesCollection tsc = new TimeSeriesCollection();
        for (TimeSeries s : series)
            tsc.addSeries(s);

        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                "GRAFICO PRESENZE", "Date", "Number", tsc, true, false, false);

        XYPlot plot = (XYPlot)chart.getPlot();
        plot.setBackgroundPaint(new Color(80, 80, 80));
        ValueAxis y = new NumberAxis();
        y.setLowerBound(0);
        y.setUpperBound(60);
        plot.setRangeAxis(y);

        ChartPanel panel = new ChartPanel(chart);
        setContentPane(panel);
    }
}
