package site.bitlab16;

import site.bitlab16.datasources.BasicSource;

public interface Simulator {

    SimulatorType getSimulatorType();

    void setSimulatorType(SimulatorType type);

    BasicSource[] getSources();

    void setSources(BasicSource[] sources);

    boolean writeOutput();

    
}
