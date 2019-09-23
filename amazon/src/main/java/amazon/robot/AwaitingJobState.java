package amazon.robot;

import amazon.*;
import simulator.*;
import simulator.utils.*;

class AwaitingJobState implements RobotState {
  private final Robot context;

  public AwaitingJobState(Robot context) {
    this.context = context;
  }

  @Override
  public void tick(Warehouse warehouse, int currentTickCount) throws Exception {
    // If at Charging Pod, charge, otherwise move towards it.
    if (this.context.getLocation().equals(this.context.chargingPod.getLocation()))
      this.context.charge();
    else
      this.context.move(warehouse.getFloor(), this.context.chargingPod.getLocation());

    // Check if there's an acceptable Job to pick up.
    this.pickupJob(warehouse.getJobManager(), warehouse.getFloor());
  }

  /**
	 * @param jobManager
	 * @param floor
	 * @throws Exception
	 */
	private void pickupJob(ItemManager<Job> jobManager, Floor floor) throws Exception {
		if (!jobManager.areItemsToPickup())
			return;

		// Check out next Job and validate estimated cost.
		Job tempJob = jobManager.viewNextPickup();
		double estimatedCostWithLeeway = estimatePowerUnitCostForJob(tempJob, floor);
		if (estimatedCostWithLeeway > this.context.powerUnits)
			return;

		// Pickup Job
		this.context.currentJob = jobManager.pickup();
    this.context.setPathFinder(null);
    this.context.state = this.context.collectingItemState;

		// this.log("Job to %s then %s picked up.", this.context.currentJob.getStorageShelf().getLocation(),
		// 		this.context.currentJob.getPackingStation().getLocation());
  }

  enum CostEstimationFactors { Laidened, Unlaidened };
  
  /**
	 * Given a storage shelf and packing station this will calculate the number of power units to make
	 * the trip back to its charging pod with a leeway of 20%.
	 * 
	 * @param storageShelf
	 * @param packingStation
	 * @param warehouse
	 * @return
	 * @throws LocationNotValidException
	 */
	private double estimatePowerUnitCostForJob(Job job, Floor floor) throws Exception {

		// Setup Cost Estimator
		IPathCostEstimator<CostEstimationFactors> costEstimator = new BasicPathCostEstimator<>(this.context.getLocation());
		costEstimator.addCost(CostEstimationFactors.Laidened, Double.valueOf(Robot.POWER_UNITS_CARRYING));
		costEstimator.addCost(CostEstimationFactors.Unlaidened, Double.valueOf(Robot.POWER_UNITS_EMPTY));

		// Add path Locations
		costEstimator.addLocation(job.getStorageShelf().getLocation(), CostEstimationFactors.Unlaidened);
		costEstimator.addLocation(job.getPackingStation().getLocation(), CostEstimationFactors.Laidened);
		costEstimator.addLocation(this.context.chargingPod.getLocation(), CostEstimationFactors.Unlaidened);

		// Add leeway to estimated cost and return.
		double estimatedCostWithLeeway = costEstimator.getEstimatedCost() * 1.2;
		return estimatedCostWithLeeway;
	}

}