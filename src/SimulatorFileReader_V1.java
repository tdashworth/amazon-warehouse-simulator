import java.nio.file.*;
import java.util.*;
import model.*;

/**
 * TODO JavaDoc description.
 */
public class SimulatorFileReader_V1 implements SimulatorFileReader {
	private int width;
	private int height;
	private int capacity;
	private int chargeSpeed;
	private HashMap<String, Entity> entities;
	private Deque<Order> orders; 

	/**
	 * TODO JavaDoc description.
	 */
	public SimulatorFileReader_V1() {
		this.entities = new HashMap<String, Entity>();
		this.orders = new LinkedList<Order>();
	}

	/**
	 * TODO JavaDoc description.
	 */
	@Override
	public Simulator read(Path fileLocation) throws Exception {
		List<String> lines = Files.readAllLines(fileLocation);
		if (lines.size() == 0)
			throw new Exception("File is empty.");
		
		if (!lines.get(0).startsWith("format 1"))
			throw new Exception("File format is not '1'.");
		
		lines.stream().forEachOrdered(this::parseLine);
		
		Floor floor = new Floor(this.width, this.height); 
		Simulator simulator = new Simulator(floor, capacity, chargeSpeed, entities, orders);
		return simulator;
	}
	
	/**
	 * TODO JavaDoc description.
	 */
	private void parseLine(String line) {
		List<String> words = Arrays.asList(line.split(" "));
		Location location;
			
		if (words.size() == 0)
			; // Empty line.
		
		switch (words.get(0)) {
		case "width":
			this.width = Integer.parseInt(words.get(1));
			break;

		case "height":
			this.height = Integer.parseInt(words.get(1));
			break;
			
		case "capacity":
			this.capacity = Integer.parseInt(words.get(1));
			break;
			
		case "chargeSpeed":
			this.chargeSpeed = Integer.parseInt(words.get(1));
			break;
			
		case "podRobot":
			location = new Location(Integer.parseInt(words.get(3)), Integer.parseInt(words.get(4)));
			ChargingPod chargingPod = new ChargingPod(words.get(1), location);
			Robot robot = new Robot(words.get(2), location, chargingPod);
			this.entities.put(words.get(1), chargingPod);
			this.entities.put(words.get(2), robot);
			break;
			
		case "shelf":
			location = new Location(Integer.parseInt(words.get(2)), Integer.parseInt(words.get(3)));
			StorageShelf storageShelf = new StorageShelf(words.get(1), location);
			this.entities.put(words.get(1), storageShelf);
			break;	
			
		case "station":
			location = new Location(Integer.parseInt(words.get(2)), Integer.parseInt(words.get(3)));
			PackingStation packingStation = new PackingStation(words.get(1), location);
			this.entities.put(words.get(1), packingStation);
			break;
			
		case "order":
			Order order = new Order(words.subList(2, words.size()-1), Integer.parseInt(words.get(1)));
			orders.add(order);
			break;
			
		default:
			break;
		}
	}

}
