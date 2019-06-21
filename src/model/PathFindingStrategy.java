package model;

import java.util.ArrayList;
import java.util.Collections;

import PathFindingTEMP.Node;

public class PathFindingStrategy {

	
	//TODO convert node to actually use the existing floor as nodes
	//TODO Get the code to detect other robots as a collision hazard and go around them
	public ArrayList<Location> getNextMove(Location currentPos, Location targetPos, Warehouse warehouse) throws LocationNotValidException {


		//INITIAL SETUP


		//An ArrayList for storing open (unexplored) and closed (already explored) Locations
		ArrayList<Node> closed = new ArrayList<Node>();
		ArrayList<Node> open = new ArrayList<Node>();

		//Store the target X and Y values as variables to prevent repeated method calls
		int tx = targetPos.getColumn();
		int ty = targetPos.getRow();

		//Store the current X and Y values as variables to prevent repeated method calls
		int cx = currentPos.getColumn();
		int cy = currentPos.getRow();
		
		//Store the floor in a variable
		Floor grid = warehouse.getFloor();
		
		//Store the warehouse's X and Y values as variables as these are used multiple times
		int maxX = grid.getNumberOfColumns();
		int maxY = grid.getNumberOfRows();

		
		//INITIAL TESTING


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


		//PATH FINDING SETUP


		//The 'nodes' variable is used to store the floors tiles as nodes, which has added variables for searching
		Node[][] nodes = new Node[maxX][maxY];
		for (int x=0;x<maxX;x++) {
			for (int y=0;y<maxY;y++) {
				Node n = new Node(x,y);
				if(grid.getEntities()[x][y] != null) {
					//TODO Set the node to blocked/closed, as it cannot be used
				}	
				nodes[x][y] = n;
			}
		}

		//Add the node the robot is currently at to the open list
		open.add(nodes[cx][cy]);
		Collections.sort(open);


		//BEGIN THE ACTUAL PATH FINDING


		while(open.size() != 0) {

			// pull out the first node in our open list, this is determined to 
			// be the most likely to be the next step based on our heuristic
			Node current = open.get(0);

			if (current == nodes[tx][ty]) {
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

					if (!((xp < 0) || (yp < 0) || (xp >= maxX) || (yp >= maxY))) {
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
							int dx = tx - xp;
							int dy = ty - yp;
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

		
		//ASSESS THE RESULT AND RETURN THE APPROPRIATE RESPONSE

		
		//No path was found
		//TODO return some sort of exception to deal with this
		if (nodes[tx][ty].getParent() == null) {
			return null;
		}

		//A path was found - iterate these into a 'path' variable to return them in the correct order
		ArrayList<Location> path = new ArrayList<Location>();
		Node target = nodes[tx][ty];
		
		while (target != nodes[cx][cy]) {
			path.add(0, new Location(target.getX(),target.getY()));
			target = target.getParent();
		}
		path.add(0, currentPos);

		//Return the calculated path
		return path;
	}

}