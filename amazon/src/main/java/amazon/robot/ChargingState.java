package amazon.robot;

import amazon.Warehouse;

class ChargingState implements RobotState {
  private final Robot context;

  public ChargingState(Robot context) {
    this.context = context;
  }

  @Override
  public void tick(Warehouse warehouse, int currentTickCount) throws Exception {
    this.context.charge();
  }
}