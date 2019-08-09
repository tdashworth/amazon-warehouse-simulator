package model;

import javafx.scene.Node;
import utils.PathFindingStrategy;

/**
 * A model of something that moves during the simulation.
 */
public abstract class Mover extends Actor {
  protected PathFindingStrategy pathFinder;
	protected Location previousLocation;

	public Mover(String uid, Location location, Node sprite) {
		super(uid, location, sprite);
	}

  public abstract void tick(Warehouse warehouse) throws Exception;
  
  /**
	 * Moves the robot to another location depending on whether it needs to charge
	 * to deliver an item
	 * 
	 * @throws Exception
	 * 
	 */
	protected void move(Floor floor, Location targetLocation) throws Exception {
		this.log("Moving from %s to %s.", this.location, targetLocation);

		boolean pathFound = this.pathFinder.calculatePath(this.location, targetLocation);

		if (!pathFound) {
			this.log("No path found.");
			return;
		}

		// this.log("Path: " + this.pathFinder.getPath());

		Location newLocation = this.pathFinder.getNextLocation();

		boolean successfulMove = floor.moveEntity(this.location, newLocation);
		if (!successfulMove) {
			this.log("Unable to make move to ", newLocation.toString());
			throw new Exception("Unable to make move to " + newLocation.toString());
		}

		this.previousLocation = this.location;
		this.location = newLocation;
	}
}
