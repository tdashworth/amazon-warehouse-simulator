package View;
import simulation.*;
import model.*;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;


public class WarehouseController {

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
	private int capacity;
	private int chargeSpeed;
	private Floor floor;
	private HashMap<String, Entity> entities;
	private Deque<Order> orders;


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


		sldCapacity.valueProperty().addListener((observable, oldValue, newValue) -> {
			capacity = newValue.intValue();
			lblCapacity.setText("Battery Capacity: " + Integer.toString(newValue.intValue()));
		});


		sldCharge.valueProperty().addListener((observable, oldValue, newValue) -> {
			chargeSpeed = newValue.intValue();
			lblCharge.setText("Charge speed:" + Integer.toString(newValue.intValue()));
		});
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

	}


	/**
	 * 
	 * Run simulation which is triggered by pressing the run button, this sets up the
	 * simulation and starts the run method in simulation.
	 * @throws Exception 
	 */
	@FXML public void runSimulation() throws Exception {
		
		//need to populate the Hash map and deque with entities and orders, not sure the best way to do this yet.
		HashMap<String, Entity> entities = new HashMap<String,Entity>();

		Deque<Order> orders = new LinkedList<Order>();
		
		
		Circle robot1 = new Circle(12.5);
		robot1.setFill(Color.web("ffff00"));
		grdWarehouse.add(robot1, 0,0);
		

		for(int i = 5; i < rows; i++) {
			RowConstraints rowConst = new RowConstraints();
			rowConst.setMinHeight(30);
			grdWarehouse.getRowConstraints().add(rowConst);    
		}

		columns = Integer.parseInt(txtCol.getText());

		for(int i = 5; i < columns; i++) {
			ColumnConstraints column = new ColumnConstraints();
			column.setMinWidth(30);
			grdWarehouse.getColumnConstraints().add(column);
		}

	


		floor = new Floor(rows, columns);
		System.out.println(floor.toString());
		Simulator s = new Simulator(floor, capacity, chargeSpeed, entities, orders);
		s.run();
		
		lblCount.setText("Total tick count: " + s.getTotalTickCount());

	}


}
