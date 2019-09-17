package amazon;

import simulator.AMover;
import simulator.Floor;
import simulator.Location;
import simulator.utils.BasicPathCostEstimator;
import simulator.utils.IPathCostEstimator;
import simulator.utils.ItemManager;

public class Robot extends AMover<Warehouse> {
	protected int powerUnits;
	protected Job currentJob;
	protected ChargingPod chargingPod;

	private final int powerUnitsCapacity;
	private final int powerUnitsChargeSpeed;
	private final static int POWER_UNITS_EMPTY = 1;
	private final static int POWER_UNITS_CARRYING = 2;

	public static enum RobotStatus {
		AwaitingJob, Charging, CollectingItem, DeliveringItem
	}

	/**
	 * @param uid
	 * @param location
	 */
	public Robot(String uid, Location location, ChargingPod chargingPod, int powerUnitsCapacity,
			int powerUnitsChargeSpeed) {
		super(uid, location);

		if (chargingPod == null)
			throw new IllegalArgumentException("'chargingPod' is a required, non-null parameter.");

		if (powerUnitsCapacity < 0)
			throw new IllegalArgumentException("'powerUnitsCapacity' must be a positive integer.");

		if (powerUnitsChargeSpeed < 0)
			throw new IllegalArgumentException("'powerUnitsChargeSpeed' must be a positive integer.");

		this.chargingPod = chargingPod;
		this.powerUnits = powerUnitsCapacity;
		this.powerUnitsCapacity = powerUnitsCapacity;
		this.powerUnitsChargeSpeed = powerUnitsChargeSpeed;
	}

	@Override
	public void tick(Warehouse warehouse, int currentTickCount) throws Exception {
		super.tick(warehouse, currentTickCount);
		
		RobotStatus status = this.getStatus();
		this.log("Ticking with status: %s.", status);

		switch (status) {
			case CollectingItem:
				this.collectItemFromStorageShelf(warehouse);
				break;

			case DeliveringItem:
				this.deliverItemToPackingStation(warehouse);
				break;

			case AwaitingJob:
				this.awaitForJob(warehouse);
				break;

			case Charging:
				this.charge();
				break;
		}
	}

	private void collectItemFromStorageShelf(Warehouse warehouse) throws Exception {
		this.move(warehouse.getFloor(), this.currentJob.getStorageShelf().getLocation());
		this.log("Moved closer to assigned Storage Shelf.");

		if (this.location.equals(this.currentJob.getStorageShelf().getLocation())) {
			this.currentJob.collected();
			this.log("Reached assigned Storage Shelf.");
		}
	}

	private void deliverItemToPackingStation(Warehouse warehouse) throws Exception {
		this.move(warehouse.getFloor(), this.currentJob.getPackingStation().getLocation());
		this.log("Moved closer to assigned Packing Station.");

		if (this.location.equals(this.currentJob.getPackingStation().getLocation())) {
			this.currentJob.delivered();
			warehouse.getJobManager().complete(currentJob);
			this.currentJob = null;
			this.log("Reached assigned Packing Station.");
		}
	}

	private void awaitForJob(Warehouse warehouse) throws Exception {
		// If at Charging Pod, charge, otherwise move towards it.
		if (this.location.equals(this.chargingPod.getLocation()))
			this.charge();
		else
			this.move(warehouse.getFloor(), this.chargingPod.getLocation());

		// Check if there's an acceptable Job to pick up.
		this.pickupJob(warehouse.getJobManager(), warehouse.getFloor());
	}

	/**
	 * Increases the robots power units
	 */
	private void charge() {
		if (this.powerUnits + powerUnitsChargeSpeed >= powerUnitsCapacity) {
			this.powerUnits = powerUnitsCapacity;
			this.log("Fully charged to %s", this.powerUnits);
		} else {
			this.powerUnits += powerUnitsChargeSpeed;
			this.log("Charge increased to %s.", this.powerUnits);
		}
	}

	@Override
	protected void move(Floor floor, Location targetLocation) throws Exception {
		super.move(floor, targetLocation);

		int powerUnitsToDeduct = this.hasItem() ? POWER_UNITS_CARRYING : POWER_UNITS_EMPTY;
		this.powerUnits -= powerUnitsToDeduct;

		this.log("Power Units (post move): %s.", this.powerUnits);
	}

	/**
	 * @param jobManager
	 * @param floor
	 * @throws Exception
	 */
	private void pickupJob(ItemManager<Job> jobManager, Floor floor) throws Exception {
		if (!jobManager.areItemsToPickup())
			return;

		// Check out next Job and validate estimated cost.
		Job tempJob = jobManager.viewNextPickup();
		double estimatedCostWithLeeway = estimatePowerUnitCostForJob(tempJob, floor);
		if (estimatedCostWithLeeway > this.powerUnits)
			return;

		// Pickup Job
		this.currentJob = jobManager.pickup();
		this.pathFinder = null;

		this.log("Job to %s then %s picked up.", this.currentJob.getStorageShelf().getLocation(),
				this.currentJob.getPackingStation().getLocation());
	}

	/**
	 * Given a storage shelf and packing station this will calculate the number of power units to make
	 * the trip back to its charging pod with a leeway of 20%.
	 * 
	 * @param storageShelf
	 * @param packingStation
	 * @param warehouse
	 * @return
	 * @throws LocationNotValidException
	 */
	private double estimatePowerUnitCostForJob(Job job, Floor floor) throws Exception {

		// Setup Cost Estimator
		IPathCostEstimator costEstimator = new BasicPathCostEstimator(this.location);
		costEstimator.addCost("laidened", Double.valueOf(POWER_UNITS_CARRYING));
		costEstimator.addCost("unlaidened", Double.valueOf(POWER_UNITS_EMPTY));

		// Add path Locations
		costEstimator.addLocation(job.getStorageShelf().getLocation(), "unlaidened");
		costEstimator.addLocation(job.getPackingStation().getLocation(), "laidened");
		costEstimator.addLocation(this.chargingPod.getLocation(), "unlaidened");

		// Add leeway to estimated cost and return.
		double estimatedCostWithLeeway = costEstimator.getEstimatedCost() * 1.2;
		return estimatedCostWithLeeway;
	}

	/**
	 * Determines the robot's status based on its current state.
	 */
	public RobotStatus getStatus() {
		boolean isAtChargingPod = this.location.equals(this.chargingPod.getLocation());
		boolean isBatteryBelowHalfCharge = this.powerUnits < (powerUnitsCapacity * 0.5);

		if (isBatteryBelowHalfCharge && isAtChargingPod)
			return RobotStatus.Charging;

		if (this.currentJob != null && this.currentJob.getStatus() == Job.JobStatus.Collecting)
			return RobotStatus.CollectingItem;

		if (this.currentJob != null && this.currentJob.getStatus() == Job.JobStatus.Delivering)
			return RobotStatus.DeliveringItem;

		return RobotStatus.AwaitingJob;
	}

	/**
	 * Checks to see if the robot is carrying an item
	 * 
	 * @return boolean
	 */
	private boolean hasItem() {
		return this.currentJob != null && this.currentJob.getStatus() == Job.JobStatus.Delivering;
	}

	/**
	 * Get the power units
	 */
	public int getPowerUnits() {
		return powerUnits;
	}

	/**
	 * @return A string representation of a robot.
	 */
	public String toString() {
		String result = super.toString();
		result += ", " + "Status: " + this.getStatus();
		result += ", " + "Power: " + this.getPowerUnits();
		if (this.currentJob != null)
			result += ", " + "Current Job: " + this.currentJob;

		return result;
	}
}
