package View;

import java.io.File;
import java.nio.file.Paths;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import model.Entity;
import model.Floor;
import model.Location;
import model.Order;
import model.Robot;
import simulation.SimFileFormatException;
import simulation.Simulator;

public class WarehouseController {

	private Simulator simulation;
	private Timeline timeline;
	private StackPane[][] floor;
	private final static int TILE_DIMENSION = 50;

	@FXML
	private GridPane grdWarehouse;
	@FXML
	private Label labelChargeCapacity, labelChargeSpeed, labelTickCount, labelFileName;
	@FXML
	private Button buttonLoadFile, buttonOneTick, buttonTenTicks, buttonIndefiniteTicks, buttonPause, buttonStop;
	@FXML
	private ListView<Robot> robotsList;
	@FXML
	private ListView<Order> unassignedOrders, assignedOrders, dispatchedOrders;

	public WarehouseController() {
		timeline = new Timeline(new KeyFrame(Duration.seconds(0.25), actionEvent -> runASingleTick()));
		timeline.setOnFinished(actionEvent -> this.setButtonsDisablement(false));
	}

	// UI Callable Methods

	/**
	 * Allows the user to select a file of configurations
	 * @param event
	 */
	@FXML
	public void selectFile() {
		try {
			// Ask user to select a file.
			FileChooser fileChooser = new FileChooser();
			FileChooser.ExtensionFilter simFilter = new FileChooser.ExtensionFilter("Simulation files (*.sim)", "*.sim");
			fileChooser.getExtensionFilters().add(simFilter);
			File file = fileChooser.showOpenDialog(WarehouseView.getPrimaryStage());

			if (file == null)
				return; // Catch no file selected.

			this.labelFileName.setText(file.getName());
			this.loadSimulation(file.getAbsolutePath());

			// Disable load button and enable the run buttons.
			buttonLoadFile.setDisable(true);
			buttonStop.setDisable(false);
			this.setButtonsDisablement(false);
		} catch (Exception e) {
			alertErrorOccured(e);
		}
	}

	/**
	 * Runs one tick of the simulation
	 */

	@FXML
	public void runOneTick() {
		this.timeline.setCycleCount(1);
		this.timeline.play();
	}

	/**
	 * Runs ten ticks of the simulation
	 */

	@FXML
	public void runTenTicks() {
		this.setButtonsDisablement(true);
		this.timeline.setCycleCount(10);
		this.timeline.play();
	}

	/**
	 * Runs the simulation indefinitely
	 */

	@FXML
	public void runIndefiniteTicks() {
		this.setButtonsDisablement(true);
		this.timeline.setCycleCount(Timeline.INDEFINITE);
		this.timeline.play();
	}

	/**
	 * Stops the simulation
	 */

	@FXML
	public void pauseSimulation() {
		this.timeline.stop();
		this.setButtonsDisablement(false);
	}

	@FXML
	public void stopSimulation() {
		this.timeline.stop();
		this.simulation = null;

		// Disable run/pause/stop buttons and enable load button.
		this.setButtonsDisablement(true);
		buttonPause.setDisable(true);
		buttonStop.setDisable(true);
		buttonLoadFile.setDisable(false);
	}

	// Extracted Logic

	private void runASingleTick() {
		try {
			// Execute simulation tick.
			this.simulation.tick();

			// Redraw the updated view components.
			this.drawRobots();
			this.refreshListViews();
			this.labelTickCount.setText(String.valueOf(this.simulation.getTotalTickCount()));

			// Simulation complete, alert and close application.
			if (this.simulation.isComplete()) {
				this.timeline.stop();
				alertSimulationComplete();
			}
		} catch (Exception e) {
			this.alertErrorOccured(e);
			this.pauseSimulation();
			e.printStackTrace();
		}
	}

	/**
	 * Loads a simulation from the given file
	 * @param fileName
	 * @throws Exception
	 */	

	private void loadSimulation(String fileName) throws Exception {
		if (fileName == null)
			throw new SimFileFormatException("", "No file passed.");

		// Create the simulation from the selected file.
		this.simulation = Simulator.createFromFile(Paths.get(fileName));

		// Set up the various view components.
		Floor floor = this.simulation.getWarehouse().getFloor();
		this.createFloor(floor.getNumberOfColumns(), floor.getNumberOfRows());
		this.drawInitialEntities();
		this.refreshListViews();
		this.labelChargeCapacity.setText(String.valueOf(this.simulation.getMaxChargeCapacity()));
		this.labelChargeSpeed.setText(String.valueOf(this.simulation.getChargeSpeed()));
	}

	/**
	 * Creates the warehouse floor from the columns and rows provided.
	 * @param columns
	 * @param rows
	 */

	private void createFloor(int columns, int rows) {
		this.floor = new StackPane[columns][rows];

		// Add Column Constraints.
		this.grdWarehouse.getColumnConstraints().clear();
		for (int column = 0; column < columns; column++) {
			this.grdWarehouse.getColumnConstraints().add(new ColumnConstraints(TILE_DIMENSION));
		}

		// Add Row Constraints.
		this.grdWarehouse.getRowConstraints().clear();
		for (int row = 0; row < rows; row++) {
			this.grdWarehouse.getRowConstraints().add(new RowConstraints(TILE_DIMENSION));
		}

		// Add StackPane to each cell.
		this.grdWarehouse.getChildren().removeIf(node -> node instanceof StackPane);
		System.out.println(this.grdWarehouse.getChildren());
		for (int column = 0; column < columns; column++) {
			for (int row = 0; row < rows; row++) {
				StackPane stackPane = new StackPane();
				GridPane.setConstraints(stackPane, column, row);
				this.floor[column][row] = stackPane;
				this.grdWarehouse.getChildren().add(stackPane);
			}

			this.grdWarehouse.getColumnConstraints().add(new ColumnConstraints(TILE_DIMENSION));

			for (int row = 0; row < rows; row++) {
				this.grdWarehouse.getRowConstraints().add(new RowConstraints(TILE_DIMENSION));
			}

		}
	}
			/**
			 * Puts a shape representation of the entities onto the floor of the warehouse
			 */

			private void drawInitialEntities() {
				for (Entity entity : this.simulation.getWarehouse().getEntities()) {
					StackPane stackPane = this.floor[entity.getLocation().getColumn()][entity.getLocation().getRow()];
					stackPane.getChildren().add(entity.getSprite());
				}
			}

			/**
			 * puts a shape representation of the robots onto the floor.
			 */

			private void drawRobots() {
				for (Robot robot : this.simulation.getRobots()) {
					// Skip if not moved.
					if (robot.getLocation().equals(robot.getPreviousLocation()))
						continue;

					// Erase old sprite.
					StackPane oldStackPane = this.getStackPaneForLocation(robot.getPreviousLocation());
					oldStackPane.getChildren().remove(robot.getSprite());

					// Draw new sprite.
					StackPane newStackPane = this.getStackPaneForLocation(robot.getLocation());
					newStackPane.getChildren().add(robot.getSprite());
				}
			}

			/**
			 * Returns the stackpane object at a given location
			 * @param location
			 * @return StackPane
			 */

			private StackPane getStackPaneForLocation(Location location) {
				return this.floor[location.getColumn()][location.getRow()];
			}

			private void refreshListViews() {
				this.robotsList.getItems().clear();
				this.robotsList.getItems().addAll(this.simulation.getRobots());

				this.unassignedOrders.getItems().clear();
				this.unassignedOrders.getItems().addAll(this.simulation.getWarehouse().getUnassignedOrders());

				this.assignedOrders.getItems().clear();
				this.assignedOrders.getItems().addAll(this.simulation.getWarehouse().getAssignedOrders());

				this.dispatchedOrders.getItems().clear();
				this.dispatchedOrders.getItems().addAll(this.simulation.getWarehouse().getDispatchedOrders());
			}

			/**
			 * Disables the buttons depending on the boolean value disabled
			 * @param disabled
			 */

			private void setButtonsDisablement(boolean disabled) {
				buttonOneTick.setDisable(disabled);
				buttonTenTicks.setDisable(disabled);
				buttonIndefiniteTicks.setDisable(disabled);
				buttonPause.setDisable(!disabled);
			}

			/**
			 * Shows and alert to show the simulation is complete.
			 */

			public void alertSimulationComplete() {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("Simulation Complete");
				alert.setHeaderText("Congratulations, the simulation is complete!");
				alert.setContentText("Total tick count: " + simulation.getTotalTickCount());
				alert.setOnCloseRequest((event) -> this.stopSimulation());
				alert.show();
			}

			/**
			 * Shows an alert when an error occurs
			 * @param error
			 */

			public void alertErrorOccured(Exception error) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Fatal Error Occured");
				alert.setHeaderText("An error has occur thats stopped the simulation from continuing.");
				alert.setContentText(error.toString());
				alert.setOnCloseRequest((event) -> this.stopSimulation());
				alert.show();
			}

		}
