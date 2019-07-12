package model;

import java.text.MessageFormat;

public class Robot extends Entity implements Actor {
	private int powerUnits;
	private StorageShelf storageShelf;
	private PackingStation packingStation;
	private ChargingPod chargingPod;

	private PathFindingStrategy pathFinder;
	private Location previousLocation;

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

		POWER_UNITS_EMPTY = 1;
		POWER_UNITS_CARRYING = 2;
	}

	@Override
	public void tick(Warehouse warehouse) {
		try {
			Status status = this.getStatus();

			switch (status) {
			case CollectingItem:
				move(warehouse, this.storageShelf.getLocation());
				this.collectItemFromStorageShelf(warehouse);
				break;

			case ReturningItem:
				move(warehouse, this.packingStation.getLocation());
				this.returnItemToPackingStation(warehouse);
				break;

			default:
				// Status: Charging or GoingToCharge
				if (powerUnits < (warehouse.getMaxChargeCapacity() / 2)) {
					charge(warehouse.getChargeSpeed());
				} else {
					move(warehouse, this.chargingPod.getLocation());
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Increases the robots power units
	 */
	public void charge(int chargeSpeed) {
		this.powerUnits += chargeSpeed;
		this.log("Charge increased to " + this.powerUnits);
	}

	private void collectItemFromStorageShelf(Warehouse warehouse) throws Exception {
		this.move(warehouse, this.storageShelf.getLocation());
		this.storageShelf = null;
	}

	private void returnItemToPackingStation(Warehouse warehouse) throws Exception {
		this.move(warehouse, this.packingStation.getLocation());
		this.packingStation.recieveItem(storageShelf);
		this.packingStation = null;
	}

	/**
	 * Moves the robot to another location depending on whether it needs to charge
	 * to deliver an item
	 * 
	 * @throws Exception
	 * 
	 */
	private void move(Warehouse warehouse, Location targetLocation) throws Exception {
		this.log(String.format("Moving from %s to %s.", this.location, targetLocation));
		this.log("Power Units (pre move): " + this.powerUnits);

		this.pathFinder.calculatePath(this.location, targetLocation);

		this.log("Path: " + this.pathFinder.getPath());

		Location newLocation = this.pathFinder.getNextLocation();

		boolean successfulMove = warehouse.getFloor().moveEntity(this.location, newLocation);
		if (!successfulMove)
			return;
		// TODO handle this better!

		this.previousLocation = this.location;
		this.location = newLocation;

		int powerUnitsToDeduct = this.hasItem() ? POWER_UNITS_CARRYING : POWER_UNITS_EMPTY;
		this.powerUnits -= powerUnitsToDeduct;

		this.log("Power Units (post move): " + this.powerUnits);
	}

	/**
	 * @param storageShelf
	 * @param packingStation
	 * @return
	 * @throws LocationNotValidException
	 */
	public boolean acceptJob(StorageShelf storageShelf, PackingStation packingStation, Warehouse warehouse) {
		// TODO check various condition whether the robot can accept a job.
		// TODO Check that the robots current charge is enough to get it from...
		// - its current location to the requested shelf (costs 1 per move)
		// - from that shelf to the packing station (costs 2 per move)
		// - from that packing station to the charging pod (costs 1 per move)

		boolean acceptJob = !this.hasItem();
		if (acceptJob) {
			this.storageShelf = storageShelf;
			this.packingStation = packingStation;
			this.pathFinder = new PathFindingStrategy(warehouse.getFloor());
		}
		return acceptJob;
	}

	/*
	 * Determines the robot's status based on its current state.
	 */
	public Status getStatus() {
		if (this.location.equals(chargingPod.getLocation()))
			return Status.Charging;
		else if (this.storageShelf != null)
			return Status.CollectingItem;
		else if (this.hasItem())
			return Status.ReturningItem;
		else
			return Status.GoingToCharge;
	}

	/**
	 * Checks to see if the robot is carrying an item
	 * 
	 * @return boolean
	 */
	public boolean hasItem() {
		return this.storageShelf == null && this.packingStation != null;
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
		return MessageFormat.format(
				"Robot:" + " - UID: {0}" + " - {1}" + " - Power Units: {2}" + " - StorageShelf: {3}"
						+ " - Packing Station: {4}" + " - Charging Pod: {5}",
				this.uid, this.location, this.powerUnits, this.storageShelf.getUID(), this.packingStation.getUID(),
				this.chargingPod.getUID());
	}

	public void setPowerUnits(int units) {
		powerUnits = units;
	}

}
