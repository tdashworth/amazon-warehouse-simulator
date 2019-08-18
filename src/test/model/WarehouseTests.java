package test.model;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import org.junit.Test;

import main.model.AbstractEntity;
import main.model.Floor;
import main.model.Order;
import main.model.PackingStation;
import main.model.StorageShelf;
import main.model.Warehouse;

public class WarehouseTests {

	@Test
	public void testConstructorWithValidParametersShouldSuccussfullyCreate() throws Exception {
		Floor floor = mock(Floor.class);
		HashMap<String, AbstractEntity> entities = new HashMap<>();
		Deque<Order> orders = new ArrayDeque<>();

		Warehouse warehouse = new Warehouse(floor, entities, orders);

		assertEquals(floor, warehouse.getFloor());
		assertNotNull(warehouse.getEntities());
		assertNotNull(warehouse.getOrderManager());
		assertNotNull(warehouse.getJobManager());
	}

	@Test
	public void testConstructorWithEntitiesShouldSuccussfullyCreate() throws Exception {
		Floor floor = mock(Floor.class);
		HashMap<String, AbstractEntity> entities = new HashMap<>();
		entities.put("ss1", mock(StorageShelf.class));
		entities.put("ps1", mock(PackingStation.class));
		Deque<Order> orders = new ArrayDeque<>();

		Warehouse warehouse = new Warehouse(floor, entities, orders);

		assertEquals(2, warehouse.getEntities().size());
	}

	@Test
	public void testConstructorWithOrdersShouldSuccussfullyCreate() throws Exception {
		Floor floor = mock(Floor.class);
		HashMap<String, AbstractEntity> entities = new HashMap<>();
		Deque<Order> orders = new ArrayDeque<>();
		orders.add(mock(Order.class));

		Warehouse warehouse = new Warehouse(floor, entities, orders);

		assertEquals(1, warehouse.getOrderManager().getAwaiting().size());
	}

	@Test
	public void testConstructorWithNullFloorShouldThrowIllegalArgumentException() throws Exception {
		HashMap<String, AbstractEntity> entities = new HashMap<>();
		Deque<Order> orders = new ArrayDeque<>();

		try {
			new Warehouse(null, entities, orders);
			fail("A null parameter should fail this.");
		} catch (Exception e) {
			assertEquals("'floor' is a required, non-null parameter.", e.getMessage());
		}
	}

	@Test
	public void testConstructorWithNullEntitiesShouldThrowIllegalArgumentException() throws Exception {
		Floor floor = mock(Floor.class);
		Deque<Order> orders = new ArrayDeque<>();

		try {
			new Warehouse(floor, null, orders);
			fail("A null parameter should fail this.");
		} catch (Exception e) {
			assertEquals("'entities' is a required, non-null parameter.", e.getMessage());
		}
	}

	@Test
	public void testConstructorWithNullOrdersShouldThrowIllegalArgumentException() throws Exception {
		Floor floor = mock(Floor.class);
		HashMap<String, AbstractEntity> entities = new HashMap<>();

		try {
			new Warehouse(floor, entities, null);
			fail("A null parameter should fail this.");
		} catch (Exception e) {
			assertEquals("'orders' is a required, non-null parameter.", e.getMessage());
		}
	}

	@Test
	public void testGetEntityByUIDWithValidUIDShouldReturnTheEntity() {
		Floor floor = mock(Floor.class);
		StorageShelf storageShelf = mock(StorageShelf.class);
		HashMap<String, AbstractEntity> entities = new HashMap<>();
		entities.put("ss1", storageShelf);
		Deque<Order> orders = new ArrayDeque<>();
		Warehouse warehouse = new Warehouse(floor, entities, orders);

		assertEquals(storageShelf, warehouse.getEntityByUID("ss1"));
	}

	@Test
	public void testGetEntityByUIDWithInvalidUIDShouldReturnNull() {
		Floor floor = mock(Floor.class);
		StorageShelf storageShelf = mock(StorageShelf.class);
		HashMap<String, AbstractEntity> entities = new HashMap<>();
		entities.put("ss1", storageShelf);
		Deque<Order> orders = new ArrayDeque<>();
		Warehouse warehouse = new Warehouse(floor, entities, orders);

		assertEquals(null, warehouse.getEntityByUID("cp1"));
	}

}
