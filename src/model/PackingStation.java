package model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * A simple representation of a packing station within the warehouse.
 */
public class PackingStation extends Actor {
	protected Order currentOrder;
	protected int tickCountWhenOrderAssigned;
	protected int remainingPackingTicks;
	protected List<StorageShelf> storageShelvesVisited;

	/**
	 * A simple representation of a packing station within the warehouse.
	 * 
	 * @param uid
	 * @param location
	 */
	public PackingStation(String uid, Location location) {
		super(uid, location, new Rectangle(35, 35, Color.AQUAMARINE));
	}

	@Override
	public void tick(Warehouse warehouse) throws Exception {
		this.log("Ticking.");

		if (this.currentOrder == null)
			this.pickOrder(warehouse);

		else if (this.currentOrder.getStorageShelfUIDs().size() == this.storageShelvesVisited.size()
				&& this.remainingPackingTicks != 0)
			this.packOrder();

		else if (this.currentOrder.getStorageShelfUIDs().size() == this.storageShelvesVisited.size()
				&& this.remainingPackingTicks == 0)
			this.dispatchOrder(warehouse);

		else
			; // wait...
	}

	/**
	 * Pick an unassigned order from the warehouse.
	 * 
	 * @param warehouse
	 * @throws LocationNotValidException
	 */
	private void pickOrder(Warehouse warehouse) throws Exception {
		this.log("Picking new order.");

		if (!warehouse.getOrderManager().areItemsToPickup()) {
			this.log("No orders left.");
			return;
		}

		this.currentOrder = warehouse.getOrderManager().pickup();
		this.tickCountWhenOrderAssigned = warehouse.getTotalTickCount();
		this.remainingPackingTicks = this.currentOrder.getNumberOfTicksToPack();
		this.storageShelvesVisited = new ArrayList<StorageShelf>();
		this.requestJobs(warehouse, this.currentOrder.getStorageShelfUIDs());
		this.log("Picked order: " + this.currentOrder.hashCode());
	}

	/**
	 * Creates a Job for each Storage Shelf and adds to Warehouse Job Manager.   
	 * 
	 * @param The storage shelf UID.
	 * @param Thw warehouse reference.
	 * @throws LocationNotValidException
	 */
	private void requestJobs(Warehouse warehouse, List<String> storageShelvesToRequest) throws Exception {
		this.log("Requesting Jobs: " + storageShelvesToRequest);

		for (String storageShelfUID : storageShelvesToRequest) {
			StorageShelf storageShelf = (StorageShelf) warehouse.getEntityByUID(storageShelfUID);
			warehouse.getJobManager().add(new Job(storageShelf, this));
		}
	}

	/**
	 * Pack an order, decrements the number of packing ticks remaining.
	 */
	public void packOrder() {
		this.remainingPackingTicks--;
	}

	/**
	 * Dispatch an order from the warehouse when it has been packed.
	 * 
	 * @param warehouse
	 * @throws Exception
	 */
	private void dispatchOrder(Warehouse warehouse) throws Exception {
		this.log("Dispatching order %s.", this.currentOrder.hashCode());
		int totalNumberOfTicksToPack = warehouse.getTotalTickCount() - this.tickCountWhenOrderAssigned;
		this.currentOrder.setTotalNumberOfTicksToPack(totalNumberOfTicksToPack);
		warehouse.getOrderManager().complete(this.currentOrder);
		this.currentOrder = null;
	}

	/**
	 * Take note that a robot has returned from a storage shelf.
	 * 
	 * @param The storage shelf reference.
	 * @throws Exception
	 */
	public void recieveItem(StorageShelf storageShelf) throws Exception {
		if (!this.currentOrder.getStorageShelfUIDs().contains(storageShelf.getUID()))
			throw new Exception("Storage Shelf not required by current order.");

		this.log("Item recieved from %s", storageShelf.getUID());
		this.storageShelvesVisited.add(storageShelf);
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
	 * Returns the current order of the packing station.
	 * 
	 * @return the current order.
	 */
	public Order getCurrentOrder() {
		return currentOrder;
	}

	/**
	 * @return A string representation of a packing station.
	 */
	public String toString() {
		String result = super.toString();

		if (this.currentOrder != null) {
			result += ", " + "Current Order: " + this.currentOrder;
			result += ", " + "Tick count when assigned: " + this.tickCountWhenOrderAssigned;
			result += ", " + "Remaining packing ticks: " + this.remainingPackingTicks;
			result += ", " + "Visited Shelves: " + this.storageShelvesVisited.stream()
					.map(shelf -> shelf.getUID()).collect(Collectors.joining(","));
		}

		return result;
	}
}
