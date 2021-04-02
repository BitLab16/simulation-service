package site.bitlab16;

import site.bitlab16.sources.SimulatedSource;

public interface SimulatorBuilder<T> {
    void reset();
    void setSimulatorType(SimulatorType type);
    void setSimulatedSource(SimulatedSource[] sources);
    default void setOutput(T output) throws Exception {
        throw new Exception("setOutput non e' definito per questo simulator");
    };
}
