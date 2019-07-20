package model;

/**
 *	A model of something that acts per tick of the simulation.
 */
public interface Actor {
	
	/**
	 * This is to execute on every tick/step of the simulation allowing each Actor to make their "turn".
	 * @param warehouse
	 * @throws Exception 
	 */
	public abstract void tick(Warehouse warehouse) throws Exception;
}
