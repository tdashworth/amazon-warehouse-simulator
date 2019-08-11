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
import model.Robot;
import model.StorageShelf;
import simulation.Simulator;

public class SimulatorTest {

	@Test
	public void createSimulatorTest() {
		// Set up the basic variables for a new Simulator
		Floor floor = new Floor(2, 2);
		HashMap<String, Entity> entities = new HashMap<String, Entity>();
		Deque<Order> orders = new LinkedList<Order>();
		int chargeSpeed = 1;
		int capacity = 1;

		// Create the new simulator, based on the above variables
		Simulator s = null;
		try {
			s = new Simulator(floor, capacity, chargeSpeed, entities, orders);
		} catch (LocationNotValidException e) {
			e.printStackTrace();
		}

		// Test to ensure that the simulator was set up correctly
		assertEquals(0, s.getTotalTickCount());
		assertEquals(1, s.getChargeSpeed());
		assertEquals(1, s.getMaxChargeCapacity());
		assertEquals(true, s.isComplete());
		assertEquals(0, s.getWarehouse().getEntities().size());
		assertEquals(0, s.getRobots().size());
		assertEquals(2, s.getWarehouse().getFloor().getNumberOfColumns());
		assertEquals(2, s.getWarehouse().getFloor().getNumberOfRows());
		assertEquals("Floor: - Size: 2 rows by 2 columns.", s.getWarehouse().getFloor().toString());

		// Modify the capacity variable, and add an order, robot and charging pod.
		capacity = 20;

		ArrayList<String> sids = new ArrayList<String>();
		sids.add("ss0");
		orders.add(new Order(sids, 10));

		Robot r = new Robot("r0", new Location(0, 0), new ChargingPod("c0", new Location(0, 0)), 2);
		entities.put(r.getUID(), r);

		// Create a new simulator with the updated variables
		try {
			s = new Simulator(floor, capacity, chargeSpeed, entities, orders);
		} catch (LocationNotValidException e) {
			e.printStackTrace();
		}

		// Ensure that the new simulator was set up correctly
		assertEquals(0, s.getTotalTickCount());
		assertEquals(1, s.getChargeSpeed());
		assertEquals(20, s.getMaxChargeCapacity());
		assertEquals(false, s.isComplete());
		assertEquals(1, s.getWarehouse().getEntities().size());
		// Robot tests
		assertEquals(1, s.getRobots().size());
		assertTrue(new Location(0, 0).equals(s.getRobots().get(0).getLocation()));
		assertEquals(2, s.getRobots().get(0).getPowerUnits());
		assertEquals("r0", s.getRobots().get(0).getUID());
		// Floor tests
		assertEquals(2, s.getWarehouse().getFloor().getNumberOfColumns());
		assertEquals(2, s.getWarehouse().getFloor().getNumberOfRows());
		assertEquals("Floor: - Size: 2 rows by 2 columns.", s.getWarehouse().getFloor().toString());
	}

	@Test
	public void runSimulatorTest() {
		// Create the simulator basics (a place to store the floor, entities and orders)
		Floor floor = new Floor(3, 3);
		HashMap<String, Entity> entities = new HashMap<String, Entity>();
		Deque<Order> orders = new LinkedList<Order>();

		// Set up the basic simulator variables
		int chargeSpeed = 1;
		int capacity = 20;

		// Create an order for the simulation
		ArrayList<String> sids = new ArrayList<String>();
		sids.add("ss0");
		orders.add(new Order(sids, 10));

		// Add one of each entity to the entities list
		Robot r = new Robot("r0", new Location(0, 0), new ChargingPod("c0", new Location(0, 0)), 2);
		entities.put(r.getUID(), r);

		StorageShelf ss = new StorageShelf("ss0", new Location(1, 0));
		entities.put(ss.getUID(), ss);

		PackingStation ps = new PackingStation("ps0", new Location(2, 0));
		entities.put(ps.getUID(), ps);

		// Create the simulator
		Simulator s = null;
		try {
			s = new Simulator(floor, capacity, chargeSpeed, entities, orders);
		} catch (LocationNotValidException e) {
			e.printStackTrace();
		}

		// Run the simulator, then test to ensure that the simulator completed
		// successfully
		try {
			s.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertTrue(s.isComplete());
		assertTrue(s.getWarehouse().areAllOrdersDispatched());
		assertNull(s.getWarehouse().getUnassignedOrder());
		assertTrue(s.getTotalTickCount() <= 21); // This is to ensure program efficiency isn't decreased
	}

	@Test
	public void tickSimulatorTest() {
		// Create the simulator basics (a place to store the floor, entities and orders)
		Floor floor = new Floor(3, 3);
		HashMap<String, Entity> entities = new HashMap<String, Entity>();
		Deque<Order> orders = new LinkedList<Order>();

		// Set up the basic simulator variables
		int chargeSpeed = 1;
		int capacity = 20;

		// Create an order for the simulation
		ArrayList<String> sids = new ArrayList<String>();
		sids.add("ss0");
		orders.add(new Order(sids, 10));

		// Add one of each entity to the entities list
		Robot r = new Robot("r0", new Location(0, 0), new ChargingPod("c0", new Location(0, 0)), 2);
		entities.put(r.getUID(), r);

		StorageShelf ss = new StorageShelf("ss0", new Location(1, 0));
		entities.put(ss.getUID(), ss);

		PackingStation ps = new PackingStation("ps0", new Location(2, 0));
		entities.put(ps.getUID(), ps);

		// Create the simulator
		Simulator s = null;
		try {
			s = new Simulator(floor, capacity, chargeSpeed, entities, orders);
		} catch (LocationNotValidException e) {
			e.printStackTrace();
		}

		// Make the simulator tick, and then check to see if the expected happened,
		// repeatedly
		try {
			s.tick();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Tests for first tick
		assertEquals(3, s.getRobots().get(0).getPowerUnits());
		assertEquals(1, s.getTotalTickCount());
		assertNotNull(ps.getCurrentOrder()); // Packing station
		assertNull(s.getWarehouse().getUnassignedOrder());

		// Take the total tick count to 11...
		try {
			for (int i = 0; i <= 8; i++)
				s.tick();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Tests for 10th tick
		assertEquals(7, s.getRobots().get(0).getPowerUnits());
		assertEquals(10, s.getTotalTickCount());
		assertEquals("ss0", ps.getStorageShelvesVisited().get(0).getUID()); // Packing
																			// station
		assertNull(s.getWarehouse().getUnassignedOrder());
	}

	@Test
	public void neverAcceptJobTest() {
		fail("Not yet implemented.");
		// Should set up a sim where the robot will never have enough battery and ensure
		// failure is notified and exits.
	}

	@Test
	public void robotRunsOutOfBatteryTest() {
		fail("Not yet implemented.");
		// Set up a scenario where a robot accepts a job, but then other things get in
		// the way
		// so it runs out of battery. Test to ensure program handles correctly.
	}

}