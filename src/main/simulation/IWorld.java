package main.simulation;

import main.simulation.Floor;

public interface IWorld {
  public boolean isComplete();

  public Floor getFloor();

  public ISimulatorReport<? extends IWorld> getReportWriter();
}