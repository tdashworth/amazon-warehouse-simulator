package main.simulation;

import javafx.scene.Node;
import main.utils.AStarPathFinder;
import main.utils.AbstractPathFinder;

/**
 * A.amazon of something that moves during the simulation.
 */
public abstract class AMover<W extends AWorld> extends AEntity implements IActor<W> {
	protected AbstractPathFinder pathFinder;
	protected Location previousLocation;

	public AMover(String uid, Location location, Node sprite) {
		super(uid, location, sprite);
	}

	public void tick(W world, int currentTickCount) throws Exception {
		this.previousLocation = this.location;
	};
	
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
