package simulation;

import java.io.IOException;
import java.nio.file.Path;
import model.LocationNotValidException;

public interface SimulatorFileReader {
	/**
	 * TODO JavaDoc description.
	 * 
	 * @throws IOException
	 * @throws LocationNotValidException
	 */
	public Simulator read(Path fileLocation)
			throws SimFileFormatException, IOException, LocationNotValidException;
}
