package test.model;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import org.junit.Test;

import main.model.AbstractEntity;
import main.model.Floor;
import main.model.Order;
import main.model.Warehouse;

public class WarehouseTests {

	@Test
	public void warehouseCreationTest() throws Exception {
		// Set up the basic variables for a new Warehouse
		Floor floor = new Floor(2, 2);
		HashMap<String, AbstractEntity> entities = new HashMap<String, AbstractEntity>();
		Deque<Order> orders = new LinkedList<Order>();
		Warehouse w = new Warehouse(floor, entities, orders);

		// Test to ensure that the warehouse was set up correctly
		// assertEquals(null, w.getOrderManager().pickup());
		assertEquals(true, w.getOrderManager().areAllItemsComplete());
		assertEquals(2, w.getFloor().getNumberOfColumns());
		assertEquals(2, w.getFloor().getNumberOfRows());
		assertEquals("Floor - Size: 2 columns by 2 rows.", w.getFloor().toString());
	}

	@Test
	public void pickupTest() throws Exception {
		// Set up the basic variables for a new Warehouse
		Floor floor = new Floor(2, 2);
		HashMap<String, AbstractEntity> entities = new HashMap<String, AbstractEntity>();
		Deque<Order> orders = new LinkedList<Order>();
		// create an order to add to the warehouse
		ArrayList<String> shelf = new ArrayList<String>();
		shelf.add("ss1");
		Order o = new Order(shelf, 2);
		orders.add(o);
		Warehouse w = new Warehouse(floor, entities, orders);

		// warehouse with one order
		assertEquals(o, w.getOrderManager().pickup());
		// assertEquals(null, w.getOrderManager().pickup());
		assertEquals(true, w.getOrderManager().getProgressing().contains(o));
	}

	@Test
	public void completeTest() throws Exception {
		// Set up the basic variables for a new Warehouse
		Floor floor = new Floor(2, 2);
		HashMap<String, AbstractEntity> entities = new HashMap<String, AbstractEntity>();
		Deque<Order> orders = new LinkedList<Order>();
		// create an order to add to the warehouse
		ArrayList<String> shelf = new ArrayList<String>();
		shelf.add("ss1");
		Order o = new Order(shelf, 2);
		orders.add(o);
		Warehouse w = new Warehouse(floor, entities, orders);

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
		HashMap<String, AbstractEntity> entities = new HashMap<String, AbstractEntity>();
		Deque<Order> orders = new LinkedList<Order>();
		// create an order to add to the warehouse
		ArrayList<String> shelf = new ArrayList<String>();
		shelf.add("ss1");
		Order o = new Order(shelf, 2);
		orders.add(o);
		Warehouse w = new Warehouse(floor, entities, orders);

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
