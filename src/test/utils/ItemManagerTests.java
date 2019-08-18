package test.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.junit.Test;
import main.utils.ItemManager;

public class ItemManagerTests {

  @Test
  public void testConstructorWithNoParametersShouldSuccussfullyCreate() {
    ItemManager<String> itemManager = new ItemManager<>();
    assertCollectionSizes(itemManager, 0, 0, 0);
  }
  
  @Test
  public void testConstructorWithItemsArrayShouldSuccussfullyCreateWithThoseItemsInAwaiting() {
    Collection<String> items = Arrays.asList("Item 1", "Item 2", "Item 3");
    ItemManager<String> itemManager = new ItemManager<>(items);
    assertCollectionSizes(itemManager, 3, 0, 0);
  }

  @Test
  public void testConstructorWithIntegerShouldSuccussfullyCreateWithThatAsTheInitialCapacity() {
    ItemManager<String> itemManager = new ItemManager<>(10);
    assertCollectionSizes(itemManager, 0, 0, 0);
  }

  @Test
  public void testAddWithValidItemShouldAddToAwaiting() throws Exception {
    ItemManager<String> itemManager = new ItemManager<>();

    itemManager.add("Item 1");
    assertCollectionSizes(itemManager, 1, 0, 0);
    itemManager.add("Item 2");
    assertCollectionSizes(itemManager, 2, 0, 0);
    itemManager.add("Item 3");
    assertCollectionSizes(itemManager, 3, 0, 0);
  }

  @Test
  public void testAddWithNullItemShouldThrowException() throws Exception {
    ItemManager<String> itemManager = new ItemManager<>();

    try {
      itemManager.add(null);
      fail("Add should fail because null values are forbidden.");
    } catch (Exception e) {
      assertEquals("Null items are not accepted.", e.getMessage());
      assertCollectionSizes(itemManager, 0, 0, 0);
    }
  }

  @Test
  public void testViewNextPickupWhenItemAvailableShouldReturnThatItem() throws Exception {
    List<String> items = Arrays.asList("Item 1", "Item 2", "Item 3");
    ItemManager<String> itemManager = new ItemManager<>(items);

    assertEquals(items.get(0), itemManager.viewNextPickup());
    assertCollectionSizes(itemManager, 3, 0, 0);
  }

  @Test
  public void testViewNextPickupWhenNoItemAvailableShouldThrowException() throws Exception {
    ItemManager<String> itemManager = new ItemManager<>();

    try {
      itemManager.viewNextPickup();
      fail("Pickup should fail because all item are progressing.");
    } catch (Exception e) {
      assertEquals("No more awaiting items.", e.getMessage());
      assertCollectionSizes(itemManager, 0, 0, 0);
    }
  }

  @Test
  public void testPickupWithItemsAvailableShouldMoveOutOfAwaitingAndToProgressing() throws Exception {
    List<String> items = Arrays.asList("Item 1", "Item 2", "Item 3");
    ItemManager<String> itemManager = new ItemManager<>(items);

    assertEquals(items.get(0), itemManager.pickup());
    assertCollectionSizes(itemManager, 2, 1, 0);

    assertEquals(items.get(1), itemManager.pickup());
    assertCollectionSizes(itemManager, 1, 2, 0);

    assertEquals(items.get(2), itemManager.pickup());
    assertCollectionSizes(itemManager, 0, 3, 0);
  }

  @Test
  public void testPickupWhenNoItemAvailableShouldThrowException() throws Exception {
    ItemManager<String> itemManager = new ItemManager<>();

    try {
      itemManager.pickup();
      fail("Pickup should fail because all item are progressing.");
    } catch (Exception e) {
      assertEquals("No more awaiting items.", e.getMessage());
      assertCollectionSizes(itemManager, 0, 0, 0);
    }
  }

  @Test
  public void testCompleteWithValidItemsShouldMoveFromProgressingToCompleted() throws Exception {
    List<String> items = Arrays.asList("Item 1", "Item 2", "Item 3");
    ItemManager<String> itemManager = new ItemManager<>(items);
    String item1 = itemManager.pickup();
    String item2 = itemManager.pickup();

    itemManager.complete(item1);
    assertCollectionSizes(itemManager, 1, 1, 1);

    itemManager.complete(item2);
    assertCollectionSizes(itemManager, 1, 0, 2);
  }

  @Test
  public void testCompleteWithInvalidItemShouldThrowException() throws Exception {
    List<String> items = Arrays.asList("Item 1", "Item 2", "Item 3");
    ItemManager<String> itemManager = new ItemManager<>(items);

    try {
      itemManager.complete(items.get(0));
      fail("Complete should fail because that item is not in progressing.");
    } catch (Exception e) {
      assertEquals("Item was not found in Progressing.", e.getMessage());
      assertCollectionSizes(itemManager, 3, 0, 0);
    }
  }

  @Test
  public void testCompleteWithNullItemShouldThrowException() throws Exception {
    List<String> items = Arrays.asList("Item 1", "Item 2", "Item 3");
    ItemManager<String> itemManager = new ItemManager<>(items);

    try {
      itemManager.complete(null);
      fail("Complete should fail because null won't exist.");
    } catch (Exception e) {
      assertEquals("Item was not found in Progressing.", e.getMessage());
      assertCollectionSizes(itemManager, 3, 0, 0);
    }
  }

  @Test
  public void testAreItemsToPickup() throws Exception {
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
  public void testAreAllItemsComplete() throws Exception {
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
