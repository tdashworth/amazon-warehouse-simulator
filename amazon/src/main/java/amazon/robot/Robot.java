package amazon.robot;

import amazon.*;
import simulator.*;
import simulator.utils.AbstractPathFinder;

public class Robot extends AMover<Warehouse> implements RobotState {
	protected int powerUnits;
	protected Job currentJob;
	protected ChargingPod chargingPod;

	protected final int powerUnitsCapacity;
	protected final int powerUnitsChargeSpeed;
	protected final static int POWER_UNITS_EMPTY = 1;
	protected final static int POWER_UNITS_CARRYING = 2;

	public final RobotState awaitingJobState = new AwaitingJobState(this);
	public final RobotState chargingState = new ChargingState(this);
	public final RobotState collectingItemState = new CollectingItemState(this);
	public final RobotState deliveringItemState = new DeliveringItemState(this);

	protected RobotState state;

	/**
	 * @param uid
	 * @param location
	 */
	public Robot(String uid, Location location, ChargingPod chargingPod, int powerUnitsCapacity,
			int powerUnitsChargeSpeed) {
		super(uid, location);

		if (chargingPod == null)
			throw new IllegalArgumentException("'chargingPod' is a required, non-null parameter.");

		if (powerUnitsCapacity < 0)
			throw new IllegalArgumentException("'powerUnitsCapacity' must be a positive integer.");

		if (powerUnitsChargeSpeed < 0)
			throw new IllegalArgumentException("'powerUnitsChargeSpeed' must be a positive integer.");

		this.chargingPod = chargingPod;
		this.powerUnits = powerUnitsCapacity;
		this.powerUnitsCapacity = powerUnitsCapacity;
		this.powerUnitsChargeSpeed = powerUnitsChargeSpeed;
		this.state = this.awaitingJobState;
	}

	@Override
	public void tick(Warehouse warehouse, int currentTickCount) throws Exception {
		super.tick(warehouse, currentTickCount);

		// this.log("Ticking with status: %s.", status);
		this.state.tick(warehouse, currentTickCount);
	}

	/**
	 * Increases the robots power units
	 */
	protected void charge() {
		if (this.powerUnits + powerUnitsChargeSpeed >= powerUnitsCapacity) {
			this.powerUnits = powerUnitsCapacity;
			this.log("Fully charged to %s", this.powerUnits);
		} else {
			this.powerUnits += powerUnitsChargeSpeed;
			this.log("Charge increased to %s.", this.powerUnits);
		}
	}

	@Override
	protected void move(Floor floor, Location targetLocation) throws Exception {
		super.move(floor, targetLocation);

		int powerUnitsToDeduct = this.hasItem() ? POWER_UNITS_CARRYING : POWER_UNITS_EMPTY;
		this.powerUnits -= powerUnitsToDeduct;

		this.log("Power Units (post move): %s.", this.powerUnits);
	}

	protected void setPathFinder(AbstractPathFinder pathFinder) {
		this.pathFinder = pathFinder;
	}

	/**
	 * The robot's state.
	 */
	public RobotState getStatus() {
		return this.state;
	}

	/**
	 * Checks to see if the robot is carrying an item
	 * 
	 * @return boolean
	 */
	protected boolean hasItem() {
		return this.currentJob != null && this.currentJob.getStatus() == Job.JobStatus.Delivering;
	}

	/**
	 * Get the power units
	 */
	public int getPowerUnits() {
		return powerUnits;
	}

	/**
	 * @return A string representation of a robot.
	 */
	public String toString() {
		String result = super.toString();
		result += ", " + "Status: " + this.getStatus();
		result += ", " + "Power: " + this.getPowerUnits();
		if (this.currentJob != null)
			result += ", " + "Current Job: " + this.currentJob;

		return result;
	}
}
