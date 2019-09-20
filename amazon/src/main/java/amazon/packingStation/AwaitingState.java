package amazon.packingStation;

import amazon.StorageShelf;
import amazon.Warehouse;

public class AwaitingState implements PackingStationState {
  private final PackingStation context;

  public AwaitingState(PackingStation context) {
    this.context = context;
  }

  @Override
  public void tick(Warehouse world, int currentTickCount) throws Exception {

    boolean allItemsRecieved = this.context.currentOrder.getStorageShelfUIDs()
        .size() == this.context.storageShelvesVisited.size();
    if (allItemsRecieved)
      this.context.state = this.context.packingState;
  }

  @Override
  public void recieveItem(StorageShelf storageShelf) throws IllegalArgumentException {
    if (storageShelf == null)
      throw new IllegalArgumentException("'storageShelf' is a required, non-null parameter.");

    if (!this.context.currentOrder.getStorageShelfUIDs().contains(storageShelf.getUID()))
      throw new IllegalArgumentException(
          "Storage Shelf '" + storageShelf.getUID() + "' is not required by current order.");

    // this.log("Item recieved from %s", storageShelf.getUID());
    this.context.storageShelvesVisited.add(storageShelf);
  }
}
