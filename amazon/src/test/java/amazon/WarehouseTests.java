package amazon;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.util.*;
import org.junit.Test;
import simulator.AEntity;
import simulator.Floor;
import simulator.LocationNotValidException;
import amazon.Order;
import amazon.PackingStation;
import amazon.StorageShelf;
import amazon.Warehouse;

public class WarehouseTests {

	@Test
	public void testConstructorWithValidParametersShouldSuccussfullyCreate() throws Exception {
		Floor floor = mock(Floor.class);
		List<AEntity> entities = new ArrayList<>();
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
		List<AEntity> entities = new ArrayList<>();
		entities.add(mockStorageShelf("ss1"));
		entities.add(mockPackingStation("ps1"));
		Deque<Order> orders = new ArrayDeque<>();

		Warehouse warehouse = new Warehouse(floor, entities, orders);

		assertEquals(2, warehouse.getEntities().size());
	}

	@Test
	public void testConstructorWithOrdersShouldSuccussfullyCreate() throws Exception {
		Floor floor = mock(Floor.class);
		List<AEntity> entities = new ArrayList<>();
		Deque<Order> orders = new ArrayDeque<>();
		orders.add(mock(Order.class));

		Warehouse warehouse = new Warehouse(floor, entities, orders);

		assertEquals(1, warehouse.getOrderManager().getAwaiting().size());
	}

	@Test
	public void testConstructorWithNullFloorShouldThrowIllegalArgumentException() throws Exception {
		List<AEntity> entities = new ArrayList<>();
		Deque<Order> orders = new ArrayDeque<>();

		try {
			new Warehouse(null, entities, orders);
			fail("A null parameter should fail this.");
		} catch (Exception e) {
			assertEquals("'floor' is a required, non-null parameter.", e.getMessage());
		}
	}

	@Test
	public void testConstructorWithNullEntitiesShouldThrowIllegalArgumentException()
			throws Exception {
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
		List<AEntity> entities = new ArrayList<>();

		try {
			new Warehouse(floor, entities, null);
			fail("A null parameter should fail this.");
		} catch (Exception e) {
			assertEquals("'orders' is a required, non-null parameter.", e.getMessage());
		}
	}

	@Test
	public void testGetEntityByUIDWithValidUIDShouldReturnTheEntity()
			throws LocationNotValidException {
		Floor floor = mock(Floor.class);
		StorageShelf storageShelf = mockStorageShelf("ss1");
		List<AEntity> entities = new ArrayList<>();
		entities.add(storageShelf);
		Deque<Order> orders = new ArrayDeque<>();
		Warehouse warehouse = new Warehouse(floor, entities, orders);

		assertEquals(storageShelf, warehouse.getEntityByUID("ss1"));
	}

	@Test
	public void testGetEntityByUIDWithInvalidUIDShouldReturnNull() throws LocationNotValidException {
		Floor floor = mock(Floor.class);
		StorageShelf storageShelf = mock(StorageShelf.class);
		List<AEntity> entities = new ArrayList<>();
		entities.add(storageShelf);
		Deque<Order> orders = new ArrayDeque<>();
		Warehouse warehouse = new Warehouse(floor, entities, orders);

		assertEquals(null, warehouse.getEntityByUID("cp1"));
	}

	private StorageShelf mockStorageShelf(String uid) {
		StorageShelf storageShelf = mock(StorageShelf.class);
		when(storageShelf.getUID()).thenReturn(uid);

		return storageShelf;
	}

	private PackingStation mockPackingStation(String uid) {
		PackingStation packingStation = mock(PackingStation.class);
		when(packingStation.getUID()).thenReturn(uid);

		return packingStation;
	}

}
