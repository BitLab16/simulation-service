package site.bitlab16.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Timestamp;

import org.junit.Before;
import org.junit.Test;
public class SourceRecordTest {

    SourceRecord record;
    Timestamp timestamp;

	@Before
	public void setup() {
        timestamp = new Timestamp(2000, 2, 3, 11, 30, 00, 00);
        record = new SourceRecord(1L, 2, timestamp, 4, 5, false, 6, 7, 8, 9);
	}

    @Test
    public void constructorTest() {
        assertNotNull(record);
    }

    @Test
    public void getDetectionTimeTest() {
        assertEquals(timestamp, record.getDetectionTime());
    }
}
