package View;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("Warehouse.fxml"));
		
						
		BorderPane mainContainer = (BorderPane) loader.load();
		
		Scene mainScene = new Scene(mainContainer, 500, 500);
		primaryStage.setScene(mainScene);
		primaryStage.setTitle("Amazon Warehouse Simulator");
		primaryStage.show();
	}
	
	
	public static void main (String[] args) {
		launch(args);
		

	}
	
}