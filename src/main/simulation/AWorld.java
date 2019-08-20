package main.simulation;

import java.util.*;
import main.simulation.Floor;

public abstract class AWorld {
  protected final Floor floor;
  protected final HashMap<String, AEntity> entities;

  public AWorld(int floorColumns, int floorWidth, List<AEntity> entities)
      throws LocationNotValidException {
    this(new Floor(floorColumns, floorWidth), entities);
  }

  public AWorld(Floor floor, List<AEntity> entities) throws LocationNotValidException {
    if (floor == null)
      throw new IllegalArgumentException("'floor' is a required, non-null parameter.");

    if (entities == null)
      throw new IllegalArgumentException("'entities' is a required, non-null parameter.");

    this.floor = floor;
    this.entities = new HashMap<>();

    for (AEntity entity : entities) {
      this.entities.put(entity.getUID(), entity);

      if (entity instanceof AMover) {
        floor.loadMover((AMover<?>) entity);
      }
    }
  }

  /**
   * @param uid
   * @return The entity with the given UID.
   */
  public AEntity getEntityByUID(String uid) {
    return this.entities.get(uid);
  }

  public Floor getFloor() {
  return this.floor;
  }

  /**
   * @return an unmodifable collection of entities.
   */
  public Collection<AEntity> getEntities() {
    return Collections.unmodifiableCollection(this.entities.values());
  }

  public abstract boolean isComplete();

  // public ISimulatorReport<? extends AWorld> getReportWriter();

  protected void log(String message) {
    String classType = this.getClass().getSimpleName();
    System.out.println(String.format("%s: %s", classType, message));
  }

  protected void log(String format, Object... args) {
    this.log(String.format(format, args));
  }
}
