package site.bitlab16;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import site.bitlab16.datasources.BasicSource;
import site.bitlab16.datasources.profiles.CityBuildingProfile;
import site.bitlab16.datasources.weekly_data.WeeklyRawData;
public class CSVSimulatorTest {

	private CSVSimulator simulator;

	@Before
	public void setup() {
		this.simulator = new CSVSimulator();
	}

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

	@Test
	public void writeOutputTest() throws IOException {

        File filename;
        filename = folder.newFile();
        this.simulator.setOutputFileName(filename.getPath());

		BasicSource paolotti = new CityBuildingProfile(1, .2f, 1f, 1.8f, 3f, 1.1f, new WeeklyRawData());
		simulator.setSources(new BasicSource[]{paolotti});
		simulator.writeOutput();

        long count = Files.lines(filename.toPath()).count();
        assertTrue( count > 1000 && count < 10000000);
		
        // 420768 ==> i dati del paolotti

		//TODO: come testare che i dati siano anche corretti?
		// non eccede lo scopo dello unit testing?
	}


}
