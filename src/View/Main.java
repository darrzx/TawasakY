package View;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application{
	
	static Stage stage;

	public static void main(String[] args) {
		launch(args);
	}
	
	public static void switchScene(Scene newScene) {
        stage.setScene(newScene);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }

	@Override
	public void start(Stage arg0) throws Exception {
		stage = arg0;
        stage.setResizable(false);
        stage.setTitle("TawasakY");
        new LoginPage();
	}

}
