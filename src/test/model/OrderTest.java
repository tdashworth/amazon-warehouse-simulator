package test.model;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import main.model.Order;

public class OrderTest {

	@Test
	public void constructorTest() {
		// Working constructor.
		List<String> storageShelves = Arrays.asList("ss1", "ss2", "ss3");
		Order order = new Order(storageShelves, 5);

		assertTrue(order.getStorageShelfUIDs().containsAll(storageShelves));
		assertEquals(5, order.getNumberOfTicksToPack());

		// Fails if storageShelves parameter is null
		try {
			order = new Order(null, 5);
			fail("A null parameter should fail.");
		} catch (Exception e) {
			assertEquals("'storageShelfUIDs' is a required, non-null parameter with atleast one element.",
					e.getMessage());
		}

		// Fails if storageShelves is empty.
		try {
			order = new Order(new ArrayList<>(), 5);
			fail("An empty array should fail.");
		} catch (Exception e) {
			assertEquals("'storageShelfUIDs' is a required, non-null parameter with atleast one element.",
					e.getMessage());
		}

		// Fails if numberOfTicksToPack is negative.
		try {
			order = new Order(storageShelves, -5);
			fail("A negative parameter should fail.");
		} catch (Exception e) {
			assertEquals("'numberOfTicksToPack' must be a positive integer.", e.getMessage());
		}
	}

	@Test
	public void setTotalNumberOfTicksToPackTest() {
		Order order = new Order(Arrays.asList("ss1", "ss2", "ss3"), 5);

		// Positive Case
		order.setTotalNumberOfTicksToPack(15);

		assertEquals(15, order.getTotalNumberOfTicksToPack());
		assertEquals(5, order.getNumberOfTicksToPack());

		// Negative Case
		try {
			order.setTotalNumberOfTicksToPack(-15);
			fail("A negative parameter should fail.");
		} catch (Exception e) {
			assertEquals("'totalNumberOfTicksToPack' must be a positive integer.", e.getMessage());
		}
	}

}
