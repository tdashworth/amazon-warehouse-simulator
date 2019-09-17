package simulator.utils;

import java.util.*;

import simulator.Location;

public class BasicPathCostEstimator<F extends Enum<F>> implements IPathCostEstimator<F> {
  private final HashMap<F, Double> costFactors;
  private Location currentLocation;
  private Double estimatedCost = 0.0;

  public BasicPathCostEstimator(Location origin) {
    this.costFactors = new HashMap<F, Double>();
    this.currentLocation = origin;
  }

  @Override
  public void addCost(F factor, Double value) throws Exception {
    if (factor == null)
      throw new Exception("The parameter 'factor' must not be null.");

    if (value == null)
      throw new Exception("The parameter 'value' must not be null.");

    this.costFactors.put(factor, value);
  }

  @Override
  public void addLocation(Location location, F factor) throws Exception {
    if (location == null)
      throw new Exception("The parameter 'location' must not be null.");

    if (factor == null)
      throw new Exception("The parameter 'factor' must not be null.");

    if (!this.costFactors.containsKey(factor))
      throw new Exception(
          "Cost factor '" + factor.name() + "' has not been set. Use the addCost method to set it before using it here.");

    Double costValue = this.costFactors.get(factor);
    this.estimatedCost += this.currentLocation.getManhattanDistanceTo(location) * costValue;
    this.currentLocation = location;
  }

  @Override
  public double getEstimatedCost() {
    return this.estimatedCost;
  }
}
