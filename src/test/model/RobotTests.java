package test.model;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import main.model.ChargingPod;
import main.model.Floor;
import main.model.Job;
import main.model.Location;
import main.model.LocationNotValidException;
import main.model.PackingStation;
import main.model.Robot;
import main.model.StorageShelf;
import main.model.Warehouse;
import main.model.Job.JobStatus;
import main.model.Robot.RobotStatus;
import main.utils.ItemManager;

public class RobotTests {

	@Test
	public void testConstructorWithValidParametersShouldSuccussfullyCreate() throws Exception {
		String uid = "r1";
		Location location = new Location(0, 0);
		ChargingPod chargingPod = mockChargingPodWithLocation(location);

		Robot robot = new Robot(uid, location, chargingPod, 50, 5);

		assertEquals(uid, robot.getUID());
		assertEquals(location, robot.getLocation());
		assertTrue(robot.getSprite() instanceof Circle);
		assertEquals(15, ((Circle) robot.getSprite()).getRadius(), 0.0);
		assertEquals(Color.RED, ((Circle) robot.getSprite()).getFill());
		assertEquals(RobotStatus.AwaitingJob, robot.getStatus());
	}

	@Test
	public void testConstructorWithNullUIDShouldThrowIllegalArgumentException() throws Exception {
		Location location = new Location(0, 0);
		ChargingPod chargingPod = mockChargingPodWithLocation(location);

		try {
			new Robot(null, location, chargingPod, 50, 5);
			fail("A null parameter should fail this.");
		} catch (IllegalArgumentException e) {
			assertEquals("'uid' is a required, non-null parameter.", e.getMessage());
		}
	}

	@Test
	public void testConstructorWithNullLoationShouldThrowIllegalArgumentException() throws Exception {
		ChargingPod chargingPod = mockChargingPodWithLocation(new Location(0, 0));

		try {
			new Robot("r1", null, chargingPod, 50, 5);

			fail("A null parameter should fail this.");
		} catch (IllegalArgumentException e) {
			assertEquals("'location' is a required, non-null parameter.", e.getMessage());
		}
	}

	@Test
	public void testConstructorWithNullChargingPodShouldThrowIllegalArgumentException() {
		try {
			new Robot("r1", new Location(0, 0), null, 50, 5);

			fail("A null parameter should fail this.");
		} catch (IllegalArgumentException e) {
			assertEquals("'chargingPod' is a required, non-null parameter.", e.getMessage());
		}
	}

	@Test
	public void testConstructorWithNegativePowerUnitsCapacityShouldThrowIllegalArgumentException()
			throws Exception {
		ChargingPod chargingPod = mockChargingPodWithLocation(new Location(0, 0));

		try {
			new Robot("r1", new Location(0, 0), chargingPod, -50, 5);

			fail("A negative parameter should fail this.");
		} catch (IllegalArgumentException e) {
			assertEquals("'powerUnitsCapacity' must be a positive integer.", e.getMessage());
		}
	}

	@Test
	public void testConstructorWithNegativePowerUnitsChargeSpeedShouldThrowIllegalArgumentException()
			throws Exception {
		String uid = "r1";
		Location location = new Location(0, 0);
		ChargingPod chargingPod = mockChargingPodWithLocation(location);

		try {
			new Robot(uid, location, chargingPod, 50, -5);

			fail("A negative parameter should fail this.");
		} catch (IllegalArgumentException e) {
			assertEquals("'powerUnitsChargeSpeed' must be a positive integer.", e.getMessage());
		}
	}

	@Test
	public void testTickAwaitingJobWhenNotAtChargingPodShouldMoveToIt() throws Exception {
		ChargingPod chargingPod = mockChargingPodWithLocation(new Location(0, 0));
		Robot robot = new Robot("r1", new Location(1, 0), chargingPod, 50, 5);
		Warehouse warehouse = mockWarehouse(robot);

		robot.tick(warehouse, 1);

		assertTrue(chargingPod.getLocation().equals(robot.getLocation()));
		assertEquals(RobotStatus.AwaitingJob, robot.getStatus());
		assertEquals(49, robot.getPowerUnits());
	}

	@Test
	public void testTickAwaitingJobWhenAtChargingPodShouldCharge() throws Exception {
		ChargingPod chargingPod = mockChargingPodWithLocation(new Location(0, 0));
		Robot robot = new Robot("r1", new Location(0, 0), chargingPod, 50, 5) {
			{
				this.powerUnits = 40;
			}
		};
		Warehouse warehouse = mockWarehouse(robot);

		robot.tick(warehouse, 1);

		assertTrue(chargingPod.getLocation().equals(robot.getLocation()));
		assertEquals(RobotStatus.AwaitingJob, robot.getStatus());
		assertEquals(45, robot.getPowerUnits());
	}

	@Test
	public void testTickAwaitingJobWhenJobAvailableShouldPickItUp() throws Exception {
		ChargingPod chargingPod = mockChargingPodWithLocation(new Location(0, 0));
		StorageShelf storageShelf = mockStorageShelfWithLocation(new Location(1, 1));
		PackingStation packingStation = mockPackingStationWithLocation(new Location(0, 1));
		Robot robot = new Robot("r1", new Location(1, 0), chargingPod, 50, 5);
		Job job = mock(Job.class);
		when(job.getStorageShelf()).thenReturn(storageShelf);
		when(job.getPackingStation()).thenReturn(packingStation);
		when(job.getStatus()).thenReturn(JobStatus.Collecting);
		Warehouse warehouse = mockWarehouse(robot);
		when(warehouse.getJobManager().areItemsToPickup()).thenReturn(true);
		when(warehouse.getJobManager().viewNextPickup()).thenReturn(job);
		when(warehouse.getJobManager().pickup()).thenReturn(job);

		robot.tick(warehouse, 1);

		assertEquals(RobotStatus.CollectingItem, robot.getStatus());
		verify(warehouse.getJobManager()).pickup();
	}

	@Test
	public void testTickCollectingItemWhenMoreThanTwoMovesAwayShouldMoveCloser() throws Exception {
		ChargingPod chargingPod = mockChargingPodWithLocation(new Location(0, 0));
		StorageShelf storageShelf = mockStorageShelfWithLocation(new Location(1, 1));
		Job job = mock(Job.class);
		when(job.getStatus()).thenReturn(JobStatus.Collecting);
		when(job.getStorageShelf()).thenReturn(storageShelf);

		Robot robot = new Robot("r1", new Location(0, 0), chargingPod, 50, 5) {
			{
				this.currentJob = job;
			}
		};
		Warehouse warehouse = mockWarehouse(robot);

		robot.tick(warehouse, 1);

		assertTrue(robot.getLocation().equals(new Location(1, 0))
				|| robot.getLocation().equals(new Location(0, 1)));
		assertEquals(49, robot.getPowerUnits());
		assertEquals(RobotStatus.CollectingItem, robot.getStatus());
		verify(job, never()).collected();
	}

	@Test
	public void testTickCollectingItemWhenOneMoveAwayShouldMoveAndNotifyJobOfCollection()
			throws Exception {
		ChargingPod chargingPod = mockChargingPodWithLocation(new Location(0, 0));
		StorageShelf storageShelf = mockStorageShelfWithLocation(new Location(1, 1));
		Job job = mock(Job.class);
		when(job.getStatus()).thenReturn(JobStatus.Collecting);
		when(job.getStorageShelf()).thenReturn(storageShelf);
		Robot robot = new Robot("r1", new Location(1, 0), chargingPod, 50, 5) {
			{
				this.currentJob = job;
			}
		};
		Warehouse warehouse = mockWarehouse(robot);

		robot.tick(warehouse, 1);
		when(job.getStatus()).thenReturn(JobStatus.Delivering);

		assertTrue(robot.getLocation().equals(new Location(1, 1)));
		assertEquals(49, robot.getPowerUnits());
		assertEquals(RobotStatus.DeliveringItem, robot.getStatus());
		verify(job).collected();
	}

	@Test
	public void testTickDelieveringItemWhenMoreThanTwoMovesAwayShouldMoveCloser() throws Exception {
		ChargingPod chargingPod = mockChargingPodWithLocation(new Location(1, 0));
		PackingStation packingStation = mockPackingStationWithLocation(new Location(0, 0));
		Job job = mock(Job.class);
		when(job.getStatus()).thenReturn(JobStatus.Delivering);
		when(job.getPackingStation()).thenReturn(packingStation);
		Robot robot = new Robot("r1", new Location(1, 1), chargingPod, 50, 5) {
			{
				this.currentJob = job;
			}
		};
		Warehouse warehouse = mockWarehouse(robot);

		robot.tick(warehouse, 1);

		assertTrue(robot.getLocation().equals(new Location(1, 0))
				|| robot.getLocation().equals(new Location(0, 1)));
		assertEquals(48, robot.getPowerUnits());
		assertEquals(RobotStatus.DeliveringItem, robot.getStatus());
		verify(job, never()).delivered();
		verify(warehouse.getJobManager(), never()).complete(job);
	}

	@Test
	public void testTickDelieveringItemWhenOneMoveAwayShouldMoveAndCompleteJob() throws Exception {
		ChargingPod chargingPod = mockChargingPodWithLocation(new Location(1, 0));
		PackingStation packingStation = mockPackingStationWithLocation(new Location(0, 0));
		Job job = mock(Job.class);
		when(job.getStatus()).thenReturn(JobStatus.Delivering);
		when(job.getPackingStation()).thenReturn(packingStation);
		Robot robot = new Robot("r1", new Location(0, 1), chargingPod, 50, 5) {
			{
				this.currentJob = job;
			}
		};
		Warehouse warehouse = mockWarehouse(robot);

		robot.tick(warehouse, 1);

		assertTrue(robot.getLocation().equals(new Location(0, 0)));
		assertEquals(48, robot.getPowerUnits());
		assertEquals(RobotStatus.AwaitingJob, robot.getStatus());
		verify(job).delivered();
		verify(warehouse.getJobManager()).complete(job);
	}

	@Test
	public void testTickChargingWhenAtleastChargeSpeedLessThanCapacityShouldIncreasePowerUnitsBySpeed()
			throws Exception {
		ChargingPod chargingPod = mockChargingPodWithLocation(new Location(1, 0));
		Robot robot = new Robot("r1", new Location(1, 0), chargingPod, 50, 5) {
			{
				this.powerUnits = 15;
			}
		};
		Warehouse warehouse = mockWarehouse(robot);

		robot.tick(warehouse, 1);

		assertEquals(20, robot.getPowerUnits());
		assertEquals(RobotStatus.Charging, robot.getStatus());
	}

	@Test
	public void testTickChargingWhenWithinChargeSpeedLessThanCapacityShouldIncreasePowerUnitsToCapacity()
			throws Exception {
		ChargingPod chargingPod = mockChargingPodWithLocation(new Location(1, 0));
		Robot robot = new Robot("r1", new Location(1, 0), chargingPod, 50, 5) {
			{
				this.powerUnits = 48;
			}
		};
		Warehouse warehouse = mockWarehouse(robot);

		robot.tick(warehouse, 1);

		assertEquals(50, robot.getPowerUnits());
	}

	@SuppressWarnings("unchecked")
	private Warehouse mockWarehouse(Robot robot) throws LocationNotValidException {
		Floor floor = new Floor(2, 2);
		floor.loadMover(robot);

		ItemManager<Job> jobManager = mock(ItemManager.class);

		Warehouse warehouse = mock(Warehouse.class);
		when(warehouse.getFloor()).thenReturn(floor);
		when(warehouse.getJobManager()).thenReturn(jobManager);

		return warehouse;
	}

	private PackingStation mockPackingStationWithLocation(Location location)
			throws LocationNotValidException {
		PackingStation packingStation = mock(PackingStation.class);
		when(packingStation.getLocation()).thenReturn(location);

		return packingStation;
	}

	private ChargingPod mockChargingPodWithLocation(Location location)
			throws LocationNotValidException {
		ChargingPod chargingPod = mock(ChargingPod.class);
		when(chargingPod.getLocation()).thenReturn(location);

		return chargingPod;
	}

	private StorageShelf mockStorageShelfWithLocation(Location location)
			throws LocationNotValidException {
		StorageShelf storageShelf = mock(StorageShelf.class);
		when(storageShelf.getLocation()).thenReturn(location);

		return storageShelf;
	}
}
