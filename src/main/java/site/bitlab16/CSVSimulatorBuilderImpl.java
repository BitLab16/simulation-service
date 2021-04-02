package site.bitlab16;

import site.bitlab16.sources.SimulatedSource;

import java.util.List;

public class CSVSimulatorBuilderImpl implements SimulatorBuilder<String>{

    private CSVSimulator simulator;

    @Override
    public void reset() {
        this.simulator = new CSVSimulator();
    }

    @Override
    public void setSimulatorType(SimulatorType type) {
        simulator.setSimulatorType(type);
    }

    @Override
    public void setSimulatedSource(SimulatedSource[] sources) {
        simulator.setSources(sources);
    }

    @Override
    public void setOutput(String output) {
        simulator.setOutputFileName(output);
    }

    public CSVSimulator build() {
        return simulator;
    }
}