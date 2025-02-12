package View;

import java.sql.SQLException;
import java.util.ArrayList;

import Connect.Connect;
import Controller.AuthController;
import Controller.CartController;
import Model.Cart;
import Model.Motorcycle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableSelectionModel;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CartPage extends BorderPane{
	
	GridPane motorCartGrid;
	BorderPane cartMotorBorder;
	VBox vertikalCartMotorTop;
	HBox horizontalCartMotor;
	TableView<Cart> cartTable;
	ArrayList<Cart> carts;
	
	Label motorQuantityCartLabel;
	Spinner<Integer> motorQuantityCartSpinner;
	Button deleteButton, updateButton, checkoutButton;
	public static ArrayList<Cart> checkout = new ArrayList<>();
	
	Connect connect = Connect.getInstance();
	
	public void initialization() {
		motorCartGrid = new GridPane();
		cartMotorBorder = new BorderPane();

		vertikalCartMotorTop = new VBox();
		horizontalCartMotor = new HBox();
		
		cartTable = new TableView<Cart>();
		cartTable.setMaxHeight(300);

		motorQuantityCartLabel = new Label("Motorcycle Quantity");
		motorQuantityCartSpinner = new Spinner<>();
		
		deleteButton = new Button("Delete");
		updateButton = new Button("Update");
		checkoutButton = new Button("Checkout");

		
		SpinnerValueFactory<Integer> quantitySpinner = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10000, 1);
		motorQuantityCartSpinner.setValueFactory(quantitySpinner);
		
		motorQuantityCartSpinner.setMinWidth(250);
	}
	
	public void setPosition() {
		carts = new ArrayList<Cart>();
		
		TableColumn<Cart, String> idCol = new TableColumn<>("Motorcycle ID");
		idCol.setCellValueFactory(new PropertyValueFactory<>("motorID"));

		TableColumn<Cart, String> nameCol = new TableColumn<>("Motorcycle Name");
		nameCol.setCellValueFactory(new PropertyValueFactory<>("motorName"));

		TableColumn<Cart, String> priceCol = new TableColumn<>("Motorcycle Price");
		priceCol.setCellValueFactory(new PropertyValueFactory<>("motorPrice"));
		
		TableColumn<Cart, Integer> quantityCol = new TableColumn<>("Quantity");
		quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		
		cartTable.getColumns().addAll(idCol, nameCol, priceCol, quantityCol);
		cartTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	
		motorCartGrid.setAlignment(Pos.CENTER);
		motorCartGrid.setHgap(10);
		motorCartGrid.setVgap(20);
		motorCartGrid.setPadding(new Insets(35, 55, 35, 55));
		horizontalCartMotor.setSpacing(100);
		horizontalCartMotor.setAlignment(Pos.CENTER);
		horizontalCartMotor.setPadding(new Insets(0, 0, 50, 0));

		motorCartGrid.add(motorQuantityCartLabel, 0, 0);
		motorCartGrid.add(motorQuantityCartSpinner, 1, 0);
		horizontalCartMotor.getChildren().addAll(deleteButton, updateButton, checkoutButton);
		
		vertikalCartMotorTop.getChildren().addAll(cartTable, motorCartGrid);
		cartMotorBorder.setCenter(vertikalCartMotorTop);
		cartMotorBorder.setBottom(horizontalCartMotor);
			
		cartTable.setOnMouseClicked(cartTableMouseEvent());
		refreshTable();
	}
	
	private EventHandler<MouseEvent> cartTableMouseEvent(){
		return new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				// TODO Auto-generated method stub
				TableSelectionModel<Cart> tableSelectionModel = cartTable.getSelectionModel();
				tableSelectionModel.setSelectionMode(SelectionMode.SINGLE);
				
				Cart cart = tableSelectionModel.getSelectedItem();
				
				Alert a = new Alert(AlertType.ERROR);
				Alert b = new Alert(AlertType.INFORMATION);
							
				motorQuantityCartSpinner.getValueFactory().setValue(cart.getQuantity());
			}
			
		};
	}
	
	public void getCart() {
		carts.clear();
		
		String query = String.format("SELECT * FROM Cart\n"
				+ "LEFT JOIN Motorcycle ON Cart.MotorcycleID = Motorcycle.MotorcycleID\n" 
				+ "WHERE Cart.UserID = '%s'", AuthController.tempUserID); 
		connect.rs = connect.execQuery(query);
		
		try {
			while(connect.rs.next()) {
				String motorId = connect.rs.getString("MotorcycleID");
				String motorName = connect.rs.getString("MotorcycleName");
				Integer quantity = connect.rs.getInt("Quantity");
				String motorPrice = connect.rs.getString("MotorcyclePrice");
				carts.add(new Cart(motorId, AuthController.tempUserID, motorName, motorPrice, quantity));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void refreshTable() {
		getCart();
		ObservableList<Cart> regObs = FXCollections.observableArrayList(carts);
		cartTable.setItems(regObs);
	}
	
	public void setEventHandler() {
		deleteButton.setOnAction(e->{
			TableSelectionModel<Cart> cartSelectionModel = cartTable.getSelectionModel();
			cartSelectionModel.setSelectionMode(SelectionMode.SINGLE);
			
			if (cartSelectionModel.isEmpty()) {
				Alert a = new Alert(AlertType.ERROR);
				a.setContentText("Choose Item First!");
				a.showAndWait();
			}
			else {
				Cart cart = cartSelectionModel.getSelectedItem();
				
				CartController cc = new CartController();
				
				cc.removeCart(cart.getMotorID());
				motorQuantityCartSpinner.getValueFactory().setValue(1);

				Alert b = new Alert(AlertType.INFORMATION);
				b.setContentText("Succesfully Remove Selected Item!");
				b.showAndWait();
				refreshTable();
			}
		});
		
		updateButton.setOnAction(e->{
			TableSelectionModel<Cart> cartSelectionModel = cartTable.getSelectionModel();
			cartSelectionModel.setSelectionMode(SelectionMode.SINGLE);
			
			if (cartSelectionModel.isEmpty()) {
				Alert a = new Alert(AlertType.ERROR);
				a.setContentText("Choose Item First!");
				a.showAndWait();
			}
			else {
				Cart cart = cartSelectionModel.getSelectedItem();
				
				String motorId = cart.getMotorID();
				String userId = AuthController.tempUserID;
				Integer quantity = motorQuantityCartSpinner.getValue();
				
				CartController cc = new CartController();
				
				cc.updateCart(motorId, userId, quantity);
				motorQuantityCartSpinner.getValueFactory().setValue(1);

				Alert b = new Alert(AlertType.INFORMATION);
				b.setContentText("Succesfully Update Selected Item!");
				b.showAndWait();
				refreshTable();
			}
		});
		
		checkoutButton.setOnAction(e->{
			CartController cc = new CartController();
			int flag = 0;
			String query = "SELECT * FROM Cart WHERE userId LIKE '"+AuthController.tempUserID+"'";
			connect.execQuery(query);
			
			try {
				if (connect.rs.next() == false) {
					flag = 1;
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			if(flag == 1) {
				Alert a = new Alert(AlertType.ERROR);
				a.setContentText("Cart Can't Be Empty!");
				a.showAndWait();
			}
			else {
				getCartBeforeCheckout();
				
				for (int i = 0; i < checkout.size(); i++) {
					cc.removeCart(checkout.get(i).getMotorID());
				}
				
				cc.checkout();
				refreshTable();		
			}
		});
	}
	
	public void getCartBeforeCheckout() {
		String query = String.format("SELECT * FROM Cart\n"
				+ "LEFT JOIN Motorcycle ON Cart.MotorcycleID = Motorcycle.MotorcycleID\n" 
				+ "WHERE Cart.UserID = '%s'", AuthController.tempUserID); 
		connect.rs = connect.execQuery(query);
	
		try {
			while(connect.rs.next()) {
				String motorId = connect.rs.getString("MotorcycleID");
				String motorName = connect.rs.getString("MotorcycleName");
				Integer quantity = connect.rs.getInt("Quantity");
				String motorPrice = connect.rs.getString("MotorcyclePrice");
				checkout.add(new Cart(motorId, AuthController.tempUserID, motorName, motorPrice, quantity));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public CartPage() {
		initialization();
		setPosition();
		refreshTable();
		setEventHandler();
		this.setCenter(cartMotorBorder);
	}

}
