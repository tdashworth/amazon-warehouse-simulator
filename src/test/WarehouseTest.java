package test;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import org.junit.Test;
import model.Entity;
import model.Floor;
import model.LocationNotValidException;
import model.Order;
import model.Warehouse;
import simulation.Simulator;

public class WarehouseTest {

	@Test
	public void warehouseCreationTest() throws Exception {
		// Set up the basic variables for a new Warehouse
		Floor floor = new Floor(2, 2);
		HashMap<String, Entity> entities = new HashMap<String, Entity>();
		Deque<Order> orders = new LinkedList<Order>();

		Simulator s = null;
		try {
			s = new Simulator(floor, entities, orders);
		} catch (LocationNotValidException e) {
			e.printStackTrace();
		}
		Warehouse w = new Warehouse(floor, entities, orders, s);

		// Test to ensure that the warehouse was set up correctly
		// assertEquals(null, w.getOrderManager().pickup());
		assertEquals(0, w.getTotalTickCount());
		assertEquals(true, w.getOrderManager().areAllItemsComplete());
		assertEquals(2, w.getFloor().getNumberOfColumns());
		assertEquals(2, w.getFloor().getNumberOfRows());
		assertEquals("Floor - Size: 2 columns by 2 rows.", w.getFloor().toString());
	}

	@Test
	public void pickupTest() throws Exception {
		// Set up the basic variables for a new Warehouse
		Floor floor = new Floor(2, 2);
		HashMap<String, Entity> entities = new HashMap<String, Entity>();
		Deque<Order> orders = new LinkedList<Order>();
		// create an order to add to the warehouse
		ArrayList<String> shelf = new ArrayList<String>();
		shelf.add("ss1");
		Order o = new Order(shelf, 2);
		orders.add(o);

		Simulator s = null;
		try {
			s = new Simulator(floor, entities, orders);
		} catch (LocationNotValidException e) {
			e.printStackTrace();
		}
		Warehouse w = new Warehouse(floor, entities, orders, s);

		// warehouse with one order
		assertEquals(o, w.getOrderManager().pickup());
		// assertEquals(null, w.getOrderManager().pickup());
		assertEquals(true, w.getOrderManager().getProgressing().contains(o));
	}

	@Test
	public void completeTest() throws Exception {
		// Set up the basic variables for a new Warehouse
		Floor floor = new Floor(2, 2);
		HashMap<String, Entity> entities = new HashMap<String, Entity>();
		Deque<Order> orders = new LinkedList<Order>();
		// create an order to add to the warehouse
		ArrayList<String> shelf = new ArrayList<String>();
		shelf.add("ss1");
		Order o = new Order(shelf, 2);
		orders.add(o);

		Simulator s = null;
		try {
			s = new Simulator(floor, entities, orders);
		} catch (LocationNotValidException e) {
			e.printStackTrace();
		}
		Warehouse w = new Warehouse(floor, entities, orders, s);

		// warehouse with one order
		assertEquals(o, w.getOrderManager().pickup());
		// assertEquals(null, w.getOrderManager().pickup());
		try {
			w.getOrderManager().complete(o);
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertEquals(true, w.getOrderManager().getProgressing().isEmpty());
		assertEquals(true, w.getOrderManager().getCompleted().contains(o));
	}

	@Test
	public void assignJobToRobotTest() {
		// fail("Not yet implemented");
	}

	@Test
	public void allOrdersAreDispatchedTest() throws Exception {
		// Set up the basic variables for a new Warehouse
		Floor floor = new Floor(2, 2);
		HashMap<String, Entity> entities = new HashMap<String, Entity>();
		Deque<Order> orders = new LinkedList<Order>();
		// create an order to add to the warehouse
		ArrayList<String> shelf = new ArrayList<String>();
		shelf.add("ss1");
		Order o = new Order(shelf, 2);
		orders.add(o);

		Simulator s = null;
		try {
			s = new Simulator(floor, entities, orders);
		} catch (LocationNotValidException e) {
			e.printStackTrace();
		}
		Warehouse w = new Warehouse(floor, entities, orders, s);

		assertEquals(false, w.getOrderManager().areAllItemsComplete());

		assertEquals(o, w.getOrderManager().pickup());
		// assertEquals(null, w.getOrderManager().pickup());
		try {
			w.getOrderManager().complete(o);
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertEquals(true, w.getOrderManager().getProgressing().isEmpty());
		assertEquals(true, w.getOrderManager().getCompleted().contains(o));

		assertEquals(true, w.getOrderManager().areAllItemsComplete());
	}

}
