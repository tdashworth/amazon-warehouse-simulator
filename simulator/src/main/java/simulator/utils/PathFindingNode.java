package simulator.utils;

import simulator.Location;

/**
 * A single Node in the search graph
 */
public class PathFindingNode extends Location implements Comparable<PathFindingNode> {
	private int numberOfStepsFromStart;
	private PathFindingNode previousNodeInPath;
	private double directDistanceToTarget;

	/**
	 * A single Node in the search graph
	 */
	public PathFindingNode(int column, int row) {
		super(column, row);
	}
	
	/**
	 * A single Node in the search graph
	 */
	public PathFindingNode(Location location) {
		super(location.getColumn(), location.getRow());
	}

	/**
	 * Compares this Node to another using the numberOfStepsFromStart and directDistanceToTarget
	 * returning +ve if this is more cost effective or -ve otherwise.
	 */
	public int compareTo(PathFindingNode other) {
		double thisCost = this.directDistanceToTarget + this.numberOfStepsFromStart;
		double otherCost = other.directDistanceToTarget + other.numberOfStepsFromStart;

		if (thisCost < otherCost) {
			return -1;
		} else if (thisCost > otherCost) {
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * @return The number of steps prior to reaching this Node.
	 */
	public int getNumberOfStepsFromStart() {
		return this.numberOfStepsFromStart;
	}

	/**
	 * @param steps The number of steps prior to reaching this Node.
	 */
	public void setNumberOfStepsFromStart(int steps) {
		this.numberOfStepsFromStart = steps;
	}

	/**
	 * @return The number of steps prior to reaching this Node.
	 */
	public double getDirectDistanceToTarget() {
		return this.directDistanceToTarget;
	}

	/**
	 * @param directDistanceToTarget The direct distance from this to the target Node.
	 */
	public void setDirectDistanceToTarget(double directDistanceToTarget) {
		this.directDistanceToTarget = directDistanceToTarget;
	}

	/**
	 * @return The previous Node to this in the path to get here.
	 */
	public PathFindingNode getPreviousNodeInPath() {
		return this.previousNodeInPath;
	}

	/**
	 * @param previousNode The previous Node to this to determine the path it has taken to get to this
	 *                     Node.
	 */
	public void setPreviousNodeInPath(PathFindingNode previousNode) {
		this.previousNodeInPath = previousNode;
	}
}
