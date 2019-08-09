package model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import utils.PathFindingStrategy;

public class Robot extends Mover {
	private int powerUnits;
	private StorageShelf storageShelf;
	private StorageShelf holdingItem;
	private PackingStation packingStation;
	private ChargingPod chargingPod;

	private final int powerUnitsCapacity;
	private final int powerUnitsChargeSpeed;
	private final static int POWER_UNITS_EMPTY = 1;
	private final static int POWER_UNITS_CARRYING = 2;

	public static enum RobotStatus {
		GoingToCharge, Charging, CollectingItem, ReturningItem
	}

	/**
	 * @param uid
	 * @param location
	 */
	public Robot(String uid, Location location, ChargingPod chargingPod, int powerUnitsCapacity, int powerUnitsChargeSpeed) {
		super(uid, location, new Circle(15, Color.RED));
		this.chargingPod = chargingPod;
		this.powerUnits = powerUnitsCapacity;
		this.powerUnitsCapacity = powerUnitsCapacity;
		this.powerUnitsChargeSpeed = powerUnitsChargeSpeed;
	}

	@Override
	public void tick(Warehouse warehouse) throws Exception {
		RobotStatus status = this.getStatus();
		this.log("Ticking with status: %s.", status);
		this.previousLocation = this.location;

		switch (status) {
		case CollectingItem:
			this.collectItemFromStorageShelf(warehouse);
			break;

		case ReturningItem:
			this.returnItemToPackingStation(warehouse);
			break;

		case Charging:
			this.charge();
			break;

		case GoingToCharge:
			this.move(warehouse.getFloor(), this.chargingPod.getLocation());
			break;

		}
	}

	/**
	 * Increases the robots power units
	 */
	public void charge() {
		if (this.powerUnits + powerUnitsChargeSpeed >= powerUnitsCapacity) {
			this.powerUnits = powerUnitsCapacity;
			this.log("Fully charged to %s", this.powerUnits);
		} else {
			this.powerUnits += powerUnitsChargeSpeed;
			this.log("Charge increased to %s.", this.powerUnits);
		}
	}

	private void collectItemFromStorageShelf(Warehouse warehouse) throws Exception {
		this.move(warehouse.getFloor(), this.storageShelf.getLocation());
		this.log("Moved closer to assigned Storage Shelf.");

		if (this.location.equals(this.storageShelf.getLocation())) {
			this.holdingItem = this.storageShelf;
			this.storageShelf = null;
			this.log("Reached assigned Storage Shelf.");
		}
	}

	private void returnItemToPackingStation(Warehouse warehouse) throws Exception {
		this.move(warehouse.getFloor(), this.packingStation.getLocation());
		this.log("Moved closer to assigned Packing Station.");

		if (this.location.equals(this.packingStation.getLocation())) {
			this.packingStation.recieveItem(this.holdingItem);
			this.packingStation = null;
			this.holdingItem = null;
			this.log("Reached assigned Packing Station.");
		}
	}

	protected void move(Floor floor, Location targetLocation) throws Exception {
		super.move(floor, targetLocation);

		int powerUnitsToDeduct = this.hasItem() ? POWER_UNITS_CARRYING : POWER_UNITS_EMPTY;
		this.powerUnits -= powerUnitsToDeduct;

		this.log("Power Units (post move): %s.", this.powerUnits);
	}

	/**
	 * @param storageShelf
	 * @param packingStation
	 * @param warehouse
	 * @return
	 * @throws LocationNotValidException
	 */
	public boolean acceptJob(StorageShelf storageShelf, PackingStation packingStation, Warehouse warehouse)
			throws LocationNotValidException {

		if (this.storageShelf != null || this.hasItem())
			return false;

		double estimatedCostWithLeeway = estimatePowerUnitCostForJob(storageShelf, packingStation, warehouse);

		if (estimatedCostWithLeeway > this.powerUnits)
			return false;

		this.storageShelf = storageShelf;
		this.packingStation = packingStation;
		this.pathFinder = new PathFindingStrategy(warehouse.getFloor());

		this.log("Accepted Job to %s then %s.", storageShelf.getLocation(), packingStation.getLocation());

		return true;
	}

	/*
	 * Given a storage shelf and packing station this will calculate the number of
	 * power units to make the trip back to its charging pod with a leeway of 20%.
	 * 
	 * @param storageShelf
	 * 
	 * @param packingStation
	 * 
	 * @param warehouse
	 * 
	 * @return
	 * 
	 * @throws LocationNotValidException
	 */
	private double estimatePowerUnitCostForJob(StorageShelf storageShelf, PackingStation packingStation,
			Warehouse warehouse) throws LocationNotValidException {
		PathFindingStrategy tempPathFinder = new PathFindingStrategy(warehouse.getFloor(), false);

		tempPathFinder.calculatePath(this.getLocation(), storageShelf.getLocation());
		int numberOfMovesToStorageShelf = tempPathFinder.getNumberOfRemainingSteps();

		tempPathFinder.calculatePath(storageShelf.getLocation(), packingStation.getLocation());
		int numberOfMovesToPackingStation = tempPathFinder.getNumberOfRemainingSteps();

		tempPathFinder.calculatePath(packingStation.getLocation(), this.chargingPod.getLocation());
		int numberOfMovesToChargingStation = tempPathFinder.getNumberOfRemainingSteps();

		double unlaidenedCost = (numberOfMovesToStorageShelf + numberOfMovesToChargingStation) * POWER_UNITS_EMPTY;
		double carryingCost = (numberOfMovesToPackingStation) * POWER_UNITS_CARRYING;

		double estimatedCostWithLeeway = (unlaidenedCost + carryingCost) * 1.2;
		return estimatedCostWithLeeway;
	}

	/**
	 * Determines the robot's status based on its current state.
	 */
	public RobotStatus getStatus() {
		boolean isAtChargingPod = this.location.equals(this.chargingPod.getLocation());
		boolean isBatteryBelowHalfCharge = this.powerUnits < (powerUnitsCapacity * 0.5);

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
		return powerUnits;
	}

	public Location getPreviousLocation() {
		return previousLocation;
	}

	/**
	 * @return A string representation of a robot.
	 */
	public String toString() {
		String result = super.toString();
		result += ", " + "Status: " + this.getStatus();
		result += ", " + "Power: " + this.getPowerUnits();
		if (this.storageShelf != null)
			result += ", " + "Storage Shelf: " + this.storageShelf.getUID();
		if (this.packingStation != null)
			result += ", " + "Packing Station: " + this.packingStation.getUID();

		return result;
	}

	public void setPowerUnits(int units) {
		powerUnits = units;
	}

}
