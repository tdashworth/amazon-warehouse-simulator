package main.amazon;

import java.io.IOException;
import java.nio.file.*;
import main.simulation.*;
import java.util.*;
import java.util.stream.Collectors;

public class Program {

  /**
   * Main method, creates a simulator and starts the simulation run method.
   */
  @SuppressWarnings("unchecked")
  public static void main(String[] args) {
    if (args.length == 0) {
      System.out.println("Error, usage: java " + Program.class.getName() + " inputfile");
      System.exit(1);
    }

    try {
      Warehouse warehouse = createFromFile(Paths.get(args[0]));
      List<IActor<Warehouse>> actors = warehouse.getEntities().stream().sorted((e1, e2) -> e1.getUID().compareTo(e2.getUID()))
				.filter(entity -> entity instanceof IActor).map(entity -> (IActor<Warehouse>) entity)
				.collect(Collectors.toList());

      new Simulator<>(warehouse, actors).run();
    } catch (IOException | SimFileFormatException | LocationNotValidException e) {
      System.out.println("Error reading SIM file - " + e.toString());
      System.exit(1);
    } catch (Exception e) {
      System.out.println("Error running simulation - " + e.toString());
      e.printStackTrace();
      System.exit(1);
    }
  }

  /**
   * Returns a Simulator configured from the file given. Based off the first line of the file, the
   * correct reader will be chosen to parse the file.
   * 
   * @param fileLocation
   * @return a configured simulation.
   * @throws IOException
   * @throws SimFileFormatException
   * @throws LocationNotValidException
   */
  public static Warehouse createFromFile(Path fileLocation)
      throws IOException, SimFileFormatException, LocationNotValidException {
    ISimulatorFileReader<Warehouse> fileReader;
    List<String> lines = Files.readAllLines(fileLocation);

    if (lines.size() == 0)
      throw new SimFileFormatException("", "File is empty or of wrong format.");

    switch (lines.get(0)) {
      case "format 1":
        fileReader = new WarehouseV1FileReader();
        break;
      default:
        throw new SimFileFormatException(lines.get(0), "File is empty or of wrong format.");
    }

    return fileReader.read(fileLocation);
  }

}
