package amazon.robot;

import amazon.Warehouse;

class CollectingItemState implements RobotState {
  private final Robot context;

  public CollectingItemState(Robot context) {
    this.context = context;
  }

  @Override
  public void tick(Warehouse warehouse, int currentTickCount) throws Exception {
    this.context.move(warehouse.getFloor(), this.context.currentJob.getStorageShelf().getLocation());
    //this.log("Moved closer to assigned Storage Shelf.");

    if (this.context.getLocation().equals(this.context.currentJob.getStorageShelf().getLocation())) {
      this.context.currentJob.collected();
      this.context.state = this.context.deliveringItemState;
      //this.log("Reached assigned Storage Shelf.");
    }
  }

}