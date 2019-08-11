package test.simulation;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Paths;

import org.junit.Test;

import main.model.Location;
import main.model.LocationNotValidException;
import main.simulation.SimFileFormatException;
import main.simulation.Simulator;
import main.simulation.SimulatorFileReader_V1;

public class SimulatorFileReader_V1Test {

	@Test
	public void fileReaderV1ReadTest() {
		//Create a new file reader and simulator to store the file reader result
		SimulatorFileReader_V1 fr = new SimulatorFileReader_V1();
		Simulator s = null;
		
		//Attempt to read from the file
		try {
			s = fr.read(Paths.get("./sample-configs/oneOfEverything.sim"));
		} catch (IOException | SimFileFormatException | LocationNotValidException e) {
			System.out.println("Testing Error reading SIM file - " + e.toString());
			System.exit(1);
		} catch (Exception e) {
			System.out.println("Testing Error running simulation - " + e.toString());
			e.printStackTrace();
			System.exit(1);
		}
		
		//Ensure that the result simulation matches the file that was read in
		assertEquals(0, s.getTotalTickCount());
		assertEquals(false, s.isComplete());
		assertEquals(4, s.getWarehouse().getEntities().size());
		//Robot tests
		assertEquals(1, s.getRobots().size());
		assertTrue(new Location(2, 0).equals(s.getRobots().get(0).getLocation()));
		assertEquals(20, s.getRobots().get(0).getPowerUnits());
		assertEquals("r0", s.getRobots().get(0).getUID());
		//Floor tests
		assertEquals(3, s.getWarehouse().getFloor().getNumberOfColumns());
		assertEquals(3, s.getWarehouse().getFloor().getNumberOfRows());
		assertEquals("Floor - Size: 3 columns by 3 rows.", s.getWarehouse().getFloor().toString());	
	}
	
	@Test
	public void readInvalidConfigFileTest() {
		// fail("Not yet implemented");
	}

}
