package View;

import java.awt.Component;
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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
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
	private TextArea txtRows;
	@FXML
	private TextArea txtCol;
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
	@FXML
	private Label lblFile;
	private int rows;
	private int columns;
	@FXML private Button btnLoad;

	/**
	 * initialize the simulation, add listeners to the sliders and text areas, buttons
	 */
	@FXML
	public void initialize() {

		robots = new ArrayList<Robot>();

		txtRows.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					txtRows.setText("5");
				}
				rows = Integer.parseInt(txtRows.getText());
			}
		});

		txtCol.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					txtCol.setText("5");
				}
				columns = Integer.parseInt(txtCol.getText());
			}
		});

		sldCapacity.valueProperty().addListener((observable, oldValue, newValue) -> {
			lblCapacity.setText("Battery Capacity: " + Integer.toString(newValue.intValue()));
			for (Robot r : robots) {
				r.setPowerUnits(newValue.intValue());
			}
			sim.setMaxChargeCapacity(newValue.intValue());
		});

		sldCharge.valueProperty().addListener((observable, oldValue, newValue) -> {
			lblCharge.setText("Charge speed:" + Integer.toString(newValue.intValue()));
			sim.setChargeSpeed(newValue.intValue());
		});

	}

	public void upload() {
		FileChooser fil_chooser = new FileChooser();
		File file = fil_chooser.showOpenDialog(WarehouseView.getPrimaryStage());
		if (file != null) {
			lblFile.setText(file.getAbsolutePath());
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

		btnLoad.setDisable(false);
	}

	/**
	 * 
	 * Run one tick of the simulation triggered by pressing the run one tick button, it calls the tick method from simulation
	 * and also moves the robots around the warehouse. 
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
	}

	private void runOneTickSaftely() {
		try {
			runOneTick();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(2);
		}
	}

	/**
	 * Loads up the simulation using either a file or user configurations
	 * 
	 * @throws IOException
	 * @throws SimFileFormatException
	 * @throws LocationNotValidException
	 */
	public void loadSimulation() throws IOException, SimFileFormatException, LocationNotValidException {

		btnLoad.setDisable(true);

		if(!lblFile.getText().contentEquals("Selected file: ")) {

			sim = Simulator.createFromFile(Paths.get(lblFile.getText()));

			// sets the grid size to be the same as the floor in the file
			Floor f = sim.getFloor();

			for (int i = 0; i < f.getNumberOfRows(); i++) {
				RowConstraints rowConst = new RowConstraints();
				rowConst.setMinHeight(40);
				grdWarehouse.getRowConstraints().add(rowConst);
			}

			for (int i = 0; i < f.getNumberOfColumns(); i++) {
				ColumnConstraints column = new ColumnConstraints();
				column.setMinWidth(40);
				grdWarehouse.getColumnConstraints().add(column);
			}

			sldCapacity.valueProperty().setValue(sim.getMaxChargeCapacity());
			sldCharge.valueProperty().setValue(sim.getChargeSpeed());

			txtRows.setText(Integer.toString(f.getNumberOfRows()));
			txtCol.setText(Integer.toString(f.getNumberOfColumns()));


			List<Actor> actors = sim.getActors();

			for (Actor a : actors) {


				if (a instanceof Robot) {
					robots.add((Robot) a);
					Location l = ((Robot) a).getLocation();
					StackPane stk = new StackPane();
					GridPane.setConstraints(stk, l.getColumn(), l.getRow());
					grdWarehouse.getChildren().add(stk);
					Circle cp1 = new Circle();
					cp1.setFill(Color.MEDIUMORCHID);
					cp1.setRadius(20);
					stk.getChildren().add(cp1);
					Circle rb1 = new Circle();
					rb1.setFill(Color.RED);
					rb1.setRadius(15);
					stk.getChildren().add(rb1);

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
			//needs to create a simulator from user configurations
			Floor floor = new Floor(rows, columns);
			HashMap<String, Entity> entities = new HashMap<String, Entity>();
			Deque<Order> orders = new LinkedList<Order>();

			sim = new Simulator(floor, 0, 0, entities, orders);
			for (int i = 0; i < rows; i++) {
				RowConstraints rowConst = new RowConstraints();
				rowConst.setMinHeight(40);
				grdWarehouse.getRowConstraints().add(rowConst);
			}

			for (int i = 0; i < columns; i++) {
				ColumnConstraints column = new ColumnConstraints();
				column.setMinWidth(40);
				grdWarehouse.getColumnConstraints().add(column);
			}

			for(int i = 0; i < columns; i++) {
				for(int j = 0; j <rows; j++) {
					StackPane stk = new StackPane();
					GridPane.setConstraints(stk, i, j);
					grdWarehouse.getChildren().add(stk);
				}
			}

			for(Node node : grdWarehouse.getChildren()) {

				node.setOnMouseClicked(new EventHandler<MouseEvent>() {
					public void handle(MouseEvent event) {
						int uid = 1;
						if (event.getClickCount() == 1) {
							int row = grdWarehouse.getRowIndex(node);
							int col = grdWarehouse.getColumnIndex(node);
							Circle r = new Circle();
							r.setRadius(15);
							r.setFill(Color.RED);
							GridPane.setConstraints(r, col, row);
							grdWarehouse.getChildren().add(r);	
							Location l = new Location(col, row);
							ChargingPod c = new ChargingPod("cp"+ uid, l);
							Robot robo = new Robot("rb"+ uid, l, c, sim.getMaxChargeCapacity());
						}
						uid++;
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
	 * @return a Simulator
	 */

	public Simulator getSimulation() {
		return sim;
	}

	/**
	 * Runs the simulation for ten ticks
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
	 * @throws Exception
	 */
	@FXML
	public void runToEnd() throws Exception {
		Timeline timeline = new Timeline();
		timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(0.25), ea -> {
			runOneTickSaftely();
			if (sim.isComplete()) {
				timeline.stop();
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information Dialog");
				alert.setHeaderText("Congratulations, the simulation is complete!");
				alert.setContentText("Total tick count: "+ sim.getTotalTickCount());
				alert.show();
			}
		}));
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();
	}


	/**
	 * Informs the ListView that one of its items has been modified.
	 *
	 * @param listView The ListView to trigger.
	 * @param newValue The new value of the list item that changed.
	 * @param i The index of the list item that changed.
	 */
	public static <T> void triggerUpdate(ListView<T> listView, T newValue, int i) {
		EventType<? extends ListView.EditEvent<T>> type = ListView.editCommitEvent();
		Event event = new ListView.EditEvent<>(listView, type, newValue, i);
		listView.fireEvent(event);
	}


	/**
	 * Checks when any properties of a robot has changed so it can update the observable list
	 */

	public void robotListChanges() {
		new Thread(() -> {
			IntStream.range(0, sim.robotsProperty().size()).forEach(i -> {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
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

}
