package model;

/**
 * A strategy to calculating the power cost of a journey on a tile based
 * floor. Laden and unladen stops can be added to individually with independent
 * power costs.
 */
public class BasicPathCostEstimator implements PathCostEstimator {
	/** The number of power units (cost) to decrease for each step while NOT ladened. */
	private int unladened;
	/** The number of power units (cost) to decrease for each step while ladened. */
	private int ladened;
	/** The current location in the path to calculate the next stop from. */
	private Location currentLocation;
	/** The running cost after each stop. */
	private int runningCost;

	public BasicPathCostEstimator(int unladened, int ladened) {
		this.unladened = unladened;
		this.ladened = ladened;
		this.runningCost = 0;
	}

	@Override
	public void setStart(Location location) {
		if (location == null)
			throw new IllegalArgumentException("Parameter 'location' is not expected to be null.");
		
		if (this.currentLocation != null)
			throw new IllegalStateException("A location has already been set. Please create a new instance to start again.");

		this.currentLocation = location;
	}

	@Override
	public void addUnladenedStop(Location location) {
		if (location == null)
			throw new IllegalArgumentException("Parameter 'location' is not expected to be null.");
		
		int numberOfSteps = this.currentLocation.getManhattanDistanceTo(location);
		this.runningCost += numberOfSteps * this.unladened;
		this.currentLocation = location;
	}

	@Override
	public void addLadenedStop(Location location) {
		if (location == null)
			throw new IllegalArgumentException("Parameter 'location' is not expected to be null.");
		
		int numberOfSteps = this.currentLocation.getManhattanDistanceTo(location);
		this.runningCost += numberOfSteps * this.ladened;
		this.currentLocation = location;
	}

	@Override
	public int getEstimatedCost() {
		return this.runningCost;
	}

	@Override
	public double getEstimatedCost(double leeway) {
		return this.runningCost * (1 + leeway);
	}
}
