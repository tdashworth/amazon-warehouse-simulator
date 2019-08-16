package test.model;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Test;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import main.model.Job;
import main.model.Location;
import main.model.Order;
import main.model.PackingStation;
import main.model.StorageShelf;
import main.model.Warehouse;
import main.model.PackingStation.PackingStationStatus;
import main.utils.ItemManager;

public class PackingStationTest {

	@Test
	public void constructorTest() {
		String uid = "ps1";
		Location location = new Location(0, 0);
		PackingStation packingStation = new PackingStation(uid, location);

		assertEquals(uid, packingStation.getUID());
		assertEquals(location, packingStation.getLocation());
		assertTrue(packingStation.getSprite() instanceof Rectangle);
		assertEquals(35, ((Rectangle) packingStation.getSprite()).getHeight(), 0.0);
		assertEquals(35, ((Rectangle) packingStation.getSprite()).getWidth(), 0.0);
		assertEquals(Color.AQUAMARINE, ((Rectangle) packingStation.getSprite()).getFill());
		assertEquals(PackingStationStatus.Picking, packingStation.getStatus());

		// Negative Cases
		try {
			packingStation = new PackingStation(null, location);
			fail("A null parameter should fail this.");
		} catch (Exception e) {
			assertEquals("'uid' is a required, non-null parameter.", e.getMessage());
		}

		try {
			packingStation = new PackingStation(uid, null);
			fail("A null parameter should fail this.");
		} catch (Exception e) {
			assertEquals("'location' is a required, non-null parameter.", e.getMessage());
		}
	}

	@Test
	public void tickPickOrderTest() throws Exception {
		ItemManager<Order> orderManager = new ItemManager<>();
		orderManager.add(new Order(Arrays.asList("ss1", "ss2"), 5)); 
		ItemManager<Job> jobManager = new ItemManager<>();
		
		Warehouse warehouse = mock(Warehouse.class);
		when(warehouse.getOrderManager()).thenReturn(orderManager);
		when(warehouse.getJobManager()).thenReturn(jobManager);
		when(warehouse.getTotalTickCount()).thenReturn(1);
		when(warehouse.getEntityByUID("ss1")).thenReturn(new StorageShelf("ss1", new Location(1, 0)));
		when(warehouse.getEntityByUID("ss2")).thenReturn(new StorageShelf("ss2", new Location(0, 1)));
		
		// Positive Case
		PackingStation packingStation = new PackingStation("ps1", new Location(0, 0));
		packingStation.tick(warehouse);
		
		assertEquals(0, orderManager.getAwaiting().size());
		assertEquals(1, orderManager.getProgressing().size());
		assertEquals(2, jobManager.getAwaiting().size());
		assertEquals(PackingStationStatus.Awaiting, packingStation.getStatus());
		
		// Negative Case
		orderManager = new ItemManager<>();
		jobManager = new ItemManager<>();
		when(warehouse.getOrderManager()).thenReturn(orderManager);
		when(warehouse.getJobManager()).thenReturn(jobManager);
		packingStation = new PackingStation("ps1", new Location(0, 0));
		packingStation.tick(warehouse);

		assertEquals(0, orderManager.getAwaiting().size());
		assertEquals(0, orderManager.getProgressing().size());
		assertEquals(0, jobManager.getAwaiting().size());
		assertEquals(PackingStationStatus.Picking, packingStation.getStatus());
	}

	@Test
	public void tickPackOrderTest() throws Exception {
		StorageShelf storageShelf = new StorageShelf("ss1", new Location(1, 0));
		Order order = new Order(Arrays.asList(storageShelf.getUID()), 3);
		Warehouse warehouse = mock(Warehouse.class);

		PackingStation packingStation = new PackingStation("ps1", new Location(0, 0)) {
			{
				this.currentOrder = order;
				this.remainingPackingTicks = order.getNumberOfTicksToPack();
				this.storageShelvesVisited = Arrays.asList(storageShelf);
			}
		};
		assertEquals(PackingStationStatus.Packing, packingStation.getStatus());
		packingStation.tick(warehouse);
		assertEquals(PackingStationStatus.Packing, packingStation.getStatus());
		packingStation.tick(warehouse);
		assertEquals(PackingStationStatus.Packing, packingStation.getStatus());
		packingStation.tick(warehouse);
		assertEquals(PackingStationStatus.Dispatching, packingStation.getStatus());
	}

	@Test
	public void tickDispatchOrderTest() throws Exception {
		StorageShelf storageShelf = new StorageShelf("ss1", new Location(1, 0));
		Order order = new Order(Arrays.asList(storageShelf.getUID()), 3);
		ItemManager<Order> orderManager = new ItemManager<>();
		orderManager.add(order);

		Warehouse warehouse = mock(Warehouse.class);
		when(warehouse.getOrderManager()).thenReturn(orderManager);

		PackingStation packingStation = new PackingStation("ps1", new Location(0, 0)) {
			{
				this.currentOrder = orderManager.pickup();;
				this.remainingPackingTicks = 0;
				this.storageShelvesVisited = Arrays.asList(storageShelf);
			}
		};

		assertEquals(PackingStationStatus.Dispatching, packingStation.getStatus());
		packingStation.tick(warehouse);
		assertEquals(PackingStationStatus.Picking, packingStation.getStatus());
		assertEquals(0, orderManager.getProgressing().size());
		assertEquals(1, orderManager.getCompleted().size());
	}

	@Test
	public void recieveItemTest() throws Exception {
		PackingStation packingStation = new PackingStation("ps1", new Location(0, 0)) {
			{
				this.currentOrder = new Order(Arrays.asList("ss1"), 0);
				this.storageShelvesVisited = new ArrayList<>();
			}
		};

		// Positive Case
		StorageShelf item = new StorageShelf("ss1", new Location(0, 0));
		packingStation.recieveItem(item);

		// Negative Case
		try {
			packingStation.recieveItem(null);
			fail("Null parameter should fail.");
		} catch (IllegalArgumentException e) {
			assertEquals("'storageShelf' is a required, non-null parameter.", e.getMessage());
		}

		try {
			item = new StorageShelf("ss2", new Location(0, 0));
			packingStation.recieveItem(item);
			fail("Not required Storage Shelf should fail.");
		} catch (IllegalArgumentException e) {
			assertEquals("Storage Shelf 'ss2' is not required by current order.", e.getMessage());
		}
	}
}
