package test.simulation;

import static org.junit.Assert.*;
import org.junit.Test;
import javafx.scene.Node;
import javafx.scene.shape.Circle;
import main.simulation.Location;
import main.simulation.AMover;
import main.simulation.AWorld;

public class AbstractMoverTests {

  class ConcreteAbstractMover extends AMover<AWorld> {

    public ConcreteAbstractMover(String uid, Location location, Node sprite) {
      super(uid, location, sprite);
    }

    public void tick(AWorld world, int currentTickCount) throws Exception {
      throw new Exception("Not implemented.");
    };
  }

  @Test
  public void testConstructorWithValidParametersShouldSuccussfullyCreate() {
    String uid = "uid";
    Location location = new Location(0, 0);
    Node sprite = new Circle();
    AMover<?> mover = new ConcreteAbstractMover(uid, location, sprite);

    assertEquals(uid, mover.getUID());
    assertEquals(location, mover.getLocation());
    assertEquals(sprite, mover.getSprite());
  }

  @Test
  public void testConstructorWithNullUIDShouldThrowIllegalArgumentException() throws Exception {
    Location location = new Location(0, 0);
    Node sprite = new Circle();

    try {
      new ConcreteAbstractMover(null, location, sprite);
      fail("A null parameter should fail this.");
    } catch (Exception e) {
      assertEquals("'uid' is a required, non-null parameter.", e.getMessage());
    }
  }

  @Test
  public void testConstructorWithNullLocationShouldThrowIllegalArgumentException()
      throws Exception {
    String uid = "uid";
    Node sprite = new Circle();

    try {
      new ConcreteAbstractMover(uid, null, sprite);
      fail("A null parameter should fail this.");
    } catch (Exception e) {
      assertEquals("'location' is a required, non-null parameter.", e.getMessage());
    }
  }
}
