package site.bitlab16;

import site.bitlab16.datasources.BasicSource;

public interface Simulator {

    public SimulatorType getSimulatorType();

    public void setSimulatorType(SimulatorType type);

    public BasicSource[] getSources();

    public void setSources(BasicSource[] sources);

    public void writeOutput();

    
}
