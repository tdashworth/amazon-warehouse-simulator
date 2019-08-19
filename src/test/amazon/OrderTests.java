package test.amazon;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import main.amazon.Order;

public class OrderTests {

	@Test
	public void testConstructorWithValidParametersShouldSuccussfullyCreate() {
		List<String> storageShelves = Arrays.asList("ss1", "ss2", "ss3");
		Order order = new Order(storageShelves, 5);

		assertTrue(order.getStorageShelfUIDs().containsAll(storageShelves));
		assertEquals(5, order.getNumberOfTicksToPack());
	}

	@Test
	public void testConstructorWithNullStorageShelvesShouldThrowIllegalArgumentException() {
		try {
			new Order(null, 5);
			fail("A null parameter should fail.");
		} catch (Exception e) {
			assertEquals("'storageShelfUIDs' is a required, non-null parameter with atleast one element.",
					e.getMessage());
		}
	}

	@Test
	public void testConstructorWithEmptyStorageShelvesShouldThrowIllegalArgumentException() {
		try {
			new Order(new ArrayList<>(), 5);
			fail("An empty array should fail.");
		} catch (Exception e) {
			assertEquals("'storageShelfUIDs' is a required, non-null parameter with atleast one element.",
					e.getMessage());
		}
	}

	@Test
	public void testConstructorWithNegativeTicksToPackShouldThrowIllegalArgumentException() {
		List<String> storageShelves = Arrays.asList("ss1", "ss2", "ss3");

		try {
			new Order(storageShelves, -5);
			fail("A negative parameter should fail.");
		} catch (Exception e) {
			assertEquals("'numberOfTicksToPack' must be a positive integer.", e.getMessage());
		}
	}

	@Test
	public void testSetTotalNumberOfTicksToPackWithPositiveParameterShouldNotThrowException() {
		Order order = new Order(Arrays.asList("ss1", "ss2", "ss3"), 5);

		order.setTotalNumberOfTicksToPack(15);

		assertEquals(15, order.getTotalNumberOfTicksToPack());
		assertEquals(5, order.getNumberOfTicksToPack());
	}

	@Test
	public void testSetTotalNumberOfTicksToPackWithNegativeParameterShouldThrowIllegalArgumentException() {
		Order order = new Order(Arrays.asList("ss1", "ss2", "ss3"), 5);

		try {
			order.setTotalNumberOfTicksToPack(-15);
			fail("A negative parameter should fail.");
		} catch (IllegalArgumentException e) {
			assertEquals("'totalNumberOfTicksToPack' must be a positive integer.", e.getMessage());
		}
	}

}
