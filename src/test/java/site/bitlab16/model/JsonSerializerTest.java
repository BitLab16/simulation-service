package site.bitlab16.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.sql.Timestamp;

import com.fasterxml.jackson.databind.ObjectMapper;

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
        SourceRecord record = new SourceRecord(1L, 2, timestamp, 4, 5, false,
                new Indexes(6, 7, 8, 9));
        byte[] output = serializer.serialize("", record);
        assertTrue(new String(output).contains("\"detectionTime\":"));
        assertTrue(new String(output).contains("\"isHoliday\":false"));
        assertTrue(new String(output).contains("\"weatherIndex\":7.0"));
    }

    @Test
    public void constructor2Test() throws NoSuchFieldException, IllegalAccessException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectMapper mapper2;
        serializer = new JsonSerializer(mapper);

        Field field = JsonSerializer.class.getDeclaredField("mapper");
        field.setAccessible(true);
        mapper2 = (ObjectMapper)field.get(serializer);
        
        assertEquals(mapper, mapper2);
    }

    @Test
    public void counfigureTest() throws NoSuchFieldException, IllegalAccessException {
        serializer.configure(null, false);
        
        Field field = JsonSerializer.class.getDeclaredField("mapper");
        field.setAccessible(true);
        ObjectMapper mapper = (ObjectMapper)field.get(serializer);

        assertNotNull(mapper);
    }

    @Test
    public void closeTest() throws NoSuchFieldException, IllegalAccessException {
        serializer.close();

        Field field = JsonSerializer.class.getDeclaredField("mapper");
        field.setAccessible(true);
        ObjectMapper mapper = (ObjectMapper)field.get(serializer);
        
        assertNull(mapper);
    }

}
