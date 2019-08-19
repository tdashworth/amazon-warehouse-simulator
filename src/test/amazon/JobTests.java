package test.amazon;

import org.junit.Test;
import main.amazon.Job;
import main.simulation.Location;
import main.amazon.PackingStation;
import main.amazon.StorageShelf;
import main.amazon.Job.JobStatus;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class JobTests {
  @Test
  public void testConstructorWithValidParametersShouldSuccussfullyCreate() {
    StorageShelf storageShelf = new StorageShelf("ss1", new Location(0, 0));
    PackingStation packingStation = new PackingStation("ps1", new Location(0, 0));
    Job job = new Job(storageShelf, packingStation);

    assertEquals(JobStatus.Collecting, job.getStatus());
    assertEquals(storageShelf, job.getStorageShelf());
    assertEquals(packingStation, job.getPackingStation());
  }

  @Test
  public void testCollectedShouldUpdateTheStatusToDelievering() {
    StorageShelf storageShelf = new StorageShelf("ss1", new Location(0, 0));
    PackingStation packingStation = new PackingStation("ps1", new Location(0, 0));
    Job job = new Job(storageShelf, packingStation);

    job.collected();

    assertEquals(JobStatus.Delivering, job.getStatus());
    assertEquals(storageShelf, job.getStorageShelf());
    assertEquals(packingStation, job.getPackingStation());
  }

  @Test
  public void testDelieveredShouldUpdateTheStatusToDelievered() throws Exception {
    StorageShelf storageShelf = new StorageShelf("ss1", new Location(0, 0));
    PackingStation packingStation = mock(PackingStation.class);
    Job job = new Job(storageShelf, packingStation);
    job.collected();

    job.delivered();

    assertEquals(JobStatus.Delivered, job.getStatus());
    assertEquals(storageShelf, job.getStorageShelf());
    assertEquals(packingStation, job.getPackingStation());
  }
}
