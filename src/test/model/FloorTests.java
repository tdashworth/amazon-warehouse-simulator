package test.model;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.Test;
import main.model.Floor;
import main.model.Location;
import main.model.LocationNotValidException;
import main.model.AbstractMover;
import main.model.Robot;

public class FloorTests {

  @Test
  public void testConstructorWithValidParametersShouldSuccussfullyCreate() {
    Floor floor = new Floor(5, 3);

    assertEquals(5, floor.getNumberOfColumns());
    assertEquals(3, floor.getNumberOfRows());
  }

  @Test
  public void testConstructorWithNegativeColumnsShouldThrowIllegalArgumentException()
      throws Exception {
    try {
      new Floor(-5, 3);
      fail("A negative parameter should fail this.");
    } catch (Exception e) {
      assertEquals("'numberOfColumns' must be a positive integer.", e.getMessage());
    }
  }

  @Test
  public void testConstructorWithNegativeRowsShouldThrowIllegalArgumentException()
      throws Exception {
    try {
      new Floor(5, -3);
      fail("A negative parameter should fail this.");
    } catch (Exception e) {
      assertEquals("'numberOfRows' must be a positive integer.", e.getMessage());
    }
  }

  @Test
  public void testIsLocationValidWithValidParametersShouldReturnTrue() {
    Floor floor = new Floor(5, 3);

    assertTrue(floor.isLocationValid(new Location(2, 1)));
    assertTrue(floor.isLocationValid(new Location(0, 0)));
    assertTrue(floor.isLocationValid(new Location(4, 2)));
  }

  @Test
  public void testIsLocationValidWithInvalidParametersShouldReturnFalse() {
    Floor floor = new Floor(5, 3);

    assertFalse(floor.isLocationValid(new Location(-1, -1)));
    assertFalse(floor.isLocationValid(new Location(5, 3)));
  }

  @Test
  public void testLoadMoverWithAValidRobotLocationShouldNotThrowException()
      throws LocationNotValidException {
    Floor floor = new Floor(5, 3);
    AbstractMover robot = mockRobotWithLocation(new Location(2, 1));

    floor.loadMover(robot);
  }

  @Test
  public void testLoadMoverWithAnInvalidRobotLocationShouldThrowLocationNotValidException() {
    Floor floor = new Floor(5, 3);
    AbstractMover robot = mockRobotWithLocation(new Location(5, 5));

    try {
      floor.loadMover(robot);
      fail("Robot load should have failed as it's location isn't within the floor boundaries.");
    } catch (LocationNotValidException e) {
      assertEquals("Location: [5, 5] is invalid because it's outside of the floor boundaries.",
          e.getMessage());
    }
  }

  @Test
  public void testLoadMoverWithTwoMoversAtTheSameLocationShouldThrowLocationNotValidException()
      throws LocationNotValidException {
    Floor floor = new Floor(5, 3);
    AbstractMover robot = mockRobotWithLocation(new Location(2, 2));
    floor.loadMover(robot);

    try {
      floor.loadMover(robot);
      fail("Robot load should have failed as there is something already at it's location.");
    } catch (LocationNotValidException e) {
      assertEquals("Location: [2, 2] is invalid because it's already occupied.", e.getMessage());
    }
  }

  @Test
  public void testIsLocationValidAndEmptyWithValidRobotLocationsShouldReturnTrue()
      throws LocationNotValidException {
    Floor floor = new Floor(5, 3);
    floor.loadMover(mockRobotWithLocation(new Location(2, 1)));

    assertTrue(floor.isLocationValidAndEmpty(new Location(0, 0)));
    assertTrue(floor.isLocationValidAndEmpty(new Location(4, 2)));
  }

  @Test
  public void testIsLocationValidAndEmptyWithInvalidRobotLocationsShouldReturnFalse()
      throws LocationNotValidException {
    Floor floor = new Floor(5, 3);
    floor.loadMover(mockRobotWithLocation(new Location(2, 1)));

    assertFalse(floor.isLocationValidAndEmpty(new Location(2, 1)));
    assertFalse(floor.isLocationValidAndEmpty(new Location(-1, -1)));
  }

  @Test
  public void testMoveEntityWithAValidSequnceOfMovesShouldNeverThrowException() throws Exception {
    Floor floor = new Floor(5, 3);
    floor.loadMover(mockRobotWithLocation(new Location(1, 1)));
    floor.loadMover(mockRobotWithLocation(new Location(4, 2)));

    floor.moveEntity(new Location(1, 1), new Location(1, 2)); // r1 down
    floor.moveEntity(new Location(4, 2), new Location(3, 2)); // r2 left
    floor.moveEntity(new Location(1, 2), new Location(2, 2)); // r1 right
    floor.moveEntity(new Location(3, 2), new Location(3, 1)); // r2 up
  }

  @Test
  public void testMoveEntityWithAnInvalidOriginLocationShouldThrowException() throws Exception {
    Floor floor = new Floor(5, 3);
    floor.loadMover(mockRobotWithLocation(new Location(1, 1)));
    floor.loadMover(mockRobotWithLocation(new Location(4, 2)));

    // Negative case
    try {
      floor.moveEntity(new Location(-1, 0), new Location(0, 0));
      fail("Cannot move a location not within the floor boundaries.");
    } catch (Exception e) {
      assertEquals("Location: [-1, 0] is invalid because it's outside of the floor boundaries.",
          e.getMessage());
    }
  }

  @Test
  public void testMoveEntityWithAnEmptyOriginLocationShouldThrowException() throws Exception {
    Floor floor = new Floor(5, 3);
    floor.loadMover(mockRobotWithLocation(new Location(1, 1)));
    floor.loadMover(mockRobotWithLocation(new Location(4, 2)));

    try {
      floor.moveEntity(new Location(1, 2), new Location(2, 2));
      fail("Cannot move an empty location.");
    } catch (Exception e) {
      assertEquals("Location: [1, 2] is invalid because it's empty and nothing to move.",
          e.getMessage());
    }
  }

  @Test
  public void testMoveEntityWithAnInvalidTargetLocationShouldThrowException() throws Exception {
    Floor floor = new Floor(5, 3);
    floor.loadMover(mockRobotWithLocation(new Location(1, 1)));
    floor.loadMover(mockRobotWithLocation(new Location(4, 2)));

    try {
      floor.moveEntity(new Location(1, 1), new Location(6, 4));
      fail("Cannot move outside the floor boundaries.");
    } catch (Exception e) {
      assertEquals("Location: [6, 4] is invalid because it's outside of the floor boundaries.",
          e.getMessage());
    }
  }

  @Test
  public void testMoveEntityWithANonEmptyTargetLocationShouldThrowException() throws Exception {
    Floor floor = new Floor(5, 3);
    floor.loadMover(mockRobotWithLocation(new Location(1, 1)));
    floor.loadMover(mockRobotWithLocation(new Location(4, 2)));

    try {
      floor.moveEntity(new Location(1, 1), new Location(4, 2));
      fail("Cannot move to an occupied location.");
    } catch (Exception e) {
      assertEquals("Location: [4, 2] is invalid because it's already occupied.", e.getMessage());
    }
  }

  private Robot mockRobotWithLocation(Location location) {
    Robot robot = mock(Robot.class);
    when(robot.getLocation()).thenReturn(location);

    return robot;
  }
}
