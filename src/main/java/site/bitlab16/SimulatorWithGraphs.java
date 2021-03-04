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
import org.jfree.chart.axis.DateAxis;
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
        TimeInstant end = new TimeInstant(new GregorianCalendar(2019, Calendar.JANUARY, 2), 0);
        for (int i = 0; i < sources.length; i++) {
            series.add( new TimeSeries("Series" + (i+1) + "_1", Minute.class) );
            series.add( new TimeSeries("Series" + (i+1) + "_2", Minute.class) );
        }
        Random random = new Random();
        
        /// GET USEFUL DATA INTO VARS
        while ( ! when.equals(end) ) {

            for (int i = 0; i < sources.length; i++) {

                if (sources[i].shouldPublish(when)) {

                    int num = sources[i].getValue(when);
                    int offset = (int)Math.round(random.nextGaussian()*3*((num+5)/65.));

                    series.get(i*2).add(when.getMinute(), Math.max(0, num));
                    series.get(i*2+1).add(when.getMinute(), Math.max(0, num+offset));
                    System.out.println("Sorgente" + (i+1) + "; " + when.getMinute() + "; " + num + "; " + (num+offset));
                }

                when.advance();
            }
        }
        

        /// DISPLAY STUFF
        TimeSeriesCollection tsc = new TimeSeriesCollection();
        for (TimeSeries s : series)
            tsc.addSeries(s);
        
        JFreeChart chart = ChartFactory.createTimeSeriesChart(
            "GRAFICO PRESENZE", "Date", "Number", tsc, false, false, false);  
    
        XYPlot plot = (XYPlot)chart.getPlot();
        plot.setBackgroundPaint(new Color(255,255, 127));
        DateAxis y = new DateAxis();
        y.setLowerBound(0);
        y.setUpperBound(60);
        plot.setRangeAxis(y);
        
        ChartPanel panel = new ChartPanel(chart);
        setContentPane(panel);
      }  


}
