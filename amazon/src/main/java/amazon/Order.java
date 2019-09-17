package amazon;

import java.util.Collections;
import java.util.List;

/**
 * A representation of an Order to pack containing a list of StorageShelves to visit.
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
		if (storageShelfUIDs == null || storageShelfUIDs.isEmpty())
			throw new IllegalArgumentException(
					"'storageShelfUIDs' is a required, non-null parameter with atleast one element.");

		if (numberOfTicksToPack < 0)
			throw new IllegalArgumentException("'numberOfTicksToPack' must be a positive integer.");

		this.storageShelfUIDs = storageShelfUIDs;
		this.numberOfTicksToPack = numberOfTicksToPack;
	}

	/**
	 * @return The storage shelf UIDs required in the order.
	 */
	public List<String> getStorageShelfUIDs() {
		return Collections.unmodifiableList(this.storageShelfUIDs);
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
	 * 
	 * @param The total number of ticks to pack from assigned.
	 */
	public void setTotalNumberOfTicksToPack(int totalNumberOfTicksToPack) {
		if (totalNumberOfTicksToPack < 0)
			throw new IllegalArgumentException("'totalNumberOfTicksToPack' must be a positive integer.");

		this.totalNumberOfTicksToPack = totalNumberOfTicksToPack;
	}

	/**
	 * @return A string representation of the order.
	 */
	@Override
	public String toString() {
		String result = "Order(" + this.hashCode() + "): ";
		result += "Storage Shelfs: [" + String.join(", ", this.storageShelfUIDs) + "]";
		result += ", " + "Ticks to pack: " + this.numberOfTicksToPack;
		if (this.totalNumberOfTicksToPack > 0)
			result += ", " + "Total ticks to pack: " + this.totalNumberOfTicksToPack;

		return result;
	}

}
