package model;

import java.text.MessageFormat;

import helpers.AStarPathFinder;
import helpers.BasicPathCostEstimator;
import helpers.PathCostEstimator;
import helpers.PathFinder;

public class Robot extends Entity implements Actor {
	private int currentPowerUnits;
	private final int maxPowerUnits;
	private static int POWER_UNITS_EMPTY = 1;
	private static int POWER_UNITS_CARRYING = 2;

	private StorageShelf storageShelf;
	private StorageShelf holdingItem;
	private PackingStation packingStation;
	private ChargingPod chargingPod;

	private PathFinder pathFinder;
	private Location previousLocation;
	private RobotStatus status;

	public static enum RobotStatus {
		GoingToCharge, Charging, CollectingItem, ReturningItem
	}

	/**
	 * @param uid
	 * @param location
	 */
	public Robot(String uid, Location location, ChargingPod chargingPod, int powerUnits) {
		super(uid, location);
		this.chargingPod = chargingPod;
		this.currentPowerUnits = powerUnits;
		this.maxPowerUnits = powerUnits;
		this.status = RobotStatus.Charging;
	}

	@Override
	public void tick(Floor floor) throws Exception {
		this.status = this.getStatus();
		this.log("Ticking with status: %s.", status);

		switch (status) {
		case CollectingItem:
			this.collectItemFromStorageShelf(floor);
			break;

		case ReturningItem:
			this.returnItemToPackingStation(floor);
			break;

		case Charging:
			break;

		case GoingToCharge:
			this.move(floor, this.chargingPod.getLocation());
			break;
		}
	}

	/**
	 * Increases the robots power units
	 */
	public void charge(ChargingPod chargingPod, int chargeSpeed) {
		if (!this.chargingPod.equals(chargingPod))
			throw new IllegalArgumentException("Invalid 'chargingPod' given. It must be the same as the one registed");

		int proposedPowerUnits = this.currentPowerUnits + chargeSpeed;
		this.currentPowerUnits = Integer.min(proposedPowerUnits, this.maxPowerUnits);
		this.log("Charged to %s.", this.currentPowerUnits);
	}

	private void collectItemFromStorageShelf(Floor floor) throws Exception {
		this.move(floor, this.storageShelf.getLocation());
		this.log("Moved closer to assigned Storage Shelf.");

		if (this.location.equals(this.storageShelf.getLocation())) {
			this.holdingItem = this.storageShelf;
			this.storageShelf = null;
			this.log("Reached assigned Storage Shelf.");
		}
	}

	private void returnItemToPackingStation(Floor floor) throws Exception {
		this.move(floor, this.packingStation.getLocation());
		this.log("Moved closer to assigned Packing Station.");

		if (this.location.equals(this.packingStation.getLocation())) {
			this.packingStation.recieveItem(this.holdingItem);
			this.packingStation = null;
			this.holdingItem = null;
			this.log("Reached assigned Packing Station.");
		}
	}

	/**
	 * Moves the robot to another location depending on whether it needs to charge
	 * to deliver an item
	 * 
	 * @throws Exception
	 * 
	 */
	private void move(Floor floor, Location targetLocation) throws Exception {
		this.log("Moving from %s to %s.", this.location, targetLocation);

		boolean pathFound = this.pathFinder.calculatePath(this.location, targetLocation);

		if (!pathFound)
			return; // TODO handle better!

		// this.log("Path: " + this.pathFinder.getPath());

		Location newLocation = this.pathFinder.getNextLocation();

		boolean successfulMove = floor.moveEntity(this.location, newLocation);
		if (!successfulMove)
			return; // TODO handle this better!

		this.previousLocation = this.location;
		this.location = newLocation;

		int powerUnitsToDeduct = this.hasItem() ? POWER_UNITS_CARRYING : POWER_UNITS_EMPTY;
		this.currentPowerUnits -= powerUnitsToDeduct;

		this.log("Power Units (post move): %s.", this.currentPowerUnits);
	}

	/**
	 * @param storageShelf
	 * @param packingStation
	 * @param warehouse
	 * @return
	 */
	public boolean acceptJob(StorageShelf storageShelf, PackingStation packingStation, Warehouse warehouse) {
		if (this.storageShelf != null || this.hasItem())
			return false;

		double estimatedCostWithLeeway = estimatePowerUnitCostForJob(storageShelf, packingStation);

		if (estimatedCostWithLeeway > this.currentPowerUnits)
			return false;

		this.storageShelf = storageShelf;
		this.packingStation = packingStation;
		this.pathFinder = new AStarPathFinder(warehouse.getFloor());

		this.log("Accepted Job to %s then %s.", storageShelf.getLocation(), packingStation.getLocation());

		return true;

	}

	/**
	 * Given a storage shelf and packing station this will calculate the number of
	 * power units to make the trip back to its charging pod with a leeway of 20%.
	 * 
	 * @param storageShelf
	 * @param packingStation
	 * @param warehouse
	 * @return
	 * 
	 * @throws LocationNotValidException
	 */
	private double estimatePowerUnitCostForJob(StorageShelf storageShelf, PackingStation packingStation) {
		PathCostEstimator costEstimator = new BasicPathCostEstimator(POWER_UNITS_EMPTY, POWER_UNITS_CARRYING);
		costEstimator.setStart(this.location);
		costEstimator.addUnladenedStop(storageShelf.getLocation());
		costEstimator.addLadenedStop(packingStation.getLocation());
		costEstimator.addUnladenedStop(this.chargingPod.getLocation());
		return costEstimator.getEstimatedCost(.2);
	}

	/**
	 * Determines the robot's status based on its current state.
	 */
	public RobotStatus getStatus() {
		boolean isAtChargingPod = this.location.equals(this.chargingPod.getLocation());
		boolean isBatteryBelowHalfCharge = this.currentPowerUnits < (this.maxPowerUnits * 0.5);

		if (isBatteryBelowHalfCharge && isAtChargingPod) {// Running low of powerUnits
			return RobotStatus.Charging;
		} else if (this.storageShelf != null) {// Storage Shelf Assigned
			return RobotStatus.CollectingItem;
		} else if (this.hasItem()) {
			return RobotStatus.ReturningItem; // Item collected
		} else if (isAtChargingPod) {
			return RobotStatus.Charging; // Nothing to do and already at Charging Pod
		} else {
			return RobotStatus.GoingToCharge; // Nothing to do so go charge
		}
	}

	/**
	 * Checks to see if the robot is carrying an item
	 * 
	 * @return boolean
	 */
	public boolean hasItem() {
		return this.holdingItem != null;
	}

	/**
	 * Get the power units
	 */
	public int getPowerUnits() {
		return currentPowerUnits;
	}

	public Location getPreviousLocation() {
		return previousLocation;
	}

	/**
	 * @return A string representation of a robot.
	 */
	public String toString() {

		if (this.packingStation != null && this.storageShelf != null) {
			return MessageFormat.format(
					"Robot:" + " {0}" + "- Status: {1}" + "- Power: {2}" + "- StorageShelf: {3}"
							+ "- Packing Station: {4}" + " {5}",
					this.uid, this.status, this.currentPowerUnits, this.storageShelf.getUID(),
					this.packingStation.getUID(), this.location);
		} else if (this.packingStation != null) {
			return MessageFormat.format(
					"Robot:" + "{0}" + "- Status: {1}" + "- Power: {2}" + "- StorageShelf: {3}"
							+ " Packing Station: null" + " {4} ",
					this.uid, this.status, this.currentPowerUnits, this.storageShelf, this.location);
		} else {
			return MessageFormat.format(
					"Robot:" + "{0}" + "- Status: {1}" + "- Power: {2}" + "- StorageShelf: null"
							+ "- Packing Station: {3}" + " {4} ",
					this.uid, this.status, this.currentPowerUnits, this.packingStation, this.location);
		}
	}

}
