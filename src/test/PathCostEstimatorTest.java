package test;

import org.junit.Test;
import static org.junit.Assert.*;
import utils.PathCostEstimator;
import utils.BasicPathCostEstimator;
import model.Location;

public class PathCostEstimatorTest {

  @Test
  public void constructorTest() {
    Location origin = new Location(0, 0);

    PathCostEstimator pathCostEstimator = new BasicPathCostEstimator(origin);

    assertEquals(0.0, pathCostEstimator.getEstimatedCost(), 0.0);
  }

  @Test
  public void addCostTest() {
    Location origin = new Location(0, 0);
    PathCostEstimator pathCostEstimator = new BasicPathCostEstimator(origin);

    // Positive case
    try {
      pathCostEstimator.addCost("cost1", 1.0);
      pathCostEstimator.addCost("cost2", 2.2);
    } catch (Exception e) {
      fail(e.toString());
    }

    // Negative case
    try {
      pathCostEstimator.addCost(null, 1.0);
      fail("Adding a cost with a null label is forbiden and should fail.");
    } catch (Exception e) {
      assertEquals("The parameter 'label' must not be null.", e.getMessage());
    }

    try {
      pathCostEstimator.addCost("cost2", null);
      fail("Adding a cost with a null value is forbiden and should fail.");
    } catch (Exception e) {
      assertEquals("The parameter 'value' must not be null.", e.getMessage());
    }
  }

  @Test
  public void addLocationTest() throws Exception {
    Location origin = new Location(0, 0);
    PathCostEstimator pathCostEstimator = new BasicPathCostEstimator(origin);
    pathCostEstimator.addCost("cost1", 1.0);
    
    // Positive case
    try {
      pathCostEstimator.addLocation(new Location(0, 1), "cost1");
    } catch (Exception e) {
      fail(e.toString());
    }

    // Negative case
    try {
      pathCostEstimator.addLocation(null, "cost1");
      fail("Adding a location with a null value is forbiden and should fail.");
    } catch (Exception e) {
      assertEquals("The parameter 'location' must not be null.", e.getMessage());
    }

    try {
      pathCostEstimator.addLocation(new Location(0, 1), null);
      fail("Adding a location with a null cost label is forbiden and should fail.");
    } catch (Exception e) {
      assertEquals(
          "Cost label 'null' has not been set. Use the addCost method to set it before using it here.",
          e.getMessage());
    }

    try {
      pathCostEstimator.addLocation(new Location(0, 1), "cost2");
      fail("Adding a location with cost label of 'cost2' should fail becasue it doesn't exist.");
    } catch (Exception e) {
      assertEquals(
          "Cost label 'cost2' has not been set. Use the addCost method to set it before using it here.",
          e.getMessage());
    }

  }
}
