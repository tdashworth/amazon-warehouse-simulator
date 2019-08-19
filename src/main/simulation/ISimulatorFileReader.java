package main.simulation;

import java.io.IOException;
import java.nio.file.Path;

import main.model.LocationNotValidException;

public interface ISimulatorFileReader {
	/**
	 * TODO JavaDoc description.
	 * 
	 * @throws IOException
	 * @throws LocationNotValidException
	 */
	public Simulator read(Path fileLocation)
			throws SimFileFormatException, IOException, LocationNotValidException;
}
