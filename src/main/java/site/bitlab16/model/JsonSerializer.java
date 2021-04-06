package site.bitlab16.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kjetland.jackson.jsonSchema.JsonSchemaGenerator;
import org.apache.kafka.common.serialization.Serializer;
import java.util.Map;

public class JsonSerializer implements Serializer<SourceRecord> {

    private ObjectMapper mapper;
    private JsonSchemaGenerator jsonSchemaGenerator;

    public JsonSerializer(ObjectMapper mapper) {
        this.mapper = mapper;
        this.jsonSchemaGenerator = new JsonSchemaGenerator(this.mapper);
    }

    public JsonSerializer() {
        this(null);
    }

    @Override
    public void configure(Map<String, ?> map, boolean b) {
        this.mapper = new ObjectMapper();
        this.jsonSchemaGenerator = new JsonSchemaGenerator(this.mapper);
    }

    @Override
    public void close() {
        this.mapper = null;
        this.jsonSchemaGenerator = null;
    }

    @Override
    public byte[] serialize(String s, SourceRecord sourceRecord) {
        try {
            JsonNode jsonSchema = jsonSchemaGenerator.generateJsonSchema(SourceRecord.class);

            var a = mapper.writeValueAsBytes(sourceRecord);
            return a;
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }

    }
}
