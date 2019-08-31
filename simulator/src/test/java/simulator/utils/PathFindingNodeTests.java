package simulator.utils;

import org.junit.Test;
import simulator.Location;
import simulator.utils.PathFindingNode;
import static org.junit.Assert.*;

public class PathFindingNodeTests {

  @Test
  public void testConstructorWithValidParametersShouldSuccussfullyCreate() {
    PathFindingNode pathFindingNode = new PathFindingNode(new Location(4, 7));

    assertEquals(4, pathFindingNode.getColumn());
    assertEquals(7, pathFindingNode.getRow());
  }

  @Test
  public void testNumberOfStepsFromStartWithSetShouldBeEqualToGet() {
    PathFindingNode pathFindingNode = new PathFindingNode(new Location(4, 7));
    
    pathFindingNode.setNumberOfStepsFromStart(5);
    assertEquals(5, pathFindingNode.getNumberOfStepsFromStart());

    pathFindingNode.setNumberOfStepsFromStart(-5);
    assertEquals(-5, pathFindingNode.getNumberOfStepsFromStart());
  }

  @Test
  public void testDirectDistanceToTargetWithSetShouldBeEqualToGet() {
    PathFindingNode pathFindingNode = new PathFindingNode(new Location(4, 7));

    pathFindingNode.setDirectDistanceToTarget(2.5);
    assertEquals(2.5, pathFindingNode.getDirectDistanceToTarget(), 0.1);

    pathFindingNode.setDirectDistanceToTarget(-2.5);
    assertEquals(-2.5, pathFindingNode.getDirectDistanceToTarget(), 0.1);
  }

  @Test
  public void testCompareToShouldReturnPositiveNegativeAndZeroRespectively() {
    PathFindingNode pathFindingNode1 = new PathFindingNode(new Location(4, 7));
    pathFindingNode1.setDirectDistanceToTarget(1.1);
    pathFindingNode1.setNumberOfStepsFromStart(3);
    PathFindingNode pathFindingNode2 = new PathFindingNode(new Location(4, 7));
    pathFindingNode2.setDirectDistanceToTarget(2.2);
    pathFindingNode2.setNumberOfStepsFromStart(1);

    assertEquals(1, pathFindingNode1.compareTo(pathFindingNode2));
    assertEquals(-1, pathFindingNode2.compareTo(pathFindingNode1));
    assertEquals(0, pathFindingNode1.compareTo(pathFindingNode1));
  }
}
