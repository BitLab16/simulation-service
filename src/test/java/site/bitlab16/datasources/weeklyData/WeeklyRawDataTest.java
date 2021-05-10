package site.bitlab16.datasources.weeklyData;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
public class WeeklyRawDataTest {

	private WeeklyRawData wrd;

	@Before
	public void setup() {
		this.wrd = new WeeklyRawData();
	}

	@Test
	public void constructorTest() {
        assertNotNull(wrd);
        assertEquals(wrd.size(), 100);
        int avg = 0;
        for (int i = 0; i < wrd.size(); i++)
            avg += Arrays.stream(wrd.get(i).getWeek()).sum() / wrd.size();
        avg /= wrd.size();
        assertEquals(383, avg);
	}

    @Test
    public void getFlowTest() throws Exception {
        Method getflowptr = WeeklyRawData.class.getDeclaredMethod("getFlow", String[].class, int.class );
        getflowptr.setAccessible(true);
        
        int flow1 = (int)getflowptr.invoke(wrd, new String[] {"0,0.310858"}, 0);
        int flow2 = (int)getflowptr.invoke(wrd, new String[] {"18,3.42372"}, 0);

        assertEquals(11, flow1);
        assertEquals(40, flow2);

    }
}
