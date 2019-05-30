/**
 * 
 */
package model;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

/**
 *	TODO JavaDoc description.
 */
public class PackingStation extends Entity implements Actor {
	private Order currentOrder;
	private int tickCountWhenOrderAssigned;
	private int remainingPackingTicks;
	private List<StorageShelf> storageShelvesVisited;

	/**
	 * TODO JavaDoc description.
	 * @param uid
	 * @param location
	 */
	public PackingStation(String uid, Location location) {
		super(uid, location);
	}

	@Override
	public void tick() {
		// TODO determine "state" of packing station and call the next action to
		// perform.
	}

	/**
	 * TODO JavaDoc description.
	 */
	private void pickOrder() {
		// TODO pick unassigned order from warehouse queue and set internal variables.
		// TODO request items for each shelf in order.
	}

	/**
	 * TODO JavaDoc description.
	 */
	private void packOrder() {
		// TODO count down remaining packing ticks.
	}

	/**
	 * TODO JavaDoc description.
	 */
	private void dispatchOrder() {
		// TODO move order to dispatched queue in warehouse and clear internal
		// variables.
	}

	/**
	 * TODO JavaDoc description.
	 */
	private void requestItem() {
		// TODO send request for a robot to retrieve item from shelf.
	}

	/**
	 * TODO JavaDoc description.
	 * @param storageShelf
	 */
	public void recieveItem(StorageShelf storageShelf) {
		// TODO take note that the shelf has been visited and retrieved.
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
