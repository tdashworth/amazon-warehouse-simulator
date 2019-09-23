package amazon;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Test;

import amazon.Job;
import simulator.Location;
import amazon.Order;
import amazon.packingStation.*;
import amazon.StorageShelf;
import amazon.Warehouse;
import simulator.utils.ItemManager;;

public class PackingStationTests {

	@Test
	public void testConstructorWithValidParametersShouldSuccussfullyCreate() {
		String uid = "ps1";
		Location location = new Location(0, 0);
		PackingStation packingStation = new PackingStation(uid, location);

		assertEquals(uid, packingStation.getUID());
		assertEquals(location, packingStation.getLocation());
		assertEquals(PickingState.class, packingStation.getStatus().getClass());
	}

	@Test
	public void testConstructorWithNullUIDShouldThrowIllegalArgumentException() throws Exception {
		Location location = new Location(0, 0);

		try {
			new PackingStation(null, location);
			fail("A null parameter should fail this.");
		} catch (Exception e) {
			assertEquals("'uid' is a required, non-null parameter.", e.getMessage());
		}
	}

	@Test
	public void testConstructorWithNullLocationShouldThrowIllegalArgumentException()
			throws Exception {
		try {
			new PackingStation("ps1", null);
			fail("A null parameter should fail this.");
		} catch (Exception e) {
			assertEquals("'location' is a required, non-null parameter.", e.getMessage());
		}
	}

	@Test
	public void testTickPickOrderWithOrderToPickupShouldPickUpOrder() throws Exception {
		ItemManager<Order> orderManager = new ItemManager<>();
		orderManager.add(new Order(Arrays.asList("ss1", "ss2"), 5));
		ItemManager<Job> jobManager = new ItemManager<>();

		Warehouse warehouse = mock(Warehouse.class);
		when(warehouse.getOrderManager()).thenReturn(orderManager);
		when(warehouse.getJobManager()).thenReturn(jobManager);
		when(warehouse.getEntityByUID("ss1")).thenReturn(new StorageShelf("ss1", new Location(1, 0)));
		when(warehouse.getEntityByUID("ss2")).thenReturn(new StorageShelf("ss2", new Location(0, 1)));

		PackingStation packingStation = new PackingStation("ps1", new Location(0, 0));
		packingStation.tick(warehouse, 1);

		assertEquals(0, orderManager.getAwaiting().size());
		assertEquals(1, orderManager.getProgressing().size());
		assertEquals(2, jobManager.getAwaiting().size());
		assertEquals(AwaitingState.class, packingStation.getStatus().getClass());
	}

	@Test
	public void testTickPickOrderWithNoOrderToPickupShouldDoNothing() throws Exception {
		ItemManager<Order> orderManager = new ItemManager<>();
		ItemManager<Job> jobManager = new ItemManager<>();

		Warehouse warehouse = mock(Warehouse.class);
		when(warehouse.getOrderManager()).thenReturn(orderManager);
		when(warehouse.getJobManager()).thenReturn(jobManager);
		when(warehouse.getEntityByUID("ss1")).thenReturn(new StorageShelf("ss1", new Location(1, 0)));
		when(warehouse.getEntityByUID("ss2")).thenReturn(new StorageShelf("ss2", new Location(0, 1)));

		PackingStation packingStation = new PackingStation("ps1", new Location(0, 0));
		packingStation.tick(warehouse, 1);

		assertEquals(0, orderManager.getAwaiting().size());
		assertEquals(0, orderManager.getProgressing().size());
		assertEquals(0, jobManager.getAwaiting().size());
		assertEquals(PickingState.class, packingStation.getStatus().getClass());
	}

	@Test
	public void testTickPackOrderShouldDecrementUntilZero() throws Exception {
		StorageShelf storageShelf = new StorageShelf("ss1", new Location(1, 0));
		Order order = new Order(Arrays.asList(storageShelf.getUID()), 3);
		Warehouse warehouse = mock(Warehouse.class);

		PackingStation packingStation = new PackingStation("ps1", new Location(0, 0)) {
			{
				this.currentOrder = order;
				this.storageShelvesVisited = Arrays.asList(storageShelf);
				this.state = new PackingState(this, order.getNumberOfTicksToPack());
			}
		};
		assertEquals(PackingState.class, packingStation.getStatus().getClass());
		packingStation.tick(warehouse, 1);
		assertEquals(PackingState.class, packingStation.getStatus().getClass());
		packingStation.tick(warehouse, 1);
		assertEquals(PackingState.class, packingStation.getStatus().getClass());
		packingStation.tick(warehouse, 1);
		assertEquals(DispatchingState.class, packingStation.getStatus().getClass());
	}

	@Test
	public void testTickDispatchOrderShouldCompleteOrderAndReturnBackToPicking() throws Exception {
		StorageShelf storageShelf = new StorageShelf("ss1", new Location(1, 0));
		Order order = new Order(Arrays.asList(storageShelf.getUID()), 3);
		ItemManager<Order> orderManager = new ItemManager<>();
		orderManager.add(order);

		Warehouse warehouse = mock(Warehouse.class);
		when(warehouse.getOrderManager()).thenReturn(orderManager);

		PackingStation packingStation = new PackingStation("ps1", new Location(0, 0)) {
			{
				this.currentOrder = orderManager.pickup();
				this.storageShelvesVisited = Arrays.asList(storageShelf);
				this.state = new DispatchingState(this);
			}
		};

		assertEquals(DispatchingState.class, packingStation.getStatus().getClass());
		packingStation.tick(warehouse, 1);
		assertEquals(PickingState.class, packingStation.getStatus().getClass());
		assertEquals(0, orderManager.getProgressing().size());
		assertEquals(1, orderManager.getCompleted().size());
	}

	@Test
	public void testRecieveItemWithValidStorageShelfShouldNotThrowException() throws Exception {
		PackingStation packingStation = new PackingStation("ps1", new Location(0, 0)) {
			{
				this.currentOrder = new Order(Arrays.asList("ss1"), 0);
				this.storageShelvesVisited = new ArrayList<>();
				this.state = new AwaitingState(this);
			}
		};

		StorageShelf item = new StorageShelf("ss1", new Location(0, 0));
		packingStation.recieveItem(item);
	}

	@Test
	public void testRecieveItemWithNullStorageShelfShouldNotThrowIllegalArgumentException()
			throws Exception {
		PackingStation packingStation = new PackingStation("ps1", new Location(0, 0)) {
			{
				this.currentOrder = new Order(Arrays.asList("ss1"), 0);
				this.storageShelvesVisited = new ArrayList<>();
				this.state = new AwaitingState(this);
			}
		};

		try {
			packingStation.recieveItem(null);
			fail("Null parameter should fail.");
		} catch (IllegalArgumentException e) {
			assertEquals("'storageShelf' is a required, non-null parameter.", e.getMessage());
		}
	}

	@Test
	public void testRecieveItemWithInvalidStorageShelfShouldNotThrowIllegalArgumentException()
			throws Exception {
		PackingStation packingStation = new PackingStation("ps1", new Location(0, 0)) {
			{
				this.currentOrder = new Order(Arrays.asList("ss1"), 0);
				this.storageShelvesVisited = new ArrayList<>();
				this.state = new AwaitingState(this);
			}
		};

		try {
			StorageShelf item = new StorageShelf("ss2", new Location(0, 0));
			packingStation.recieveItem(item);
			fail("Not required Storage Shelf should fail.");
		} catch (IllegalArgumentException e) {
			assertEquals("Storage Shelf 'ss2' is not required by current order.", e.getMessage());
		}
	}
}
