package model;

import java.util.ArrayList;
import java.util.Collections;

public class PathFindingStrategy {
	
	//TODO Test robot collision on path finding
	
	/**
	 * Calculates the optimum path from one location to another
	 * 
	 * @param currentPos
	 * @param targetPos
	 * @param warehouse
	 * @return
	 * @throws LocationNotValidException
	 */
	public ArrayList<Location> getPath(Location currentPos, Location targetPos, Warehouse warehouse) throws LocationNotValidException {
		
		//An ArrayList for storing open (unexplored) and closed (explored) Locations
		ArrayList<Node> closed = new ArrayList<Node>();
		ArrayList<Node> open = new ArrayList<Node>();
		
		//Store the variables that are used often to prevent repeated method calls
		int targetX = targetPos.getColumn();
		int targetY = targetPos.getRow();
		int currentX = currentPos.getColumn();
		int currentY = currentPos.getRow();
		
		Floor floor = warehouse.getFloor();
		int floorMaxX = floor.getNumberOfColumns();
		int floorMaxY = floor.getNumberOfRows();
		
		//TODO Some of the below checking may not be needed as it may be covered within an earlier class
		//TODO These will also need to be handled if kept, rather than just printing to console
		if(!floor.locationIsValid(currentPos)) {
			System.out.println("The robots current destination is invalid.");
			System.exit(1);
		}
		if(!floor.locationIsValid(targetPos)) {
			System.out.println("The robots target destination is invalid.");
			System.exit(2);
		}
		if(currentPos.equals(targetPos)) {
			System.out.println("We are already at the target destination.");
			System.exit(3);
		}


		//The 'nodes' variable is used to store the floor tiles as nodes, which have added variables used for searching
		Node[][] nodes = new Node[floorMaxX][floorMaxY];
		for (int x=0;x<floorMaxX;x++) {
			for (int y=0;y<floorMaxY;y++) {
				Node n = new Node(x,y);
				nodes[x][y] = n;
			}
		}

		//Add the node the robot is currently at to the open list
		open.add(nodes[currentX][currentY]);
		Collections.sort(open);

		
		//Whilst there is still nodes to explore....
		while(open.size() != 0) {
			//Get the first element from open (this will be the one with the lowest heuristic value)
			Node current = open.get(0);

			//If we have the target node, we have a full path. Exit the loop
			if (current == nodes[targetX][targetY]) {
				break;
			}

			open.remove(current);
			closed.add(current);

			//Evaluate the four tiles surrounding the current one as possible next steps
			for (int x=-1; x<2; x++) {
				for (int y=-1; y<2; y++) {
					
					//If we are attempting to assess the node we are already at, skip it and continue
					if ((x == 0) && (y == 0)) {
						continue;
					}

					//If we are attempting to assess a node diagonal to the current node, skip it and continue
					if ((x != 0) && (y != 0)) {
						continue;
					}

					//Calculate the location of the neighbour node and evaluate it
					int assessedNodeX = x + current.getX();
					int assessedNodeY = y + current.getY();

					if (!((assessedNodeX < 0) || (assessedNodeY < 0) || (assessedNodeX >= floorMaxX) || (assessedNodeY >= floorMaxY ) || (floor.getEntities()[assessedNodeX][assessedNodeY] != null))) {
						
						//The nextStepCost is the cost to get to the current node +1
						float nextStepCost = current.getCost() + 1;
						Node neighbour = nodes[assessedNodeX][assessedNodeY];

						if (nextStepCost < neighbour.getCost()) {
							if (open.contains(neighbour)) {
								open.remove(neighbour);
							}
							if (closed.contains(neighbour)) {
								closed.remove(neighbour);
							}
						}

						//If the node hasn't been assessed before, set it's cost to the current cost and add it to the open list
						if (!open.contains(neighbour) && !closed.contains(neighbour)) {
							neighbour.setCost(nextStepCost);
							int dx = targetX - assessedNodeX;
							int dy = targetY - assessedNodeY;
							float heuristic = (float) Math.sqrt((dx*dx)+(dy*dy));
							neighbour.setHeuristic(heuristic);
							neighbour.setParent(current);
							open.add(neighbour);
							Collections.sort(open);
						}
					}
				}
			}
		}

		//No path was found
		//TODO return some sort of exception to deal with this
		if (nodes[targetX][targetY].getParent() == null) {
			return null;
		}

		//A path was found - iterate these into a 'path' variable to return them in the correct order
		ArrayList<Location> path = new ArrayList<Location>();
		Node target = nodes[targetX][targetY];
		
		while (target != nodes[currentX][currentY]) {
			path.add(0, new Location(target.getX(),target.getY()));
			target = target.getParent();
		}
		
		return path;
	}

}