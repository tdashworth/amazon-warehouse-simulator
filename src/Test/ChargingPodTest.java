package Test;

import static org.junit.Assert.*;

import org.junit.Test;

import model.ChargingPod;
import model.Location;

public class ChargingPodTest {

	@Test
	public void chargingPodCreationTest() {
		//Create a simple location and charging pod, and ensure that it creates successfully (via ToString)
		Location l = new Location(1, 1);
		ChargingPod cp = new ChargingPod("0000", l);
		assertEquals("Charging Pod: - UID: 0000 - Location: 1, 1.", cp.toString());
		
		//Create another simple location and charging pod, and ensure that it creates successfully (via ToString)
		Location l2 = new Location(3, 5);
		ChargingPod cp2 = new ChargingPod("1234", l2);
		assertEquals("Charging Pod: - UID: 1234 - Location: 3, 5.", cp2.toString());
	}

}
