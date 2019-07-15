package Test;

import static org.junit.Assert.*;

import org.junit.Test;

import model.Location;
import model.PackingStation;
import model.StorageShelf;

public class PackingStationTest {

	@Test
	public void packingStationCreationTest() {
		//Create a new packing station to test
		Location l = new Location(2, 2);
		PackingStation ps = new PackingStation("1", l);
		
		//Test the new packing station
		assertEquals("1", ps.getUID());
		assertEquals(l, ps.getLocation());
	}
	
	@Test
	public void tickTest() {
		fail("Not yet implemented");
	}

	@Test
	public void pickOrderTest() {
		fail("Not yet implemented");
	}

	@Test
	public void requestItemsTest() {
		fail("Not yet implemented");
	}

	@Test
	public void packOrderTest() {	
		fail("Not yet implemented");
	}
	
	@Test
	public void dispatchOrderTest() {
		fail("Not yet implemented");
	}
	
	@Test
	public void recieveItemTest() {
//		//Create a packing station to test
	//	Location l = new Location(2, 2);
		//PackingStation ps = new PackingStation("1", l);

//		//Create a storage shelf "item" to deliver to the packing station
	//	Location l2 = new Location(1, 1);
		//StorageShelf ss = new StorageShelf("2", l2);
		
		//Test to ensure the packing station receives the item
//		assertEquals(null, ps.getStorageShelvesVisited());
	//	ps.recieveItem(ss);
		//assertEquals(1, ps.getStorageShelvesVisited().size());
	//	assertEquals(ss, ps.getStorageShelvesVisited().get(0));
		
		//Create another storage shelf "item" to deliver to the packing station
//		Location l3 = new Location(1, 1);
	//	StorageShelf ss2 = new StorageShelf("3", l3);
		
		//Test to ensure the packing station receives the item
//		ps.recieveItem(ss2);
	//	assertEquals(2, ps.getStorageShelvesVisited().size());
		//assertEquals(ss, ps.getStorageShelvesVisited().get(0));
	//	assertEquals(ss2, ps.getStorageShelvesVisited().get(1));
		
		fail("Not yet implemented");
	}

}
