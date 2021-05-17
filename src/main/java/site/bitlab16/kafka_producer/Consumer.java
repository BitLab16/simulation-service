package site.bitlab16.kafka_producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.LongSerializer;
import site.bitlab16.model.JsonSerializer;
import site.bitlab16.model.SourceRecord;

import java.util.Properties;
import java.util.concurrent.BlockingDeque;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Consumer implements Runnable, AutoCloseable {

    private static final Logger LOGGER = LoggerFactory.getLogger(Consumer.class);

    private static final String TOPIC = "SIMULATOR-SOURCE_";

    private final org.apache.kafka.clients.producer.Producer<Long, SourceRecord> kafkaProducer;

    private final BlockingDeque<SourceRecord> queue;

    private boolean stopCondition = false;

    public Consumer(String clientName, String bootstrapServer, BlockingDeque<SourceRecord> queue) {

        this.queue = queue;

        var properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        properties.put(ProducerConfig.CLIENT_ID_CONFIG, clientName);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName());

        kafkaProducer = new KafkaProducer<>(properties);
    }

    public void setStopCondition(boolean stopCondition) {
        this.stopCondition = stopCondition;
    }

    @Override
    public void close() {
        kafkaProducer.close();
    }

    @Override
    public void run() {
        try {
            while(!stopCondition) {
                long millisecond = System.currentTimeMillis();
                var sourceRecord = queue.take();
                ProducerRecord<Long, SourceRecord> kafkaRecord = new ProducerRecord<>(TOPIC+sourceRecord.getPoint(), millisecond, sourceRecord);
                kafkaProducer.send(kafkaRecord);
            }
        } catch (InterruptedException ex) {
            kafkaProducer.flush();
            close();
            LOGGER.error("Thread interrotto, errore: {}", ex.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
