package model;

/**
 * A single Node in the search graph
 */
public class Node extends Location implements Comparable<Node> {
	private int numberOfStepsFromStart;
	private Node previousNodeInPath;
	private double directDistanceToTarget;

	/**
	 * A single Node in the search graph
	 */
	public Node(int column, int row) {
		super(column, row);
	}

	/**
	 * Compares this Node to another using the numberOfStepsFromStart and
	 * directDistanceToTarget returning +ve if this is more cost effective or -ve
	 * otherwise.
	 */
	public int compareTo(Node other) {
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
		return numberOfStepsFromStart;
	}

	/**
	 * @param steps The number of steps prior to reaching this Node.
	 */
	public void setNumberOfStepsFromStart(int steps) {
		this.numberOfStepsFromStart = steps;
	}

	/**
	 * @param directDistanceToTarget The direct distance from this to the target
	 *                               Node.
	 */
	public void setDirectDistanceToTarget(double directDistanceToTarget) {
		this.directDistanceToTarget = directDistanceToTarget;
	}

	/**
	 * @return The previous Node to this in the path to get here.
	 */
	public Node getPreviousNodeInPath() {
		return previousNodeInPath;
	}

	/**
	 * @param previousNode The previous Node to this to determine the path it has
	 *                     taken to get to this Node.
	 */
	public void setPreviousNodeInPath(Node previousNode) {
		this.previousNodeInPath = previousNode;
	}
}
