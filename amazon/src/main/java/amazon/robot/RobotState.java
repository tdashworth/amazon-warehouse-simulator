package amazon.robot;

import amazon.Warehouse;

interface RobotState {
  public void tick(Warehouse warehouse, int currentTickCount) throws Exception;
}