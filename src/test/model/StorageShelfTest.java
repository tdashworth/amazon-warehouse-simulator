package test.model;

import static org.junit.Assert.*;
import org.junit.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import main.model.StorageShelf;
import main.model.Location;

public class StorageShelfTest {

  @Test
  public void constructorTest() {
    String uid = "ss1";
    Location location = new Location(0, 0);
    StorageShelf storageShelf = new StorageShelf(uid, location);

    assertEquals(uid, storageShelf.getUID());
    assertEquals(location, storageShelf.getLocation());
    assertTrue(storageShelf.getSprite() instanceof Rectangle);
    assertEquals(35, ((Rectangle) storageShelf.getSprite()).getHeight(), 0.0);
    assertEquals(35, ((Rectangle) storageShelf.getSprite()).getWidth(), 0.0);
    assertEquals(Color.DARKSALMON, ((Rectangle) storageShelf.getSprite()).getFill());

    // Negative Cases
    try {
      storageShelf = new StorageShelf(null, location);
      fail("A null parameter should fail this.");
    } catch (Exception e) {
      assertEquals("'uid' is a required, non-null parameter.", e.getMessage());
    }

    try {
      storageShelf = new StorageShelf(uid, null);
      fail("A null parameter should fail this.");
    } catch (Exception e) {
      assertEquals("'location' is a required, non-null parameter.", e.getMessage());
    }
  }
}
