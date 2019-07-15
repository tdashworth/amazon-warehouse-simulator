package model;

import java.text.MessageFormat;
import java.util.Observable;

public class Robot extends Entity implements Actor {
	private int powerUnits;
	private StorageShelf storageShelf;
	private StorageShelf holdingItem;
	private PackingStation packingStation;
	private ChargingPod chargingPod;

	private PathFindingStrategy pathFinder;
	private Location previousLocation;
	private Status status;
	private static int POWER_UNITS_EMPTY = 1;
	private static int POWER_UNITS_CARRYING = 2;

	public static enum Status {
		GoingToCharge, Charging, CollectingItem, ReturningItem
	}

	/**
	 * @param uid
	 * @param location
	 */
	public Robot(String uid, Location location, ChargingPod chargingPod, int powerUnits) {
		super(uid, location);
		this.chargingPod = chargingPod;
		this.powerUnits = powerUnits;
		this.status = Status.Charging;
	}

	@Override
	public void tick(Warehouse warehouse) {
		try {
			Status status = this.calculateStatus(warehouse);
			this.log("Ticking with status: %s.", status);

			switch (status) {
			case CollectingItem:
				this.collectItemFromStorageShelf(warehouse);
				break;

			case ReturningItem:
				this.returnItemToPackingStation(warehouse);
				break;

			case Charging:
				charge(warehouse.getChargeSpeed(), warehouse.getMaxChargeCapacity());
				break;

			case GoingToCharge:
				this.move(warehouse, this.chargingPod.getLocation());
				break;

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Increases the robots power units
	 */
	public void charge(int chargeSpeed, int maxCharge) {
		if (this.powerUnits + chargeSpeed >= maxCharge) {
			this.powerUnits = maxCharge;
			this.log("Fully charged to %s", this.powerUnits);
		} else {
			this.powerUnits += chargeSpeed;
			this.log("Charge increased to %s.", this.powerUnits);
		}
	}

	private void collectItemFromStorageShelf(Warehouse warehouse) throws Exception {
		this.move(warehouse, this.storageShelf.getLocation());
		this.log("Moved closer to assigned Storage Shelf.");

		if (this.location.equals(this.storageShelf.getLocation())) {
			this.holdingItem = this.storageShelf;
			this.storageShelf = null;
			this.log("Reached assigned Storage Shelf.");
		}
	}

	private void returnItemToPackingStation(Warehouse warehouse) throws Exception {
		this.move(warehouse, this.packingStation.getLocation());
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
	private void move(Warehouse warehouse, Location targetLocation) throws Exception {
		this.log("Moving from %s to %s.", this.location, targetLocation);

		boolean pathFound = this.pathFinder.calculatePath(this.location, targetLocation);

		if (!pathFound)
			return; // TODO handle better!

		// this.log("Path: " + this.pathFinder.getPath());

		Location newLocation = this.pathFinder.getNextLocation();

		boolean successfulMove = warehouse.getFloor().moveEntity(this.location, newLocation);
		if (!successfulMove)
			return; // TODO handle this better!

		this.previousLocation = this.location;
		this.location = newLocation;

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
	public Status calculateStatus(Warehouse warehouse) {
		boolean isAtChargingPod = this.location.equals(this.chargingPod.getLocation());
		boolean isBatteryBelowHalfCharge = this.powerUnits < (warehouse.getMaxChargeCapacity() * 0.5);

		if (isBatteryBelowHalfCharge && isAtChargingPod) {// Running low of powerUnits
			status = Status.Charging;
			return Status.Charging;
		}
		else if (this.storageShelf != null) {// Storage Shelf Assigned
			status = Status.CollectingItem;
			return Status.CollectingItem;
		}
		else if (this.hasItem()) {
			status = Status.ReturningItem;		
			return Status.ReturningItem; // Item collected
		}	
		else if (isAtChargingPod) {
			status = Status.Charging;		
			return Status.Charging; // Nothing to do and already at Charging Pod
		}
		else {
			status = Status.GoingToCharge;
			return Status.GoingToCharge; // Nothing to do so go charge
		}		
	}
	
	/**
	 *Returns the robot's current status 
	 * @return Status
	 */
	public Status getStatus() {
		return status;
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

		if(this.packingStation != null && this.storageShelf != null ) {
			return MessageFormat.format(
					"Robot:" + " - UID: {0}" + " - {1}" + " - Power: {2}" + " - StorageShelf: {3}"
							+ " - Packing Station: {4}" + " - Charging Pod: {5}" + " - Status: {6}" ,
							this.uid, this.location, this.powerUnits, this.storageShelf.getUID(), this.packingStation.getUID(),
							this.chargingPod.getUID(), this.status);
		}
		else { return MessageFormat.format(
				"Robot:" + " - UID: {0}" + " - {1}" + " - Power: {2}" + " - StorageShelf: null"
						+ " - Packing Station: null" + " - Charging Pod: {3}" + " - Status: {4} ",
						this.uid, this.location, this.powerUnits, this.chargingPod.getUID(), this.status);
		}
	}

	public void setPowerUnits(int units) {
		powerUnits = units;
	}

}
