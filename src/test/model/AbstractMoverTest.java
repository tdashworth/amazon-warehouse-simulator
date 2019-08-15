package test.model;

import static org.junit.Assert.*;
import org.junit.Test;
import javafx.scene.Node;
import javafx.scene.shape.Circle;
import main.model.Location;
import main.model.Warehouse;
import main.model.AbstractMover;

public class AbstractMoverTest {

  class ConcreteAbstractMover extends AbstractMover {

    public ConcreteAbstractMover(String uid, Location location, Node sprite) {
      super(uid, location, sprite);
    }

    public void tick(Warehouse warehouse) throws Exception {
      throw new Exception("Not implemented.");
    };
  }

  @Test
  public void constructorTest() {
    String uid = "uid";
    Location location = new Location(0, 0);
    Node sprite = new Circle();
    AbstractMover mover = new ConcreteAbstractMover(uid, location, sprite);

    assertEquals(uid, mover.getUID());
    assertEquals(location, mover.getLocation());
    assertEquals(sprite, mover.getSprite());

    // Negative Cases
    try {
      mover = new ConcreteAbstractMover(null, location, sprite);
      fail("A null parameter should fail this.");
    } catch (Exception e) {
      assertEquals("'uid' is a required, non-null parameter.", e.getMessage());
    }

    try {
      mover = new ConcreteAbstractMover(uid, null, sprite);
      fail("A null parameter should fail this.");
    } catch (Exception e) {
      assertEquals("'location' is a required, non-null parameter.", e.getMessage());
    }
  }
}
