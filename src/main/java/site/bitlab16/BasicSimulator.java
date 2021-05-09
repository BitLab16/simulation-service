package site.bitlab16;

import site.bitlab16.datasources.BasicSource;

public class BasicSimulator implements Simulator {

    protected SimulatorType type;

    protected BasicSource sources[];

    public BasicSimulator() {}

    public SimulatorType getSimulatorType() {
        return type;
    }

    public void setSimulatorType(SimulatorType type) {
        this.type = type;
    }

    public BasicSource[] getSources() {
        return sources;
    }

    public void setSources(BasicSource[] sources) {
        this.sources = sources;
    }

    public void writeOutput() {}

}
