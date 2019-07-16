package View;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CompleteController {

	@FXML
	private Label lblTickCount;
	private WarehouseController wc;
	
	@FXML
	public void initialize() {

		lblTickCount.setText("Total tick count: " + wc.getSimulation().getTotalTickCount());
	}
	
}
