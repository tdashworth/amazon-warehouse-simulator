package Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;

import org.junit.Test;

import model.ChargingPod;
import model.Entity;
import model.Floor;
import model.Location;
import model.LocationNotValidException;
import model.Order;
import model.PackingStation;
import model.PathFindingStrategy;
import model.Robot;
import model.Robot.RobotStatus;
import model.StorageShelf;
import model.Warehouse;
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
		assertEquals("Robot(2) - Location: [2, 2], Status: Charging, Power: 8", r.toString());
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
		assertEquals(RobotStatus.Charging, r.getStatus());
	}

	/**
	 * Test the functionality of accept job, and tick
	 * @throws Exception
	 */
	@Test
	public void robotTest() throws Exception {
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

		StorageShelf ss = new StorageShelf("ss0", new Location(2,0));
		entities.put(ss.getUID(), ss);

		PackingStation ps = new PackingStation("ps0", new Location(2,1));
		entities.put(ps.getUID(), ps);

		//Create the simulator
		Simulator s = null;
		try {
			s = new Simulator(floor, capacity, chargeSpeed, entities, orders);
		} catch (LocationNotValidException e) {
			e.printStackTrace();
		}

		//Get the storage shelf and packing station for the robot to assess
		Warehouse warehouse = s.getWarehouse();

		//Test to ensure that the acceptJob method is working successfully, robot won't accept job without enough power units
		boolean test = true;
		try {
			test = r.acceptJob(ss, ps, warehouse);
		} catch (LocationNotValidException e) {
			e.printStackTrace();
		}
		assertFalse(test);
		assertEquals(null, r.getShelf());

		//Ensure the robot has enough charge to accept the job, and test the accept method again
		r.charge(8, 20);
		test = true;
		try {
			test = r.acceptJob(ss, ps, warehouse);
		} catch (LocationNotValidException e) {
			e.printStackTrace();
		}
		assertTrue(test);
		assertEquals(ss, r.getShelf());

		//Ensure that acceptJob fails, as the robot already has a storage shelf
		test = true;
		try {
			test = r.acceptJob(ss, ps, warehouse);
		} catch (LocationNotValidException e) {
			e.printStackTrace();
		}
		assertFalse(test);
		assertEquals(ss, r.getShelf());
		
		//Test tick when robot has accepted job and needs to collect item (from two spaces away so after two ticks the robot should have the item).
		r.setPowerUnits(20);
		r.tick(warehouse);
		assertEquals(RobotStatus.CollectingItem, r.getStatus());
		assertNotEquals("Location: 0,0", r.getLocation().toString());
		assertEquals(19, r.getPowerUnits());
		assertEquals(false, r.hasItem());
		r.tick(warehouse);
		assertEquals(true, r.hasItem());
		
		//check that robot returns the item to the packing station when it has the item.
		r.tick(warehouse);
		assertEquals(RobotStatus.ReturningItem, r.getStatus());
		assertEquals(null,r.getShelf());
		assertEquals(16, r.getPowerUnits());
		assertEquals(ps.getLocation().toString(), r.getLocation().toString());
		assertEquals(false, r.hasItem());
		
	}


	@Test
	public void tickTest() throws Exception {	

		Floor floor = new Floor(3, 3);
		HashMap<String, Entity> entities = new HashMap<String, Entity>();
		Deque<Order> orders = new LinkedList<Order>();
		PathFindingStrategy pf = new PathFindingStrategy(floor, true);

		//Set up the basic simulator variables
		int chargeSpeed = 1;
		int capacity = 20;
		Location l = new Location(0,0);

		//Create an order for the simulation
		ArrayList<String> sids = new ArrayList<String>();
		sids.add("ss0");
		orders.add(new Order(sids, 10));

		//Add one of each entity to the entities list
		Robot r = new Robot("r0", l, new ChargingPod("c0", l), 2);
		entities.put(r.getUID(), r);
		r.setPathFinder(pf);

		//Create the simulator
		Simulator s = null;
		s = new Simulator(floor, capacity, chargeSpeed, entities, orders);

		Warehouse warehouse = s.getWarehouse();

		//Test tick when robot needs to charge and is at charging station

		r.setPowerUnits(2);
		try {
			r.tick(warehouse);
		} catch (Exception e) {
			e.printStackTrace();
		}		
		assertEquals(3, r.getPowerUnits());
		assertEquals(l, r.getLocation());
		assertEquals(RobotStatus.Charging, r.getStatus());
			
	}

}
