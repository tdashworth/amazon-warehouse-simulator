package View;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;



public class WarehouseView extends Application {

	private static Stage primaryStage;


	@Override
	public void start(Stage primaryStage) throws Exception {
		setPrimaryStage(primaryStage);
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("Warehouse.fxml"));
		BorderPane mainContainer = (BorderPane) loader.load();
		Scene mainScene = new Scene(mainContainer, 1800, 900);
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

