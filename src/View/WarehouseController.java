package View;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import model.Actor;
import model.ChargingPod;
import model.Entity;
import model.Floor;
import model.Location;
import model.LocationNotValidException;
import model.Order;
import model.PackingStation;
import model.Robot;
import model.StorageShelf;
import simulation.SimFileFormatException;
import simulation.Simulator;

public class WarehouseController {

	@FXML
	private TextField txtRows;
	@FXML
	private TextField txtCol;
	@FXML
	private Pane paneWarehouse;
	@FXML
	private GridPane grdWarehouse;
	@FXML
	private Slider sldCapacity;
	@FXML
	private Label lblCapacity;
	@FXML
	private Label lblCharge;
	@FXML
	private Slider sldCharge;
	@FXML
	private Label lblCount;
	private Simulator sim;
	@FXML
	private Label lblRows;
	@FXML
	private Label lblCol;
	@FXML
	ListView<Robot> robotsList;
	@FXML
	ListView<Order> unassignedOrders;
	@FXML
	ListView<Order> assignedOrders;
	@FXML
	ListView<Order> dispatchedOrders;
	private ArrayList<Robot> robots;
	@FXML
	private Button btnUploadFile;
	private int rows;
	private int columns;
	@FXML
	private Button btnPack;
	@FXML
	private Button btnRobot;
	@FXML
	private Button btnShelf;

	/**
	 * initialize the simulation, add listeners to the sliders, text areas and
	 * buttons
	 */
	@FXML
	public void initialize() { 

		robots = new ArrayList<Robot>();

		txtRows.textProperty().addListener((observable, oldValue, newValue) -> {
				if (!newValue.matches("\\d*")) {
					txtRows.setText(oldValue);
					return;
				}

				rows = Integer.parseInt(txtRows.getText());
				int rowsToAdd = rows - grdWarehouse.getRowConstraints().size();
				for (int i = 0; i < rowsToAdd; i++) {
					grdWarehouse.getRowConstraints().add(new RowConstraints(40, 40, 40));
				}
				for (int i = 0; i < (-1 * rowsToAdd); i++) {
					grdWarehouse.getRowConstraints().remove(0);
				}
			});

		txtCol.textProperty().addListener((observable, oldValue, newValue) -> {
				if (!newValue.matches("\\d*")) {
					txtCol.setText(oldValue);
					return;
				}
				
				columns = Integer.parseInt(txtCol.getText());
				int columnsToAdd = columns - grdWarehouse.getColumnConstraints().size();
				for (int i = 0; i < columnsToAdd; i++) {
					grdWarehouse.getColumnConstraints().add(new ColumnConstraints(40, 40, 40));
				}
				for (int i = 0; i < (-1 * columnsToAdd); i++) {
					grdWarehouse.getColumnConstraints().add(new ColumnConstraints(40, 40, 40));
				}
			});

		sldCapacity.valueProperty().addListener((observable, oldValue, newValue) -> {
			lblCapacity.setText("Battery Capacity: " + Integer.toString(newValue.intValue()));
			if (robots.size() > 0) {
				for (Robot r : robots) {
					r.setPowerUnits(newValue.intValue());
				}
			}
			if (sim != null) {
				sim.setMaxChargeCapacity(newValue.intValue());
			}
		});

		sldCharge.valueProperty().addListener((observable, oldValue, newValue) -> {
			lblCharge.setText("Charge speed:" + Integer.toString(newValue.intValue()));
			if (sim != null) {
				sim.setChargeSpeed(newValue.intValue());
			}
		});

	}

	/**
	 * Create a file chooser so the user can upload a file, must be sim file or
	 * warning is shown
	 */

	public void upload() {
		FileChooser fileChooser = new FileChooser();
		File file = fileChooser.showOpenDialog(WarehouseView.getPrimaryStage());
		try {
			this.loadSimulation(file.getAbsolutePath());
		} catch (Exception e) {
			alertErrorOccured(e);
		}
	}

	/**
	 * Resets the simulation back to it's default settings
	 */
	@FXML
	public void reset() {
		robotsList.getItems().clear();
		unassignedOrders.getItems().clear();
		assignedOrders.getItems().clear();
		dispatchedOrders.getItems().clear();

		for (int i = sim.getFloor().getNumberOfRows() - 1; i >= 0; i--) {
			grdWarehouse.getRowConstraints().remove(i);
		}
		for (int i = sim.getFloor().getNumberOfColumns() - 1; i >= 0; i--) {
			grdWarehouse.getColumnConstraints().remove(i);
		}
		grdWarehouse.getChildren().clear();

		btnUploadFile.setDisable(false);
	}

	/**
	 * 
	 * Run one tick of the simulation triggered by pressing the run one tick button,
	 * it calls the tick method from simulation and also moves the robots around the
	 * warehouse.
	 * 
	 * @throws Exception
	 */

	@FXML
	public void runOneTick() throws Exception {
		sim.tick();

		grdWarehouse.getChildren().removeIf(n -> n instanceof Circle);
		lblCount.setText("Total tick count: " + sim.getTotalTickCount());
		
		for (Robot robo : robots) {
			Location l = robo.getLocation();
			Circle r = new Circle();
			r.setRadius(15);
			r.setFill(Color.RED);
			GridPane.setConstraints(r, l.getColumn(), l.getRow());
			grdWarehouse.getChildren().add(r);
		}

		robotListChanges();

		unassignedOrders.setItems(sim.unassignedOrdersProperty());
		assignedOrders.setItems(sim.assignedOrdersProperty());
		dispatchedOrders.setItems(sim.dispatchedOrdersProperty());

		if (sim.isComplete()) {
			alertSimulationComplete();
		}
	}

	private void runOneTickSaftely() {
		try {
			runOneTick();
		} catch (Exception e) {
			alertErrorOccured(e);
		}
	}

	/**
	 * Loads up the simulation using either a file or user configurations
	 * 
	 * @throws IOException
	 * @throws SimFileFormatException
	 * @throws LocationNotValidException
	 */
	public void loadSimulation(String fileName) throws IOException, SimFileFormatException, LocationNotValidException {

		btnUploadFile.setDisable(true);

		if (fileName != null) {
			sim = Simulator.createFromFile(Paths.get(fileName));
			Floor f = sim.getFloor();

			sldCapacity.valueProperty().setValue(sim.getMaxChargeCapacity());
			sldCharge.valueProperty().setValue(sim.getChargeSpeed());

			txtRows.setText(Integer.toString(f.getNumberOfRows()));
			txtCol.setText(Integer.toString(f.getNumberOfColumns()));

			List<Actor> actors = sim.getActors();

			for (Actor a : actors) {

				if (a instanceof Robot) {
					robots.add((Robot) a);
					Location l = ((Robot) a).getLocation();
					Rectangle charge = new Rectangle();
					charge.setHeight(35);
					charge.setWidth(35);
					charge.setFill(Color.GOLD);
					Circle r = new Circle();
					r.setRadius(15);
					r.setFill(Color.RED);
					GridPane.setConstraints(charge, l.getColumn(), l.getRow());
					GridPane.setConstraints(r, l.getColumn(), l.getRow());
					grdWarehouse.getChildren().add(charge);
					grdWarehouse.getChildren().add(r);

				}
				if (a instanceof PackingStation) {
					Location l = ((PackingStation) a).getLocation();
					StackPane stk = new StackPane();
					GridPane.setConstraints(stk, l.getColumn(), l.getRow());
					grdWarehouse.getChildren().add(stk);
					Rectangle ps1 = new Rectangle();
					ps1.setFill(Color.AQUAMARINE);
					ps1.setHeight(35);
					ps1.setWidth(35);
					stk.getChildren().add(ps1);
				}
				if (a instanceof StorageShelf) {
					Location l = ((StorageShelf) a).getLocation();
					StackPane stk = new StackPane();
					GridPane.setConstraints(stk, l.getColumn(), l.getRow());
					grdWarehouse.getChildren().add(stk);
					Rectangle ss1 = new Rectangle();
					ss1.setFill(Color.DARKSALMON);
					ss1.setHeight(35);
					ss1.setWidth(35);
					stk.getChildren().add(ss1);
				}
			}
		}

		else {
			// needs to create a simulator from user configurations
			Floor floor = new Floor(rows, columns);
			HashMap<String, Entity> entities = new HashMap<String, Entity>();
			Deque<Order> orders = new LinkedList<Order>();

			sim = new Simulator(floor, 0, 0, entities, orders);

			for (Node node : grdWarehouse.getChildren()) {

				node.setOnMouseClicked(new EventHandler<MouseEvent>() {
					public void handle(MouseEvent event) {
						int uid = 1;
						if (event.getClickCount() == 1) {
							int row = GridPane.getRowIndex(node);
							int col = GridPane.getColumnIndex(node);

							if (btnRobot.isDisabled()) {
								Rectangle charge = new Rectangle();
								charge.setHeight(35);
								charge.setWidth(35);
								charge.setFill(Color.GOLD);
								Circle r = new Circle();
								r.setRadius(15);
								r.setFill(Color.RED);
								GridPane.setConstraints(charge, col, row);
								GridPane.setConstraints(r, col, row);
								grdWarehouse.getChildren().add(charge);
								grdWarehouse.getChildren().add(r);
								Location l = new Location(col, row);
								ChargingPod c = new ChargingPod("cp" + uid, l);
								Robot robo = new Robot("rb" + uid, l, c, sim.getMaxChargeCapacity());
								sim.addRobot(robo);
								try {
									floor.loadEntity(robo);
								} catch (LocationNotValidException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								robotsList.setItems(sim.robotsProperty());
							}
							if (btnShelf.isDisabled()) {
								Rectangle shelf = new Rectangle();
								shelf.setFill(Color.DARKSALMON);
								shelf.setHeight(35);
								shelf.setWidth(35);
								GridPane.setConstraints(shelf, col, row);
								grdWarehouse.getChildren().add(shelf);
								Location l = new Location(col, row);
								StorageShelf s = new StorageShelf("ss" + uid, l);
								sim.addActor(s);
								try {
									floor.loadEntity(s);
								} catch (LocationNotValidException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							if (btnPack.isDisabled()) {
								Rectangle ps1 = new Rectangle();
								ps1.setFill(Color.AQUAMARINE);
								ps1.setHeight(35);
								ps1.setWidth(35);
								GridPane.setConstraints(ps1, col, row);
								grdWarehouse.getChildren().add(ps1);
								Location l = new Location(col, row);
								PackingStation ps = new PackingStation("ps" + uid, l);
								try {
									floor.loadEntity(ps);
								} catch (LocationNotValidException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							uid++;
						}
					}
				});
			}
		}
		robotsList.setItems(sim.robotsProperty());
		unassignedOrders.setItems(sim.unassignedOrdersProperty());
		grdWarehouse.setGridLinesVisible(true);
	}

	/**
	 * returns a Simulator object
	 * 
	 * @return a Simulator
	 */

	public Simulator getSimulation() {
		return sim;
	}

	/**
	 * Runs the simulation for ten ticks
	 * 
	 * @throws Exception
	 */

	@FXML
	public void runTenTicks() throws Exception {
		Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.5), ea -> runOneTickSaftely()));
		timeline.setCycleCount(10);
		timeline.play();
	}

	/**
	 * Runs the simulation to the end, pops up a new window with stats
	 * 
	 * @throws Exception
	 */
	@FXML
	public void runToEnd() throws Exception {
		Timeline timeline = new Timeline();
		timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(0.25), ea -> {
			runOneTickSaftely();
			if (sim.isComplete()) {
				timeline.stop();
			}
		}));
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();
	}

	public void alertSimulationComplete() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Simulation Complete");
		alert.setHeaderText("Congratulations, the simulation is complete!");
		alert.setContentText("Total tick count: " + sim.getTotalTickCount());
		alert.setOnCloseRequest((event) -> System.exit(0));
		alert.show();
	}

	public void alertErrorOccured(Exception error) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Fatal Error Occured");
		alert.setHeaderText("An error has occur thats stopped the simulation from continuing.");
		alert.setContentText(error.toString());
		alert.setOnCloseRequest((event) -> System.exit(0));
		alert.show();
	}

	/**
	 * Informs the ListView that one of its items has been modified.
	 *
	 * @param listView The ListView to trigger.
	 * @param newValue The new value of the list item that changed.
	 * @param i        The index of the list item that changed.
	 */
	public static <T> void triggerUpdate(ListView<T> listView, T newValue, int i) {
		EventType<? extends ListView.EditEvent<T>> type = ListView.editCommitEvent();
		Event event = new ListView.EditEvent<>(listView, type, newValue, i);
		listView.fireEvent(event);
	}

	/**
	 * Checks when any properties of a robot has changed so it can update the
	 * observable list
	 */

	public void robotListChanges() {
		new Thread(() -> {
			IntStream.range(0, sim.robotsProperty().size()).forEach(i -> {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					alertErrorOccured(e);
					e.printStackTrace();
				}
				System.out.println(sim.robotsProperty().get(i));
				Platform.runLater(() -> {
					sim.robotsProperty().get(i);
					triggerUpdate(robotsList, sim.robotsProperty().get(i), i);
				});
			});
		}).start();
	}

	@FXML
	public void btnPackClicked() {
		btnPack.setDisable(true);
		btnRobot.setDisable(false);
		btnShelf.setDisable(false);
	}

	@FXML
	public void btnRobotClicked() {
		btnPack.setDisable(false);
		btnRobot.setDisable(true);
		btnShelf.setDisable(false);
	}

	@FXML
	public void btnShelfClicked() {
		btnPack.setDisable(false);
		btnRobot.setDisable(false);
		btnShelf.setDisable(true);
	}

	@FXML
	public void generateOrders() {

		int i = 0;
		while (!(sim.getActors().get(i) instanceof StorageShelf)) {
			i++;
		}
		StorageShelf s = (StorageShelf) sim.getActors().get(i);
		List<String> shelves = new ArrayList<String>();
		shelves.add(s.getUID());
		Order o1 = new Order(shelves, 4);
	}
}
