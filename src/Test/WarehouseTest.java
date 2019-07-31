package Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.junit.Test;

import model.Entity;
import model.Floor;
import model.LocationNotValidException;
import model.Order;
import model.Warehouse;
import simulation.Simulator;

public class WarehouseTest {

	@Test
	public void warehouseCreationTest() {
		//Set up the basic variables for a new Warehouse
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

		//Test to ensure that the warehouse was set up correctly
		assertEquals(null, w.getUnassignedOrder());
		assertEquals(0, w.getTotalTickCount());
		assertEquals(true, w.areAllOrdersDispatched());
		assertEquals(2, w.getFloor().getNumberOfColumns());
		assertEquals(2, w.getFloor().getNumberOfRows());
		assertEquals("Floor: - Size: 2 rows by 2 columns.", w.getFloor().toString());
	}

	@Test
	public void getUnassignedOrderTest() {
		//Set up the basic variables for a new Warehouse
		Floor floor = new Floor(2, 2);
		HashMap<String, Entity> entities = new HashMap<String, Entity>();
		Deque<Order> orders = new LinkedList<Order>();
		//create an order to add to the warehouse
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

		//warehouse with one order
		assertEquals(o,w.getUnassignedOrder());
		assertEquals(null, w.getUnassignedOrder());
		assertEquals(true, w.getAssignedOrders().contains(o));
	}

	@Test
	public void dispatchOrderTest() {
		//Set up the basic variables for a new Warehouse
		Floor floor = new Floor(2, 2);
		HashMap<String, Entity> entities = new HashMap<String, Entity>();
		Deque<Order> orders = new LinkedList<Order>();
		//create an order to add to the warehouse
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

		//warehouse with one order
		assertEquals(o,w.getUnassignedOrder());
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
		fail("Not yet implemented");
	}

	@Test
	public void allOrdersAreDispatchedTest() {
		//Set up the basic variables for a new Warehouse
				Floor floor = new Floor(2, 2);
				HashMap<String, Entity> entities = new HashMap<String, Entity>();
				Deque<Order> orders = new LinkedList<Order>();
				//create an order to add to the warehouse
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
				
				assertEquals(o,w.getUnassignedOrder());
				assertEquals(null, w.getUnassignedOrder());
				try {
					w.dispatchOrder(o);
				} catch (Exception e) {
					e.printStackTrace();
				}
				assertEquals(true, w.getAssignedOrders().isEmpty());
				assertEquals(true, w.getDispatchedOrders().contains(o));
				
				assertEquals(true,w.areAllOrdersDispatched());
	}

}
