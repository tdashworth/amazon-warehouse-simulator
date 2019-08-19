package main.amazon;

import java.util.*;
import main.utils.ItemManager;
import main.simulation.AEntity;
import main.simulation.AMover;
import main.simulation.Floor;
import main.simulation.ISimulatorReport;
import main.simulation.IWorld;
import main.simulation.LocationNotValidException;

public class Warehouse implements IWorld {
	private final Floor floor;
	private final ItemManager<Order> orderManager;
	private final ItemManager<Job> jobManager;
	private final Map<String, AEntity> entities;

	/**
	 * A representation of a warehouse
	 * 
	 * @param floor
	 * @param entities
	 * @param orders
	 * @throws LocationNotValidException
	 */
	@SuppressWarnings("unchecked")
	public Warehouse(Floor floor, Map<String, AEntity> entities, Deque<Order> orders)
			throws LocationNotValidException {
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

		for (AEntity entity : entities.values()) {
			if (entity instanceof AMover) {
				floor.loadMover((AMover<Warehouse>) entity);
			}
		}
	}

	/**
	 * @param uid
	 * @return The entity with the given UID.
	 */
	public AEntity getEntityByUID(String uid) {
		return this.entities.get(uid);
	}

	/**
	 * @return The warehouse floor.
	 */
	public Floor getFloor() {
		return this.floor;
	}

	public final boolean isComplete() {
		return this.getOrderManager().areAllItemsComplete();
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
	 * @return an unmodifable collection of entities.
	 */
	public Collection<AEntity> getEntities() {
		return Collections.unmodifiableCollection(this.entities.values());
	}

	public ISimulatorReport<Warehouse> getReportWriter() {
		return new BasicSimulatorReport(this);
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
