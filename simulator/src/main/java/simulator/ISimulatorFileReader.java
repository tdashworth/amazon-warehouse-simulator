package simulator;

import java.nio.file.Path;
import java.util.List;

public interface ISimulatorFileReader<W extends AWorld> {
	/**
	 * TODO JavaDoc description.
	 */
	public void read(Path fileLocation) throws Exception;

	public W getWorld() throws Exception;

	public List<IActor<W>> getActors();
}
