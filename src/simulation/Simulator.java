package simulation;
import View.*;
import javafx.beans.property.IntegerProperty;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

import model.*;

public class Simulator {
	private int totalTickCount;
	private List<Actor> actors;
	private Warehouse warehouse;
	private int chargeSpeed;
	private int maxChargeCapacity;
	private WarehouseController controller;
	private ObservableList<ChargingPod> chargePods;
	private ObservableList<Robot> robots;
	private ObservableList<PackingStation> packingStations;
	private ObservableList<StorageShelf> shelves;
	private ObservableList<Order> assignedOrders;
	private ObservableList<Order> unassignedOrders;
	private ObservableList<Order> dispatchedOrders;
	private WarehouseView view;
	private Floor floor;
	/**
	 * Main method, creates a simulator and starts the simulation run method.
	 */
	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("Error, usage: java ClassName inputfile");
			System.exit(1);
		}

		Simulator simulator;
		try {
			simulator = createFromFile(Paths.get(args[0]));
			simulator.run();
		} catch (IOException | SimFileFormatException | LocationNotValidException e) {
			System.out.println("Error reading SIM file - " + e.toString());
			System.exit(1);
		} catch (Exception e) {
			System.out.println("Error running simulation - " + e.toString());
			System.exit(1);
		}
	}

	/**
	 * Returns a Simulator configured from the file given. Based off the first line
	 * of the file, the correct reader will be chosen to parse the file.
	 * 
	 * @param fileLocation
	 * @return a configured simulation.
	 * @throws IOException
	 * @throws SimFileFormatException
	 * @throws LocationNotValidException
	 */
	public static Simulator createFromFile(Path fileLocation)
			throws IOException, SimFileFormatException, LocationNotValidException {
		SimulatorFileReader simulatorFileReader;
		List<String> lines = Files.readAllLines(fileLocation);

		if (lines.size() == 0)
			throw new SimFileFormatException("", "File is empty or of wrong format.");

		switch (lines.get(0)) {
		case "format 1":
			simulatorFileReader = new SimulatorFileReader_V1();
			break;
		default:
			throw new SimFileFormatException(lines.get(0), "File is empty or of wrong format.");
		}

		Simulator simulator = simulatorFileReader.read(fileLocation);
		return simulator;
	}

	/**
	 * Simulator constructor setting up the warehouse and entities 
	 * @throws LocationNotValidException
	 */
	public Simulator(Floor floor, int capacity, int chargeSpeed, HashMap<String, Entity> entities, Deque<Order> orders)
			throws LocationNotValidException {
		this.floor = floor;
		this.totalTickCount = 0;
		this.warehouse = new Warehouse(floor, entities, orders, this);
		this.actors = entities.values()
				.stream()
				.sorted((e1, e2) -> e1.getUID().compareTo(e2.getUID()))
				.filter(entity -> entity instanceof Actor)
				.map(entity -> (Actor) entity)
				.collect(Collectors.toList());
		this.chargeSpeed = chargeSpeed;
		this.maxChargeCapacity = capacity;
		for (Entity entity : entities.values()) {
			if (entity instanceof Robot)
				floor.loadEntity(entity);
		}
	}

	/**
	 * Simulator run method, keeps the simulation until all orders have been dispatched.
	 * @throws Exception 
	 */
	public void run() throws Exception {
		while (!this.warehouse.areAllOrdersDispatched())
			tick();
		System.out.println("All orders have been dispatched.");
	}
	
	
	/**
	 * Set the view object
	 * @param WarehouseView
	 */
	public void setView(WarehouseView view) {
		this.view = view;
	}
	
		
	/**
	 * Tick method which gets all of the actors to tick simultaneously.
	 * @throws Exception 
	 */
	public void tick() throws Exception {
		this.totalTickCount++;
		for (Actor actor : actors) {
			actor.tick(this.warehouse);
		}
	}

	/**
	 * @return the totalTickCounts
	 */
	public int getTotalTickCount() {
		return totalTickCount;
	}

	/**
	 * sets the tick count
	 * @param ticks
	 */
	
	public void setTotalTickCount(int ticks) {
		totalTickCount = ticks;
	}
	
	public int getChargeSpeed() {
		return chargeSpeed;
	}
	
	/**
	 * @return the maxChargeCapacity
	 */

	public int getMaxChargeCapacity() {
		return maxChargeCapacity;
	}
	
	public Floor getFloor() {
		return floor;
	}

	public 	ObservableList<ChargingPod> chargePodsProperty(){
		return chargePods;
	}
	public ObservableList<Robot> robotsProperty(){
		return robots;
	}
	public ObservableList<PackingStation> packingStationsProperty(){
		return packingStations;
	}
	public ObservableList<StorageShelf> storageShelfProperty(){
		return shelves;
	}	
	public ObservableList<Order> assignedOrdersProperty(){
		return assignedOrders;
	}
	public ObservableList<Order> unassignedOrdersProperty(){
		return unassignedOrders;
	}
	public ObservableList<Order> dispatchedOrdersProperty(){
		return dispatchedOrders;
	}
	
	
	
}
