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
	private List<String> unrequestedStorageShelves;

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
		if (this.currentOrder == null)
			this.pickOrder(warehouse);
		
		else if (this.unrequestedStorageShelves.size() > 0)
			this.requestItems(warehouse);
		
		else if (this.currentOrder.getStorageShelfUIDs().size() == this.storageShelvesVisited.size() && this.remainingPackingTicks != 0)
			this.packOrder();
		
		else if (this.currentOrder.getStorageShelfUIDs().size() == this.storageShelvesVisited.size() && this.remainingPackingTicks == 0)
			this.dispatchOrder(warehouse);
		
		else
			; // wait...
	}

	/**
	 * Pick an unassigned order from the warehouse
	 * @param warehouse
	 */
	private void pickOrder(Warehouse warehouse) {
		this.currentOrder = warehouse.getUnassignedOrder();
		this.tickCountWhenOrderAssigned = warehouse.getTotalTickCount();
		this.remainingPackingTicks = this.currentOrder.getNumberOfTicksToPack();
		this.storageShelvesVisited = new ArrayList<StorageShelf>();
		this.unrequestedStorageShelves = this.currentOrder.getStorageShelfUIDs();
		
		this.requestItems(warehouse);
	}
	
	/**
	 * TODO JavaDoc description.
	 * @param The storage shelf UID.
	 * @param Thw warehouse reference.
	 */
	private void requestItems(Warehouse warehouse) {
		for (String storageShelfUID : this.unrequestedStorageShelves) {
			StorageShelf storageShelf = (StorageShelf) warehouse.getEntityByUID(storageShelfUID);
			
			if (warehouse.assignJobToRobot(storageShelf, this))
				this.unrequestedStorageShelves.remove(storageShelfUID);
		}
		
		// TODO What if there are no robots available? 
	}

	/**
	 * Pack an order, decrements the number of packing ticks remaining.
	 */
	private void packOrder() {
		this.remainingPackingTicks--;
	}

	/**
	 * Dispatch an order from the warehouse when it has been packed.
	 * @param warehouse
	 * @throws Exception 
	 */
	private void dispatchOrder(Warehouse warehouse) throws Exception {
		int totalNumberOfTicksToPack = warehouse.getTotalTickCount() - this.tickCountWhenOrderAssigned; 
		this.currentOrder.setTotalNumberOfTicksToPack(totalNumberOfTicksToPack);
		warehouse.dispatchOrder(this.currentOrder);
		this.currentOrder = null;
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
