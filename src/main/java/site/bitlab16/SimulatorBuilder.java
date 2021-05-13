package site.bitlab16;

import site.bitlab16.datasources.BasicSource;

public interface SimulatorBuilder<T> {
    void reset();
    void setSimulatorType(SimulatorType type);
    void setSimulatedSource(BasicSource[] sources);
    default void setOutput(T output) {
        throw new UnsupportedOperationException();
    }
    Simulator build();
}
