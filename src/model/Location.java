/**
 * 
 */
package model;

import java.text.MessageFormat;

/** 
 *	A representation of a point on a grid in row and column format.
 */
public class Location {
	
	private int row;
	private int column;

	/**
	 * Represents a point on a grid (row and column) 
	 * @param row
	 * @param column
	 * @throws Exception 
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
	 *	@return A string representation of the location.
	 */
	@Override
	public String toString() {
		return MessageFormat.format("Location: {0}, {1} [Row, Column]", this.row, this.column);
	}
	
	/**
	 * @param location
	 * @return A boolean whether this and the given location are the same.
	 */
	public boolean equals(Location location) {
		return (this.row == location.getRow()) && (this.column == location.getColumn());
	}
}
