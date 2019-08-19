package main.simulation;

/**
 * A representation of a grid floor.
 */
public class Floor {
	private int numberOfRows;
	private int numberOfColumns;
	private AMover<?>[][] grid;

	/**
	 * A representation of a grid floor.
	 * 
	 * @param numberOfRows    width
	 * @param numberOfColumns height
	 */
	public Floor(int numberOfColumns, int numberOfRows) {
		if (numberOfColumns < 0)
			throw new IllegalArgumentException("'numberOfColumns' must be a positive integer.");

		if (numberOfRows < 0)
			throw new IllegalArgumentException("'numberOfRows' must be a positive integer.");

		this.numberOfColumns = numberOfColumns;
		this.numberOfRows = numberOfRows;
		this.grid = new AMover[numberOfColumns][numberOfRows];
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
	 * 
	 * @param entity
	 * @throws Exception
	 */
	public void loadMover(AMover<?> moverEntity) throws LocationNotValidException {
		Location location = moverEntity.getLocation();
		this.validateLocation(location);
		grid[location.getColumn()][location.getRow()] = moverEntity;
	}


	/**
	 * Validates whether the location passed is valid on the floor and is empty.
	 * 
	 * @param location
	 * @throws LocationNotValidException
	 */
	public void validateLocation(Location location) throws LocationNotValidException {
		// Check location is on the grid.
		if (!this.isLocationValid(location))
			throw new LocationNotValidException(location, "it's outside of the floor boundaries.");

		// Checks location is empty.
		if (!this.isLocationValidAndEmpty(location))
			throw new LocationNotValidException(location, "it's already occupied.");
	}

	/**
	 * Returns whether a location is on the grid or not
	 * 
	 * @param location
	 * @return
	 */
	public boolean isLocationValid(Location location) {
		boolean withinColumns =
				0 <= location.getColumn() && location.getColumn() < this.numberOfColumns;
		boolean withinRows = 0 <= location.getRow() && location.getRow() < this.numberOfRows;
		return withinColumns && withinRows;
	}

	/**
	 * Returns whether a location is empty or not
	 * 
	 * @param location
	 * @return
	 */
	public boolean isLocationValidAndEmpty(Location location) {
		if (!this.isLocationValid(location))
			return false;

		return (grid[location.getColumn()][location.getRow()] == null);
	}

	/**
	 * Move an entity to a new location.
	 * 
	 * @param oldLocation
	 * @param newLocation
	 * @return
	 * @throws Exception
	 */
	public void moveEntity(Location oldLocation, Location newLocation) throws Exception {
		// Check old location is on the grid.
		if (!this.isLocationValid(oldLocation))
			throw new LocationNotValidException(oldLocation, "it's outside of the floor boundaries.");

		// Check old location is not empty.
		if (this.isLocationValidAndEmpty(oldLocation))
			throw new LocationNotValidException(oldLocation, "it's empty and nothing to move.");

		// Check new location is on the grid.
		if (!this.isLocationValid(newLocation))
			throw new LocationNotValidException(newLocation, "it's outside of the floor boundaries.");

		// Check new location is empty.
		if (!this.isLocationValidAndEmpty(newLocation))
			throw new LocationNotValidException(newLocation, "it's already occupied.");

		// Move entity.
		AMover<?> moverEntity = grid[oldLocation.getColumn()][oldLocation.getRow()];
		grid[oldLocation.getColumn()][oldLocation.getRow()] = null;
		grid[newLocation.getColumn()][newLocation.getRow()] = moverEntity;
	}

	/**
	 * To String method of floor
	 * 
	 * @return A string representation of the floor.
	 */
	public String toString() {
		return String.format("Floor - Size: %s columns by %s rows.", this.numberOfColumns,
				this.numberOfRows);
	}
}
