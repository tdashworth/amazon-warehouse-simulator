package amazon;

import static org.junit.Assert.*;
import org.junit.*;
import amazon.ChargingPod;
import simulator.Location;

public class ChargingPodTests {

  @Test
  public void testConstructorWithValidParametersShouldSuccussfullyCreate() {
    String uid = "uid";
    Location location = new Location(0, 0);
    ChargingPod chargingPod = new ChargingPod(uid, location);

    assertEquals(uid, chargingPod.getUID());
    assertEquals(location, chargingPod.getLocation());
  }

  @Test
  public void testConstructorWithNullUIDShouldThrowIllegalArgumentException() throws Exception {
    Location location = new Location(0, 0);

    try {
      new ChargingPod(null, location);
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
      new ChargingPod(uid, null);
      fail("A null parameter should fail this.");
    } catch (Exception e) {
      assertEquals("'location' is a required, non-null parameter.", e.getMessage());
    }
  }
}
