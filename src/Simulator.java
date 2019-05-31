import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

import model.*;

public class Simulator {
	private int totalTickCounts;
	private List<Actor> actors;
	private Warehouse warehouse;
	private int chargeSpeed;
	private int maxChargeCapacity;
	
	/**
	 * TODO JavaDoc description.
	 */
	public static void main(String[] args) throws Exception {
		if (args.length == 0) {
			System.out.println("Error, usage: java ClassName inputfile");
			System.exit(1);
		}

		Simulator simulator = createFromFile(Paths.get(args[0]));
		simulator.run();
	}

	/**
	 * Returns a Simulator configured from the file given. Based off the first line
	 * of the file, the correct reader will be chosen to parse the file.
	 * 
	 * @param fileLocation
	 * @return a configured simulation.
	 * @throws Exception if the file is empty or has the wrong format for known
	 *                   readers.
	 */
	private static Simulator createFromFile(Path fileLocation) throws Exception {
		SimulatorFileReader simulatorFileReader;
		List<String> lines = Files.readAllLines(fileLocation);

		if (lines.size() == 0)
			throw new Exception("File is empty or of wrong format.");

		switch (lines.get(0)) {
		case "format 1":
			simulatorFileReader = new SimulatorFileReader_V1();
			break;
		default:
			throw new Exception("File is empty or of wrong format.");
		}

		Simulator simulator = simulatorFileReader.read(fileLocation);
		return simulator;
	}

	/**
	 * TODO JavaDoc description.
	 */
	Simulator(Floor floor, int capacity, int chargeSpeed, HashMap<String, Entity> entities,
			Deque<Order> orders) {
		this.totalTickCounts = 0;
		this.warehouse = new Warehouse(floor, entities, orders);
		this.actors = entities.values().stream()
				.sorted((e1, e2) -> e1.getUID().compareTo(e2.getUID()))
				.filter(entity -> entity instanceof Actor)
				.map(entity -> (Actor) entity)
				.collect(Collectors.toList());
		this.chargeSpeed = chargeSpeed;
		this.maxChargeCapacity = capacity;
	}

	/**
	 * TODO JavaDoc description.
	 */
	private void run() {
		while (!this.warehouse.areAllOrdersDispatched())
			tick();
	}
	
	/**
	 * TODO JavaDoc description.
	 */
	private void tick() {
		this.totalTickCounts++;
		this.actors.forEach(actor -> {
			try {
				actor.tick(this.warehouse);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}

}
