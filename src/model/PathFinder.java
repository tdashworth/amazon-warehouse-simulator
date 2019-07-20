package model;

import java.util.Collection;

public interface PathFinder {

	/**
	 * Given two location, this will find an optimal path between them.
	 * 
	 * @param beginningLocation the Location to start the search.
	 * @param targetLocation    the Location to end the search.
	 * @return a boolean value; true if a path was found, otherwise false.
	 * @throws LocationNotValidException
	 */
	boolean calculatePath(Location beginningLocation, Location targetLocation);

	/**
	 * @return the entire path.
	 */
	Collection<Location> getPath();

	/**
	 * @return the next step in the path (which is removed).
	 */
	Location getNextLocation();

	/**
	 * @return the number of steps in the path.
	 */
	int getNumberOfRemainingSteps();

}