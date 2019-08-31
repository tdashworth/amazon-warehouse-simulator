package amazon;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

import simulator.AEntity;
import simulator.ISimulatorReport;
import simulator.Simulator;

public class BasicSimulatorReport implements ISimulatorReport<Warehouse> {
  private BufferedWriter writer;
  private Warehouse warehouse;

  public BasicSimulatorReport(Warehouse warehouse) {
    this.warehouse = warehouse;
  }

  public void write(Simulator<?> simulator, Path fileLocation) throws Exception {
    if (simulator == null)
      throw new IllegalArgumentException("'simulator' is a required, non-null parameter.");

    if (fileLocation == null)
      throw new IllegalArgumentException("'fileLocation' is a required, non-null parameter.");

    try {
      this.writer = new BufferedWriter(new FileWriter(fileLocation.toAbsolutePath().toFile()));
      this.writeHeader(simulator);
      this.writeEntities();
      this.writeOrders();
    } catch (IOException e) {
      throw new Exception("Error while writing report.");
    } finally {
      this.writer.close();
    }
  }

  private void writeHeader(Simulator<?> simulator) throws IOException {
    this.writeLine("Amazon Factory Robots Simulator");
    this.writeLine("Total tick count to completion: " + simulator.getTotalTickCount());
    this.writeLine();
  }

  private void writeEntities() throws IOException {
    this.writeLine("Simulator Entities:");

    for (AEntity entity : warehouse.getEntities()) {
      this.writeLine(entity);
    }
    this.writeLine();
  }

  private void writeOrders() throws IOException {
    this.writeLine("Simulator Orders:");

    for (Order order : warehouse.getOrderManager().getCompleted()) {
      this.writeLine(order);
    }
    this.writeLine();
  }

  private void writeLine(String line) throws IOException {
    this.writer.append(line);
    this.writer.newLine();
  }

  private void writeLine(Object object) throws IOException {
    this.writeLine(object.toString());
  }

  private void writeLine() throws IOException {
    this.writeLine("");
  }
}
