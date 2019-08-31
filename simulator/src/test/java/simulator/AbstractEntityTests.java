package simulator;

import static org.junit.Assert.*;
import org.junit.Test;
import simulator.Location;
import simulator.AEntity;

public class AbstractEntityTests {

  class ConcreteAbstractEntity extends AEntity {

    public ConcreteAbstractEntity(String uid, Location location) {
      super(uid, location);
    }
  }

  @Test
  public void testConstructorWithValidParametersShouldSuccussfullyCreate() {
    String uid = "uid";
    Location location = new Location(0, 0);
    AEntity entity = new ConcreteAbstractEntity(uid, location);

    assertEquals(uid, entity.getUID());
    assertEquals(location, entity.getLocation());
  }

  @Test
  public void testConstructorWithNullUIDShouldThrowIllegalArgumentException() throws Exception {
    Location location = new Location(0, 0);

    try {
      new ConcreteAbstractEntity(null, location);
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
      new ConcreteAbstractEntity(uid, null);
      fail("A null parameter should fail this.");
    } catch (Exception e) {
      assertEquals("'location' is a required, non-null parameter.", e.getMessage());
    }
  }
}
