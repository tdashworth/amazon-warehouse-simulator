package simulator.utils.pathFinder;

import java.util.Collection;

import simulator.Floor;
import simulator.Location;
import simulator.LocationNotValidException;

/**
 * A strategy to finding an optimal path between two location on a floor. This
 * strategy explores nodes in a recursive fashion choosing the "most efficient"
 * node to explore next. The most efficient node is determined by a combination
 * of heuristic (closest direct distance) and cost (number of steps it took to
 * travel to the node).
 */
public class AStarPathFinder extends PerpendicularAStarPathFinder {

  public AStarPathFinder(Floor floor, Location origin, Location target) throws LocationNotValidException {
    super(floor, origin, target);
  }

  @Override
  protected Collection<Location> getLocationsToExplore(PathFindingNode currentNode) {
    Collection<Location> nodes = super.getLocationsToExplore(currentNode);
    
    nodes.add(currentNode.transform(+1, -1)); // Above Right
    nodes.add(currentNode.transform(+1, +1)); // Below Right
    nodes.add(currentNode.transform(-1, -1)); // Below Left
    nodes.add(currentNode.transform(-1, +1)); // Above Left

    return nodes;
	}
  
}