package model;
import java.text.MessageFormat;
import java.util.*;

import simulation.Simulator;

public class Warehouse {
	private Floor floor;
	private Map<String, Entity> entities;
	private Deque<Order> unassignedOrders;
	private Set<Order> assignedOrders;
	private Deque<Order> dispatchedOrders;
	private Simulator simulator;

	/**
	 * A representation of a warehouse
	 * @param floor
	 * @param entities
	 * @param orders
	 */
	public Warehouse(Floor floor, Map<String, Entity> entities, Deque<Order> orders, Simulator simulator) {
		this.floor = floor;
		this.entities = entities;
		this.unassignedOrders = orders;
		this.assignedOrders = new HashSet<Order>();
		this.dispatchedOrders = new LinkedList<Order>();
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
	public boolean assignJobToRobot(StorageShelf storageShelf, PackingStation packingStation) {
		for (Entity entity : this.entities.values()) {
			// If entity is a robot and the robot accepts the job, return true, otherwise keep going.
			if (entity instanceof Robot && ((Robot) entity).acceptJob(storageShelf, packingStation))
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
	 * @return true if all orders have been dispatched.
	 */
	public boolean areAllOrdersDispatched() {
		return this.assignedOrders.isEmpty() && this.unassignedOrders.isEmpty();
	}
	
	/**
	 * @return the totalTickCounts
	 */
	public int getTotalTickCount() {
		return this.simulator.getTotalTickCount();
	}

	/**
	 * @return the chargeSpeed
	 */
	public int getChargeSpeed() {
		return this.simulator.getChargeSpeed();
	}

	/**
	 * @return the maxChargeCapacity
	 */
	public int getMaxChargeCapacity() {
		return this.simulator.getMaxChargeCapacity();
	}
	
	/**
	 * @return A string representation of the warehouse.
	 */
	public String toString() {
		return"Warehouse:"
				+ "unassignedOrders: " + unassignedOrders.size()
				+ "assignedOrders: " + assignedOrders.size()
				+ "dispatchedOrders: " + dispatchedOrders.size();
	}
}
