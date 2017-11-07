package application;
	
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;



public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {

			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/application/ViewforBreakfast.fxml"), null);
			Parent root = fxmlLoader.load();
			Scene scene = new Scene(root);			
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}

