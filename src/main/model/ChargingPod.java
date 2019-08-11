package main.model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * A class to represent a charging pod.
 */
public class ChargingPod extends AbstractEntity {

	/**
	 * @param uid
	 * @param location
	 */
	public ChargingPod(String uid, Location location) {
		super(uid, location, new Rectangle(35, 35, Color.GOLD));
	}

}
