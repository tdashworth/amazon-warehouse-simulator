package Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

import model.*;

public class TestRobot {
	
	private Location l;
	private Location l2;
	private ChargingPod ch;
	private StorageShelf ss;
	
	@Before
	public void setUp() {
		try {
			this.l = new Location(1,1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			this.l2 = new Location(1,2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.ss = new StorageShelf("s1", l2);
		
		this.ch = new ChargingPod("c1", l);
		
	}
	
	
	@Test
	public void testRobot() {
		Robot r = new Robot("r1", l, ch, 10);
		r.charge(2);
		assertEquals(12, r.getPowerUnits());
		r.charge(5);
		assertEquals(17, r.getPowerUnits());
		assertEquals(l,r.getLocation());
				
	}
	/**
	 * Test to see if the robot power goes down when moving and location is changed
	 
	@Test
	public void testRobotMoveNoItems() {
		Robot r = new Robot("r1", l2, ch,10);
		move();
		assertEquals(false, r.getLocation().equals(ch.getLocation()));
		assertEquals(true, r.getPowerUnits()<10);
				
		
	}
	/**
	 * Test to see if the robots power goes down double when moving and carrying an item 
	 
	
	@Test
	public void testRobotMoveWithItem() {
		Robot r = new Robot("r1", l, ch,10);
		r.hasItem = true;
		move();
		assertFalse(r.getLocation().equals(ch.getLocation()));
		assertEquals(true, r.getPowerUnits()<8);
				
	}*/
}
