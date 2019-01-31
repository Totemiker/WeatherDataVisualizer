package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main_GUI extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("WDAGui.fxml"));

			Parent root = loader.load();
			Scene scene = new Scene(root);
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			// System.out.println("MAIN: "+scene.getStylesheets());
			// primaryStage.setMaximized(true);

			primaryStage.setScene(scene);
			primaryStage.setTitle("WDA");
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
