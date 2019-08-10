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
	public LocationNotValidException(int column, int row, String message) {
		super();
		this.column = column;
		this.row = row;
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
		return "LocationNotValidException: " + column + ", " + row
				+ " [column, row] is invalid because " + message;
	}
}
