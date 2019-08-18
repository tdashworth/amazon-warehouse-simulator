package main.model;

import java.util.*;
import main.utils.ItemManager;

public class Warehouse {
	private final Floor floor;
	private final ItemManager<Order> orderManager;
	private final ItemManager<Job> jobManager;
	private final Map<String, AbstractEntity> entities;

	/**
	 * A representation of a warehouse
	 * 
	 * @param floor
	 * @param entities
	 * @param orders
	 */
	public Warehouse(Floor floor, Map<String, AbstractEntity> entities, Deque<Order> orders) {
		if (floor == null)
			throw new IllegalArgumentException("'floor' is a required, non-null parameter.");

		if (entities == null)
			throw new IllegalArgumentException("'entities' is a required, non-null parameter.");

		if (orders == null)
			throw new IllegalArgumentException("'orders' is a required, non-null parameter.");

		this.floor = floor;
		this.entities = entities;
		this.orderManager = new ItemManager<Order>(orders);
		this.jobManager = new ItemManager<Job>();
	}

	/**
	 * @param uid
	 * @return The entity with the given UID.
	 */
	public final AbstractEntity getEntityByUID(String uid) {
		return this.entities.get(uid);
	}

	/**
	 * @return The warehouse floor.
	 */
	public final Floor getFloor() {
		return this.floor;
	}

	/**
	 * @return The order manager.
	 */
	public final ItemManager<Order> getOrderManager() {
		return this.orderManager;
	}

	/**
	 * @return The job manager.
	 */
	public final ItemManager<Job> getJobManager() {
		return this.jobManager;
	}

	/**
	 * @return an unmodifable collection of entities.
	 */
	public final Collection<AbstractEntity> getEntities() {
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
