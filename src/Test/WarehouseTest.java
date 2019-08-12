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
import model.Warehouse;
import simulation.Simulator;

public class WarehouseTest {

	@Test
	public void warehouseCreationTest() {
		// Set up the basic variables for a new Warehouse
		Floor floor = new Floor(2, 2);
		HashMap<String, Entity> entities = new HashMap<String, Entity>();
		Deque<Order> orders = new LinkedList<Order>();
		int chargeSpeed = 1;
		int capacity = 1;

		Simulator s = null;
		try {
			s = new Simulator(floor, capacity, chargeSpeed, entities, orders);
		} catch (LocationNotValidException e) {
			e.printStackTrace();
		}
		Warehouse w = new Warehouse(floor, entities, orders, s);

		// Test to ensure that the warehouse was set up correctly
		assertEquals(null, w.getUnassignedOrder());
		assertEquals(0, w.getTotalTickCount());
		assertEquals(true, w.areAllOrdersDispatched());
		assertEquals(2, w.getFloor().getNumberOfColumns());
		assertEquals(2, w.getFloor().getNumberOfRows());
		assertEquals("Floor: - Size: 2 rows by 2 columns.", w.getFloor().toString());
	}

	@Test
	public void getUnassignedOrderTest() {
		// Set up the basic variables for a new Warehouse
		Floor floor = new Floor(2, 2);
		HashMap<String, Entity> entities = new HashMap<String, Entity>();
		Deque<Order> orders = new LinkedList<Order>();
		// create an order to add to the warehouse
		ArrayList<String> shelf = new ArrayList<String>();
		shelf.add("ss1");
		Order o = new Order(shelf, 2);
		orders.add(o);
		int chargeSpeed = 1;
		int capacity = 1;

		Simulator s = null;
		try {
			s = new Simulator(floor, capacity, chargeSpeed, entities, orders);
		} catch (LocationNotValidException e) {
			e.printStackTrace();
		}
		Warehouse w = new Warehouse(floor, entities, orders, s);

		// warehouse with one order
		assertEquals(o, w.getUnassignedOrder());
		assertEquals(null, w.getUnassignedOrder());
		assertEquals(true, w.getAssignedOrders().contains(o));
	}

	@Test
	public void dispatchOrderTest() {
		// Set up the basic variables for a new Warehouse
		Floor floor = new Floor(2, 2);
		HashMap<String, Entity> entities = new HashMap<String, Entity>();
		Deque<Order> orders = new LinkedList<Order>();
		// create an order to add to the warehouse
		ArrayList<String> shelf = new ArrayList<String>();
		shelf.add("ss1");
		Order o = new Order(shelf, 2);
		orders.add(o);
		int chargeSpeed = 1;
		int capacity = 1;

		Simulator s = null;
		try {
			s = new Simulator(floor, capacity, chargeSpeed, entities, orders);
		} catch (LocationNotValidException e) {
			e.printStackTrace();
		}
		Warehouse w = new Warehouse(floor, entities, orders, s);

		// warehouse with one order
		assertEquals(o, w.getUnassignedOrder());
		assertEquals(null, w.getUnassignedOrder());
		try {
			w.dispatchOrder(o);
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertEquals(true, w.getAssignedOrders().isEmpty());
		assertEquals(true, w.getDispatchedOrders().contains(o));
	}

	@Test
	public void assignJobToRobotTest() {

		// Create the simulator basics (a place to store the floor, entities and orders)
		Floor floor = new Floor(3, 3);
		HashMap<String, Entity> entities = new HashMap<String, Entity>();
		Deque<Order> orders = new LinkedList<Order>();

		StorageShelf ss = new StorageShelf("ss0", new Location(2, 2));
		entities.put(ss.getUID(), ss);

		PackingStation ps = new PackingStation("ps0", new Location(0, 2));
		entities.put(ps.getUID(), ps);

		ArrayList<String> sids = new ArrayList<String>();
		sids.add("ss0");
		orders.add(new Order(sids, 10));

		// Set up the basic simulator variables
		int chargeSpeed = 1;
		int capacity = 25;

		Robot r = new Robot("r2", new Location(0, 0), new ChargingPod("c0", new Location(0, 0)), 25);
		entities.put("r2", r);
		Simulator s = null;
		try {
			s = new Simulator(floor, capacity, chargeSpeed, entities, orders);
		} catch (LocationNotValidException e) {
			e.printStackTrace();
		}
		
		try {
			s.run();	
		} catch (Exception e) {
			fail(e.getMessage());
		}
		assertEquals(true, s.isComplete());
	}

	@Test
	public void allOrdersAreDispatchedTest() {
		// Set up the basic variables for a new Warehouse
		Floor floor = new Floor(2, 2);
		HashMap<String, Entity> entities = new HashMap<String, Entity>();
		Deque<Order> orders = new LinkedList<Order>();
		// create an order to add to the warehouse
		ArrayList<String> shelf = new ArrayList<String>();
		shelf.add("ss1");
		Order o = new Order(shelf, 2);
		orders.add(o);
		int chargeSpeed = 1;
		int capacity = 1;

		Simulator s = null;
		try {
			s = new Simulator(floor, capacity, chargeSpeed, entities, orders);
		} catch (LocationNotValidException e) {
			e.printStackTrace();
		}
		Warehouse w = new Warehouse(floor, entities, orders, s);

		assertEquals(false, w.areAllOrdersDispatched());

		assertEquals(o, w.getUnassignedOrder());
		assertEquals(null, w.getUnassignedOrder());
		try {
			w.dispatchOrder(o);
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertEquals(true, w.getAssignedOrders().isEmpty());
		assertEquals(true, w.getDispatchedOrders().contains(o));

		assertEquals(true, w.areAllOrdersDispatched());
	}

}
