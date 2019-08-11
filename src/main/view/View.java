package main.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.stage.Stage;

public class View extends Application {
	private static Stage primaryStage;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		setPrimaryStage(primaryStage);
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("Main.fxml"));
		SplitPane mainContainer = (SplitPane) loader.load();
		Scene mainScene = new Scene(mainContainer, 1800, 1000);
		primaryStage.setScene(mainScene);
		primaryStage.setTitle("Amazon Warehouse Simulator");
		primaryStage.show();
	}

	private void setPrimaryStage(Stage stage) {
		View.primaryStage = stage;
	}

	static public Stage getPrimaryStage() {
		return View.primaryStage;
	}

}

