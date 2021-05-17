package site.bitlab16;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.bitlab16.datasources.BasicSource;

public class CSVSimulator implements Simulator {

    private static final Logger LOGGER = LoggerFactory.getLogger(CSVSimulator.class);
    private String outputFileName = "data.csv";
    private final BasicSimulator simulator = new BasicSimulator();

    @Override
    public boolean writeOutput() {

        /// INIT VARS
        var when = new TimeInstant(new GregorianCalendar(2018, Calendar.JANUARY, 1), 0);
        var end = new TimeInstant(new GregorianCalendar(2023, Calendar.JANUARY, 1), 0);

        ArrayList<String> outfile = new ArrayList<>();
        outfile.add("ID,timestamp,stagione,meteo,eventi,attivita,festivita," +
                "indicemeteo,indiceattivita,indiceeventi,indiceoragiorno,flow");

        while ( ! when.equals(end) ) {

            for (var i = 0; i < simulator.sources.length; i++) {

                int flow = simulator.sources[i].getValue(when);

                // csv
                String line = simulator.sources[i].getSeed() + ",";
                line += when.toString() + ',';
                final var seasons = new int[]{0,0,0,1,1,1,2,2,2,3,3,3};
                line += seasons[when.getDay().get(Calendar.MONTH)] + ",";
                line += simulator.sources[i].getModifierMeteoAsEnum(when) + ",";
                line += (simulator.sources[i].getEventi(when)==0 ? 0 : 1) + ",";
                line += (simulator.sources[i].getAttivita(when)==0 ? 0 : 1) + ",";
                line += (simulator.sources[i].getFestivita(when)==0 ? 0 : 1) + ",";
                line += simulator.sources[i].getIndiceMeteo() + ",";
                line += simulator.sources[i].getIndiceAttivita() + ",";
                line += simulator.sources[i].getIndiceEventi() + ",";
                line += simulator.sources[i].getIndiceOrario() + ",";
                line += flow;
                outfile.add(line);
            }

            when.advance();
        }
        try (var writer = new FileWriter(outputFileName) ) {
            for(String line: outfile)
                writer.write(line + System.lineSeparator());
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        return true;
    }

    public void setOutputFileName(String outputFileName) {
        this.outputFileName = outputFileName;
    }

    @Override
    public SimulatorType getSimulatorType() {
        return simulator.getSimulatorType();
    }

    @Override
    public void setSimulatorType(SimulatorType type) {
        simulator.setSimulatorType(type);
    }

    @Override
    public BasicSource[] getSources() {
        return simulator.getSources();
    }

    @Override
    public void setSources(BasicSource[] sources) {
        simulator.setSources(sources);
    }

}
