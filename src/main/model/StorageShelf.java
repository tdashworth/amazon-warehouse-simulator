
package main.model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * A class to represent a storage shelf.
 */
public class StorageShelf extends AbstractEntity {

	/**
	 * @param uid
	 * @param location
	 */
	public StorageShelf(String uid, Location location) {
		super(uid, location, new Rectangle(35, 35, Color.DARKSALMON));
	}
}
