package model;

import java.util.Map;
import java.util.Deque;
import java.util.Set;

/**
 *	TODO JavaDoc description.
 */
public class Warehouse {
	private Floor floor;
	private Map<String, Entity> entities;
	private Deque<Order> unassignedOrders;
	private Set<Order> assignedOrders;
	private Deque<Order> dispatchedOrders;

	/**
	 * TODO JavaDoc description.
	 * @param floor
	 * @param entities
	 * @param orders
	 */
	public Warehouse(Floor floor, Map<String, Entity> entities, Deque<Order> orders) {
		this.floor = floor;
		this.entities = entities;
		this.unassignedOrders = orders;
	}
	
	/**
	 * @param uid
	 * @return The entity with the given UID.
	 */
	public Entity getEntityByUID(String uid) {
		return this.entities.get(uid);
	}
	
	/**
	 * Picks the next unassigned order and moves it to the assigned orders set.
	 * @return A new order. 
	 */
	public Order getUnassignedOrder() {
		Order order = this.unassignedOrders.pop();
		this.assignedOrders.add(order);
		return order;
	}
	
	/**
	 * Moves the order given from the assigned set to dispatched queue.
	 * @param order
	 * @throws Exception
	 */
	public void dispatchOrder(Order order) throws Exception {
		if(!this.assignedOrders.contains(order))
			throw new Exception("Order was not found in Assigned Orders.");
		
		this.assignedOrders.remove(order);
		this.dispatchedOrders.add(order);
	}
	
	/**
	 * Searches for a robot that will accept the job and assign to it.
	 * @param storageShelf
	 * @param packingStation
	 */
	public void assignJobToRobot(StorageShelf storageShelf, PackingStation packingStation) {
		// TODO filter entities for robots then ask each one if they can accept the job. 
		// Maybe throw exception if it couldn't be assigned.
	}
	
	/**
	 * @return The warehouse floor. 
	 */
	public Floor getFloor() {
		return this.floor;
	}
	
	/**
	 * @return A string representation of the warehouse.
	 */
	public String toString() {
		// TODO Warehouse toString. 
		return "Warehouse: ...";
	}

	/**
	 * @return true if all orders have been dispatched.
	 */
	public boolean areAllOrdersDispatched() {
		return this.assignedOrders.size() == 0 && this.unassignedOrders.isEmpty();
	}

}
