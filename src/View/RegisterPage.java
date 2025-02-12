package View;

import java.sql.ResultSet;
import java.util.Random;

import Connect.Connect;
import Controller.AuthController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class RegisterPage {
	Connect connect = Connect.getInstance();
	Random rand = new Random();

	Scene RegistScene;
	
	BorderPane registBorder;
	GridPane registGrid;
	VBox vertikalRegister;
	HBox horizontalRegister;
	
	Label registLabel, usernameRegistLabel, passwordRegistLabel, confPasswordRegistLabel, genderRegistLabel, addressRegistLabel;
	TextField usernameRegistTF;
	PasswordField passwordRegistPF, confPassRegistPF;
	RadioButton maleRegist, femaleRegist;
	ToggleGroup genderRegist;
	TextArea addressRegist;
	CheckBox checkRegist;
	Button registButton;
	Hyperlink goToLogin;
	
	public void initialization() {
		registBorder = new BorderPane();
		registGrid = new GridPane();
		vertikalRegister = new VBox();
		horizontalRegister = new HBox();
		RegistScene = new Scene(registBorder, 780, 580);
		registBorder.setStyle("-fx-background-color: #99A98F");
		
		registLabel = new Label("REGISTER");
		usernameRegistLabel = new Label("Username");
		passwordRegistLabel = new Label("Password");
		confPasswordRegistLabel = new Label("Confirmation Password");
		genderRegistLabel = new Label("Gender");
		addressRegistLabel = new Label("Address");

		usernameRegistTF = new TextField();
		passwordRegistPF = new PasswordField();
		confPassRegistPF = new PasswordField();
		maleRegist = new RadioButton("Male");
		femaleRegist = new RadioButton("Female");
		genderRegist = new ToggleGroup();
		addressRegist = new TextArea();
		checkRegist = new CheckBox("I Agree With Terms and Conditions");
		registButton = new Button("Register");
		goToLogin = new Hyperlink();

		goToLogin.setText("Already have an account? Login here!");
		goToLogin.setStyle("-fx-text-fill: #064663");
		
		usernameRegistTF.setPromptText("Enter your email...");
		passwordRegistPF.setPromptText("Enter your password...");
		confPassRegistPF.setPromptText("Enter your Confirmation Password...");
		addressRegist.setPromptText("Enter your Address...");
		
		usernameRegistTF.setMaxWidth(250);
		passwordRegistPF.setMaxWidth(250);
		confPassRegistPF.setMaxWidth(250);
		addressRegist.setMaxHeight(50);
		addressRegist.setMaxWidth(250);
		
		maleRegist.setToggleGroup(genderRegist);
		femaleRegist.setToggleGroup(genderRegist);
	}
	
	public void setPosition() {
		registGrid.add(usernameRegistLabel, 0, 0);
		registGrid.add(passwordRegistLabel, 0, 1);
		registGrid.add(confPasswordRegistLabel, 0, 2);
		registGrid.add(genderRegistLabel, 0, 3);
		registGrid.add(addressRegistLabel, 0, 4);
		registGrid.add(usernameRegistTF, 1, 0);
		registGrid.add(passwordRegistPF, 1, 1);
		registGrid.add(confPassRegistPF, 1, 2);
		horizontalRegister.getChildren().addAll(maleRegist, femaleRegist);
		registGrid.add(horizontalRegister, 1, 3);
		registGrid.add(addressRegist, 1, 4);
		registGrid.add(checkRegist, 1, 5);
		vertikalRegister.getChildren().addAll(registButton, goToLogin);
		
		registBorder.setTop(registLabel);
		registBorder.setCenter(registGrid);
		registBorder.setBottom(vertikalRegister);
		
		BorderPane.setAlignment(registLabel, Pos.CENTER);
		BorderPane.setAlignment(vertikalRegister, Pos.CENTER);
		vertikalRegister.setAlignment(Pos.CENTER);		
		vertikalRegister.setSpacing(15);
		horizontalRegister.setSpacing(50);
		registGrid.setAlignment(Pos.CENTER);
		registBorder.setPadding(new Insets(40));
		registLabel.setFont(new Font("San-Serrif", 35));
		usernameRegistLabel.setFont(new Font("San-Serrif", 15));
		passwordRegistLabel.setFont(new Font("San-Serrif", 15));
		confPasswordRegistLabel.setFont(new Font("San-Serrif", 15));
		genderRegistLabel.setFont(new Font("San-Serrif", 15));
		addressRegistLabel.setFont(new Font("San-Serrif", 15));
		goToLogin.setFont(new Font("San-Serrif", 15));
		registButton.setPadding(new Insets(7, 20, 7, 20));
		registGrid.setVgap(12);
		registGrid.setHgap(20);
	}
	
	public String randomID() {
		String ID = "";
		ID += "US";
		ID += (char) (rand.nextInt(9) + '0');
		ID += (char) (rand.nextInt(9) + '0');
		ID += (char) (rand.nextInt(9) + '0');
		
		return ID;
	}
	
	public boolean checkUniqueUsername(String username) {
		String query = "SELECT UserName FROM User WHERE UserName LIKE '"+username+"';";
		ResultSet rs = connect.execQuery(query);
		
		try {
			if (!rs.isBeforeFirst()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	boolean isAlphanumeric(String tempString){
    	boolean isAlpha = false;
    	boolean isNumeric = false;
        if (tempString == null || tempString.length() == 0){
            return false;
        }
        else{
            for (int i = 0; i < tempString.length(); i++){
                char tempChar = tempString.charAt(i);
                
                if (Character.isDigit(tempChar)){
                    isNumeric = true;
                }
                else {
                	isAlpha = true;
                }
                
                if (isNumeric && isAlpha) {
                	return true;
                }
            }

            return false;
        }
    }
	
	public void setEventHandler() {
		Alert a = new Alert(AlertType.ERROR);
		registButton.setOnAction(e->{
			String username = usernameRegistTF.getText().toString();
			String password = passwordRegistPF.getText().toString();
			String confpass = confPassRegistPF.getText().toString();
			String address = addressRegist.getText().toString();
			boolean male = maleRegist.isSelected();
			boolean female = femaleRegist.isSelected();
			boolean checkbox = checkRegist.isSelected();

			if(username.isEmpty()) {
				a.setContentText("Username Must Be Filled!");
				a.showAndWait();
			}else if (!checkUniqueUsername(username)) {
				a.setContentText("Username Must Be Unique!");
				a.showAndWait();
			}else if (!isAlphanumeric(password)) {
				a.setContentText("Password Must Be Alphanumeric!");
				a.showAndWait();
			}else if (!confpass.equals(password)) {
				a.setContentText("Password Did Not Match!");
				a.showAndWait();
			}else if(!male && !female) {
				a.setContentText("Gender Must Be Filled!");
				a.showAndWait();
			}else if (address.length() < 10 || address.length() > 30) {
				a.setContentText("Address Must Be Between 10 - 30 Characters!");
				a.showAndWait();
			}else if(!checkbox) {
				a.setContentText("CheckBox Must Be Checked!");
				a.showAndWait();
			}else {
				AuthController ac = new AuthController();
				String id = randomID();
				String gender;
				if(maleRegist.isSelected()) {
					gender = maleRegist.getText().toString();
				}else {
					gender = femaleRegist.getText().toString();
				}
				String role = "Customer";
				ac.Register(id, username, password, gender, address, role);
				
				Alert success = new Alert(AlertType.INFORMATION);
				success.setContentText("You Have Been Successfully Registered!");
				success.showAndWait();
				
				new LoginPage();
			}
		});

		goToLogin.setOnAction(e -> {
			new LoginPage();
		});
	}

	public RegisterPage() {
		initialization();
		setPosition();
		setEventHandler();
		Main.switchScene(RegistScene);
	}

}
