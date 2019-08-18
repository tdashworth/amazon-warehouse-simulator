package test.model;

import static org.junit.Assert.*;
import org.junit.Test;
import javafx.scene.Node;
import javafx.scene.shape.Circle;
import main.model.Location;
import main.model.Warehouse;
import main.model.AbstractMover;

public class AbstractMoverTests {

  class ConcreteAbstractMover extends AbstractMover {

    public ConcreteAbstractMover(String uid, Location location, Node sprite) {
      super(uid, location, sprite);
    }

    public void tick(Warehouse warehouse, int currentTickCount) throws Exception {
      throw new Exception("Not implemented.");
    };
  }

  @Test
  public void testConstructorWithValidParametersShouldSuccussfullyCreate() {
    String uid = "uid";
    Location location = new Location(0, 0);
    Node sprite = new Circle();
    AbstractMover mover = new ConcreteAbstractMover(uid, location, sprite);

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
