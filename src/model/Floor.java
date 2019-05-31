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
	 * @param numberofRows
	 * @param numberOfColumns
	 */
	public Floor(int numberofRows, int numberOfColumns) {
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
	 * TODO JavaDoc description.
	 * @param entity
	 * @throws Exception
	 */
	public void loadEntity(Entity entity) throws Exception {
		Location location = entity.getLocation();
		
		// Check location is on the grid.
		if (location.getColumn() > this.numberOfColumns || location.getRow() > this.numberOfRows)
			throw new Exception("The location is outside of the floor.");
		
		// Checks location is empty.
		if (grid[location.getColumn()][location.getRow()] == null)
			throw new Exception("That location is not empty.");
		
		grid[location.getColumn()][location.getRow()] = entity;
	}
	
	/**
	 * TODO JavaDoc description.
	 * @param oldLocation
	 * @param newLocation
	 * @return
	 * @throws Exception
	 */
	public boolean moveEntity(Location oldLocation, Location newLocation) throws Exception {
		// Check old location is on the grid.
		if (oldLocation.getColumn() > this.numberOfColumns || oldLocation.getRow() > this.numberOfRows)
			throw new Exception("The old location is outside of the floor.");
		
		// Check new location is on the grid.
		if (newLocation.getColumn() > this.numberOfColumns || newLocation.getRow() > this.numberOfRows)
			throw new Exception("The new location is outside of the floor.");
		
		// Check new location is empty.
		if (grid[newLocation.getColumn()][newLocation.getRow()] == null)
			return false;
		
		// Move entity.
		Entity entity = grid[oldLocation.getColumn()][oldLocation.getRow()];
		grid[oldLocation.getColumn()][oldLocation.getRow()] = null;
		grid[newLocation.getColumn()][newLocation.getRow()] = entity;
		return true;
	}
	
	/**
	 *	@return A string representation of the floor.
	 */
	public String toString() {
		return MessageFormat.format("Floor:"
				+ " - Size: {0} rows by {1} columns.", 
				this.numberOfRows, this.numberOfColumns);
	}
}
