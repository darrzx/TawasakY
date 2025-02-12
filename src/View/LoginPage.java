package View;

import Controller.AuthController;
import Controller.PageController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

public class LoginPage {
	Scene LoginScene;
	
	BorderPane loginBorder;
	GridPane loginGrid;
	
	Label loginLabel, usernameLoginLabel, passwordLoginLabel;
	TextField usernameLoginTF;
	PasswordField passwordLoginPF;
	Button loginButton;
	Hyperlink goToRegister;
	
	public void initialization() {
		loginBorder = new BorderPane();
		loginGrid = new GridPane();
		LoginScene = new Scene(loginBorder, 620, 380);
		loginBorder.setStyle("-fx-background-color: #99A98F");
		
		loginLabel = new Label("Welcome to TawasakY Store");
		usernameLoginTF = new TextField();
		passwordLoginPF = new PasswordField();
		usernameLoginLabel = new Label("Username");
		passwordLoginLabel = new Label("Password");
		loginButton = new Button("Login");
		goToRegister = new Hyperlink();
		goToRegister.setText("Doesn't have an account? Register here!");
		goToRegister.setStyle("-fx-text-fill: #064663");
//		
		usernameLoginTF.setPromptText("Enter your email...");
		passwordLoginPF.setPromptText("Enter your password...");
		
		loginButton.setMinWidth(100);
		usernameLoginTF.setMinWidth(200);
		passwordLoginPF.setMinWidth(200);
	}
	
	public void setPosition() {
		loginGrid.add(usernameLoginLabel, 0, 0);
		loginGrid.add(passwordLoginLabel, 0, 1);
		loginGrid.add(usernameLoginTF, 1, 0);
		loginGrid.add(passwordLoginPF, 1, 1);
		loginGrid.add(loginButton, 1, 3);
		loginBorder.setTop(loginLabel);
		loginBorder.setCenter(loginGrid);
		loginBorder.setBottom(goToRegister);
		
		BorderPane.setAlignment(loginLabel, Pos.CENTER);
		BorderPane.setAlignment(goToRegister, Pos.CENTER);
		loginGrid.setAlignment(Pos.CENTER);
		loginBorder.setPadding(new Insets(40));
		loginLabel.setFont(new Font("San-Serrif", 35));
		usernameLoginLabel.setFont(new Font("San-Serrif", 15));
		passwordLoginLabel.setFont(new Font("San-Serrif", 15));
		goToRegister.setFont(new Font("San-Serrif", 15));
		BorderPane.setMargin(loginGrid, new Insets(50, 0, 0, 0));
		BorderPane.setMargin(goToRegister, new Insets(0, 15, 0, 0));
		loginButton.setPadding(new Insets(7, 20, 7, 20));
		loginGrid.setVgap(20);
		loginGrid.setHgap(20);
	}
	
	public void setEventHandler() {
		loginButton.setOnAction(e->{
			String username = usernameLoginTF.getText();
			String password = passwordLoginPF.getText();
			AuthController ac = new AuthController();
			Alert a = new Alert(AlertType.ERROR);
			if (username.length() == 0 ) {
				a.setContentText("Username Must Be Filled!");
				a.showAndWait();
			}
			else if (password.length() == 0) {
				a.setContentText("Password Must Be Filled!");
				a.showAndWait();
			}
			else if (ac.Login(username, password) == false) {
				a.setContentText("Invalid Credentials");
				a.showAndWait();
			}
			else {
				Alert b = new Alert(AlertType.INFORMATION);
				b.setContentText("Succesfully Login!");
				b.showAndWait();

				AuthController.tempUserID = AuthController.authUser.getID();
				AuthController.tempUserRole = AuthController.authUser.getRole();
				new PageController(AuthController.tempUserRole, AuthController.tempUserID);
			}
		});

		goToRegister.setOnAction(e -> {
			new RegisterPage();
		});
	}

	public LoginPage() {
		initialization();
		setPosition();
		setEventHandler();
		Main.switchScene(LoginScene);
	}

}
