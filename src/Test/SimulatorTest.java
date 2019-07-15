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
import model.Robot;
import simulation.Simulator;

public class SimulatorTest {

	@Test
	public void createSimulatorTest() {
		//Set up and create a very basic simulator which will be used for testing
		Floor floor = new Floor(2, 2);
		int chargeSpeed = 1;
		int capacity = 1;
		HashMap<String, Entity> entities = new HashMap<String, Entity>();
		Deque<Order> orders = new LinkedList<Order>();
		Simulator s = null;
		try {
			s = new Simulator(floor, capacity, chargeSpeed, entities, orders);
		} catch (LocationNotValidException e) {
			e.printStackTrace();
		}
		
		//Test to ensure that the simulator was set up correctly
		assertEquals(0, s.getTotalTickCount());
		assertEquals(1, s.getChargeSpeed());
		assertEquals(1, s.getMaxChargeCapacity());
		assertEquals(true, s.isComplete());
		assertEquals(0, s.getActors().size());
		assertEquals(0, s.getRobots().size());
		assertEquals(2, s.getFloor().getNumberOfColumns());
		assertEquals(2, s.getFloor().getNumberOfRows());
		assertEquals("Floor: - Size: 2 rows by 2 columns.", s.getFloor().toString());
		
		
		//Set up and create a slightly more advanced simulator which will be used for testing		
		capacity = 20;
		//Create a single order for the simulator
		ArrayList<String> sids = new ArrayList<String>();
		sids.add("ss0");
		orders.add(new Order(sids, 10));
		//Add one entity to the simulation creation variables
		Robot r = new Robot("r0", new Location(0,0), new ChargingPod("c0", new Location(0,0)), 2); 
		entities.put(r.getUID(), r);
		
		try {
			s = new Simulator(floor, capacity, chargeSpeed, entities, orders);
		} catch (LocationNotValidException e) {
			e.printStackTrace();
		}
		
		//Ensure that the result simulation matches the file that was read in
		assertEquals(0, s.getTotalTickCount());
		assertEquals(1, s.getChargeSpeed());
		assertEquals(20, s.getMaxChargeCapacity());
		assertEquals(false, s.isComplete());
		assertEquals(1, s.getActors().size());
		//Robot tests
		assertEquals(1, s.getRobots().size());
		assertTrue(new Location(0, 0).equals(s.getRobots().get(0).getLocation()));
		assertEquals(2, s.getRobots().get(0).getPowerUnits());
		assertEquals("r0", s.getRobots().get(0).getUID());
		//Floor tests
		assertEquals(2, s.getFloor().getNumberOfColumns());
		assertEquals(2, s.getFloor().getNumberOfRows());
		assertEquals("Floor: - Size: 2 rows by 2 columns.", s.getFloor().toString());
	}
	
	@Test
	public void runSimulatorTest() {
		fail("Not yet implemented");
	}
	
	@Test
	public void tickSimulatorTest() {
		fail("Not yet implemented");
	}
	
	@Test
	public void resetSimulatorTest() {
		fail("Not yet implemented");
	}
	
	@Test
	public void simulatorCompletesTest() {
		fail("Not yet implemented");
	}
	
	@Test
	public void furtherSimulatorTestsHerePleaseTest() {
		fail("ADD MORE TESTS BELOW");
	}

}
