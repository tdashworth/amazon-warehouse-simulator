package amazon;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import simulator.*;

public class App {

  /**
   * Main method, creates a simulator and starts the simulation run method.
   */
  public static void main(String[] args) {
    if (args.length == 0) {
      System.out.println("Error, usage: java " + App.class.getName() + " inputfile");
      System.exit(1);
    }

    try {
      createFromFile(Paths.get(args[0])).run();
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
  public static Simulator<Warehouse> createFromFile(Path fileLocation) throws Exception {
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

    fileReader.read(fileLocation);
    return new Simulator<Warehouse>(fileReader.getWorld(), fileReader.getActors());
  }

}
