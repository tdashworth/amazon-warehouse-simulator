package model;

import java.util.ArrayList;
import PathFindingTEMP.Node;

public class PathFindingStrategy {

	public ArrayList<Location> getNextMove(Location currentPos, Location targetPos, Warehouse warehouse) {
		
		//An ArrayList for storing explored Locations
		ArrayList<Node> explored = new ArrayList<Node>();
		//An ArrayList for storing unexplored Locations
		ArrayList<Node> open = new ArrayList<Node>();

		Floor grid = warehouse.getFloor();
		
		//Some of the below checking may not be needed as it may be covered within an earlier class
		if(!grid.locationIsValid(currentPos))
			System.out.println("The robots current destination is invalid.");
		if(!grid.locationIsValid(targetPos)) 
			System.out.println("The robots target destination is invalid.");
		if(currentPos.equals(targetPos))
			System.out.println("We are already at the target destination.");
		

		//nodes, used to store all of the grid tiles from the floor as a node, which allows storage of extra variables for searching
		Node[][] nodes = new Node[grid.getNumberOfRows()][grid.getNumberOfColumns()];
		for (int x=0;x<grid.getNumberOfRows();x++) {
			for (int y=0;y<grid.getNumberOfColumns();y++) {
				//for each column of each row, add the location into the nodes array. 
				nodes[x][y] = new Node(grid.getEntities()[x][y].getLocation().getColumn(),grid.getEntities()[x][y].getLocation().getRow());
			}
		}

		open.add(nodes[currentPos.getColumn()][currentPos.getRow()]);
		//Should already be null      nodes[currentPos.getColumn()][currentPos.getRow()].setParent(null);

		while(open.size() != 0) {

			// pull out the first node in our open list, this is determined to 
			// be the most likely to be the next step based on our heuristic
			Node current = open.get(0);
			if (current == nodes[targetPos.getColumn()][targetPos.getRow()]) {
				break;
			}

			open.remove(current);
			explored.add(current);
			
			
			
			
			

			// search through all the neighbours of the current node evaluating
			// them as next steps

			for (int x=-1;x<2;x++) {
				for (int y=-1;y<2;y++) {
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
					
					//if (NOT ON THE GRID && NOT AT X OR Y) {
					//	invalid = IS SOMEONE IN MY SPACE;
					//}
					
					//return !invalid OPPOSITE OF FINAL RESULT;
					
					if (!((x < 0) || (y < 0) || (x >= grid.getNumberOfColumns()) || (y >= grid.getNumberOfRows()))) {
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
							if (explored.contains(neighbour)) {
								explored.remove(neighbour);
							}
						}

						// if the node hasn't already been processed and discarded then
						// reset it's cost to our current cost and add it as a next possible
						// step (i.e. to the open list)

						if (!open.contains(neighbour) && !(explored.contains(neighbour))) {
							neighbour.setCost(nextStepCost);
							//neighbour.heuristic = getHeuristicCost(mover, xp, yp, tx, ty);   //#][3#][3#][3#][3#][3#][3#][3#][3#][3#][3#][3#][3#][3#][3#][3
							open.add(neighbour);
						}
					}
				}
			}
		}

		// since we'e've run out of search 
		// there was no path. Just return null

		if (nodes[targetPos.getColumn()][targetPos.getRow()].getParent() == null) {
			return null;
		}

		// At this point we've definitely found a path so we can uses the parent
		// references of the nodes to find out way from the target location back
		// to the start recording the nodes on the way.

		ArrayList<Location> path = new ArrayList<Location>();
		Node target = nodes[targetPos.getColumn()][targetPos.getRow()];
		while (target != nodes[currentPos.getColumn()][currentPos.getRow()]) {
			
			path.add(0, grid.getEntities()[targetPos.getColumn()][targetPos.getRow()].location);
			target = target.getParent();
		}
		path.add(0, currentPos);

		// thats it, we have our path 
		return path;
	}
}