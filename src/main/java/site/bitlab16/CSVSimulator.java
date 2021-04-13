package site.bitlab16;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class CSVSimulator extends BasicSimulator {

    String outputFileName = "data.csv";

    @Override
    public void writeOutput() {

        /// INIT VARS
        TimeInstant when = new TimeInstant(new GregorianCalendar(2018, Calendar.JANUARY, 1), 0);
        TimeInstant end = new TimeInstant(new GregorianCalendar(2023, Calendar.JANUARY, 1), 0);

        ArrayList<String> outfile = new ArrayList<>();
        outfile.add("ID,timestamp,stagione,meteo,eventi,attivita,festivita," +
                "indicemeteo,indiceattivita,indiceeventi,indiceoragiorno,flow");

        while ( ! when.equals(end) ) {

            for (int i = 0; i < sources.length; i++) {

                int flow = sources[i].getValue(when);

                // csv
                String line = sources[i].getSeed() + ",";
                line += when.toString() + ',';
                final int[] seasons = new int[]{0,0,0,1,1,1,2,2,2,3,3,3};
                line += seasons[when.getDay().get(Calendar.MONTH)] + ",";
                line += sources[i].getModifierMeteoAsEnum(when) + ",";
                line += (sources[i].getEventi(when)==0 ? 0 : 1) + ",";
                line += (sources[i].getAttivita(when)==0 ? 0 : 1) + ",";
                line += (sources[i].getFestivita(when)==0 ? 0 : 1) + ",";
                line += sources[i].getIndiceMeteo() + ",";
                line += sources[i].getIndiceAttivita() + ",";
                line += sources[i].getIndiceEventi() + ",";
                line += sources[i].getIndiceOrario() + ",";
                line += flow;
                outfile.add(line);
            }

            when.advance();
        }
        try (FileWriter writer = new FileWriter(outputFileName) ) {
            for(String line: outfile)
                writer.write(line + System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getOutputFileName() {
        return outputFileName;
    }

    public void setOutputFileName(String outputFileName) {
        this.outputFileName = outputFileName;
    }

}
