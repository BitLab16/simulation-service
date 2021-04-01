package site.bitlab16;

import site.bitlab16.sources.SimulatedSource;

public class BasicSimulatorBuilderImpl implements SimulatorBuilder<String>{

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
    public void setSimulatedSource(SimulatedSource[] sources) {
        simulator.setSources(sources);
    }

    public BasicSimulator build() {
        return simulator;
    }
}
