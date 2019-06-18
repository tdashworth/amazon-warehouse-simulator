package View;
import simulation.*;
import model.*;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;


public class WarehouseController {

	@FXML private Slider sldSize;
	@FXML private Label lblSize;
	@FXML private Pane paneWarehouse;
	@FXML private GridPane grdWarehouse;
	@FXML private Slider sldCapacity;
	@FXML private Label lblCapacity;
	@FXML private Label lblCharge;
	@FXML private Slider sldCharge;
	@FXML private Label lblCount;
	private int rows;
	private int capacity;
	private int chargeSpeed;
	private Floor floor;


	@FXML public void initialize() {


		sldSize.valueProperty().addListener((observable, oldValue, newValue) -> {

			rows = newValue.intValue();	
			lblSize.setText("Size: " + Integer.toString(newValue.intValue()) + " x " + Integer.toString(newValue.intValue()));

			if(newValue.intValue() > oldValue.intValue()) {
				ColumnConstraints column = new ColumnConstraints();
				column.setMinWidth(30);
				grdWarehouse.getColumnConstraints().add(column);
				RowConstraints rowConst = new RowConstraints();
				rowConst.setMinHeight(30);
				grdWarehouse.getRowConstraints().add(rowConst);    
			}
			else if(newValue.intValue() < oldValue.intValue()) {
				int endCell = grdWarehouse.getRowConstraints().size() - 1;
				grdWarehouse.getRowConstraints().remove(endCell);
				grdWarehouse.getColumnConstraints().remove(endCell);
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

	@FXML public void reset() {
		sldSize.setValue(5.0);
		sldCapacity.setValue(10.0);
		sldCharge.setValue(1.0);
		lblCount.setText("Total tick count: 0");
		floor.clear();

	}


	@FXML public void runSimulation() {

		floor = new Floor(rows, rows);

		//Simulator s = new Simulator(f, capacity, chargeSpeed,   );
		



	}


}
