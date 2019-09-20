package amazon.packingStation;

import amazon.StorageShelf;
import amazon.Warehouse;

public interface PackingStationState {
  public void tick(Warehouse warehouse, int currentTickCount) throws Exception;

  /**
	 * Take note that a robot has returned from a storage shelf.
	 * 
	 * @param storageShelf The storage shelf reference.
	 * @throws Exception
	 */
	public void recieveItem(StorageShelf storageShelf) throws IllegalArgumentException;
} 