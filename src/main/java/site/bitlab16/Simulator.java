package site.bitlab16;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import site.bitlab16.model.SourceRecord;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.concurrent.BlockingDeque;

public class Simulator extends JFrame {

    final static long serialVersionUID = 1L;

    BlockingDeque<SourceRecord> outQueue;

    public Simulator(String title, BlockingDeque<SourceRecord> outQueue) {
        super(title);

        this.outQueue = outQueue;

        SimulatedSource s1 = new Source1();

        TimeInstant when = new TimeInstant(new GregorianCalendar(2019, Calendar.JANUARY, 1), 0);

        TimeSeries series = new TimeSeries("Series1", Minute.class);
        TimeSeries series2 = new TimeSeries("Series2", Minute.class);

        int season = 0;
        try (FileWriter out = new FileWriter("data.csv") ) {
            for (int i = 0; i<10000; i++) {

                season = i/2500;
                int num = s1.getValue(when);
                Random random = new Random();
                long offset = Math.round(random.nextGaussian()*3*((num+5)/65.));

                //System.out.println(when + " " + num);
                series.add(when.getMinute(), Math.max(0, num+offset));
                
                series2.add(when.getMinute(), num);

                    
                System.out.println("Sorgente1; " + when.getMinute() + "; " + num);
                out.write("Sorgente1; " + when.getMinute() + "; " + num + "\n");
                outQueue.add(new SourceRecord(1L, when, num, season, false, 0.0F, 0.0F, 0.0F, 0.0F));
                
                when.advance();
                when.advance();
                when.advance();
                when.advance();
                when.advance();
                when.advance();
            }
        } catch(Exception e) {
        }
        
        TimeSeriesCollection tsc = new TimeSeriesCollection();
        tsc.addSeries(series);
        tsc.addSeries(series2);
        
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
