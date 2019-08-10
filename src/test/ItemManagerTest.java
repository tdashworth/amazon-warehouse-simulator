package test;

import org.junit.Test;
import static org.junit.Assert.*;
import model.ItemManager;
import java.util.*;

public class ItemManagerTest {

  @Test
  public void constructorTest() {
    ItemManager<String> itemManager = new ItemManager<>();
    assertCollectionSizes(itemManager, 0, 0, 0);

    Collection<String> items = Arrays.asList("Item 1", "Item 2", "Item 3");
    itemManager = new ItemManager<String>(items);
    assertCollectionSizes(itemManager, 3, 0, 0);
  }

  @Test
  public void addTest() {
    ItemManager<String> itemManager = new ItemManager<>();

    // Positive Cases
    try {
      itemManager.add("Item 1");
      assertCollectionSizes(itemManager, 1, 0, 0);
      itemManager.add("Item 2");
      assertCollectionSizes(itemManager, 2, 0, 0);
      itemManager.add("Item 3");
      assertCollectionSizes(itemManager, 3, 0, 0);
    } catch (Exception e) {
      fail(e.toString());
    }

    // Negative Case
    try {
      itemManager.add(null);
      fail("Add should fail because null values are forbidden.");
    } catch (Exception e) {
      assertEquals("Null items are not accepted.", e.getMessage());
      assertCollectionSizes(itemManager, 3, 0, 0);
    }
  }

  @Test
  public void pickupTest() {
    List<String> items = Arrays.asList("Item 1", "Item 2", "Item 3");
    ItemManager<String> itemManager = new ItemManager<>(items);

    // Positive case
    try {
      assertEquals(items.get(0), itemManager.pickup());
      assertCollectionSizes(itemManager, 2, 1, 0);

      assertEquals(items.get(1), itemManager.pickup());
      assertCollectionSizes(itemManager, 1, 2, 0);

      assertEquals(items.get(2), itemManager.pickup());
      assertCollectionSizes(itemManager, 0, 3, 0);
    } catch (Exception e) {
      fail(e.toString());
    }

    // Negative case
    try {
      itemManager.pickup();
      fail("Pickuk should fail because all item are progressing.");
    } catch (Exception e) {
      assertEquals("No more awaiting items.", e.getMessage());
      assertCollectionSizes(itemManager, 0, 3, 0);
    }
  }

  @Test
  public void completeTest() throws Exception {
    List<String> items = Arrays.asList("Item 1", "Item 2", "Item 3");
    ItemManager<String> itemManager = new ItemManager<>(items);
    String item1 = itemManager.pickup();
    String item2 = itemManager.pickup();

    // Positive case
    try {
      itemManager.complete(item1);
      assertCollectionSizes(itemManager, 1, 1, 1);

      itemManager.complete(item2);
      assertCollectionSizes(itemManager, 1, 0, 2);
    } catch (Exception e) {
      fail(e.toString());
    }

    // Negative case
    try {
      itemManager.complete(item2);
      fail("Complete should fail because that item has already been completed.");
    } catch (Exception e) {
      assertEquals("Item was not found in Progressing.", e.getMessage());
      assertCollectionSizes(itemManager, 1, 0, 2);
    }

    try {
      itemManager.complete(null);
      fail("Complete should fail because null won't exist.");
    } catch (Exception e) {
      assertEquals("Item was not found in Progressing.", e.getMessage());
      assertCollectionSizes(itemManager, 1, 0, 2);
    }
  }

  @Test
  public void areItemsToPickupTest() throws Exception {
    List<String> items = Arrays.asList("Item 1", "Item 2", "Item 3");
    ItemManager<String> itemManager = new ItemManager<>(items);

    // Awaiting: 3, Progressing: 0, Complete: 0
    assertTrue(itemManager.areItemsToPickup());
    String item1 = itemManager.pickup();
    String item2 = itemManager.pickup();
    itemManager.complete(item1);
    // Awaiting: 1, Progressing: 1, Complete: 1
    assertTrue(itemManager.areItemsToPickup());
    String item3 = itemManager.pickup();
    itemManager.complete(item2);
    // Awaiting: 0, Progressing: 1, Complete: 2
    assertFalse(itemManager.areItemsToPickup());
    itemManager.complete(item3);
    // Awaiting: 0, Progressing: 0, Complete: 3
    assertFalse(itemManager.areItemsToPickup());
  }

  @Test
  public void areAllItemsCompleteTest() throws Exception {
    List<String> items = Arrays.asList("Item 1", "Item 2", "Item 3");
    ItemManager<String> itemManager = new ItemManager<>(items);
    
    // Awaiting: 3, Progressing: 0, Complete: 0
    assertFalse(itemManager.areAllItemsComplete());
    String item1 = itemManager.pickup();
    String item2 = itemManager.pickup();
    itemManager.complete(item1);
    // Awaiting: 1, Progressing: 1, Complete: 1
    assertFalse(itemManager.areAllItemsComplete());
    String item3 = itemManager.pickup();
    itemManager.complete(item2);
    // Awaiting: 0, Progressing: 1, Complete: 2
    assertFalse(itemManager.areAllItemsComplete());
    itemManager.complete(item3);
    // Awaiting: 0, Progressing: 0, Complete: 3
    assertTrue(itemManager.areAllItemsComplete());
  }

  @SuppressWarnings("rawtypes")
  private void assertCollectionSizes(ItemManager itemManager, int awaiting, int progressing,
      int completed) {
    assertEquals(awaiting, itemManager.getAwaiting().size());
    assertEquals(progressing, itemManager.getProgressing().size());
    assertEquals(completed, itemManager.getCompleted().size());
  }
}
