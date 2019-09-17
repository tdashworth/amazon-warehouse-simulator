package simulator.utils;

import java.util.*;

import simulator.Location;

public class BasicPathCostEstimator implements IPathCostEstimator {
  private final HashMap<String, Double> costFactors;
  private Location currentLocation;
  private Double estimatedCost = 0.0;

  public BasicPathCostEstimator(Location origin) {
    this.costFactors = new HashMap<String, Double>();
    this.currentLocation = origin;
  }

  @Override
  public void addCost(String label, Double value) throws Exception {
    if (label == null)
      throw new Exception("The parameter 'label' must not be null.");

    if (value == null)
      throw new Exception("The parameter 'value' must not be null.");

    this.costFactors.put(label, value);
  }

  @Override
  public void addLocation(Location location, String costLabel) throws Exception {
    if (location == null)
      throw new Exception("The parameter 'location' must not be null.");

    if (!this.costFactors.containsKey(costLabel))
      throw new Exception("Cost label '" + costLabel
          + "' has not been set. Use the addCost method to set it before using it here.");

    Double costValue = this.costFactors.get(costLabel);
    this.estimatedCost += this.currentLocation.getManhattanDistanceTo(location) * costValue;
    this.currentLocation = location;
  }

  @Override
  public double getEstimatedCost() {
    return this.estimatedCost;
  }
}
