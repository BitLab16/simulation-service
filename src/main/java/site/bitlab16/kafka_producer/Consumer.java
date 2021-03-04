package site.bitlab16.kafka_producer;

import java.util.Properties;
import java.util.concurrent.BlockingDeque;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import site.bitlab16.model.SourceRecord;

public class Consumer implements Runnable, AutoCloseable {

    private static final String TOPIC = "SIMULATOR";

    private final org.apache.kafka.clients.producer.Producer<Long, SourceRecord> kafkaProducer;

    private BlockingDeque<SourceRecord> queue;

    public Consumer(String clientName, String bootstrapServer, BlockingDeque<SourceRecord> queue) {

        this.queue = queue;

        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        properties.put(ProducerConfig.CLIENT_ID_CONFIG, clientName);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        kafkaProducer = new KafkaProducer<Long, SourceRecord>(properties);
    }

    @Override
    public void close() throws Exception {
        kafkaProducer.close();
    }

    @Override
    public void run() {
        try {
            while(true) {
                long timestamp = System.currentTimeMillis();
                SourceRecord sourceRecord = queue.take();
                ProducerRecord<Long, SourceRecord> kafkaRecord = new ProducerRecord<>(TOPIC, timestamp, sourceRecord);
                kafkaProducer.send(kafkaRecord);
            }
        } catch (InterruptedException ex) {
            System.out.println("Consumer interrotto");
        } finally {
            kafkaProducer.flush();
        }
    }
}
