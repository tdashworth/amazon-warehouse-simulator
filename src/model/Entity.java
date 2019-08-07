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
		String classType = this.getClass().getSimpleName();

		System.out.println(String.format("%s(%s): %s", classType, this.uid, message));
	}
	
	protected void log(String format, Object... args) {
		this.log(String.format(format, args));
	}
	
	
	/**
	 *	@return A string representation of a charging pod.
	 */
	public String toString() {
		String className = this.getClass().getSimpleName();
		return String.format("%s(%s) - %s", className, this.uid, this.location);
	} 
	
}
