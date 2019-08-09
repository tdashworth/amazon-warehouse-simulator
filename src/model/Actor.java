package model;

import javafx.scene.Node;

/**
 * A model of something that acts per tick of the simulation.
 */
public abstract class Actor extends Entity {

	public Actor(String uid, Location location, Node sprite) {
		super(uid, location, sprite);
	}

  /**
	 * This is to execute on every tick/step of the simulation allowing each Actor to make their "turn".
	 * @param warehouse
	 * @throws Exception 
	 */
	public abstract void tick(Warehouse warehouse) throws Exception;
}
