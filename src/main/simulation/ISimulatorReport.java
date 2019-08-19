package main.simulation;

import java.io.IOException;
import main.model.LocationNotValidException;

public interface ISimulatorReport {
  /**
   * TODO JavaDoc description.
   * 
   * @throws IOException
   * @throws LocationNotValidException
   */
  public void write(Simulator simulator) throws Exception;
}
