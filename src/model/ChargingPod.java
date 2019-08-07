/**
 * 
 */
package model;

/**
 *A class to represent a charging pod.
 */
public class ChargingPod extends Entity {

	/**
	 * @param uid
	 * @param location
	 */
	public ChargingPod(String uid, Location location) {
		super(uid, location);
	}
	
	/**
	 *	@return A string representation of a charging pod.
	 */
	public String toString() {
		return super.toString();
	}

}
