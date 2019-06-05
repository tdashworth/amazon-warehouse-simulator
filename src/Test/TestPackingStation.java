package Test;
import org.junit.Before;

import model.*;

public class TestPackingStation {

	private Order currentOrder;
	private Location l;
	private StorageShelf ss;
	private int remainingPackTicks;
	
	@Before
	public void setUp() {
		try {
			this.l = new Location(1,1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
	
}
