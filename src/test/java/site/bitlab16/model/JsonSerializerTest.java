package site.bitlab16.model;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Timestamp;

import org.junit.Before;
import org.junit.Test;
public class JsonSerializerTest {

    JsonSerializer serializer;
    
	@Before
	public void setup() {
        this.serializer = new JsonSerializer();
        this.serializer.configure(null, false);
	}

    @Test
    public void constructorTest() {
        assertNotNull(serializer);
    }

    @Test
    public void serializeTest() {
        Timestamp timestamp = new Timestamp(2000, 2, 3, 11, 30, 00, 00);
        SourceRecord record = new SourceRecord(1L, 2, timestamp, 4, 5, false, 6, 7, 8, 9);
        byte[] output = serializer.serialize("", record);
        assertTrue(new String(output).contains("\"detectionTime\":60910223400000"));
        assertTrue(new String(output).contains("\"isHoliday\":false"));
        assertTrue(new String(output).contains("\"weatherIndex\":7.0"));
    }

}
