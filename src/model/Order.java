package model;

import java.text.MessageFormat;

/**
 *
 */
public class Order {
	private final String[] storageShelfUIDs;
	private final int numberOfTicksToPack;
	private int totalNumberOfTicksToPack;
	
	/**
	 * @param storageShelfUIDs
	 * @param numberOfTicksToPack
	 */
	public Order(String[] storageShelfUIDs, int numberOfTicksToPack) {
		this.storageShelfUIDs = storageShelfUIDs;
		this.numberOfTicksToPack = numberOfTicksToPack;
	}

	/**
	 * @return The storage shelf UIDs required in the order.
	 */
	public String[] getStorageShelfUIDs() {
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
				+ " - Total number of tick to pack from assigned: {3}.", 
				String.join(", ", this.storageShelfUIDs), 
				this.numberOfTicksToPack,
				this.totalNumberOfTicksToPack);
	}

	
}
