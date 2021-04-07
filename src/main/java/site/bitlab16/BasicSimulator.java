package site.bitlab16;

import site.bitlab16.sources.SimulatedSource;

public class BasicSimulator{

    protected SimulatorType type;

    protected SimulatedSource sources[];

    public BasicSimulator(){}

    public SimulatorType getSimulatorType() {
        return type;
    }

    public void setSimulatorType(SimulatorType type) {
        this.type = type;
    }

    public SimulatedSource[] getSources() {
        return sources;
    }

    public void setSources(SimulatedSource[] sources) {
        this.sources = sources;
    }

    public void writeOutput() {}

}
