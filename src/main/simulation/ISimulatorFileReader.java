package main.simulation;

import java.io.IOException;
import java.nio.file.Path;

import main.simulation.LocationNotValidException;

public interface ISimulatorFileReader<W extends IWorld> {
	/**
	 * TODO JavaDoc description.
	 * 
	 * @throws IOException
	 * @throws LocationNotValidException
	 */
	public W read(Path fileLocation)
			throws SimFileFormatException, IOException, LocationNotValidException;
}
