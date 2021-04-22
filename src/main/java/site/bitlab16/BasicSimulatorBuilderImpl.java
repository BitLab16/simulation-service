package site.bitlab16;

import site.bitlab16.datasources.BasicSource;

public class BasicSimulatorBuilderImpl implements SimulatorBuilder<String> {

    private BasicSimulator simulator;

    @Override
    public void reset() {
        simulator = new BasicSimulator();
    }

    @Override
    public void setSimulatorType(SimulatorType type) {
        simulator.setSimulatorType(type);
    }

    @Override
    public void setSimulatedSource(BasicSource[] sources) {
        simulator.setSources(sources);
    }

    @Override
    public BasicSimulator build() {
        return simulator;
    }
}
