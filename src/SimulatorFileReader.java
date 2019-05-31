import java.nio.file.Path;

public interface SimulatorFileReader {
	/**
	 * TODO JavaDoc description.
	 */
	public Simulator read(Path fileLocation) throws Exception;
}
