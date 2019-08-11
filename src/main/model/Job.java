package main.model;

/**
 * A container for a Storage Shelf and Packing Station which make up a Job.
 */
public class Job {
  private final StorageShelf storageShelf;
  private final PackingStation packingStation;
  private JobStatus status;

  public static enum JobStatus {
    Collecting, Delivering, Delivered
  }

  /**
   * A container for a Storage Shelf and Packing Station which make up a Job.
   * 
   * @param storageShelf   the Storage Shelf for this Job.
   * @param packingStation the Packing Station for this Job.
   */
  public Job(StorageShelf storageShelf, PackingStation packingStation) {
    this.storageShelf = storageShelf;
    this.packingStation = packingStation;
    this.status = JobStatus.Collecting;
  }

  /**
   * Changes status to Delivering.
   */
  public void collected() {
    this.status = JobStatus.Delivering;
  }

  /**
   * Changes status to Delieved and notifys Packing Station of delivery.
   * 
   * @throws Exception
   */
  public void delivered() throws Exception {
    this.packingStation.recieveItem(this.storageShelf);
    this.status = JobStatus.Delivered;
  }

  /**
   * @return the job's status.
   */
  public JobStatus getStatus() {
    return this.status;
  }

  /**
   * @return the Storage Shelf for this Job.
   */
  public StorageShelf getStorageShelf() {
    return this.storageShelf;
  }

  /**
   * @return the Packing Station for this Job.
   */
  public PackingStation getPackingShelf() {
    return this.packingStation;
  }

  @Override
  public String toString() {
    String result = "Job(" + this.hashCode() + "): ";
    result += "Storage Shelf: " + this.storageShelf.getUID();
    result += ", " + "Packing Station: " + this.packingStation.getUID();
    return result;
  }
}
