package main.simulation;

/**
 * A.amazon of something that acts per tick of the simulation.
 */
public interface IActor<W extends AWorld> {

	/**
	 * This is to execute on every tick/step of the simulation allowing each Actor to make their
	 * "turn".
	 * 
	 * @param warehouse
	 * @throws Exception
	 */
	public abstract void tick(W world, int currentTickCount) throws Exception;
}
