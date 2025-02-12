package Controller;

import Connect.Connect;
import View.BuyMotorcycle;
import View.CartPage;
import View.LoginPage;
import View.Main;
import View.ManageMotorcycle;
import View.TransactionPage;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import jfxtras.labs.scene.control.window.Window;
import jfxtras.labs.scene.control.window.CloseIcon;

public class PageController {
	Scene HomeScene;
	BorderPane mainBorder;
	
	Menu menu1, menu2;
	MenuBar menuBar;
	MenuItem menuItem1, menuItem2, menuItem3, menuItem4;
	
	String userId = "";
	String userRole = "";
	
	Window window;
	
	Connect connect = Connect.getInstance();
	
	public void initialization() {
		mainBorder = new BorderPane();
		HomeScene = new Scene(mainBorder, 820, 635);
		mainBorder.setStyle("-fx-background-color: #99A98F");
		
		menuBar = new MenuBar();
		
		if(userRole.equals("Customer")) {
			window = generateWindow("Buy Motorcycle", 400, 200, new BuyMotorcycle());
		}else {
			window = generateWindow("manage Motorcycle", 400, 200, new ManageMotorcycle());
		}
		
		if (userRole.equals("Customer")) {
			menuItem1 = new MenuItem("Home");
			menuItem2 = new MenuItem("Cart");
			menuItem3 = new MenuItem("My Transactions History");
			menuItem4 = new MenuItem("Sign Out");

			menu1 = new Menu("My User");
			menu2 = new Menu("Transaction");
		}
		else if (userRole.equals("Admin")) {
			menuItem1 = new MenuItem("Home");
			menuItem2 = new MenuItem("Sign Out");

			menu1 = new Menu("My Admin");
			menu2 = new Menu("Manage");
		}
	}
	
	public void setPosition() {
		if(userRole.equals("Customer")) {
			menu1.getItems().addAll(menuItem4);
			menu2.getItems().addAll(menuItem1, menuItem2, menuItem3);
		}
		else if (userRole.equals("Admin")) {
			menu1.getItems().addAll(menuItem2);
			menu2.getItems().addAll(menuItem1);
		}
		menuBar.getMenus().addAll(menu1, menu2);
		mainBorder.setTop(menuBar);
		mainBorder.setCenter(window);
	}
	
	public void setEventHandler() {
		
		if(userRole.equals("Customer")) {
			menuItem1.setOnAction(e->{
				window = generateWindow("Buy Motorcycle", 400, 200, new BuyMotorcycle());
				mainBorder.setCenter(window);
			});
			
			menuItem2.setOnAction(e->{
				window = generateWindow("Cart", 400, 200, new CartPage());
				mainBorder.setCenter(window);
			});
			
			menuItem3.setOnAction(e->{
				window = generateWindow("My Transaction", 400, 200, new TransactionPage());
				mainBorder.setCenter(window);
			});
			
			menuItem4.setOnAction(e->{
				AuthController.tempUserID = null;
				AuthController.tempUserRole = null;
				
				new LoginPage();
			});
		}
		else if (userRole.equals("Admin")) {
			menuItem1.setOnAction(e->{
				window = generateWindow("Manage Motorcycle", 400, 200, new ManageMotorcycle());
				mainBorder.setCenter(window);
			});
			
			menuItem2.setOnAction(e->{				
				AuthController.tempUserID = null;
				AuthController.tempUserRole = null;
				
				new LoginPage();
			});
		}
	}
	
	public Window generateWindow(String title,int width,int height,Node node) {
		Window window=new Window(title);

	    // Add the content node to the window
	    window.getContentPane().getChildren().add(node);

	    // Add icons and other configurations to the window (if needed)
	    window.getRightIcons().add(new CloseIcon(window));
	    return window;
	}
	
	public PageController(String role, String id) {
		userId = id;
		userRole = role;
		initialization();
		setPosition();
		setEventHandler();
		Main.switchScene(HomeScene);
	}
	
}
