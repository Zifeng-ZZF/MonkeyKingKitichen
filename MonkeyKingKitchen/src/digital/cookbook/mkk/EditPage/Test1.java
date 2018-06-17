package digital.cookbook.mkk.EditPage;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Test1 extends Application {
	
	public static void main(String[]args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(getClass().getResource("EditPageView.fxml"));
		Scene scene = new Scene(fxmlLoader.load());
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
}
