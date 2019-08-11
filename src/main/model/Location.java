package main.model;

/**
 * A representation of a point on a grid in row and column format.
 */
public class Location {

	private int row;
	private int column;

	/**
	 * Represents a point on a grid (row and column)
	 * 
	 * @param row
	 * @param column
	 */
	public Location(int column, int row) {
		this.row = row;
		this.column = column;
	}

	/**
	 * @return The row.
	 */
	public int getRow() {
		return this.row;
	}

	/**
	 * @return The column.
	 */
	public int getColumn() {
		return this.column;
	}

	/**
	 * @return A string representation of the location.
	 */
	@Override
	public String toString() {
		return String.format("Location: [%s, %s]", this.column, this.row);
	}

	/**
	 * @param location
	 * @return A boolean whether this and the given location are the same.
	 */
	public boolean equals(Location location) {
		if (location == null)
			return false;

		return (this.row == location.getRow()) && (this.column == location.getColumn());
	}

	/**
	 * Calculates the distance following the rows and columns of the grid the locations reside on.
	 * 
	 * @param location The location to calculate distance to.
	 * @return The shortest distance from this to the given location.
	 */
	public int getManhattanDistanceTo(Location location) {
		int differenceInColumns = Math.abs(this.getColumn() - location.getColumn());
		int differenceInRows = Math.abs(this.getRow() - location.getRow());

		return differenceInColumns + differenceInRows;
	}

	/**
	 * Calculates the direct distance (line of sight) often diagonal.
	 * 
	 * @param location The location to calculate distance to.
	 * @return The shortest distance from this to the given location.
	 */
	public double getEuclideanDistanceTo(Location location) {
		int differenceInColumns = Math.abs(this.getColumn() - location.getColumn());
		int differenceInRows = Math.abs(this.getRow() - location.getRow());

		return Math.sqrt(differenceInRows ^ 2 + differenceInColumns ^ 2);
	}
}
