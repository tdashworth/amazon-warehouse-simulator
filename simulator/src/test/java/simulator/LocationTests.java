package simulator;

import org.junit.Test;
import simulator.Location;
import static org.junit.Assert.*;

public class LocationTests {

  @Test
  public void testConstructorWithValidParametersShouldSuccussfullyCreate() {
    Location location = new Location(4, 7);

    assertEquals(4, location.getColumn());
    assertEquals(7, location.getRow());
  }

  @Test
  public void testEqualsWithVariousCombinationsShouldReturnTrueOrFalseRespectively() {
    Location location1 = new Location(4, 7);
    Location location2 = new Location(4, 7);
    Location location3 = new Location(7, 4);
    Location location4 = new Location(-4, -7);

    assertTrue("Location is equal to itself.", location1.equals(location1));
    assertTrue("4, 7 is equal to 4, 7.", location1.equals(location2));
    assertFalse("4, 7 is not equal to 7, 4.", location1.equals(location3));
    assertFalse("4, 7 is not equal to null.", location1.equals(null));
    assertFalse("-4, -7 is not equal to 4, 7.", location4.equals(location2));
  }

  @Test
  public void testGetManhattanDistanceToVariousCombinationsShouldReturnCorrectly() {
    Location location1 = new Location(0, 0);
    Location location2 = new Location(4, 7);
    Location location3 = new Location(7, 4);
    Location location4 = new Location(-4, -7);

    assertEquals(11, location1.getManhattanDistanceTo(location2));
    assertEquals(6, location2.getManhattanDistanceTo(location3));
    assertEquals(11, location3.getManhattanDistanceTo(location1));
    assertEquals(0, location1.getManhattanDistanceTo(location1));
    assertEquals(22, location2.getManhattanDistanceTo(location4));
  }

  @Test
  public void testGetEuclideanDistanceToTestVariousCombinationsShouldReturnCorrectly() {
    Location location1 = new Location(0, 0);
    Location location2 = new Location(4, 7);
    Location location3 = new Location(7, 4);
    Location location4 = new Location(-4, -7);

    assertEquals(1.732, location1.getEuclideanDistanceTo(location2), 0.001);
    assertEquals(2.0, location2.getEuclideanDistanceTo(location3), 0.001);
    assertEquals(3.873, location3.getEuclideanDistanceTo(location1), 0.001);
    assertEquals(0, location1.getEuclideanDistanceTo(location1), 0.001);
    assertEquals(2.449, location2.getEuclideanDistanceTo(location4), 0.001);
  }
}
