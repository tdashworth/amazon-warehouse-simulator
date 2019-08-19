package main.simulation;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

import main.model.*;

/**
 * TODO JavaDoc description.
 */
public class Version1SimulatorFileReader implements ISimulatorFileReader {
	private int width;
	private int height;
	private int capacity;
	private int chargeSpeed;
	private HashMap<String, AbstractEntity> entities;
	private Deque<Order> orders;

	/**
	 * TODO JavaDoc description.
	 */
	public Version1SimulatorFileReader() {
		this.entities = new HashMap<String, AbstractEntity>();
		this.orders = new LinkedList<Order>();
	}

	/**
	 * TODO JavaDoc description.
	 * 
	 * @throws IOException
	 * @throws LocationNotValidException
	 */
	@Override
	public Simulator read(Path fileLocation)
			throws SimFileFormatException, IOException, LocationNotValidException {
		this.log("Reading file: %s", fileLocation.toAbsolutePath());
		List<String> lines = Files.readAllLines(fileLocation);
		if (lines.size() == 0)
			throw new SimFileFormatException("", "File is empty.");

		if (!lines.get(0).startsWith("format 1"))
			throw new SimFileFormatException(lines.get(0), "File format is not '1'.");

		for (String line : lines) {
			try {
				this.parseLine(line);
			} catch (NumberFormatException | LocationNotValidException e) {
				throw new SimFileFormatException(line, e.toString());
			}
		}

		this.log("File read. Creating Simulation...");
		Floor floor = new Floor(this.width, this.height);
		Simulator simulator = new Simulator(floor, entities, orders);
		return simulator;
	}

	/**
	 * TODO JavaDoc description.
	 * 
	 * @throws Exception
	 * @throws NumberFormatException
	 * @throws LocationNotValidException
	 */
	private void parseLine(String line) throws NumberFormatException, LocationNotValidException {
		List<String> words = Arrays.asList(line.split(" "));
		Location location;

		if (words.size() == 0)
			; // Empty line.

		switch (words.get(0)) {
			case "width":
				this.width = Integer.parseInt(words.get(1));
				this.log("Width set to %s", this.width);
				break;

			case "height":
				this.height = Integer.parseInt(words.get(1));
				this.log("Heigth set to %s", this.height);
				break;

			case "capacity":
				this.capacity = Integer.parseInt(words.get(1));
				this.log("Robot's charge capacity set to %s", this.capacity);
				break;

			case "chargeSpeed":
				this.chargeSpeed = Integer.parseInt(words.get(1));
				this.log("Robot's charge speed set to %s", this.chargeSpeed);
				break;

			case "podRobot":
				location = this.createAndValidateLocation(Integer.parseInt(words.get(3)),
						Integer.parseInt(words.get(4)));
				ChargingPod chargingPod = new ChargingPod(words.get(1), location);
				Robot robot =
						new Robot(words.get(2), location, chargingPod, this.capacity, this.chargeSpeed);
				this.entities.put(words.get(1), chargingPod);
				this.log("Charging Pod %s created at %s", chargingPod.getUID(), location);
				this.entities.put(words.get(2), robot);
				this.log("Robot %s created at %s", robot.getUID(), location);
				break;

			case "shelf":
				location = this.createAndValidateLocation(Integer.parseInt(words.get(2)),
						Integer.parseInt(words.get(3)));
				StorageShelf storageShelf = new StorageShelf(words.get(1), location);
				this.entities.put(words.get(1), storageShelf);
				this.log("Storage Shelf %s created at %s", storageShelf.getUID(), location);
				break;

			case "station":
				location = this.createAndValidateLocation(Integer.parseInt(words.get(2)),
						Integer.parseInt(words.get(3)));
				PackingStation packingStation = new PackingStation(words.get(1), location);
				this.entities.put(words.get(1), packingStation);
				this.log("Packing Station %s created at %s", packingStation.getUID(), location);
				break;

			case "order":
				Order order = new Order(words.subList(2, words.size()), Integer.parseInt(words.get(1)));
				orders.add(order);
				this.log("Order %s created.", order.hashCode());
				break;

			default:
				break;
		}
	}

	private Location createAndValidateLocation(int column, int row) throws LocationNotValidException {
		Floor floor = new Floor(this.width, this.height);
		Location location = new Location(column, row);
		floor.validateLocation(location);
		return location;
	}

	private void log(String message) {
		String classType = this.getClass().getSimpleName();
		System.out.println(String.format("%s: %s", classType, message));
	}

	private void log(String format, Object... args) {
		this.log(String.format(format, args));
	}

}
