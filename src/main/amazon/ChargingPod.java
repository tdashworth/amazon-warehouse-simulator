package main.amazon;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import main.simulation.AEntity;
import main.simulation.Location;

/**
 * A class to represent a charging pod.
 */
public class ChargingPod extends AEntity {

	/**
	 * @param uid
	 * @param location
	 */
	public ChargingPod(String uid, Location location) {
		super(uid, location, new Rectangle(35, 35, Color.GOLD));
	}

}
