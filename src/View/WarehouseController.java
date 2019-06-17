package View;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;


public class WarehouseController {

	@FXML private Slider sldSize;
	@FXML private Label lblSize;
	@FXML private Pane paneWarehouse;
	@FXML private GridPane grdWarehouse;
	@FXML private Slider sldRobot;
	@FXML private Label lblRobot;
	@FXML private Label lblCharge;
	@FXML private Slider sldCharge;


	@FXML public void initialize() {



		sldSize.valueProperty().addListener((observable, oldValue, newValue) -> {

			lblSize.setText("Size: " + Integer.toString(newValue.intValue()));
			
			
			if(newValue.intValue() > oldValue.intValue()) {
				ColumnConstraints column = new ColumnConstraints();
			    column.setMinWidth(30);
				grdWarehouse.getColumnConstraints().add(column);
				RowConstraints rowConst = new RowConstraints();
				rowConst.setMinHeight(30);
				grdWarehouse.getRowConstraints().add(rowConst);    
			}
		});
		
			
		sldRobot.valueProperty().addListener((observable, oldValue, newValue) -> {
			
			lblRobot.setText(Integer.toString(newValue.intValue()) + " robots");
		});
		
		sldCharge.valueProperty().addListener((observable, oldValue, newValue) -> {
			
			lblCharge.setText("Charge speed:" + Integer.toString(newValue.intValue()));
		});
		}
			
	@FXML public void reset() {
		sldSize.setValue(5.0);
		sldRobot.setValue(1.0);
		sldCharge.setValue(1.0);
	}
	
		

	@FXML public void runSimulation() {


	}


}
