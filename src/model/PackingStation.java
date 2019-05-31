/**
 * 
 */
package model;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *	A simple representation of a packing station within the warehouse.
 */
public class PackingStation extends Entity implements Actor {
	private Order currentOrder;
	private int tickCountWhenOrderAssigned;
	private int remainingPackingTicks;
	private List<StorageShelf> storageShelvesVisited;

	/**
	 * A simple representation of a packing station within the warehouse.
	 * @param uid
	 * @param location
	 */
	public PackingStation(String uid, Location location) {
		super(uid, location);
	}

	@Override
	public void tick(Warehouse warehouse) throws Exception {
		// TODO determine "state" of packing station and call the next action to
		// perform.
		
		if (this.currentOrder == null)
			this.pickOrder(warehouse);
		else if (this.currentOrder.getStorageShelfUIDs().size() == this.storageShelvesVisited.size() && this.remainingPackingTicks != 0)
			this.packOrder();
		else if (this.currentOrder.getStorageShelfUIDs().size() == this.storageShelvesVisited.size() && this.remainingPackingTicks == 0)
			this.dispatchOrder(warehouse);
		else
			; // wait...
	}

	/**
	 * TODO JavaDoc description.
	 */
	private void pickOrder(Warehouse warehouse) {
		this.currentOrder = warehouse.getUnassignedOrder();
		this.currentOrder.getStorageShelfUIDs().forEach(uid -> this.requestItem(uid, warehouse));
		this.tickCountWhenOrderAssigned = 0; // TODO get this value?
		this.remainingPackingTicks = this.currentOrder.getNumberOfTicksToPack();
		this.storageShelvesVisited = new ArrayList<StorageShelf>();
	}

	/**
	 * TODO JavaDoc description.
	 */
	private void packOrder() {
		this.remainingPackingTicks--;
	}

	/**
	 * TODO JavaDoc description.
	 * @throws Exception 
	 */
	private void dispatchOrder(Warehouse warehouse) throws Exception {
		int totalNumberOfTicksToPack = 0 /* TODO get this value? */ - this.tickCountWhenOrderAssigned; 
		this.currentOrder.setTotalNumberOfTicksToPack(totalNumberOfTicksToPack);
		warehouse.dispatchOrder(this.currentOrder);
		this.currentOrder = null;
	}

	/**
	 * TODO JavaDoc description.
	 * @param The storage shelf UID.
	 * @param Thw warehouse reference.
	 */
	private void requestItem(String storageShelfUID, Warehouse warehouse) {
		StorageShelf storageShelf = (StorageShelf) warehouse.getEntityByUID(uid);
		warehouse.assignJobToRobot(storageShelf, this);
		// TODO What if there are no robots available? 
	}

	/**
	 * Take note that a robot has returned from a storage shelf. 
	 * @param The storage shelf reference.
	 */
	public void recieveItem(StorageShelf storageShelf) {
		this.storageShelvesVisited.add(storageShelf);
	}

	/**
	 * @return A string representation of a packing station.
	 */
	public String toString() {
		String defaultOutput = MessageFormat.format("Packing Station:"
				+ " - UID: {0}"
				+ " - {1}",
				this.uid, 
				this.location);
		
		String orderOutput = MessageFormat.format(
				  " - Current order: {0}"
				+ " - Tick count when order assigned: {1}"
				+ " - Remaining packing ticks: {2}"
				+ " - Storage shelves visited: {3}.",
				this.currentOrder, 
				this.tickCountWhenOrderAssigned, 
				this.remainingPackingTicks,
				this.storageShelvesVisited.stream().map(shelf -> shelf.getUID()).collect(Collectors.joining(",")));

		if (currentOrder != null) {
			return defaultOutput;
		} else {
			return defaultOutput + orderOutput;
		}
	}

}
