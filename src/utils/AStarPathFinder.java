package utils;

import java.util.*;
import model.Floor;
import model.Location;
import model.LocationNotValidException;

/**
 * A strategy to finding an optimal path between two location on a floor. This strategy explores
 * nodes in a recursive fashion choosing the "most efficient" node to explore next. The most
 * efficient node is determined by a combination of heuristic (closest direct distance) and cost
 * (number of steps it took to travel to the node).
 */
public class AStarPathFinder extends PathFinder {
	private PathFindingNode[][] floorNodes;

	List<PathFindingNode> unexploredNodes;
	List<PathFindingNode> exploredNodes;

	/**
	 * Constructs a strategy for path finding with a default value to avoid collisions.
	 * 
	 * @param floor the floor determining the size and location of other robots.
	 */
	public AStarPathFinder(Floor floor, Location origin, Location target)
			throws LocationNotValidException {
		super(floor, origin, target);
	}

	/**
	 * Given a number of columns and rows (typically for the Floor), this recreates it with Nodes used
	 * by the searching.
	 * 
	 * This is a static method as it does not use or modify anything locally.
	 * 
	 * @param columns the number to columns in the floor.
	 * @param rows    the number of rows in the floor.
	 * @return a two dimensional array storing the nodes in their grid format.
	 */
	private static PathFindingNode[][] buildFloorWithNodes(int columns, int rows) {
		PathFindingNode[][] nodes = new PathFindingNode[columns][rows];

		for (int column = 0; column < columns; column++) {
			for (int row = 0; row < rows; row++) {
				nodes[column][row] = new PathFindingNode(column, row);
			}
		}

		return nodes;
	}

	/**
	 * Given two location, this will find an optimal path between them.
	 * 
	 * @param beginningLocation the Location to start the search.
	 * @param targetLocation    the Location to end the search.
	 * @return a boolean value; true if a path was found, otherwise false.
	 * @throws LocationNotValidException
	 */
	protected void calculatePath(Location beginningLocation, Location targetLocation) {
		// If both locations are the same, return true (with a path count of 0)
		if (beginningLocation.equals(targetLocation))
			return; // TODO Consider throwing an error.

		this.floorNodes = AStarPathFinder.buildFloorWithNodes(this.floor.getNumberOfColumns(),
				this.floor.getNumberOfRows());

		// Create lists for storing nodes to explore and those explored
		this.unexploredNodes = new ArrayList<PathFindingNode>(
				this.floor.getNumberOfColumns() * this.floor.getNumberOfRows());
		this.exploredNodes = new ArrayList<PathFindingNode>(
				this.floor.getNumberOfColumns() * this.floor.getNumberOfRows());

		// Convert Locations to Nodes
		PathFindingNode beginningNode = this.getNodeAtLocation(beginningLocation);
		PathFindingNode targetNode = this.getNodeAtLocation(targetLocation);

		// Add current location to begin search
		this.unexploredNodes.add(beginningNode);

		searchForPath(targetLocation);

		// No path was found
		if (targetNode.getPreviousNodeInPath() == null)
			return; // TODO Consider throwing an error.

		// Populate the path with calculated route.
		this.path = AStarPathFinder.convertLinkedNodesToPath(beginningNode, targetNode);
	}

	/**
	 * Returns a node at a given x, y coordinates. Returns null if the given coordinates are Out Of
	 * Bounds
	 * 
	 * @param column
	 * @param row
	 * @return
	 */
	private PathFindingNode getNodeAtLocation(int column, int row) {
		if (!this.floor.isLocationValid(new Location(column, row)))
			return null;

		try {
			return this.floorNodes[column][row];
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Returns a node at a given Location. Returns null if the given Location is Out Of Bounds
	 * 
	 * @param location
	 * @return
	 */
	private PathFindingNode getNodeAtLocation(Location location) {
		return this.getNodeAtLocation(location.getColumn(), location.getRow());
	}

	/**
	 * Begins the recursive like search through the unexplored Nodes.
	 * 
	 * @param targetLocation the location used to determine success.
	 */
	private void searchForPath(Location targetLocation) {
		PathFindingNode targetNode = this.getNodeAtLocation(targetLocation);
		do {
			Collections.sort(this.unexploredNodes);

			PathFindingNode currentNode = this.unexploredNodes.get(0);

			// Found target, breaking out to stop search
			if (currentNode.equals(targetLocation))
				break;

			int nextStepCost = currentNode.getNumberOfStepsFromStart() + 1;

			this.checkNodeForExplorationInDirection(nextStepCost, currentNode, targetNode, +0, -1); // ABOVE
			this.checkNodeForExplorationInDirection(nextStepCost, currentNode, targetNode, +1, +0); // RIGHT
			this.checkNodeForExplorationInDirection(nextStepCost, currentNode, targetNode, +0, +1); // BELOW
			this.checkNodeForExplorationInDirection(nextStepCost, currentNode, targetNode, -1, +0); // LEFT

			// Move current node to explored.
			this.unexploredNodes.remove(currentNode);
			this.exploredNodes.add(currentNode);

		} while (unexploredNodes.size() > 0);
	}

	/**
	 * A wrapper around checkNodeForExploration(...) defining the current node as the relative
	 * position change from the previous node.
	 * 
	 * @param nextStepCost the cost taken to travel to this node.
	 * @param previous     the previous node to the one being explored.
	 * @param target       the target node used to determine heuristic later.
	 * @param columnChange the relative change in column.
	 * @param rowChange    the relative change in row.
	 */
	private void checkNodeForExplorationInDirection(int nextStepCost, PathFindingNode previous,
			PathFindingNode target, int columnChange, int rowChange) {
		PathFindingNode current =
				this.getNodeAtLocation(previous.getColumn() + columnChange, previous.getRow() + rowChange);
		if (current != null)
			this.checkNodeForExploration(current, nextStepCost, previous, target);
	}

	/**
	 * Adding a node to explore checking the location is valid, it hasn't been explored already.
	 * 
	 * @param current      the current node to explore.
	 * @param nextStepCost the cost taken to travel to this node.
	 * @param previous     the previous node to the one being explored.
	 * @param target       the target node used to determine heuristic later.
	 */
	private void checkNodeForExploration(PathFindingNode current, int nextStepCost,
			PathFindingNode previous, PathFindingNode target) {
		// Check location validity
		if (!this.floor.isLocationValid(current))
			return; // TODO Consider throwing an error.
		if (previous.getNumberOfStepsFromStart() == 0 && !this.floor.isLocationValidAndEmpty(current))
			return; // TODO Consider throwing an error.

		// If the node's current cost is greater than the nextStepCost, remove from
		// lists as a more efficient path has been found.
		if (nextStepCost < current.getNumberOfStepsFromStart()) {
			this.exploredNodes.remove(current);
			this.unexploredNodes.remove(current);
		}

		// This node has already been handled.
		if (this.unexploredNodes.contains(current) || this.exploredNodes.contains(current))
			return; // TODO Consider throwing an error.

		current.setNumberOfStepsFromStart(nextStepCost);
		current.setDirectDistanceToTarget(current.getEuclideanDistanceTo(target));
		current.setPreviousNodeInPath(previous);
		this.unexploredNodes.add(current);
	}

	/**
	 * Converts the linked nodes to a path stack.
	 * 
	 * This is static because it doesn't use or modify anything locally.
	 * 
	 * @param start the beginning node of the path (used as a stopping condition)
	 * @param end   the target node of the path (used as the initial point)
	 */
	private static Deque<Location> convertLinkedNodesToPath(PathFindingNode start,
			PathFindingNode end) {
		Deque<Location> path = new LinkedList<Location>();
		PathFindingNode currentNode = end;

		while (!currentNode.equals(start)) {
			path.addFirst(currentNode);
			currentNode = currentNode.getPreviousNodeInPath();
		}

		return path;
	}
}
