package test;

import org.junit.Test;
import model.Job;
import model.Order;
import model.PackingStation;
import model.StorageShelf;
import model.Job.JobStatus;
import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.Arrays;

public class JobTest {
  @Test
  public void constructorTest() {
    StorageShelf storageShelf = new StorageShelf("ss1", null);
    PackingStation packingStation = new PackingStation("ps1", null);
    Job job = new Job(storageShelf, packingStation);

    assertEquals(JobStatus.Collecting, job.getStatus());
    assertEquals(storageShelf, job.getStorageShelf());
    assertEquals(packingStation, job.getPackingShelf());
  }

  @Test
  public void collectedTest() {
    StorageShelf storageShelf = new StorageShelf("ss1", null);
    PackingStation packingStation = new PackingStation("ps1", null);
    Job job = new Job(storageShelf, packingStation);

    job.collected();

    assertEquals(JobStatus.Delivering, job.getStatus());
    assertEquals(storageShelf, job.getStorageShelf());
    assertEquals(packingStation, job.getPackingShelf());
  }

  @Test
  public void deliveredTest() throws Exception {
    StorageShelf storageShelf = new StorageShelf("ss1", null);
    PackingStation packingStation = new PackingStation("ps1", null) {
      {
        // Fake the packing station has an order.
        this.currentOrder = new Order(Arrays.asList("ss1"), 0);
        // Required as a dependency.
        this.storageShelvesVisited = new ArrayList<>();
      }
    };
    Job job = new Job(storageShelf, packingStation);

    job.collected();
    job.delivered();

    assertEquals(JobStatus.Delivered, job.getStatus());
    assertEquals(storageShelf, job.getStorageShelf());
    assertEquals(packingStation, job.getPackingShelf());
  }
}
