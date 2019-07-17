package model;

import java.text.MessageFormat;
import java.util.List;

/**
 *
 */
public class Order {
	private final List<String> storageShelfUIDs;
	private final int numberOfTicksToPack;
	private int totalNumberOfTicksToPack;
	
	/**
	 * @param storageShelfUIDs
	 * @param numberOfTicksToPack
	 */
	public Order(List<String> storageShelfUIDs, int numberOfTicksToPack) {
		this.storageShelfUIDs = storageShelfUIDs;
		this.numberOfTicksToPack = numberOfTicksToPack;
	}

	/**
	 * @return The storage shelf UIDs required in the order.
	 */
	public List<String> getStorageShelfUIDs() {
		return storageShelfUIDs;
	}

	/**
	 * @return The number of ticks to pack the order.
	 */
	public int getNumberOfTicksToPack() {
		return numberOfTicksToPack;
	}
	
	/**
	 * @return The total number of ticks to pack from assigned.
	 */
	public int getTotalNumberOfTicksToPack() {
		return totalNumberOfTicksToPack;
	}

	/**
	 * Sets the total number of ticks it takes an item to pack.
	 * @param The total number of ticks to pack from assigned.
	 */
	public void setTotalNumberOfTicksToPack(int totalNumberOfTicksToPack) {
		this.totalNumberOfTicksToPack = totalNumberOfTicksToPack;
	}

	/**
	 *	@return A string representation of the order.
	 */
	@Override
	public String toString() {
		return MessageFormat.format("Order: "
				+ " - Storage Shelf UIDs: {0}"
				+ " - Number of ticks to pack order: {1}"
				+ " - Total number of tick to pack from assigned: {2}.", 
				String.join(", ", this.storageShelfUIDs), 
				this.numberOfTicksToPack,
				this.totalNumberOfTicksToPack);
	}

	
}
