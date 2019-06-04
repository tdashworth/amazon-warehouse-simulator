package model;

import java.util.ArrayList;

public interface PathFindingStrategy {

	public ArrayList<Location> calculatePath(Location current, Location storageShelf, Location packingStation);
	
}
