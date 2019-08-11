package Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;

import org.junit.Test;

import model.Entity;
import model.Floor;
import model.Location;
import model.Order;
import model.PackingStation;
import model.StorageShelf;
import model.Warehouse;
import simulation.Simulator;

public class PackingStationTest {

	@Test
	public void packingStationCreationTest() {
		// Create a new packing station to test
		Location l = new Location(2, 2);
		PackingStation ps = new PackingStation("1", l);

		// Test the new packing station
		assertEquals("1", ps.getUID());
		assertEquals(l, ps.getLocation());
	}

	@Test
	public void PickOrderTest() throws Exception {

		Floor floor = new Floor(3, 3);
		HashMap<String, Entity> entities = new HashMap<String, Entity>();
		Deque<Order> orders = new LinkedList<Order>();
		Location l2 = new Location(1, 1);
		StorageShelf ss = new StorageShelf("s2", l2);

		// Set up the basic simulator variables
		int chargeSpeed = 1;
		int capacity = 20;
		Location l = new Location(0, 0);

		// Create an order for the simulation
		ArrayList<String> sids = new ArrayList<String>();
		sids.add("ss0");
		Order order = new Order(sids, 10);
		orders.add(order);

		// Create the simulator
		Simulator s = null;
		s = new Simulator(floor, capacity, chargeSpeed, entities, orders);

		Warehouse warehouse = s.getWarehouse();
		PackingStation ps = new PackingStation("1", l);

		ps.pickOrder(warehouse);
		assertEquals(order, ps.getCurrentOrder());
		assertEquals(0, ps.getStorageShelvesVisited().size());
		assertEquals(10, ps.getRemainingPackingTicks());

		// check if the packing station has received the item.
		ps.recieveItem(ss);
		assertEquals(1, ps.getStorageShelvesVisited().size());
		assertEquals(ss, ps.getStorageShelvesVisited().get(0));

		// test dispatch order if the order is in dispatched orders list
		ps.dispatchOrder(warehouse);
		assertNull(ps.getCurrentOrder());
		assertEquals(true, warehouse.getDispatchedOrders().contains(order));

		// no orders to pick
		ps.pickOrder(warehouse);
		assertEquals(null, ps.getCurrentOrder());
	}

	@Test
	public void packOrderTest() {
		Location l = new Location(2, 2);
		PackingStation ps = new PackingStation("p1", l);
		ps.setRemainingPackingTicks(10);
		ps.packOrder();
		assertEquals(9, ps.getRemainingPackingTicks());
	}

}
