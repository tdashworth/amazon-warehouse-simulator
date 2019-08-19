package main.simulation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import main.model.AbstractEntity;
import main.model.Order;

public class BasicSimulatorReport implements ISimulatorReport {
  private final BufferedWriter writer;

  public BasicSimulatorReport(Path fileLocation) throws Exception {
    if (fileLocation == null)
      throw new IllegalArgumentException("'fileLocation' is a required, non-null parameter.");

    try {
      this.writer = new BufferedWriter(new FileWriter(fileLocation.toAbsolutePath().toFile()));
    } catch (IOException e) {
      throw new Exception(
          "Error while building report to the file: " + fileLocation.toAbsolutePath());
    }
  }

  public void write(Simulator simulator) throws Exception {
    try {
      this.writeHeader(simulator);
      this.writeEntities(simulator);
      this.writeOrders(simulator);
    } catch (IOException e) {
      throw new Exception("Error while writing report.");
    } finally {
      this.writer.close();
    }
  }

  private void writeHeader(Simulator simulator) throws IOException {
    this.writeLine("Amazon Factory Robots Simulator");
    this.writeLine("Total tick count to completion: " + simulator.getTotalTickCount());
    this.writeLine();
  }

  private void writeEntities(Simulator simulator) throws IOException {
    this.writeLine("Simulator Entities:");

    for (AbstractEntity entity : simulator.getWarehouse().getEntities()) {
      this.writeLine(entity);
    }
    this.writeLine();
  }

  private void writeOrders(Simulator simulator) throws IOException {
    this.writeLine("Simulator Orders:");

    for (Order order : simulator.getWarehouse().getOrderManager().getCompleted()) {
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
