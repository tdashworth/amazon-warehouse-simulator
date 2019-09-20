package amazon.packingStation;

import amazon.StorageShelf;
import amazon.Warehouse;

public class PackingState implements PackingStationState {
  private final PackingStation context;

  public PackingState(PackingStation context) {
    this.context = context;
  }

  @Override
  public void tick(Warehouse world, int currentTickCount) throws Exception {
    this.context.remainingPackingTicks--;

    if (this.context.remainingPackingTicks == 0)
      this.context.state = this.context.dispatchingState;
  }

  @Override
  public void recieveItem(StorageShelf storageShelf) throws IllegalArgumentException {
    if (storageShelf == null)
      throw new IllegalArgumentException("'storageShelf' is a required, non-null parameter.");
  }

}