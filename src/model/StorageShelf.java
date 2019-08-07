/**
 * 
 */
package model;


/**
 *
 */
public class StorageShelf extends Entity implements Actor {

	/**
	 * @param uid
	 * @param location
	 */
	public StorageShelf(String uid, Location location) {
		super(uid, location);
	}
	
	/**
	 *	@return A string representation of the storage shelf.
	 */
	public String toString() {
		return super.toString();
	}
	
	public void tick(Warehouse warehouse) {
		
	}
}
