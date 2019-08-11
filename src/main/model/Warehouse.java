package main.model;

import java.util.*;

import main.utils.ItemManager;
import main.simulation.Simulator;

public class Warehouse {
	private final Floor floor;
	private final ItemManager<Order> orderManager;
	private final ItemManager<Job> jobManager;
	private final Map<String, AbstractEntity> entities;
	private final Simulator simulator;

	/**
	 * A representation of a warehouse
	 * 
	 * @param floor
	 * @param entities
	 * @param orders
	 */
	public Warehouse(Floor floor, Map<String, AbstractEntity> entities, Deque<Order> orders,
			Simulator simulator) {
		this.floor = floor;
		this.entities = entities;
		this.orderManager = new ItemManager<Order>(orders);
		this.jobManager = new ItemManager<Job>();
		this.simulator = simulator;
	}

	/**
	 * @param uid
	 * @return The entity with the given UID.
	 */
	public AbstractEntity getEntityByUID(String uid) {
		return this.entities.get(uid);
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
	 * @return The job manager.
	 */
	public ItemManager<Job> getJobManager() {
		return this.jobManager;
	}

	/**
	 * @return the totalTickCounts
	 */
	public int getTotalTickCount() {
		return this.simulator.getTotalTickCount();
	}

	/**
	 * @return an unmodifable collection of entities.
	 */
	public Collection<AbstractEntity> getEntities() {
		return Collections.unmodifiableCollection(this.entities.values());
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
}
