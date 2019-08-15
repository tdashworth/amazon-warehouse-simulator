package test.model;

import static org.junit.Assert.*;
import org.junit.Test;
import javafx.scene.Node;
import javafx.scene.shape.Circle;
import main.model.Location;
import main.model.Warehouse;
import main.model.AbstractActor;

public class AbstractActorTest {

  class ConcreteAbstractActor extends AbstractActor {

    public ConcreteAbstractActor(String uid, Location location, Node sprite) {
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
    AbstractActor actor = new ConcreteAbstractActor(uid, location, sprite);

    assertEquals(uid, actor.getUID());
    assertEquals(location, actor.getLocation());
    assertEquals(sprite, actor.getSprite());

    // Negative Cases
    try {
      actor = new ConcreteAbstractActor(null, location, sprite);
      fail("A null parameter should fail this.");
    } catch (Exception e) {
      assertEquals("'uid' is a required, non-null parameter.", e.getMessage());
    }

    try {
      actor = new ConcreteAbstractActor(uid, null, sprite);
      fail("A null parameter should fail this.");
    } catch (Exception e) {
      assertEquals("'location' is a required, non-null parameter.", e.getMessage());
    }
  }
}
