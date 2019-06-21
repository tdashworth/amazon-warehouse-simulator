package model;

import java.util.ArrayList;
import java.util.Collections;

public class PathFindingStrategy {

	
	//TODO Potentially convert node to actually use the existing floor as nodes
	//TODO Get the code to detect other robots as a collision hazard and go around them
	public ArrayList<Location> getNextMove(Location currentPos, Location targetPos, Warehouse warehouse) throws LocationNotValidException {
		
		//An ArrayList for storing open (unexplored) and closed (already explored) Locations
		ArrayList<Node> closed = new ArrayList<Node>();
		ArrayList<Node> open = new ArrayList<Node>();

		//Store the Robot's target X and Y values as variables to prevent repeated method calls
		int targetX = targetPos.getColumn();
		int targetY = targetPos.getRow();

		//Store the Robot's current X and Y values as variables to prevent repeated method calls
		int currentX = currentPos.getColumn();
		int currentY = currentPos.getRow();
		
		//Store the grid in a variable
		Floor grid = warehouse.getFloor();
		
		//Store the warehouse's X and Y values as variables as these are used multiple times
		int maxX = grid.getNumberOfColumns();
		int maxY = grid.getNumberOfRows();


		//TODO Some of the below checking may not be needed as it may be covered within an earlier class
		//TODO These will also need to be handled if kept, rather than just printing to console
		if(!grid.locationIsValid(currentPos)) {
			System.out.println("The robots current destination is invalid.");
			System.exit(1);
		}
		if(!grid.locationIsValid(targetPos)) {
			System.out.println("The robots target destination is invalid.");
			System.exit(2);
		}
		if(currentPos.equals(targetPos)) {
			System.out.println("We are already at the target destination.");
			System.exit(3);
		}


		//The 'nodes' variable is used to store the floors tiles as nodes, which has added variables for searching
		Node[][] nodes = new Node[maxX][maxY];
		for (int x=0;x<maxX;x++) {
			for (int y=0;y<maxY;y++) {
				Node n = new Node(x,y);
				nodes[x][y] = n;
			}
		}

		//Add the node the robot is currently at to the open list
		open.add(nodes[currentX][currentY]);
		Collections.sort(open);


		while(open.size() != 0) {

			// pull out the first node in our open list, this is determined to 
			// be the most likely to be the next step based on our heuristic
			Node current = open.get(0);

			if (current == nodes[targetX][targetY]) {
				break;
			}

			open.remove(current);
			closed.add(current);

			// search through all the neighbours of the current node evaluating
			// them as next steps

			for (int x=-1; x<2; x++) {
				for (int y=-1; y<2; y++) {
					// not a neighbour, its the current tile

					if ((x == 0) && (y == 0)) {
						continue;
					}

					//Only X or Y can be set so as to disallow diagonal movement
					if ((x != 0) && (y != 0)) {
						continue;
					}

					// determine the location of the neighbour and evaluate it
					int xp = x + current.getX();
					int yp = y + current.getY();

					if (!((xp < 0) || (yp < 0) || (xp >= maxX) || (yp >= maxY ) || (grid.getEntities()[xp][yp] != null))) {
						// the cost to get to this node is cost the current plus the movement
						// cost to reach this node. Note that the heursitic value is only used
						// in the sorted open list

						float nextStepCost = current.getCost() + 1;
						Node neighbour = nodes[xp][yp];

						// if the new cost we've determined for this node is lower than 
						// it has been previously makes sure the node hasn'e've
						// determined that there might have been a better path to get to
						// this node so it needs to be re-evaluated

						if (nextStepCost < neighbour.getCost()) {
							if (open.contains(neighbour)) {
								open.remove(neighbour);
							}
							if (closed.contains(neighbour)) {
								closed.remove(neighbour);
							}
						}

						// if the node hasn't already been processed and discarded then
						// reset it's cost to our current cost and add it as a next possible
						// step (i.e. to the open list)

						if (!open.contains(neighbour) && !closed.contains(neighbour)) {
							neighbour.setCost(nextStepCost);
							int dx = targetX - xp;
							int dy = targetY - yp;
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

		//Return the calculated path
		return path;
	}

}