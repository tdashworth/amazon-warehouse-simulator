package model;

/**
 * A single node in the search graph
 */
public class Node implements Comparable {
	
	private int x;
	private int y;
	private float cost;
	private Node parent;
	private float heuristic;

	/**
	 * Create a new node
	 * 
	 * @param x
	 * @param y
	 */
	public Node(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Set the parent of this node
	 * 
	 * @param parent
	 */
	public void setParent(Node parent) {
		this.parent = parent;
	}

	/**
	 * compare the nodes by their heuristic value
	 */
	public int compareTo(Object other) {
		Node o = (Node) other;

		float f = heuristic + cost;
		float of = o.heuristic + o.cost;

		if (f < of) {
			return -1;
		} else if (f > of) {
			return 1;
		} else {
			return 0;
		}
	}
	
	/**
	 * return the nodes x value
	 * 
	 * @return
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * return the nodes y value
	 * 
	 * @return
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * return the nodes cost
	 * 
	 * @return
	 */
	public float getCost() {
		return cost;
	}
	
	/**
	 * set the nodes cost
	 * 
	 * @return
	 */
	public void setCost(float cost) {
		this.cost = cost;
	}
	
	/**
	 * return the nodes parent
	 * 
	 * @return
	 */
	public Node getParent() {
		return parent;
	}

	/**
	 * set the nodes heuristic value
	 * 
	 * @return
	 */
	public void setHeuristic(float heuristic) {
		this.heuristic = heuristic;
	}
}
