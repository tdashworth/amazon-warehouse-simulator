package main.model;

import javafx.scene.Node;
import main.utils.AStarPathFinder;
import main.utils.AbstractPathFinder;

/**
 * A model of something that moves during the simulation.
 */
public abstract class AbstractMover extends AbstractActor {
	protected AbstractPathFinder pathFinder;
	protected Location previousLocation;

	public AbstractMover(String uid, Location location, Node sprite) {
		super(uid, location, sprite);
	}

	public abstract void tick(Warehouse warehouse, int currentTickCount) throws Exception;

	/**
	 * Moves to another location on the floor.
	 * 
	 * @throws Exception
	 */
	protected void move(Floor floor, Location targetLocation) throws Exception {
		if (this.location.equals(targetLocation))
			return;
			
		this.log("Moving from %s to %s.", this.location, targetLocation);

		try {
			if (this.pathFinder == null)
				this.pathFinder = new AStarPathFinder(floor, this.location, targetLocation);

			Location newLocation = this.pathFinder.getNextLocation();
			floor.moveEntity(this.location, newLocation);

			this.previousLocation = this.location;
			this.location = newLocation;
		} catch (Exception ex) {
			this.log("Failed to move.");
			throw new Exception("Unable to make move because " + ex); // TODO Custom Exception
		}

		if (this.location.equals(targetLocation))
			this.pathFinder = null;
	}

	public Location getPreviousLocation() {
		return previousLocation;
	}
}
