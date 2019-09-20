package amazon.packingStation;

import amazon.StorageShelf;
import amazon.Warehouse;

public class DispatchingState implements PackingStationState {
  private final PackingStation context;

  public DispatchingState(PackingStation context) {
    this.context = context;
  }

  @Override
  public void tick(Warehouse warehouse, int currentTickCount) throws Exception {
    // this.log("Dispatching order %s.", this.context.currentOrder.hashCode());
    int totalNumberOfTicksToPack = currentTickCount - this.context.tickCountWhenOrderAssigned;
    this.context.currentOrder.setTotalNumberOfTicksToPack(totalNumberOfTicksToPack);
    warehouse.getOrderManager().complete(this.context.currentOrder);
    this.context.currentOrder = null;
    this.context.state = this.context.pickingState;
  }

  @Override
  public void recieveItem(StorageShelf storageShelf) throws IllegalArgumentException {
    if (storageShelf == null)
      throw new IllegalArgumentException("'storageShelf' is a required, non-null parameter.");
  }

}