package simulator;

import static org.junit.Assert.*;
import org.junit.Test;
import simulator.Location;
import simulator.AMover;
import simulator.AWorld;

public class AbstractMoverTests {

  class ConcreteAbstractMover extends AMover<AWorld> {

    public ConcreteAbstractMover(String uid, Location location) {
      super(uid, location);
    }

    public void tick(AWorld world, int currentTickCount) throws Exception {
      throw new Exception("Not implemented.");
    };
  }

  @Test
  public void testConstructorWithValidParametersShouldSuccussfullyCreate() {
    String uid = "uid";
    Location location = new Location(0, 0);
    AMover<?> mover = new ConcreteAbstractMover(uid, location);

    assertEquals(uid, mover.getUID());
    assertEquals(location, mover.getLocation());
  }

  @Test
  public void testConstructorWithNullUIDShouldThrowIllegalArgumentException() throws Exception {
    Location location = new Location(0, 0);

    try {
      new ConcreteAbstractMover(null, location);
      fail("A null parameter should fail this.");
    } catch (Exception e) {
      assertEquals("'uid' is a required, non-null parameter.", e.getMessage());
    }
  }

  @Test
  public void testConstructorWithNullLocationShouldThrowIllegalArgumentException()
      throws Exception {
    String uid = "uid";

    try {
      new ConcreteAbstractMover(uid, null);
      fail("A null parameter should fail this.");
    } catch (Exception e) {
      assertEquals("'location' is a required, non-null parameter.", e.getMessage());
    }
  }
}
