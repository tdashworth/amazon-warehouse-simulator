
package main.amazon;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import main.simulation.AEntity;
import main.simulation.Location;

/**
 * A class to represent a storage shelf.
 */
public class StorageShelf extends AEntity {

	/**
	 * @param uid
	 * @param location
	 */
	public StorageShelf(String uid, Location location) {
		super(uid, location, new Rectangle(35, 35, Color.DARKSALMON));
	}
}
