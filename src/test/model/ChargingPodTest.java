package test.model;

import static org.junit.Assert.*;
import org.junit.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import main.model.ChargingPod;
import main.model.Location;

public class ChargingPodTest {

  @Test
  public void constructorTest() {
    String uid = "cp1";
    Location location = new Location(0, 0);
    ChargingPod chargingPod = new ChargingPod(uid, location);

    assertEquals(uid, chargingPod.getUID());
    assertEquals(location, chargingPod.getLocation());
    assertTrue(chargingPod.getSprite() instanceof Rectangle);
    assertEquals(35, ((Rectangle) chargingPod.getSprite()).getHeight(), 0.0);
    assertEquals(35, ((Rectangle) chargingPod.getSprite()).getWidth(), 0.0);
    assertEquals(Color.GOLD, ((Rectangle) chargingPod.getSprite()).getFill());

    // Negative Cases
    try {
      chargingPod = new ChargingPod(null, location);
      fail("A null parameter should fail this.");
    } catch (Exception e) {
      assertEquals("'uid' is a required, non-null parameter.", e.getMessage());
    }

    try {
      chargingPod = new ChargingPod(uid, null);
      fail("A null parameter should fail this.");
    } catch (Exception e) {
      assertEquals("'location' is a required, non-null parameter.", e.getMessage());
    }
  }
}
