package amazon.robot;

import amazon.Warehouse;

class DeliveringItemState implements RobotState {
  private final Robot context;

  public DeliveringItemState(Robot context) {
    this.context = context;
  }

  @Override
  public void tick(Warehouse warehouse, int currentTickCount) throws Exception {
    this.context.move(warehouse.getFloor(), this.context.currentJob.getPackingStation().getLocation());
		//this.log("Moved closer to assigned Packing Station.");

		if (this.context.getLocation().equals(this.context.currentJob.getPackingStation().getLocation())) {
			this.context.currentJob.delivered();
			warehouse.getJobManager().complete(this.context.currentJob);
      this.context.currentJob = null;
      this.context.state = this.context.awaitingJobState;
			//this.log("Reached assigned Packing Station.");
		}
  }

}