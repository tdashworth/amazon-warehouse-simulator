package simulator.utils;

import simulator.Location;

public interface IPathCostEstimator {
  /**
   * Add a cost (double) per step in the path with a given label (String).
   * 
   * @param label used to associate for each location added.
   * @param value the cost per move in the path between two locations.
   * @throws Exception
   */
  public void addCost(String label, Double value) throws Exception;

  /**
   * Add a stop to estimate travelling to with a given cost factor. Note, each stop is added is
   * travelled to sequentially.
   * 
   * @param location  the Location to travel to from the
   * @param costLabel the label of the cost factoring to use.
   */
  public void addLocation(Location location, String costLabel) throws Exception;

  /**
   * Calculates and returns the estimated cost to travel to the previously added Locations via
   * addLocation().
   */
  public double getEstimatedCost();
}
