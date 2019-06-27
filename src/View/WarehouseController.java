package View;
import simulation.*;
import model.*;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Deque;
import java.util.HashMap;
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
	@FXML private Rectangle squPS1;
	@FXML private Circle rb1;
	private int rows;
	private int columns;
	private Floor floor;
	private HashMap<Location, StackPane> gridCells;
	private Simulator sim;
	@FXML ListView<Robot> robotsList;
	@FXML ListView<Order> unassignedOrders;
	@FXML ListView<Order> assignedOrders;
	@FXML ListView<Order> dispatchedOrders;

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
		txtRows.setText("5");
		txtCol.setText("5");
		sldCharge.setValue(1.0);
		lblCount.setText("Total tick count: 0");
		floor.clear();
		for(int i = rows; i > 5; i--) {
			grdWarehouse.getRowConstraints().remove(i-1);
		}
		for(int i = columns; i > 5; i--) {
			grdWarehouse.getColumnConstraints().remove(i-1);
		}

		for(Location key: gridCells.keySet()) {
			gridCells.get(key).getChildren().clear();
		}
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
	}

	public void loadSimulation() throws IOException, SimFileFormatException, LocationNotValidException {
		
		sim = Simulator.createFromFile(Paths.get("./sample-configs/oneOfEverything.sim"));
				
		lblCount.textProperty().bind(
				Bindings.concat("Total tick count:" + sim.getTotalTickCount())
				);
				
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
	
		gridCells = new HashMap<Location, StackPane>();
		columns = Integer.parseInt(txtCol.getText());
		rows = Integer.parseInt(txtRows.getText());

		for(int j=0; j < columns; j++) {
			for (int i=0; i< rows; i++) {
				StackPane stk = new StackPane();
				grdWarehouse.add(stk, j, i);
				Location location = new Location(j,i);
				gridCells.put(location, stk);
			}  
		}
		
		/*Example of how to add a shape to a cell
		StackPane stk1 = gridCells.get("1,3");
		Circle rb1 = new Circle();
		rb1.setFill(Color.AQUA);
		rb1.setRadius(15);
		stk1.getChildren().add(rb1);
		 */
		
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
