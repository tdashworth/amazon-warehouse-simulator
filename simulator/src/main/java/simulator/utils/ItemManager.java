package simulator.utils;

import java.util.*;

/**
 * A manager of items in three states: awaiting, progressing, complete.
 */
public class ItemManager<I> {
  private final Deque<I> awaiting;
  private final Set<I> progressing;
  private final Deque<I> completed;

  /**
   * A manager of items in three states: awaiting, progressing, complete.
   */
  @SuppressWarnings("unchecked")
  public ItemManager() {
    this((Collection<I>) Collections.emptyList());
  }

  /**
   * A manager of items in three states: awaiting, progressing, complete.
   * 
   * @param items a collection of items to being awaiting.
   */
  public ItemManager(Collection<I> items) {
    this.awaiting = new ArrayDeque<I>(items);
    this.progressing = new HashSet<I>(items.size());
    this.completed = new ArrayDeque<I>(items.size());
  }

  /**
   * A manager of items in three states: awaiting, progressing, complete.
   * 
   * @param defaultCapcity the default capcity of items being managed.
   */
  public ItemManager(int defaultCapcity) {
    this.awaiting = new ArrayDeque<I>(defaultCapcity);
    this.progressing = new HashSet<I>(defaultCapcity);
    this.completed = new ArrayDeque<I>(defaultCapcity);
  }

  /**
   * Adds item to awaiting.
   * 
   * @return A new item.
   * @throws Exception
   */
  public void add(I item) throws Exception {
    if (item == null)
      throw new Exception("Null items are not accepted.");

    this.awaiting.add(item);
    this.log("Item %s added as awaiting. %s remaining.", item.hashCode(), this.awaiting.size());
  }

  /**
   * Return the next awaiting item without moveing it to progressing.
   * 
   * @return An awaitng item.
   * @throws Exception
   */
  public I viewNextPickup() throws Exception {
    if (this.awaiting.isEmpty())
      throw new Exception("No more awaiting items.");

    return this.awaiting.peek();
  }

  /**
   * Picks the next awaiting item and moves it to progressing.
   * 
   * @return An awaitng item.
   * @throws Exception
   */
  public I pickup() throws Exception {
    if (this.awaiting.isEmpty())
      throw new Exception("No more awaiting items.");

    I item = this.awaiting.pop();
    this.progressing.add(item);

    this.log("Item %s progressing. %s remaining.", item.hashCode(), this.awaiting.size());
    return item;
  }

  /**
   * Moves the item given from the progressing to completed.
   * 
   * @param item completed item.
   * @throws Exception
   */
  public void complete(I item) throws Exception {
    if (!this.progressing.contains(item))
      throw new Exception("Item was not found in Progressing.");

    this.progressing.remove(item);
    this.completed.add(item);
    this.log("Item %s completed. %s remaining.", item.hashCode(), this.progressing.size());
  }

  /**
   * @return true if there are items to pickup.
   */
  public boolean areItemsToPickup() {
    return !this.awaiting.isEmpty();
  }

  /**
   * @return true if all items have been completed.
   */
  public boolean areAllItemsComplete() {
    return this.progressing.isEmpty() && this.awaiting.isEmpty();
  }

  /**
   * @return an unmodifiable collection of awaiting items.
   */
  public Collection<I> getAwaiting() {
    return Collections.unmodifiableCollection(this.awaiting);
  }

  /**
   * @return an unmodifiable collection of progressing items.
   */
  public Collection<I> getProgressing() {
    return Collections.unmodifiableCollection(this.progressing);
  }

  /**
   * @return an unmodifiable collection of completed items.
   */
  public Collection<I> getCompleted() {
    return Collections.unmodifiableCollection(this.completed);
  }

  private void log(String message) {
    String classType = this.getClass().getSimpleName();
    System.out.println(String.format("%s: %s", classType, message));
  }

  private void log(String format, Object... args) {
    this.log(String.format(format, args));
  }
}
