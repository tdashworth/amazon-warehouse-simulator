/**
 * 
 */
package model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 */
public class StorageShelf extends Entity {

	/**
	 * @param uid
	 * @param location
	 */
	public StorageShelf(String uid, Location location) {
		super(uid, location, new Rectangle(35, 35, Color.DARKSALMON));
	}
	
	/**
	 *	@return A string representation of the storage shelf.
	 */
	public String toString() {
		return super.toString();
	}
}
