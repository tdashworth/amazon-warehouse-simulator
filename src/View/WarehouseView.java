package View;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.stage.Stage;



public class WarehouseView extends Application {

	private static Stage primaryStage;

	/**
	 * starts the application, sets the title, stage and scene 
	 */

	@Override
	public void start(Stage primaryStage) throws Exception {
		setPrimaryStage(primaryStage);
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("Warehouse.fxml"));
		SplitPane mainContainer = (SplitPane) loader.load();
		Scene mainScene = new Scene(mainContainer, 1700, 900);
		primaryStage.setScene(mainScene);
		primaryStage.setTitle("Amazon Warehouse Simulator");
		primaryStage.show();
	}

	private void setPrimaryStage(Stage stage) {
		WarehouseView.primaryStage = stage;
	}

	static public Stage getPrimaryStage() {
		return WarehouseView.primaryStage;
	}

	
	public static void main (String[] args) {
		launch(args);
	}


}	

