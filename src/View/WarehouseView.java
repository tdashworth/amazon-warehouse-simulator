package View;
import model.*;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class WarehouseView extends Application {

	@FXML private TextArea txtRows;
	@FXML private TextArea txtCol;
	@FXML private Pane paneWarehouse;
	@FXML private GridPane grdWarehouse;
	@FXML private Slider sldCapacity;
	@FXML private Label lblCapacity;
	@FXML private Label lblCharge;
	@FXML private Slider sldCharge;
	@FXML private Label lblCount;
	@FXML ListView<Robot> robotsList;
	@FXML ListView<Order> unassignedOrders;
	@FXML ListView<Order> assignedOrders;
	@FXML ListView<Order> dispatchedOrders;

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("Warehouse.fxml"));
		BorderPane mainContainer = (BorderPane) loader.load();

		Scene mainScene = new Scene(mainContainer, 900, 900);
		primaryStage.setScene(mainScene);
		primaryStage.setTitle("Amazon Warehouse Simulator");
		primaryStage.show();
	}
	
	

	public static void main (String[] args) {
		launch(args);
	}
	

	
}
