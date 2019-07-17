package simulation;
import javafx.collections.FXCollections;
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
	private ObservableList<ChargingPod> chargePods;
	private ArrayList<Robot> robots;
	private ObservableList<PackingStation> packingStations;
	private ObservableList<Robot> robotList;
	private Floor floor;
	private ObservableList<Order> assignedOrders;
	private ObservableList<Order> unassignedOrders;
	private ObservableList<Order> dispatchedOrders;
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
			e.printStackTrace();
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
		robots = new ArrayList<Robot>();
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
			if (entity instanceof Robot) {
				robots.add((Robot) entity);
				floor.loadEntity(entity);
			}
		}
		
		setRobots(FXCollections.observableArrayList(robots));
		setUnassignedOrders(FXCollections.observableArrayList(orders));
				
		
	}

	/**
	 * Simulator run method, keeps the simulation until all orders have been dispatched.
	 * @throws Exception 
	 */
	public void run() throws Exception {
		while (!this.warehouse.areAllOrdersDispatched())
			tick();
		System.out.println(String.format("All orders have been dispatched. It took %s ticks.", this.totalTickCount));
	}
	
	/**
	 * returns true if all orders have been dispatched.
	 * @return boolean
	 */
	
	public boolean isComplete() {
		return this.warehouse.areAllOrdersDispatched();
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
		setAssignedOrders(FXCollections.observableArrayList(warehouse.getAssignedOrders()));
		setDispatchedOrders(FXCollections.observableArrayList(warehouse.getDispatchedOrders()));
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
	
	public void setChargeSpeed(int speed) {
		chargeSpeed = speed;
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
		return robotList;
	}
	
	public void setRobots(ObservableList<Robot> robots) {
		robotList = robots;
	}
	
	public void setAssignedOrders(ObservableList<Order> assignedOrders) {
		this.assignedOrders = assignedOrders;
	}
	
	public void setDispatchedOrders(ObservableList<Order> dispatchedOrders) {
		this.dispatchedOrders = dispatchedOrders;
	}
	
	public ObservableList<PackingStation> packingStationsProperty(){
		return packingStations;
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
	
	public void setUnassignedOrders(ObservableList<Order> orders) {
		unassignedOrders = orders;
	}


	public List<Actor> getActors() {
		return actors;
	}
	
	public ArrayList<Robot> getRobots(){
		return robots;
	}

	public void setMaxChargeCapacity(int capacity) {
		maxChargeCapacity = capacity;
	}
	
	public void resetSimulator() {
		totalTickCount = 0;
		actors.clear();
		chargeSpeed = 1;
		maxChargeCapacity = 10;
		floor.clear();
	}
	
	public Warehouse getWarehouse() {
		return warehouse;
	}

}
