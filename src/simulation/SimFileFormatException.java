package simulation;

public class SimFileFormatException extends Exception {
	private static final long serialVersionUID = -3371570467581294931L;
	private String lineText;
	private String message;

	/**
	 * @param lineText
	 * @param message
	 */
	public SimFileFormatException(String lineText, String message) {
		super();
		this.lineText = lineText;
		this.message = message;
	}

	/**
	 * @return the lineText
	 */
	public String getLineText() {
		return lineText;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return "SimFileFormatException: Line '" + lineText + "' failed becuase of " + message + ".";
	}
}
