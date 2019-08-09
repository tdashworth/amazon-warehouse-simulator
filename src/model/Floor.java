/**
 * 
 */
package model;

import java.text.MessageFormat;

/**
 *	A representation of a grid floor.
 */
public class Floor {
	private int numberOfRows;
	private int numberOfColumns;
	private Entity[][] grid;
	
	/**
	 * A representation of a grid floor.
	 * @param numberofRows width
	 * @param numberOfColumns height
	 */
	public Floor(int numberOfColumns, int numberofRows) {
		this.numberOfRows = numberofRows;
		this.numberOfColumns = numberOfColumns;
		this.grid = new Entity[numberOfColumns][numberofRows];
	}
	
	/**
	 * @return The number of rows.
	 */
	public int getNumberOfRows() {
		return this.numberOfRows;
	}
	
	/**
	 * @return The number of columns.
	 */
	public int getNumberOfColumns() {
		return this.numberOfColumns;
	}
	
	/**
	 * Place an entity on the floor.
	 * @param entity
	 * @throws Exception
	 */
	public void loadEntity(Entity entity) throws LocationNotValidException {
		Location location = entity.getLocation();
		
		// Check location is on the grid.
		if(!locationIsValid(location))
			throw new LocationNotValidException(location.getColumn(), location.getRow(), "The location is outside of the floor");
		
		// Checks location is empty.
		if(!locationIsEmpty(location))
			throw new LocationNotValidException(location.getColumn(), location.getRow(), "That location is not empty");
		
		grid[location.getColumn()][location.getRow()] = entity;
	}
	
	
	/**
	 * Validates whether the location passed is valid on the floor and is empty.
	 * @param location
	 * @throws LocationNotValidException
	 */
	public void validateLocation(Location location) throws LocationNotValidException {
		// Check location is on the grid.
		if (!this.locationIsValid(location))
			throw new LocationNotValidException(location.getColumn(), location.getRow(),
					"The location is outside of the floor");

		// Checks location is empty.
		if (!this.locationIsEmpty(location))
			throw new LocationNotValidException(location.getColumn(), location.getRow(), "That location is not empty");
	}
	
	/**
	 * Returns whether a location is on the grid or not
	 * @param location
	 * @return
	 */
	public boolean locationIsValid(Location location) {
		return (location.getColumn() <= this.numberOfColumns && location.getRow() <= this.numberOfRows);
	}
	
	/**
	 * Returns whether a location is empty or not
	 * @param location
	 * @return
	 */
	public boolean locationIsEmpty(Location location) {
		return (grid[location.getColumn()][location.getRow()] == null);
	}
	
	/**
	 * Move an entity to a new location.
	 * @param oldLocation
	 * @param newLocation
	 * @return
	 * @throws Exception
	 */
	public boolean moveEntity(Location oldLocation, Location newLocation) throws Exception {
		// Check old location is on the grid.
		if (oldLocation.getColumn() > this.numberOfColumns || oldLocation.getRow() > this.numberOfRows)
			throw new LocationNotValidException(oldLocation.getColumn(), oldLocation.getRow(), "The old location is outside of the floor.");
		
		// Check new location is on the grid.
		if (newLocation.getColumn() > this.numberOfColumns || newLocation.getRow() > this.numberOfRows)
			throw new LocationNotValidException(newLocation.getColumn(), newLocation.getRow(), "The new location is outside of the floor.");
		
		// Check new location is empty.
		if (grid[newLocation.getColumn()][newLocation.getRow()] != null)
			return false;
		
		// Move entity.
		Entity entity = grid[oldLocation.getColumn()][oldLocation.getRow()];
		grid[oldLocation.getColumn()][oldLocation.getRow()] = null;
		grid[newLocation.getColumn()][newLocation.getRow()] = entity;
		return true;
	}
	
	/**
	 * To String method of floor
	 *	@return A string representation of the floor.
	 */
	public String toString() {
		return MessageFormat.format("Floor:"
				+ " - Size: {0} rows by {1} columns.", 
				this.numberOfRows, this.numberOfColumns);
	}
}
