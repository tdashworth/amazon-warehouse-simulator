package Test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Paths;
import org.junit.Test;
import model.ChargingPod;
import model.Location;
import model.LocationNotValidException;
import model.PackingStation;
import model.Robot;
import model.StorageShelf;
import simulation.SimFileFormatException;
import simulation.Simulator;

public class RobotTest {

	@Test
	public void createRobotTest() {
		//Set up and create a new robot, ready for testing
		Location l = new Location(2, 2); 
		ChargingPod cp = new ChargingPod("1", l); 
		String robotUID = "2";
		int robotCharge = 8;
		Robot r = new Robot(robotUID, l, cp, robotCharge);
				
		//Test to ensure that the robot was created in the way we wanted it to be
		assertEquals(l, r.getLocation());
		assertEquals(robotUID, r.getUID());
		assertEquals(robotCharge, r.getPowerUnits());
		assertEquals(false, r.hasItem());
		assertEquals(null, r.getPreviousLocation());
		assertEquals("STRING", r.toString());
	}
	
	@Test
	public void chargeTest() {
		//Set up and create a new robot, ready for testing
		Location l = new Location(2, 2); 
		ChargingPod cp = new ChargingPod("1", l); 
		String robotUID = "2";
		int robotCharge = 8;
		Robot r = new Robot(robotUID, l, cp, robotCharge);
		int maxCharge = 20;
		int chargeSpeed = 1;
				
		//Call charge on the robot when it's battery is less than 50%
		assertEquals(8, r.getPowerUnits());
		r.charge(chargeSpeed, maxCharge);
		assertEquals(9, r.getPowerUnits());
		r.charge(chargeSpeed, maxCharge);
		assertEquals(10, r.getPowerUnits());
		
		//Test that the robots full charge works
		r.setPowerUnits(20);
		r.charge(chargeSpeed, maxCharge);
		assertEquals(20, r.getPowerUnits());
		r.charge(chargeSpeed, maxCharge);
		assertEquals(20, r.getPowerUnits()); 
	}
	
	@Test
	public void acceptJobTest() {
		//Create a new simulator and warehouse from file so that the robot can assess jobs
		Simulator s;
		try {
			s = Simulator.createFromFile(Paths.get("./sample-configs/oneOfEverything.sim"));
		} catch (IOException | SimFileFormatException | LocationNotValidException e) {
			System.out.println("Testing Error reading SIM file - " + e.toString());
			System.exit(1);
		} catch (Exception e) {
			System.out.println("Testing Error running simulation - " + e.toString());
			e.printStackTrace();
			System.exit(1);
		}
		
		//Get the storage shelf and packing station for the robot to assess
		StorageShelf storageShelf = (StorageShelf) s.getActors().get(0);
		PackingStation packingStation = (PackingStation) s.getActors().get(0);
		s.getRobots().get(0).acceptJob(storageShelf, packingStation, warehouse);
		
		
		
		fail("Not yet implemented");
	}
	
	@Test
	public void moveTest() {
		fail("Not yet implemented");
		
	}
	
	@Test
	public void pathFindingTest() {
		fail("Not yet implemented");
	}
	
	@Test
	public void tickTest() {
		//Set up and create a new robot, ready for testing
		Location l = new Location(2, 2); 
		ChargingPod cp = new ChargingPod("1", l); 
		String robotUID = "2";
		int robotCharge = 8;
		Robot r = new Robot(robotUID, l, cp, robotCharge);
		
		//Test tick when robot needs to charge and is at charging station
		
		//Test tick when robot needs to charge and is not at charging station

		//Test tick when robot needs to visit storage shelf

		//Test tick when robot needs to visit packing station
		fail("Not yet implemented");
	}

}
