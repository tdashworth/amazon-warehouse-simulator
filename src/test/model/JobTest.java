package test.model;

import org.junit.Test;

import main.model.Job;
import main.model.Location;
import main.model.Order;
import main.model.PackingStation;
import main.model.StorageShelf;
import main.model.Job.JobStatus;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.Arrays;

public class JobTest {
  @Test
  public void constructorTest() {
    StorageShelf storageShelf = new StorageShelf("ss1", new Location(0, 0));
    PackingStation packingStation = new PackingStation("ps1", new Location(0, 0));
    Job job = new Job(storageShelf, packingStation);

    assertEquals(JobStatus.Collecting, job.getStatus());
    assertEquals(storageShelf, job.getStorageShelf());
    assertEquals(packingStation, job.getPackingShelf());
  }

  @Test
  public void collectedTest() {
    StorageShelf storageShelf = new StorageShelf("ss1", new Location(0, 0));
    PackingStation packingStation = new PackingStation("ps1", new Location(0, 0));
    Job job = new Job(storageShelf, packingStation);

    job.collected();

    assertEquals(JobStatus.Delivering, job.getStatus());
    assertEquals(storageShelf, job.getStorageShelf());
    assertEquals(packingStation, job.getPackingShelf());
  }

  @Test
  public void deliveredTest() throws Exception {
    StorageShelf storageShelf = new StorageShelf("ss1", new Location(0, 0));
    PackingStation packingStation = new PackingStation("ps1", new Location(0, 0)) {
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
