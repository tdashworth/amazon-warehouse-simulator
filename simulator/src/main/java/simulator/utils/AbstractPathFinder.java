package simulator.utils;

import java.util.Deque;

import simulator.Floor;
import simulator.Location;
import simulator.LocationNotValidException;

public abstract class AbstractPathFinder {
  protected final Floor floor;
  protected PathFindingNode currentLocation;
  protected final PathFindingNode targetLocation;
  protected Deque<PathFindingNode> path;

  public AbstractPathFinder(Floor floor, Location origin, Location target)
      throws LocationNotValidException {
    this.floor = floor;
    this.currentLocation = new PathFindingNode(origin);
    this.targetLocation = new PathFindingNode(target);
  }

  /**
   * @return the next valid Location in the path to the target.
   */
  public PathFindingNode getNextLocation() throws Exception {
    if (this.path == null)
      this.calculatePath(this.currentLocation, this.targetLocation);

    PathFindingNode nextLocation = this.path.poll();

    if (nextLocation == null || !this.floor.isLocationValidAndEmpty(nextLocation)) {
      this.calculatePath(this.currentLocation, this.targetLocation);
      nextLocation = this.path.poll();
    }

    if (nextLocation == null)
      throw new Exception("Next step could not be found.");

    this.currentLocation = nextLocation;
    return nextLocation;
  }

  public int getNumberOfRemainingSteps() {
    if (this.path == null)
      this.calculatePath(this.currentLocation, this.targetLocation);

    return this.path.size();
  }

  protected abstract void calculatePath(Location origin, Location target);
}
