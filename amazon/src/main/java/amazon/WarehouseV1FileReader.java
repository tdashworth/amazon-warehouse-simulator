package amazon;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

import simulator.AEntity;
import simulator.Floor;
import simulator.IActor;
import simulator.ISimulatorFileReader;
import simulator.Location;
import simulator.LocationNotValidException;
import simulator.SimFileFormatException;

/**
 * TODO JavaDoc description.
 */
public class WarehouseV1FileReader implements ISimulatorFileReader<Warehouse> {
	private int width;
	private int height;
	private int capacity;
	private int chargeSpeed;
	private List<AEntity> entities;
	private Deque<Order> orders;

	/**
	 * TODO JavaDoc description.
	 */
	public WarehouseV1FileReader() {
		this.entities = new ArrayList<AEntity>();
		this.orders = new LinkedList<Order>();
	}

	@Override
	public void read(Path fileLocation) throws SimFileFormatException, IOException, LocationNotValidException {
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

	}

	public Warehouse getWorld() throws LocationNotValidException {
		Floor floor = new Floor(this.width, this.height);
		return new Warehouse(floor, entities, orders);
	}

	@SuppressWarnings("unchecked")
	public List<IActor<Warehouse>> getActors() {
		return entities.stream().sorted((e1, e2) -> e1.getUID().compareTo(e2.getUID()))
				.filter(entity -> entity instanceof IActor).map(entity -> (IActor<Warehouse>) entity)
				.collect(Collectors.toList());
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

		if (words.size() == 0)
			; // Empty line.

		switch (words.get(0)) {
		case "width":
			this.setWidth(words);
			break;

		case "height":
			this.setHeight(words);
			break;

		case "capacity":
			this.setCapacity(words);
			break;

		case "chargeSpeed":
			this.setChargeSpeed(words);
			break;

		case "podRobot":
			this.createRobotAndChargingPod(words);
			break;

		case "shelf":
			this.createStorageShelf(words);
			break;

		case "station":
			this.createPackingStation(words);
			break;

		case "order":
			this.createOrder(words);
			break;

		default:
			break;
		}
	}

	private void setWidth(List<String> words) {
		this.width = Integer.parseInt(words.get(1));
		this.log("Width set to %s", this.width);
	}

	private void setHeight(List<String> words) {
		this.height = Integer.parseInt(words.get(1));
		this.log("Height set to %s", this.height);
	}

	private void setCapacity(List<String> words) {
		this.capacity = Integer.parseInt(words.get(1));
		this.log("Capacity set to %s", this.capacity);
	}

	private void setChargeSpeed(List<String> words) {
		this.chargeSpeed = Integer.parseInt(words.get(1));
		this.log("Charge Speed set to %s", this.chargeSpeed);
	}

	private void createRobotAndChargingPod(List<String> words) throws NumberFormatException, LocationNotValidException {
		Location location = this.createAndValidateLocation(Integer.parseInt(words.get(3)),
				Integer.parseInt(words.get(4)));
		ChargingPod chargingPod = new ChargingPod(words.get(1), location);
		Robot robot = new Robot(words.get(2), location, chargingPod, this.capacity, this.chargeSpeed);
		this.entities.add(chargingPod);
		this.log("Charging Pod %s created at %s", chargingPod.getUID(), location);
		this.entities.add(robot);
		this.log("Robot %s created at %s", robot.getUID(), location);
	}

	private void createStorageShelf(List<String> words) throws NumberFormatException, LocationNotValidException {
		Location location = this.createAndValidateLocation(Integer.parseInt(words.get(2)),
				Integer.parseInt(words.get(3)));
		StorageShelf storageShelf = new StorageShelf(words.get(1), location);
		this.entities.add(storageShelf);
		this.log("Storage Shelf %s created at %s", storageShelf.getUID(), location);
	}

	private void createPackingStation(List<String> words) throws NumberFormatException, LocationNotValidException {
		Location location = this.createAndValidateLocation(Integer.parseInt(words.get(2)),
				Integer.parseInt(words.get(3)));
		PackingStation packingStation = new PackingStation(words.get(1), location);
		this.entities.add(packingStation);
		this.log("Packing Station %s created at %s", packingStation.getUID(), location);
	}

	private void createOrder(List<String> words) {
		Order order = new Order(words.subList(2, words.size()), Integer.parseInt(words.get(1)));
		orders.add(order);
		this.log("Order %s created.", order.hashCode());
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
