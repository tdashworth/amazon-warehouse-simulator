package simulator;

import java.io.IOException;
import java.nio.file.Path;

public interface ISimulatorReport<W extends AWorld> {
  /**
   * TODO JavaDoc description.
   * 
   * @throws IOException
   * @throws LocationNotValidException
   */
  public void write(Simulator<?> simulator, Path fileLocation) throws Exception;
}
