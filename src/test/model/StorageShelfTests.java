package test.model;

import static org.junit.Assert.*;
import org.junit.*;
import main.model.StorageShelf;
import main.model.Location;

public class StorageShelfTests {

  @Test
  public void testConstructorWithValidParametersShouldSuccussfullyCreate() {
    String uid = "uid";
    Location location = new Location(0, 0);
    StorageShelf storageShelf = new StorageShelf(uid, location);

    assertEquals(uid, storageShelf.getUID());
    assertEquals(location, storageShelf.getLocation());
  }

  @Test
  public void testConstructorWithNullUIDShouldThrowIllegalArgumentException() throws Exception {
    Location location = new Location(0, 0);

    try {
      new StorageShelf(null, location);
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
      new StorageShelf(uid, null);
      fail("A null parameter should fail this.");
    } catch (Exception e) {
      assertEquals("'location' is a required, non-null parameter.", e.getMessage());
    }
  }
}
