package model;

import java.util.*;
import simulation.Simulator;

public class Warehouse {
	private final Floor floor;
	private final ItemManager<Order> orderManager;
	// private final ItemManager<Job> jobManager;
	private final Map<String, Entity> entities;
	private final Simulator simulator;


	/**
	 * A representation of a warehouse
	 * 
	 * @param floor
	 * @param entities
	 * @param orders
	 */
	public Warehouse(Floor floor, Map<String, Entity> entities, Deque<Order> orders,
			Simulator simulator) {
		this.floor = floor;
		this.entities = entities;
		this.orderManager = new ItemManager<Order>(orders);
		this.simulator = simulator;
	}

	/**
	 * @param uid
	 * @return The entity with the given UID.
	 */
	public Entity getEntityByUID(String uid) {
		return this.entities.get(uid);
	}

	/**
	 * Searches for a robot that will accept the job and assign to it.
	 * 
	 * @param storageShelf
	 * @param packingStation
	 * @throws LocationNotValidException
	 */
	public boolean assignJobToRobot(StorageShelf storageShelf, PackingStation packingStation)
			throws Exception {
		for (Entity entity : this.entities.values()) {
			// If entity is a robot and the robot accepts the job, return true, otherwise keep going.
			if (entity instanceof Robot && ((Robot) entity).acceptJob(storageShelf, packingStation, this))
				return true;
		}
		// No robot accepted the job.
		return false;
	}

	/**
	 * @return The warehouse floor.
	 */
	public Floor getFloor() {
		return this.floor;
	}

	/**
	 * @return The order manager.
	 */
	public ItemManager<Order> getOrderManager() {
		return this.orderManager;
	}

	/**
	 * @return the totalTickCounts
	 */
	public int getTotalTickCount() {
		return this.simulator.getTotalTickCount();
	}

	/**
	 * @return A string representation of the warehouse.
	 */
	public String toString() {
		return "Warehouse";
	}

	protected void log(String message) {
		String classType = this.getClass().getSimpleName();
		System.out.println(String.format("%s: %s", classType, message));
	}

	protected void log(String format, Object... args) {
		this.log(String.format(format, args));
	}

	public Collection<Entity> getEntities() {
		return Collections.unmodifiableCollection(this.entities.values());
	}

}
