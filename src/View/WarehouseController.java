package View;
import simulation.*;
import model.*;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;


public class WarehouseController{

	@FXML private TextArea txtRows;
	@FXML private TextArea txtCol;
	@FXML private Pane paneWarehouse;
	@FXML private GridPane grdWarehouse;
	@FXML private Slider sldCapacity;
	@FXML private Label lblCapacity;
	@FXML private Label lblCharge;
	@FXML private Slider sldCharge;
	@FXML private Label lblCount;
	private HashMap<Location, StackPane> gridCells;
	private Simulator sim;
	@FXML ListView<Robot> robotsList;
	@FXML ListView<Order> unassignedOrders;
	@FXML ListView<Order> assignedOrders;
	@FXML ListView<Order> dispatchedOrders;
	private Robot robot; 

	/**
	 * initialize the simulation, add listeners to the sliders and text areas.
	 */
	@FXML public void initialize() {


		txtRows.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, 
					String newValue) {
				if (!newValue.matches("\\d*")) {
					txtRows.setText("5");
				}
			}
		});


		txtCol.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, 
					String newValue) {
				if (!newValue.matches("\\d*")) {
					txtCol.setText("5");
				}
			}
		});	

		//sldCapacity.valueProperty().bind(sim.chargeCapacityProperty());

		sldCapacity.valueProperty().addListener((observable, oldValue, newValue) -> {
			lblCapacity.setText("Battery Capacity: " + Integer.toString(newValue.intValue()));
		});

		//sldCharge.valueProperty().bind(sim.chargeSpeedProperty());

		sldCharge.valueProperty().addListener((observable, oldValue, newValue) -> {
			lblCharge.setText("Charge speed:" + Integer.toString(newValue.intValue()));
		});


		//unassignedOrders.setCellFactory((view) -> new OrderCell());
	}


	/**
	 * Resets the simulation back to it's default settings
	 */
	@FXML public void reset() {
	
		sldCapacity.setValue(10.0);
		txtRows.setText("0");
		txtCol.setText("0");
		sldCharge.setValue(1.0);
		lblCount.setText("Total tick count: 0");
		for(int i = sim.getFloor().getNumberOfRows()-1; i >= 0; i--) {
			grdWarehouse.getRowConstraints().remove(i);
		}
		for(int i = sim.getFloor().getNumberOfColumns()-1; i >= 0 ; i--) {
			grdWarehouse.getColumnConstraints().remove(i);
		}
		
		sim.resetSimulator();
		
	}

	/**
	 * 
	 * Run simulation which is triggered by pressing the run button, this sets up the
	 * simulation and starts the run method in simulation.
	 * Creates a hash map of cells mapped by their coordinates
	 * @throws Exception 
	 */


	@FXML public void runOneTick() throws Exception {
		sim.tick();
		System.out.println(sim.getTotalTickCount());
		lblCount.textProperty().setValue("Total tick count: " + sim.getTotalTickCount());
		Location l = robot.getLocation();
		grdWarehouse.getChildren().remove(0);
		
		Rectangle robot = new Rectangle();
		robot.setHeight(10);
		robot.setWidth(10);		
		robot.setFill(Color.RED);
		GridPane.setConstraints(robot, l.getRow(), l.getColumn());
		grdWarehouse.getChildren().add(robot);
		
		
		
	}

	public void loadSimulation() throws IOException, SimFileFormatException, LocationNotValidException {
		
		System.out.println("Loading Simulation");
		
		sim = Simulator.createFromFile(Paths.get("./sample-configs/oneOfEverything.sim"));

		//sets the grid size to be the same as the floor in the file
		Floor f = sim.getFloor();
		for(int i = 0; i < f.getNumberOfRows(); i++) {
			RowConstraints rowConst = new RowConstraints();
			rowConst.setMinHeight(30);
			grdWarehouse.getRowConstraints().add(rowConst);
		}

		for(int i = 0; i < f.getNumberOfRows(); i++) {
			ColumnConstraints column = new ColumnConstraints();
			column.setMinWidth(30);
			grdWarehouse.getColumnConstraints().add(column);
		}
		

		for(int j=0; j < f.getNumberOfColumns(); j++) {
			for (int i=0; i< f.getNumberOfRows(); i++) {
				StackPane stk = new StackPane();
				GridPane.setConstraints(stk, i,j);
				grdWarehouse.getChildren().add(stk);
			}  
		}

		sldCapacity.valueProperty().setValue(sim.getMaxChargeCapacity());
		sldCharge.valueProperty().setValue(sim.getChargeSpeed());

		txtRows.setText(Integer.toString(f.getNumberOfRows()));
		txtCol.setText(Integer.toString(f.getNumberOfColumns()));

		List<Actor> actors = sim.getActors();
		
		for(Actor a : actors) {	
			if(a instanceof ChargingPod) {
				System.out.println("Charge");
				Location l = ((ChargingPod) a).getLocation();
				StackPane stk = new StackPane();
				GridPane.setConstraints(stk, l.getRow(), l.getColumn());
				grdWarehouse.getChildren().add(stk);
				Circle cp1 = new Circle();
				cp1.setFill(Color.AQUA);
				cp1.setRadius(15);
				stk.getChildren().add(cp1);		
			}
			if(a instanceof Robot) {
				robot = (Robot) a;
				Location l = ((Robot) a).getLocation();
				StackPane stk = new StackPane();
				GridPane.setConstraints(stk, l.getRow(), l.getColumn());
				grdWarehouse.getChildren().add(stk);
				Circle cp1 = new Circle();
				cp1.setFill(Color.CHARTREUSE);
				cp1.setRadius(15);				
				stk.getChildren().add(cp1);
				Rectangle robot = new Rectangle();
				robot.setHeight(10);
				robot.setWidth(10);		
				robot.setFill(Color.RED);
				stk.getChildren().add(robot);
			}
			if(a instanceof PackingStation) {
				Location l = ((PackingStation) a).getLocation();
				StackPane stk = new StackPane();
				GridPane.setConstraints(stk, l.getRow(), l.getColumn());
				grdWarehouse.getChildren().add(stk);
				Rectangle ps1 = new Rectangle();
				ps1.setFill(Color.MIDNIGHTBLUE);
				ps1.setHeight(29);
				ps1.setWidth(29);
				stk.getChildren().add(ps1);	
			}
			//storage shelf not working?
			if(a instanceof StorageShelf) {
				System.out.println("store");
				Location l = ((StorageShelf) a).getLocation();
				StackPane stk = new StackPane();
				GridPane.setConstraints(stk, l.getRow(), l.getColumn());
				grdWarehouse.getChildren().add(stk);
				Rectangle ss1 = new Rectangle();
				ss1.setFill(Color.BURLYWOOD);
				ss1.setHeight(29);
				ss1.setWidth(29);
				stk.getChildren().add(ss1);
			}
		}


		robotsList.setItems(sim.robotsProperty());
		unassignedOrders.setItems(sim.unassignedOrdersProperty());
		assignedOrders.setItems(sim.assignedOrdersProperty());
		dispatchedOrders.setItems(sim.dispatchedOrdersProperty());


	}

	public Simulator getSimulation() {
		return sim;
	}

	@FXML public void runTenTicks() throws Exception {
		for(int i = 0 ; i < 10; i ++) {
			sim.tick();
			grdWarehouse.getChildren();
			Location l = robot.getLocation();
			Rectangle robot = new Rectangle();
			robot.setHeight(10);
			robot.setWidth(10);		
			robot.setFill(Color.RED);
			GridPane.setConstraints(robot, l.getRow(), l.getColumn());
			grdWarehouse.getChildren().add(robot);
			lblCount.textProperty().setValue("Total tick count: " + sim.getTotalTickCount());
		}

	}

	//1 ticks, 10 ticks or go to end
	//click on the cells to place the entities
	//orders randomly generated.
	//gui read in sim files
	//Model = state of simulation
	//View = representation
	//Controller - takes input - updates the model
	//every cell in the grid as an observable list, when cell changes add shape to cells. 
	//load button - sets up grid, 


}
