package simulator.utils.pathFinder;

import java.util.Deque;

import simulator.Floor;
import simulator.Location;
import simulator.LocationNotValidException;

public abstract class AbstractPathFinder implements IPathFinder {
  protected final Floor floor;
  private PathFindingNode currentLocation;
  private final PathFindingNode targetLocation;
  private Deque<PathFindingNode> path;

  public AbstractPathFinder(Floor floor, Location origin, Location target) throws LocationNotValidException {
    this.floor = floor;
    this.currentLocation = new PathFindingNode(origin);
    this.targetLocation = new PathFindingNode(target);
  }

  /**
   * Returns the next location in the path to reach the target location. If the
   * path hasn't yet been calculated or if the next location isn't valid, the path
   * will be recalculated from the current location to target.
   * 
   * @return the get next location
   */
  public PathFindingNode getNextLocation() throws Exception {
    if (this.path == null)
      this.path = this.calculatePath(this.currentLocation, this.targetLocation);

    PathFindingNode nextLocation = this.path.poll();

    if (nextLocation == null || !this.floor.isLocationValidAndEmpty(nextLocation)) {
      this.path = this.calculatePath(this.currentLocation, this.targetLocation);
      nextLocation = this.path.poll();
    }

    if (nextLocation == null)
      throw new Exception("Next step could not be found.");

    this.currentLocation = nextLocation;
    return nextLocation;
  }

  /**
   * Returns the number of the step in the CURRENT path to target. The path can
   * change as its travsered so don't expect this value to decrement consistently.
   * 
   * @return the number of steps remaining
   */
  public int getNumberOfRemainingSteps() {
    if (this.path == null)
      this.path = this.calculatePath(this.currentLocation, this.targetLocation);

    return this.path.size();
  }

  protected abstract Deque<PathFindingNode> calculatePath(Location origin, Location target);
}
