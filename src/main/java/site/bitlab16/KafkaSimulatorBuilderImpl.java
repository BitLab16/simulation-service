package site.bitlab16;

import site.bitlab16.model.SourceRecord;
import site.bitlab16.sources.SimulatedSource;

import java.util.concurrent.BlockingDeque;

public class KafkaSimulatorBuilderImpl implements SimulatorBuilder<BlockingDeque<SourceRecord>>{

    private KafkaSimulator kafkaSimulator;

    @Override
    public void reset() {
        kafkaSimulator = new KafkaSimulator();
    }

    @Override
    public void setSimulatorType(SimulatorType type) {
        kafkaSimulator.setSimulatorType(type);
    }

    @Override
    public void setSimulatedSource(SimulatedSource[] sources) {
        kafkaSimulator.setSources(sources);
    }

    @Override
    public void setOutput(BlockingDeque<SourceRecord> output) {
        kafkaSimulator.setOutQueue(output);
    }

    @Override
    public KafkaSimulator build() {
        return kafkaSimulator;
    }
}
