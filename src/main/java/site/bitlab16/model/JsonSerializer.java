package site.bitlab16.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;
import java.util.Map;

public class JsonSerializer implements Serializer<SourceRecord> {

    private ObjectMapper mapper;
    
    public JsonSerializer() {
        this(null);
    }

    public JsonSerializer(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void configure(Map<String, ?> map, boolean b) {
        this.mapper = new ObjectMapper();
    }

    @Override
    public void close() {
        this.mapper = null;
    }

    @Override
    public byte[] serialize(String s, SourceRecord sourceRecord) {
        try {
            return mapper.writeValueAsBytes(sourceRecord);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }

    }
}
