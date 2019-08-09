package model;

public class LocationNotValidException extends Exception {
	private static final long serialVersionUID = 5746669140783837476L;
	private int row;
	private int column;
	private String message;

	/**
	 * @param row
	 * @param column
	 * @param string
	 */
	public LocationNotValidException(int row, int column, String message) {
		super();
		this.row = row;
		this.column = column;
		this.message = message;
	}

	/**
	 * @return the row
	 */
	public int getRow() {
		return row;
	}

	/**
	 * @return the column
	 */
	public int getColumn() {
		return column;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return "LocationNotValidException: " + row + ", " + column
				+ " [row, column] is invalid because " + message + ".";
	}
}
