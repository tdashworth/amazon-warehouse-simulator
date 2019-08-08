/**
 * 
 */
package model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

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
		super(uid, location, new Rectangle(35, 35, Color.AQUAMARINE));
	}

	@Override
	public void tick(Warehouse warehouse) throws Exception {
		this.log("Ticking.");
		
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
	 * @throws LocationNotValidException 
	 */
	private void pickOrder(Warehouse warehouse) throws LocationNotValidException {
		this.log("Picking new order.");
		this.currentOrder = warehouse.getUnassignedOrder();
		
		if (this.currentOrder == null) {
			this.log("No orders left.");
			return;
		}
		
		this.tickCountWhenOrderAssigned = warehouse.getTotalTickCount();
		this.remainingPackingTicks = this.currentOrder.getNumberOfTicksToPack();
		this.storageShelvesVisited = new ArrayList<StorageShelf>();
		this.unrequestedStorageShelves = new ArrayList<String>(this.currentOrder.getStorageShelfUIDs());
		this.log("Picked order: " + this.currentOrder.hashCode());
		
		this.requestItems(warehouse);
	}
	
	/**
	 * TODO JavaDoc description.
	 * @param The storage shelf UID.
	 * @param Thw warehouse reference.
	 * @throws LocationNotValidException 
	 */
	private void requestItems(Warehouse warehouse) throws LocationNotValidException {
		this.log("Requesting items: " + this.unrequestedStorageShelves);
		ArrayList<String> uuidsToRemove = new ArrayList<String>();
		
		for (String storageShelfUID : this.unrequestedStorageShelves) {
			StorageShelf storageShelf = (StorageShelf) warehouse.getEntityByUID(storageShelfUID);
			
			if (warehouse.assignJobToRobot(storageShelf, this)) {
				uuidsToRemove.add(storageShelfUID);
				this.log("Storage Shelf %s assigned to a Robot.", storageShelf.getUID());
			}
		}
		
		this.unrequestedStorageShelves.removeAll(uuidsToRemove);
	}

	/**
	 * Pack an order, decrements the number of packing ticks remaining.
	 */
	public void packOrder() {
		this.remainingPackingTicks--;
	}

	/**
	 * Dispatch an order from the warehouse when it has been packed.
	 * @param warehouse
	 * @throws Exception 
	 */
	private void dispatchOrder(Warehouse warehouse) throws Exception {
		this.log("Dispatching order %s.", this.currentOrder.hashCode());
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
		this.log("Item recieved from %s", storageShelf.getUID());
		this.storageShelvesVisited.add(storageShelf);
	}
	
	/**
	 * Returns the storage shelves which have been delivered from by the robots. 
	 * @return The storage shelves visited.
	 */
	public List<StorageShelf> getStorageShelvesVisited() {
		return storageShelvesVisited;
	}
	
	/**
	 * Returns the current order of the packing station. 
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
			result += ", " + "Visited Shelves: " + this.storageShelvesVisited.stream().map(shelf -> shelf.getUID()).collect(Collectors.joining(","));
		}
		
		return result;
	}
	
	public void setRemainingPackingTicks(int ticks) {
		remainingPackingTicks = ticks;
	}
	
	public int getRemainingPackingTicks() {
		return remainingPackingTicks;
	}
}
