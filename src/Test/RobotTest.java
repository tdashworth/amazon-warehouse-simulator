package Test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;

import org.junit.Test;

import model.Actor;
import model.ChargingPod;
import model.Entity;
import model.Floor;
import model.Location;
import model.LocationNotValidException;
import model.Order;
import model.PackingStation;
import model.Robot;
import model.Robot.RobotStatus;
import model.StorageShelf;
import model.Warehouse;
import simulation.SimFileFormatException;
import simulation.Simulator;

public class RobotTest {

	@Test
	public void createRobotTest() {
		//Set up and create a new robot, ready for testing
		Location l = new Location(2, 2); 
		ChargingPod cp = new ChargingPod("1", l); 
		Robot r = new Robot("2", l, cp, 8);
				
		//Test to ensure that the robot was created in the way we wanted it to be
		assertEquals(l, r.getLocation());
		assertEquals("2", r.getUID());
		assertEquals(8, r.getPowerUnits());
		assertEquals(false, r.hasItem());
		assertEquals(null, r.getPreviousLocation());
		assertEquals("Robot:2- Status: Charging- Power: 8- StorageShelf: null- Packing Station: null Location: 2, 2 ", r.toString());
	}
	
	@Test
	public void chargeTest() {
		//Set up and create a new robot, ready for testing
		Location l = new Location(2, 2); 
		ChargingPod cp = new ChargingPod("1", l); 
		Robot r = new Robot("2", l, cp, 8);
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
		Floor floor = new Floor(3, 3);
		HashMap<String, Entity> entities = new HashMap<String, Entity>();
		Deque<Order> orders = new LinkedList<Order>();
		
		//Set up the basic simulator variables
		int chargeSpeed = 1;
		int capacity = 20;
		
		//Create an order for the simulation
		ArrayList<String> sids = new ArrayList<String>();
		sids.add("ss0");
		orders.add(new Order(sids, 10));
		
		//Add one of each entity to the entities list
		Robot r = new Robot("r0", new Location(0,0), new ChargingPod("c0", new Location(0,0)), 2);
		entities.put(r.getUID(), r);
		
		StorageShelf ss = new StorageShelf("ss0", new Location(1,0));
		entities.put(ss.getUID(), ss);
		
		PackingStation ps = new PackingStation("ps0", new Location(2,0));
		entities.put(ps.getUID(), ps);
		
		//Create the simulator
		Simulator s = null;
		try {
			s = new Simulator(floor, capacity, chargeSpeed, entities, orders);
		} catch (LocationNotValidException e) {
			e.printStackTrace();
		}
		
		//Get the storage shelf and packing station for the robot to assess
		PackingStation packingStation = (PackingStation) s.getActors().get(0);
		Robot robot = (Robot) s.getActors().get(1);
		StorageShelf storageShelf = (StorageShelf) s.getActors().get(2);
		Warehouse warehouse = s.getWarehouse();
		
		//Test to ensure that the acceptJob method is working successfully
		boolean test = true;
		try {
			test = robot.acceptJob(storageShelf, packingStation, warehouse);
		} catch (LocationNotValidException e) {
			e.printStackTrace();
		}
		assertFalse(test);
		
		//Ensure the robot has just enough charge to accept the job, and test the accept method again
		((Robot) s.getActors().get(1)).charge(4, 20);
		test = true;
		try {
			test = robot.acceptJob(storageShelf, packingStation, warehouse);
		} catch (LocationNotValidException e) {
			e.printStackTrace();
		}
		assertTrue(test);
		
		//Ensure that acceptJob fails, as the robot already has a storage shelf
		test = true;
		try {
			test = robot.acceptJob(storageShelf, packingStation, warehouse);
		} catch (LocationNotValidException e) {
			e.printStackTrace();
		}
		assertFalse(test);
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
		Robot r = new Robot("2", l, cp, 8);
		
		//Test tick when robot needs to charge and is at charging station
		
		//Test tick when robot needs to charge and is not at charging station

		//Test tick when robot needs to visit storage shelf

		//Test tick when robot needs to visit packing station
		fail("Not yet implemented");
	}

}
