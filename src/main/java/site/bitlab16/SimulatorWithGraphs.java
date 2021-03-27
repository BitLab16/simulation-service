package site.bitlab16;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

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

import site.bitlab16.sources.SimulatedSource;
import site.bitlab16.sources.Source1;

public class SimulatorWithGraphs extends JFrame implements Runnable {

    final static long serialVersionUID = 1L;
    
    private SimulatedSource sources[];

    private ArrayList<TimeSeries> series;

    public SimulatorWithGraphs(String title) {
        super(title);

        sources = new SimulatedSource[] { new Source1() };
        series = new ArrayList<>();
    }
    
    @Override
    public void run() {

        /// INIT VARS
        TimeInstant when = new TimeInstant(new GregorianCalendar(2019, Calendar.JANUARY, 1), 0);
        TimeInstant end = new TimeInstant(new GregorianCalendar(2020, Calendar.OCTOBER, 15), 0);
        for (int i = 0; i < sources.length; i++) {
            series.add( new TimeSeries("Series" + (i+1) + "_1", Minute.class) );
        }
        Random random = new Random();
        
        /// GET USEFUL DATA INTO VARS
        while ( ! when.equals(end) ) {

            for (int i = 0; i < sources.length; i++) {

                int num = sources[i].getValue(when);
                if (num != -1) {
                    series.get(i).add(when.getMinute(), Math.max(0, num));
                }

            }
            
            when.advance();
        }
        

        /// DISPLAY STUFF
        TimeSeriesCollection tsc = new TimeSeriesCollection();
        for (TimeSeries s : series)
            tsc.addSeries(s);
        
        JFreeChart chart = ChartFactory.createTimeSeriesChart(
            "GRAFICO PRESENZE", "Date", "Number", tsc, false, false, false);  
    
        XYPlot plot = (XYPlot)chart.getPlot();
        plot.setBackgroundPaint(new Color(255,255, 127));
        ValueAxis y = new NumberAxis();
        y.setLowerBound(0);
        y.setUpperBound(60);
        plot.setRangeAxis(y);
        
        ChartPanel panel = new ChartPanel(chart);
        setContentPane(panel);
      }  


}
