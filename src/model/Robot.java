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

	private static int POWER_UNITS_EMPTY;
	private static int POWER_UNITS_CARRYING;

	/**
	 * @param uid
	 * @param location
	 */
	public Robot(String uid, Location location, ChargingPod chargingPod) {
		super(uid, location);
		this.chargingPod = chargingPod;
	}

	@Override
	public void tick(Warehouse warehouse) {
		// TODO determine "state" of robot and call the next action to perform.
	}

	/**
	 * 
	 */
	private void move() {
		// TODO ask path finding module for next move and update its location.
	}

	/**
	 * 
	 */
	private void charge() {
		// TODO increase power units by global amount only when on changing pod.
	}

	/**
	 * @param storageShelf
	 * @param packingStation
	 * @return
	 */
	public boolean acceptJob(StorageShelf storageShelf, PackingStation packingStation) {
		// TODO check various condition whether the robot can accept a job.
		return false;
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
