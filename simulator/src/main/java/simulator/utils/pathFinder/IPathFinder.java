package simulator.utils.pathFinder;

public interface IPathFinder {
  /**
   * Returns the next location in the path to reach the target location.
   * 
   * @return the next valid Location in the path to the target.
   */
  public PathFindingNode getNextLocation() throws Exception;

  /**
   * Returns the number of the step in the path to target.
   * 
   * @return the number of steps remaining
   */
  public int getNumberOfRemainingSteps();
}