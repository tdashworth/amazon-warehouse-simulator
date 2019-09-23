package amazon.packingStation;

import java.util.List;
import java.util.stream.Collectors;

import amazon.*;
import simulator.*;

/**
 * A simple representation of a packing station within the warehouse.
 */
public class PackingStation extends AEntity implements IActor<Warehouse>, PackingStationState {
	protected Order currentOrder;
	protected int tickCountWhenOrderAssigned;
	protected List<StorageShelf> storageShelvesVisited;

	protected PackingStationState state;

	/**
	 * A simple representation of a packing station within the warehouse.
	 * 
	 * @param uid
	 * @param location
	 */
	public PackingStation(String uid, Location location) {
		super(uid, location);
		this.state = new PickingState(this);
	}

	@Override
	public void tick(Warehouse warehouse, int currentTickCount) throws Exception {
		this.state.tick(warehouse, currentTickCount);
	}

	@Override
	public void recieveItem(StorageShelf storageShelf) throws IllegalArgumentException {
		this.state.recieveItem(storageShelf);
	}

	/**
	 * @return the status of the packing station based of the current state.
	 */
	public PackingStationState getStatus() {
		return this.state;
	}

	/**
	 * Returns the storage shelves which have been delivered from by the robots.
	 * 
	 * @return The storage shelves visited.
	 */
	public List<StorageShelf> getStorageShelvesVisited() {
		return storageShelvesVisited;
	}

	/**
	 * @return A string representation of a packing station.
	 */
	public String toString() {
		String result = super.toString();
		// TODO Get state to cover this
		if (this.currentOrder != null) {
			result += ", " + "Current Order: " + this.currentOrder;
			result += ", " + "Tick count when assigned: " + this.tickCountWhenOrderAssigned;
			// result += ", " + "Remaining packing ticks: " + this.remainingPackingTicks;
			result += ", " + "Visited Shelves: "
					+ this.storageShelvesVisited.stream().map(shelf -> shelf.getUID()).collect(Collectors.joining(","));
		}

		return result;
	}
}
