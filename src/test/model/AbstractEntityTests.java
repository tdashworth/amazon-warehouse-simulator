package test.model;

import static org.junit.Assert.*;
import org.junit.Test;
import javafx.scene.Node;
import javafx.scene.shape.Circle;
import main.model.Location;
import main.model.AbstractEntity;

public class AbstractEntityTests {

  class ConcreteAbstractEntity extends AbstractEntity {

    public ConcreteAbstractEntity(String uid, Location location, Node sprite) {
      super(uid, location, sprite);
    }
  }

  @Test
  public void testConstructorWithValidParametersShouldSuccussfullyCreate() {
    String uid = "uid";
    Location location = new Location(0, 0);
    Node sprite = new Circle();
    AbstractEntity entity = new ConcreteAbstractEntity(uid, location, sprite);

    assertEquals(uid, entity.getUID());
    assertEquals(location, entity.getLocation());
    assertEquals(sprite, entity.getSprite());
  }

  @Test
  public void testConstructorWithNullUIDShouldThrowIllegalArgumentException() throws Exception {
    Location location = new Location(0, 0);
    Node sprite = new Circle();

    try {
      new ConcreteAbstractEntity(null, location, sprite);
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
      new ConcreteAbstractEntity(uid, null, sprite);
      fail("A null parameter should fail this.");
    } catch (Exception e) {
      assertEquals("'location' is a required, non-null parameter.", e.getMessage());
    }
  }
}
