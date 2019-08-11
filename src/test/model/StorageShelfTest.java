package test.model;

import static org.junit.Assert.*;
import org.junit.Test;

import main.model.Location;
import main.model.StorageShelf;

public class StorageShelfTest {

	@Test
	public void storageShelfCreationTest() {
		//Create a simple location and charging pod, and ensure that it creates successfully (via ToString)
		Location l = new Location(1, 1);
		StorageShelf ss = new StorageShelf("0000", l);
		assertEquals("StorageShelf(0000) - Location: [1, 1]", ss.toString());
		
		//Create another simple location and charging pod, and ensure that it creates successfully (via ToString)
		Location l2 = new Location(3, 5);
		StorageShelf ss2 = new StorageShelf("1234", l2);
		assertEquals("StorageShelf(1234) - Location: [3, 5]", ss2.toString());
	}

}
