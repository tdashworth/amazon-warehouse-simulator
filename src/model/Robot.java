package model;

import java.text.MessageFormat;

public class Robot extends Entity implements Actor {
	private int powerUnits;
	private StorageShelf storageShelf;
	private boolean storageShelfVisited;
	private boolean packingStationVisited;
	private PackingStation packingStation;
	private ChargingPod chargingPod;
	private PathFindingStrategy pathFinder;

	private static int POWER_UNITS_EMPTY;
	private static int POWER_UNITS_CARRYING;

	/**
	 * @param uid
	 * @param location
	 */
	public Robot(String uid, Location location, ChargingPod chargingPod, int powerUnits) {
		super(uid, location);
		this.chargingPod = chargingPod;
		this.powerUnits = powerUnits;
		this.pathFinder = new PathFindingStrategy();
	}

	@Override
	public void tick(Warehouse warehouse) {
		try {
			if (this.location.equals(chargingPod.getLocation()) && powerUnits < (warehouse.getMaxChargeCapacity()/2) )
				charge(warehouse.getChargeSpeed());
			else if (this.storageShelfVisited == false)
				move(warehouse, this.storageShelf.getLocation());
			else if (this.packingStationVisited == false)
				move(warehouse, this.packingStation.getLocation());
			else if (!this.location.equals(chargingPod.getLocation()))
				move(warehouse, this.chargingPod.getLocation());
			else if (this.powerUnits < warehouse.getMaxChargeCapacity())
				charge(warehouse.getChargeSpeed());
			else
				; //Wait...
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Increases the robots power units
	 */
	public void charge(int chargeSpeed) {
		// need to get charge speed from simulator
		powerUnits += chargeSpeed;
	}

	/**
	 * Moves the robot to another location depending on whether it needs to charge
	 * to deliver an item
	 * 
	 * @throws Exception
	 * 
	 */
	private void move(Warehouse warehouse, Location targetLocation) throws Exception {
		//TESTING PATH FINDING
		System.out.println("\n\n-----<Call to getNextMove>-----");
		System.out.println("Current " + this.location);
		System.out.println("Target " + targetLocation);
		System.out.println("Path: " + this.pathFinder.getNextMove(this.location, targetLocation, warehouse));
		
		Location newLocation = this.pathFinder.getNextMove(this.location, targetLocation, warehouse).get(1);
		boolean successfulMove = warehouse.getFloor().moveEntity(this.location, newLocation);

		if (successfulMove) {
			this.location = newLocation;
			
			if(this.location.equals(this.storageShelf.getLocation()))
				this.storageShelfVisited = true;
			if(this.location.equals(this.packingStation.getLocation())) {
				this.packingStationVisited = true;
				packingStation.recieveItem(storageShelf);
			}

			int powerUnitsToDeduct = this.hasItem() ? POWER_UNITS_CARRYING : POWER_UNITS_EMPTY;
			this.powerUnits = this.powerUnits - powerUnitsToDeduct;
		}
	}

	/**
	 * @param storageShelf
	 * @param packingStation
	 * @return
	 */
	public boolean acceptJob(StorageShelf storageShelf, PackingStation packingStation) {
		// TODO check various condition whether the robot can accept a job.
		boolean acceptJob = !this.hasItem() || this.storageShelf == null;
		if (acceptJob) {
			this.storageShelf = storageShelf;
			this.packingStation = packingStation;
			this.storageShelfVisited = false;
			this.packingStationVisited = false;
		}
		return acceptJob;
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

}
