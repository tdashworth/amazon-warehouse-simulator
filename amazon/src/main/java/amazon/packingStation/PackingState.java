package amazon.packingStation;

import amazon.StorageShelf;
import amazon.Warehouse;

public class PackingState implements PackingStationState {
  private final PackingStation context;
  protected int remainingPackingTicks;

  public PackingState(PackingStation context, int remainingPackingTicks) {
    this.context = context;
    this.remainingPackingTicks = remainingPackingTicks;
  }

  @Override
  public void tick(Warehouse world, int currentTickCount) throws Exception {
    this.remainingPackingTicks--;

    if (this.remainingPackingTicks == 0)
      this.context.state = new DispatchingState(context);
  }

  @Override
  public void recieveItem(StorageShelf storageShelf) throws IllegalArgumentException {
    if (storageShelf == null)
      throw new IllegalArgumentException("'storageShelf' is a required, non-null parameter.");
  }

}