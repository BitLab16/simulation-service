package site.bitlab16;

import java.awt.Color;
import java.io.FileWriter;
import java.io.IOException;
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

import site.bitlab16.sources.SimulatedSource;
import site.bitlab16.sources.Source1;

public class SimulatorWithGraphs extends JFrame implements Runnable {

    static final long serialVersionUID = 1L;
    
    private SimulatedSource sources[];

    private ArrayList<TimeSeries> series;

    public SimulatorWithGraphs(String title) {
        super(title);
        sources = new SimulatedSource[] { Source1.getInstance() };
        series = new ArrayList<>();
    }
    
    @Override
    public void run() {

        /// INIT VARS
        TimeInstant when = new TimeInstant(new GregorianCalendar(2018, Calendar.JANUARY, 1), 0);
        TimeInstant end = new TimeInstant(new GregorianCalendar(2023, Calendar.JANUARY, 1), 0);
        for (int i = 0; i < sources.length; i++) {
            series.add( new TimeSeries("Series" + (i+1) + "_1", Minute.class) );
        }
        
        ArrayList<String> outfile = new ArrayList<>();
        outfile.add("ID,timestamp,stagione,meteo,eventi,attivita,festivita," +
            "indicemeteo,indiceattivita,indiceeventi,indiceoragiorno,flow");
    

        /// GET USEFUL DATA INTO VARS
        while ( ! when.equals(end) ) {

            for (int i = 0; i < sources.length; i++) {

                int flow = sources[i].getValue(when);
                if (flow != -1) {
                    series.get(i).add(when.getMinute(), flow);
                }
                
                // csv
                String line = sources[i].getSeed() + ",";
                line += when.toString() + ',';
                final int[] seasons = new int[]{0,0,0,1,1,1,2,2,2,3,3,3};
                line += seasons[when.getDay().get(Calendar.MONTH)] + ",";
                float dataMeteo = sources[i].getModifierMeteo(when);
                int meteoAsEnum;
                if (dataMeteo < 0.05) meteoAsEnum = 0;
                else if(dataMeteo < 0.13) meteoAsEnum = 1;
                else if(dataMeteo < 0.2) meteoAsEnum = 2;
                else if(dataMeteo < 0.3) meteoAsEnum = 3;
                else if(dataMeteo < 0.5) meteoAsEnum = 4;
                else meteoAsEnum = 5;
                line += meteoAsEnum + ",";
                line += (sources[i].getEventi(when)==0 ? 0 : 1) + ",";
                line += (sources[i].getAttivita(when)==0 ? 0 : 1) + ",";
                line += (sources[i].getFestivita(when)==0 ? 0 : 1) + ",";
                line += 3 + ",";
                line += 3 + ",";
                line += 4 + ",";
                line += 2 + ",";
                line += flow;
                outfile.add(line);
            }
            
            when.advance();
        }

        try (FileWriter writer = new FileWriter("data.csv") ) {
            for(String line: outfile)
                writer.write(line + System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
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
