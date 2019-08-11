package test.model;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import main.model.ChargingPod;
import main.model.Location;
import main.model.Robot;

public class RobotTest {

	@Test
	public void createRobotTest() {
		//Set up and create a new robot, ready for testing
		Location l = new Location(2, 2); 
		ChargingPod cp = new ChargingPod("1", l); 
		Robot r = new Robot("2", l, cp, 8, 1);
				
		//Test to ensure that the robot was created in the way we wanted it to be
		assertEquals(l, r.getLocation());
		assertEquals("2", r.getUID());
		assertEquals(8, r.getPowerUnits());
		assertEquals(null, r.getPreviousLocation());
		assertEquals("Robot(2) - Location: [2, 2], Status: AwaitingJob, Power: 8", r.toString());
	}
	
	@Test
	public void chargeTest() {
		//Set up and create a new robot, ready for testing
		Location l = new Location(2, 2); 
		ChargingPod cp = new ChargingPod("1", l); 
		Robot r = new Robot("2", l, cp, 20, 1);
				
		// //Call charge on the robot when it's battery is less than 50%
		// assertEquals(8, r.getPowerUnits());
		// r.charge();
		// assertEquals(9, r.getPowerUnits());
		// r.charge();
		// assertEquals(10, r.getPowerUnits());
		
		//Test that the robots full charge works
		// r.setPowerUnits(20);
		r.charge();
		assertEquals(20, r.getPowerUnits());
		r.charge();
		assertEquals(20, r.getPowerUnits()); 
	}
	
	@Test
	public void acceptJobTest() {
		// //Create a new simulator and warehouse from file so that the robot can assess jobs
		// Floor floor = new Floor(3, 3);
		// HashMap<String, Entity> entities = new HashMap<String, Entity>();
		// Deque<Order> orders = new LinkedList<Order>();
		
		// //Set up the basic simulator variables
		// int chargeSpeed = 10;
		// int capacity = 50;
		
		// //Create an order for the simulation
		// ArrayList<String> sids = new ArrayList<String>();
		// sids.add("ss0");
		// orders.add(new Order(sids, 10));
		
		// //Add one of each entity to the entities list
		// Robot r = new Robot("r0", new Location(0,0), new ChargingPod("c0", new Location(0,0)), capacity, chargeSpeed);
		// entities.put(r.getUID(), r);
		
		// StorageShelf ss = new StorageShelf("ss0", new Location(1,0));
		// entities.put(ss.getUID(), ss);
		
		// PackingStation ps = new PackingStation("ps0", new Location(2,0));
		// entities.put(ps.getUID(), ps);
		
		// //Create the simulator
		// Simulator s = null;
		// try {
		// 	s = new Simulator(floor, entities, orders);
		// } catch (LocationNotValidException e) {
		// 	e.printStackTrace();
		// }
		
		// //Get the storage shelf and packing station for the robot to assess
		// Warehouse warehouse = s.getWarehouse();
		
		// //Test to ensure that the acceptJob method is working successfully
		// boolean test = true;
		// try {
		// 	test = r.pickupJob(ss, ps, warehouse);
		// } catch (Exception e) {
		// 	e.printStackTrace();
		// }
		// assertTrue(test);
		
		// //Ensure the robot has just enough charge to accept the job, and test the accept method again
		// test = true;
		// try {
		// 	test = r.pickupJob(ss, ps, warehouse);
		// } catch (Exception e) {
		// 	e.printStackTrace();
		// }
		// assertFalse(test);
		
		// //Ensure that acceptJob fails, as the robot already has a storage shelf
		// r.charge();
		// test = true;
		// try {
		// 	test = r.pickupJob(ss, ps, warehouse);
		// } catch (Exception e) {
		// 	e.printStackTrace();
		// }
		// assertFalse(test);
	}
	
	@Test
	public void moveTest() {
		// fail("Not yet implemented");
		
	}
	
	@Test
	public void pathFindingTest() {
		// fail("Not yet implemented");
	}
	
	@Test
	public void tickTest() {
		//Set up and create a new robot, ready for testing
		Location l = new Location(2, 2); 
		ChargingPod cp = new ChargingPod("1", l); 
		Robot r = new Robot("2", l, cp, 20, 1);
		
		//Test tick when robot needs to charge and is at charging station
		
		//Test tick when robot needs to charge and is not at charging station

		//Test tick when robot needs to visit storage shelf

		//Test tick when robot needs to visit packing station
		// fail("Not yet implemented");
	}

}
