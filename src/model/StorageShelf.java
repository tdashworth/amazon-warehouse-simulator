/**
 * 
 */
package model;

import java.text.MessageFormat;

/**
 *
 */
public class StorageShelf extends Entity {

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
		return MessageFormat.format("Storage Shelf:"
				+ " - UID: {0}"
				+ " - {1}.", 
				this.uid, 
				this.location);
	}
}
