/**
 * 
 */
package model;

import java.text.MessageFormat;

/**
 *
 */
public class ChargingPod extends Entity implements Actor {
	private Robot robot;
	private int chargeSpeed;
	
	/**
	 * @param uid
	 * @param location
	 */
	public ChargingPod(String uid, Location location, Robot robot, int chargeSpeed) {
		super(uid, location);
		this.robot = robot;
		this.chargeSpeed = chargeSpeed;
	}
	
	public void tick(Floor floor) {
		if (!this.robot.getLocation().equals(this.location))
			return;
		
		this.robot.charge(this, this.chargeSpeed);
	}
	
	/**
	 *	@return A string representation of a charging pod.
	 */
	public String toString() {
		return MessageFormat.format("Charging Pod:"
				+ " - UID: {0}"
				+ " - {1}.", 
				this.uid, 
				this.location);
	}

}
