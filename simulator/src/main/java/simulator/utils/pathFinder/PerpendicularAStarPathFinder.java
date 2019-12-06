package simulator.utils.pathFinder;

import java.util.*;

import simulator.Floor;
import simulator.Location;
import simulator.LocationNotValidException;

/**
 * A strategy to finding an optimal path between two location on a floor. This
 * strategy explores nodes in a recursive fashion choosing the "most efficient"
 * node to explore next. The most efficient node is determined by a combination
 * of heuristic (closest direct distance) and cost (number of steps it took to
 * travel to the node).
 * 
 * This strategy only navigates to perpendicular node, not diagonal.
 */
public class PerpendicularAStarPathFinder extends AbstractPathFinder {
	private PathFindingNode[][] floorNodes;

	List<PathFindingNode> unexploredNodes;
	List<PathFindingNode> exploredNodes;

	/**
	 * Constructs a strategy for path finding with a default value to avoid
	 * collisions.
	 * 
	 * @param floor the floor determining the size and location of other robots.
	 */
	public PerpendicularAStarPathFinder(Floor floor, Location origin, Location target) throws LocationNotValidException {
		super(floor, origin, target);
	}

	/**
	 * Given a number of columns and rows (typically for the Floor), this recreates
	 * it with Nodes used by the searching.
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
	protected Deque<PathFindingNode> calculatePath(Location beginningLocation, Location targetLocation) {
		// If both locations are the same, return true (with a path count of 0)
		if (beginningLocation.equals(targetLocation))
			return null; // TODO Consider throwing an error.

		this.floorNodes = PerpendicularAStarPathFinder.buildFloorWithNodes(this.floor.getNumberOfColumns(),
				this.floor.getNumberOfRows());

		// Create lists for storing nodes to explore and those explored
		this.unexploredNodes = new ArrayList<>(this.floor.getNumberOfColumns() * this.floor.getNumberOfRows());
		this.exploredNodes = new ArrayList<>(this.floor.getNumberOfColumns() * this.floor.getNumberOfRows());

		// Convert Locations to Nodes
		PathFindingNode beginningNode = this.getNodeAtLocation(beginningLocation);
		PathFindingNode targetNode = this.getNodeAtLocation(targetLocation);

		// Add current location to begin search
		this.unexploredNodes.add(beginningNode);

		searchForPath(targetLocation);

		// No path was found
		if (targetNode.getPreviousNodeInPath() == null)
			return null; // TODO Consider throwing an error.

		// Populate the path with calculated route.
		return PerpendicularAStarPathFinder.convertLinkedNodesToPath(beginningNode, targetNode);
	}

	/**
	 * Returns a node at a given x, y coordinates. Returns null if the given
	 * coordinates are Out Of Bounds
	 * 
	 * @param column
	 * @param row
	 * @return
	 */
	private PathFindingNode getNodeAtLocation(int column, int row) {
		if (!this.floor.isLocationValid(new Location(column, row)))
			return null;

		return this.floorNodes[column][row];
	}

	/**
	 * Returns a node at a given Location. Returns null if the given Location is Out
	 * Of Bounds.
	 * 
	 * @param location the Location used to select PathFindingNode
	 * @return the PathFindingNode at respective Location
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

			// Move current node to explored.
			this.unexploredNodes.remove(currentNode);
			this.exploredNodes.add(currentNode);

			// Found target, breaking out to stop search
			if (currentNode.equals(targetLocation))
				break;

			int nextStepCost = currentNode.getNumberOfStepsFromStart() + 1;

			for (Location locationForExploration : getLocationsToExplore(currentNode)) {
				PathFindingNode nodeForExploration = this.getNodeAtLocation(locationForExploration);
				boolean result = this.shouldNodeBeExploration(currentNode, nodeForExploration, nextStepCost);

				if (result) {
					nodeForExploration.setNumberOfStepsFromStart(nextStepCost);
					nodeForExploration.setDirectDistanceToTarget(nodeForExploration.getEuclideanDistanceTo(targetNode));
					nodeForExploration.setPreviousNodeInPath(currentNode);
					this.unexploredNodes.add(nodeForExploration);
				}
			}

		} while (unexploredNodes.size() > 0);
	}

	protected Collection<Location> getLocationsToExplore(PathFindingNode currentNode) {
		return Arrays.asList(currentNode.transform(+0, -1), // Above
				currentNode.transform(+1, +0), // Right
				currentNode.transform(+0, +1), // Below
				currentNode.transform(-1, +0) // Left
		);
	}

	/**
	 * Adding a node to explore checking the location is valid, it hasn't been
	 * explored already.
	 * 
	 * @param previous     the previous node to the one being explored.
	 * @param current      the current node to explore.
	 * @param nextStepCost the cost taken to travel to this node.
	 */
	private boolean shouldNodeBeExploration(PathFindingNode previous, PathFindingNode current, int nextStepCost) {
		// Check location validity
		if (!this.floor.isLocationValid(current))
			return false;
		if (previous.getNumberOfStepsFromStart() == 0 && !this.floor.isLocationValidAndEmpty(current))
			return false;

		// If the node's current cost is greater than the nextStepCost, remove from
		// lists as a more efficient path has been found.
		if (nextStepCost < current.getNumberOfStepsFromStart()) {
			this.exploredNodes.remove(current);
			this.unexploredNodes.remove(current);
		}

		// This node has already been handled.
		if (this.unexploredNodes.contains(current) || this.exploredNodes.contains(current))
			return false;

		return true;
	}

	/**
	 * Converts the linked nodes to a path stack.
	 * 
	 * This is static because it doesn't use or modify anything locally.
	 * 
	 * @param start the beginning node of the path (used as a stopping condition)
	 * @param end   the target node of the path (used as the initial point)
	 */
	private static Deque<PathFindingNode> convertLinkedNodesToPath(PathFindingNode start, PathFindingNode end) {
		Deque<PathFindingNode> path = new LinkedList<PathFindingNode>();
		PathFindingNode currentNode = end;

		while (!currentNode.equals(start)) {
			path.addFirst(currentNode);
			currentNode = currentNode.getPreviousNodeInPath();
		}

		return path;
	}
}
