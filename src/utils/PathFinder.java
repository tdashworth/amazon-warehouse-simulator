package utils;

import java.util.Deque;
import model.Floor;
import model.Location;
import model.LocationNotValidException;

public abstract class PathFinder {
  protected final Floor floor;
  protected Location currentLocation;
  protected final Location targetLocation;
  protected Deque<Location> path;

  public PathFinder(Floor floor, Location origin, Location target)
      throws LocationNotValidException {
    this.floor = floor;
    this.currentLocation = origin;
    this.targetLocation = target;
  }

  /**
   * @return the next valid Location in the path to the target.
   */
  public Location getNextLocation() throws Exception {
    if (this.path == null)
      this.calculatePath(this.currentLocation, this.targetLocation);

    Location nextLocation = this.path.poll();

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
