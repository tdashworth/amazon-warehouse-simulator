/**
 * 
 */
package model;

import java.text.MessageFormat;

/**
 *
 */
public class Robot extends Entity implements Actor {
	private int powerUnits;
	private StorageShelf storageShelf;
	private PackingStation packingStation;
	private ChargingPod chargingPod;
	private boolean hasItem;

	private static int POWER_UNITS_EMPTY;
	private static int POWER_UNITS_CARRYING;

	/**
	 * @param uid
	 * @param location
	 * TO DO - do we need to set the initial power units to 100%?
	 */
	public Robot(String uid, Location location, ChargingPod chargingPod) {
		super(uid, location);
		this.chargingPod = chargingPod;
		hasItem = false;
	}

	@Override
	public void tick(Warehouse warehouse) {
		// TODO determine "state" of robot and call the next action to perform. - not sure how to access the max charge capacity

		try {
			if (false)
				move(warehouse);
			//else if (location.equals(chargingPod.location) && powerUnits < (maxChargeCapacity/2) )
			//charge(chargeSpeed);
			else
				;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Moves the robot to another location depending on whether it needs to charge to deliver an item
	 * @throws Exception 
	 * 
	 */
	private void move(Warehouse warehouse) throws Exception {
		// TODO ask path finding module for next move and update its location.
		//for testing purposes it would be nice to be able to retrieve the number of moves so we can see if it 
		//used the right amount of power units, could this method return an int or would that be impractical?
		int numberOfSteps = 0;
		Location newLocation = null;

		if(hasItem) {
			powerUnits -= numberOfSteps * 2;
			//go to packing station
			boolean successfulMove = warehouse.getFloor().moveEntity(this.location, newLocation);

			if (successfulMove)
				this.location = newLocation;
		}

		else {
			//go to charging pod
			boolean successfulMove = warehouse.getFloor().moveEntity(this.location, newLocation);

			if (successfulMove)
				this.location = newLocation;
			powerUnits -= numberOfSteps;
		}
		}

	/**
	 * Increases the robots power units 
	 */

	//need to get charge speed from simulator
	public void charge(int chargeSpeed) {
		powerUnits += chargeSpeed;

	}

	/**
	 * Set initial power units 
	 * @param maximum charge capacity
	 */
	public void setPowerUnits(int maxChargeCapacity) {
		powerUnits = maxChargeCapacity;		
	}


	/**
	 * Get the power units 
	 */
	public int getPowerUnits() {
		return powerUnits;
	}

	/**
	 * @param storageShelf
	 * @param packingStation
	 * @return
	 */
	public boolean acceptJob(StorageShelf storageShelf, PackingStation packingStation) {
		// TODO check various condition whether the robot can accept a job.
		if(true) {
			this.storageShelf = storageShelf;
			hasItem = true;
		}
		return false;
	}

	/**
	 * Checks to see if the robot is carrying an item
	 * 
	 * @return boolean
	 */
	public boolean hasItem() {
		return hasItem;
	}
	
	
	/**
	 * @return A string representation of a robot.
	 */
	public String toString() {
		return MessageFormat.format("Robot:"
				+ " - UID: {0}"
				+ " - {1}"
				+ " - Power Units: {2}"
				+ " - StorageShelf: {3}"
				+ " - Packing Station: {4}"
				+ " - Charging Pod: {5}",
				this.uid, 
				this.location, 
				this.powerUnits,
				this.storageShelf.getUID(),
				this.packingStation.getUID(),
				this.chargingPod.getUID());
	}

}
