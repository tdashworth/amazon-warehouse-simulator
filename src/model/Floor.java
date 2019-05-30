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
	
	/**
	 * A representation of a grid floor.
	 * @param numberofRows
	 * @param numberOfColumns
	 */
	public Floor(int numberofRows, int numberOfColumns) {
		this.numberOfRows = numberofRows;
		this.numberOfColumns = numberOfColumns;
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
	 *	@return A string representation of the floor.
	 */
	public String toString() {
		return MessageFormat.format("Floor:"
				+ " - Size: {0} rows by {1} columns.", 
				this.numberOfRows, this.numberOfColumns);
	}
}
