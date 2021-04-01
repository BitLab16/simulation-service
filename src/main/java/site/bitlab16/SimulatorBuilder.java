package site.bitlab16;

import jdk.jshell.spi.ExecutionControl;
import site.bitlab16.sources.SimulatedSource;

import java.util.List;

public interface SimulatorBuilder<T> {
    void reset();
    void setSimulatorType(SimulatorType type);
    void setSimulatedSource(SimulatedSource[] sources);
    default void setOutput(T output) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("setOutput non e' definito per questo simulator");
    };
}
