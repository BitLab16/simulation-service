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

public class Graphic extends JFrame implements Runnable {

    private Simulator simulator;

    public Graphic(Simulator simulator, String title) {
        super(title);
        this.simulator = simulator;
    }

    @Override
    public void run() {

        /// INIT VARS
        TimeInstant when = new TimeInstant(new GregorianCalendar(2018, Calendar.JANUARY, 1), 0);
        TimeInstant end = new TimeInstant(new GregorianCalendar(2018, Calendar.JANUARY, 2), 0);
        ArrayList<TimeSeries> series = new ArrayList<TimeSeries>();

        for (int i = 0; i < simulator.getSources().length; i++) {
            series.add( new TimeSeries("Series" + (i+1), Minute.class) );
        }

        /// GET USEFUL DATA INTO VARS
        while ( ! when.equals(end) ) {

            for (int i = 0; i < simulator.getSources().length; i++) {

                int flow = simulator.getSources()[i].getValue(when);
                if (flow != -1) {
                    series.get(i).add(when.getMinute(), flow);
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
        y.setUpperBound(90);
        plot.setRangeAxis(y);

        ChartPanel panel = new ChartPanel(chart);
        setContentPane(panel);
    }
}
