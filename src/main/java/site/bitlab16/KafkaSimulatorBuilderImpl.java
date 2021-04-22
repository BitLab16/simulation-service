package site.bitlab16;

import java.util.concurrent.BlockingDeque;

import site.bitlab16.datasources.BasicSource;
import site.bitlab16.model.SourceRecord;

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
    public void setSimulatedSource(BasicSource[] sources) {
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
