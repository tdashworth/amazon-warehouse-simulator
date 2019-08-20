package main.simulation;

import java.io.IOException;
import java.nio.file.Path;
import main.simulation.LocationNotValidException;

public interface ISimulatorReport<W extends AWorld> {
  /**
   * TODO JavaDoc description.
   * 
   * @throws IOException
   * @throws LocationNotValidException
   */
  public void write(Simulator<?> simulator, Path fileLocation) throws Exception;
}
