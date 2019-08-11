package main.model;

public class LocationNotValidException extends Exception {
	private static final long serialVersionUID = 5746669140783837476L;
	private final Location location;
	private String message;

	/**
	 * @param row
	 * @param column
	 * @param string
	 */
	public LocationNotValidException(int column, int row, String message) {
		this(new Location(column, row), message);
	}

	/**
	 * @param location
	 * @param string
	 */
	public LocationNotValidException(Location location, String message) {
		super();
		this.location = location;
		this.message = message;
	}

	/**
	 * @return the row
	 */
	public int getRow() {
		return this.location.getRow();
	}

	/**
	 * @return the column
	 */
	public int getColumn() {
		return this.location.getColumn();
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return "LocationNotValidException: " + this.location + " is invalid because " + getMessage();
	}
}
