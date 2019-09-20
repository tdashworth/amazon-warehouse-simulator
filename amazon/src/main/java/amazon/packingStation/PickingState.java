package amazon.packingStation;

import java.util.ArrayList;
import java.util.List;

import amazon.*;

public class PickingState implements PackingStationState {
  private final PackingStation context;

  public PickingState(PackingStation context) {
    this.context = context;
  }

  @Override
  public void tick(Warehouse warehouse, int currentTickCount) throws Exception {
    //this.log("Picking new order.");

		if (!warehouse.getOrderManager().areItemsToPickup()) {
			//this.log("No orders left.");
			return;
		}

		this.context.currentOrder = warehouse.getOrderManager().pickup();
		this.context.tickCountWhenOrderAssigned = currentTickCount;
		this.context.remainingPackingTicks = this.context.currentOrder.getNumberOfTicksToPack();
		this.context.storageShelvesVisited = new ArrayList<StorageShelf>();
		this.requestJobs(warehouse, this.context.currentOrder.getStorageShelfUIDs());

		this.context.state = this.context.awaitingState;
		//this.log("Picked order: " + this.context.currentOrder.hashCode());
  }

  /**
	 * Creates a Job for each Storage Shelf and adds to Warehouse Job Manager.
	 * 
	 * @param warehouse               The reference to the warehouse.
	 * @param storageShelvesToRequest A list of Storage Shelves to request.
	 * @throws LocationNotValidException
	 */
	private void requestJobs(Warehouse warehouse, List<String> storageShelvesToRequest) throws Exception {
		//this.log("Requesting Jobs: " + storageShelvesToRequest);

		for (String storageShelfUID : storageShelvesToRequest) {
			StorageShelf storageShelf = (StorageShelf) warehouse.getEntityByUID(storageShelfUID);
			warehouse.getJobManager().add(new Job(storageShelf, context));
		}
	}

	@Override
	public void recieveItem(StorageShelf storageShelf) throws IllegalArgumentException {
		if (storageShelf == null)
      throw new IllegalArgumentException("'storageShelf' is a required, non-null parameter.");
	}
}