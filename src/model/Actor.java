package model;

/**
 *	A model of something that acts per tick of the simulation.
 */
public interface Actor {
	
	// TODO Warehouse should be passed to this as a parameter.
	public abstract void tick(Warehouse warehouse);
}
