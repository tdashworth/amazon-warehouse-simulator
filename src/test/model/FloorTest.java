package test.model;

import static org.junit.Assert.*;
import org.junit.Test;

import main.model.Floor;
import main.model.Location;
import main.model.LocationNotValidException;
import main.model.AbstractMover;
import main.model.Robot;

public class FloorTest {

  @Test
  public void constructorTest() {
    Floor floor = new Floor(5, 3);

    assertEquals(5, floor.getNumberOfColumns());
    assertEquals(3, floor.getNumberOfRows());
  }

  @Test
  public void isLocationValidTest() {
    Floor floor = new Floor(5, 3);

    // Positive case
    assertTrue(floor.isLocationValid(new Location(2, 1)));
    assertTrue(floor.isLocationValid(new Location(0, 0)));
    assertTrue(floor.isLocationValid(new Location(4, 2)));

    // Negative case
    assertFalse(floor.isLocationValid(new Location(-1, -1)));
    assertFalse(floor.isLocationValid(new Location(5, 3)));
  }

  @Test
  public void loadMoverTest() {
    Floor floor = new Floor(5, 3);
    AbstractMover robot1 = new Robot("r1", new Location(2, 1), null, 0, 0);
    AbstractMover robot2 = new Robot("r2", new Location(5, 5), null, 0, 0);
    AbstractMover robot3 = new Robot("r3", new Location(2, 1), null, 0, 0);

    // Positive case
    try {
      floor.loadMover(robot1);
    } catch (LocationNotValidException e) {
      fail(e.toString());
    }

    // Negative Case
    try {
      floor.loadMover(robot2);
      fail("Robot load should have failed as it's location isn't within the floor boundaries.");
    } catch (LocationNotValidException e) {
      assertEquals(
          "LocationNotValidException: Location: [5, 5] is invalid because it's outside of the floor boundaries.",
          e.toString());
    }

    try {
      floor.loadMover(robot3);
      fail("Robot load should have failed as there is something already at it's location.");
    } catch (LocationNotValidException e) {
      assertEquals(
          "LocationNotValidException: Location: [2, 1] is invalid because it's already occupied.",
          e.toString());
    }
  }

  @Test
  public void isLocationValidAndEmptyTest() throws LocationNotValidException {
    Floor floor = new Floor(5, 3);
    floor.loadMover(new Robot("r1", new Location(2, 1), null, 0, 0));

    // Positive case
    assertTrue(floor.isLocationValidAndEmpty(new Location(0, 0)));
    assertTrue(floor.isLocationValidAndEmpty(new Location(4, 2)));

    // Negative case
    assertFalse(floor.isLocationValidAndEmpty(new Location(2, 1)));
    assertFalse(floor.isLocationValidAndEmpty(new Location(-1, -1)));
  }
  
  @Test
  public void moveEntityTest() throws LocationNotValidException {
    Floor floor = new Floor(5, 3);
    floor.loadMover(new Robot("r1", new Location(1, 1), null, 0, 0));
    floor.loadMover(new Robot("r2", new Location(4, 2), null, 0, 0));

    // Positive case
    try {
      floor.moveEntity(new Location(1, 1), new Location(1, 2)); // r1 down
      floor.moveEntity(new Location(4, 2), new Location(3, 2)); // r2 left
      floor.moveEntity(new Location(1, 2), new Location(2, 2)); // r1 right
      floor.moveEntity(new Location(3, 2), new Location(3, 1)); // r2 up
    } catch (Exception e) {
      fail(e.toString());
    }

    // Negative case
    try {
      floor.moveEntity(new Location(-1, 0), new Location(0, 0));
      fail("Cannot move a location not within the floor boundaries.");
    } catch (Exception e) {
      assertEquals(
          "LocationNotValidException: Location: [-1, 0] is invalid because it's outside of the floor boundaries.",
          e.toString());
    }

    try {
      floor.moveEntity(new Location(1, 1), new Location(1, 2));
      fail("Cannot move an empty location.");
    } catch (Exception e) {
      assertEquals("LocationNotValidException: Location: [1, 1] is invalid because it's empty and nothing to move.", e.toString());
    }

    try {
      floor.moveEntity(new Location(2, 2), new Location(2, 3));
      fail("Cannot move outside the floor boundaries.");
    } catch (Exception e) {
      assertEquals("LocationNotValidException: Location: [2, 3] is invalid because it's outside of the floor boundaries.", e.toString());
    }

    try {
      floor.moveEntity(new Location(3, 1), new Location(2, 2));
      fail("Cannot move to an occupied location.");
    } catch (Exception e) {
      assertEquals("LocationNotValidException: Location: [2, 2] is invalid because it's already occupied.", e.toString());
    }
  }
}
