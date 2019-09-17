package amazon;

import java.util.*;

import simulator.AEntity;
import simulator.AWorld;
import simulator.Floor;
import simulator.LocationNotValidException;
import simulator.utils.ItemManager;

public class Warehouse extends AWorld {
	private final ItemManager<Order> orderManager;
	private final ItemManager<Job> jobManager;

	/**
	 * A representation of a warehouse
	 * 
	 * @param floor
	 * @param entities
	 * @param orders
	 * @throws LocationNotValidException
	 */
	public Warehouse(Floor floor, List<AEntity> entities, Deque<Order> orders)
			throws LocationNotValidException {
		super(floor, entities);

		if (orders == null)
			throw new IllegalArgumentException("'orders' is a required, non-null parameter.");

		this.orderManager = new ItemManager<Order>(orders);
		this.jobManager = new ItemManager<Job>();
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
	 * @return A string representation of the warehouse.
	 */
	public String toString() {
		return "Warehouse";
	}
}
