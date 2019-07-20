package helpers;

import model.Location;

public interface PathCostEstimator {

	/**
	 * Sets the start location of the path.
	 * 
	 * @param location The start Location
	 * @throws IllegalArgumentException Thrown if the location passed was null
	 * @throws IllegalStateException    Thrown if a location has already been set
	 */
	void setStart(Location location) throws IllegalArgumentException, IllegalStateException;

	/**
	 * Adds an unladened stop to the path.
	 * 
	 * @param location The Location to travel to
	 * @throws IllegalArgumentException Thrown if the location passed was null
	 */
	void addUnladenedStop(Location location) throws IllegalArgumentException;

	/**
	 * Adds a laden stop to the path.
	 *
	 * @param location The Location to travel to
	 * @throws IllegalArgumentException Thrown if the location passed was null
	 */
	void addLadenedStop(Location location) throws IllegalArgumentException;

	/**
	 * @return The total estimated power cost of the path added
	 */
	int getEstimatedCost();

	/**
	 * @param leeway The percentage increase to be added
	 * @return The total estimated power cost of the path added factored by leeway
	 */
	double getEstimatedCost(double leeway);

}