package model;

/**
 *	A model a something that physically exists on the warehouse floor.
 */
public abstract class Entity {
	// Stores the UID in the string format (S)SNN. 
	protected String uid;
	// Stores the current location of the entity on the warehouse floor.
	protected Location location;
	
	/**
	 * A model a something that physically exists on the warehouse floor.
	 * @param uid
	 * @param location
	 */
	public Entity(String uid, Location location) {
		this.uid = uid;
		this.location = location;
	}
	
	/**
	 * @return The UID.
	 */
	public String getUID() {
		return this.uid;
	}
	
	/**
	 * @return The location.
	 */
	public Location getLocation() {
		return this.location;
	}
	
	protected void log(String message) {
		String classType = this.getClass().getName();

		System.out.println(String.format("%s %s: %s", classType, this.uid, message));
	}
}
