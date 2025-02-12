package View;

import java.sql.SQLException;
import java.util.ArrayList;

import Connect.Connect;
import Controller.AuthController;
import Controller.CartController;
import Model.Motorcycle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableSelectionModel;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class BuyMotorcycle extends BorderPane{
	
	GridPane motorBuyGrid;
	BorderPane buyMotorBorder;
	VBox vertikalBuyMotorTop;
	TableView<Motorcycle> motorTable;
	ArrayList<Motorcycle> motors;
	
	Label motorIDBuyLabel, motorNameBuyLabel, motorPriceBuyLabel, motorQuantityBuyLabel;
	TextField motorIDBuyTF, motorNameBuyTF, motorPriceBuyTF;
	Spinner<Integer> motorQuantityBuySpinner;
	Button addToCartButton;
	
	Connect connect = Connect.getInstance();

	public void initialization() {
		motorBuyGrid = new GridPane();
		buyMotorBorder = new BorderPane();

		vertikalBuyMotorTop = new VBox();
		
		motorTable = new TableView<Motorcycle>();
		motorTable.setMaxHeight(300);

		motorIDBuyLabel = new Label("Motorcycle ID");
		motorNameBuyLabel = new Label("Motorcycle Name");
		motorPriceBuyLabel = new Label("Motorcycle Price");
		motorQuantityBuyLabel = new Label("Motorcycle Quantity");
		
		motorIDBuyTF = new TextField();
		motorIDBuyTF.setEditable(false);
		motorNameBuyTF = new TextField();
		motorNameBuyTF.setEditable(false);
		motorPriceBuyTF = new TextField();
		motorPriceBuyTF.setEditable(false);
		motorQuantityBuySpinner = new Spinner<>();
		
		addToCartButton = new Button("Add to Cart");
		
		SpinnerValueFactory<Integer> quantitySpinner = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE, 1);
		motorQuantityBuySpinner.setValueFactory(quantitySpinner);
		
		motorIDBuyTF.setMinWidth(250);
		motorNameBuyTF.setMinWidth(250);
		motorPriceBuyTF.setMinWidth(250);
		motorQuantityBuySpinner.setMinWidth(250);
	}

	public void setPosition() {
		
		motors = new ArrayList<Motorcycle>();
		
		TableColumn<Motorcycle, String> idCol = new TableColumn<>("Motorcycle ID");
		idCol.setCellValueFactory(new PropertyValueFactory<>("ID"));

		TableColumn<Motorcycle, String> nameCol = new TableColumn<>("Motorcycle Name");
		nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

		TableColumn<Motorcycle, Integer> stockCol = new TableColumn<>("Motorcycle Stock");
		stockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));

		TableColumn<Motorcycle, String> priceCol = new TableColumn<>("Motorcycle Price");
		priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
		
		motorTable.getColumns().addAll(idCol, nameCol, stockCol, priceCol);
		motorTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	
		motorBuyGrid.setAlignment(Pos.CENTER);
		motorBuyGrid.setHgap(10);
		motorBuyGrid.setVgap(20);
		motorBuyGrid.setPadding(new Insets(35, 55, 35, 55));

		motorBuyGrid.add(motorIDBuyLabel, 0, 0);
		motorBuyGrid.add(motorNameBuyLabel, 0, 1);
		motorBuyGrid.add(motorPriceBuyLabel, 0, 2);
		motorBuyGrid.add(motorQuantityBuyLabel, 0, 3);
		motorBuyGrid.add(motorIDBuyTF, 1, 0);
		motorBuyGrid.add(motorNameBuyTF, 1, 1);
		motorBuyGrid.add(motorPriceBuyTF, 1, 2);
		motorBuyGrid.add(motorQuantityBuySpinner, 1, 3);
		motorBuyGrid.add(addToCartButton, 3, 3);
		
		vertikalBuyMotorTop.getChildren().addAll(motorTable, motorBuyGrid);
		buyMotorBorder.setCenter(vertikalBuyMotorTop);
			
		motorTable.setOnMouseClicked(motorcycleTableMouseEvent());
		refreshTable();
	}

	private EventHandler<MouseEvent> motorcycleTableMouseEvent(){
		return new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				// TODO Auto-generated method stub
				TableSelectionModel<Motorcycle> tableSelectionModel = motorTable.getSelectionModel();
				tableSelectionModel.setSelectionMode(SelectionMode.SINGLE);
				
				Motorcycle motor = tableSelectionModel.getSelectedItem();
				
				Alert a = new Alert(AlertType.ERROR);
				Alert b = new Alert(AlertType.INFORMATION);
				
				motorIDBuyTF.setText(motor.getID().toString());	
				motorNameBuyTF.setText(motor.getName());
				motorPriceBuyTF.setText(motor.getPrice().toString());
			}
			
		};
	}
	
	public void getMotorcycle() {
		motors.clear();
		
		String query = "SELECT * FROM Motorcycle";
		connect.rs = connect.execQuery(query);
		
		try {
			while(connect.rs.next()) {
				String motorId = connect.rs.getString("MotorcycleID");
				String motorName = connect.rs.getString("MotorcycleName");
				Integer motorStock = connect.rs.getInt("MotorcycleStock");
				String motorPrice = connect.rs.getString("MotorcyclePrice");
				motors.add(new Motorcycle(motorId, motorName, motorStock, motorPrice));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void refreshTable() {
		getMotorcycle();
		ObservableList<Motorcycle> regObs = FXCollections.observableArrayList(motors);
		motorTable.setItems(regObs);
	}
	
	public void refreshValues() {
		motorIDBuyTF.setText("");	
		motorNameBuyTF.setText("");
		motorPriceBuyTF.setText("");
		motorQuantityBuySpinner.getValueFactory().setValue(1);
	}
	
	public void setEventHandler() {
		addToCartButton.setOnAction(e->{
			TableSelectionModel<Motorcycle> tableSelectionModel = motorTable.getSelectionModel();
			tableSelectionModel.setSelectionMode(SelectionMode.SINGLE);
			
			Motorcycle motor = tableSelectionModel.getSelectedItem();
			
			Alert a = new Alert(AlertType.ERROR);
			Alert b = new Alert(AlertType.INFORMATION);
			
			if (tableSelectionModel.isEmpty()) {
				a.setContentText("Choose Motorcycle first!");
				a.showAndWait();
			}
			else if (motorQuantityBuySpinner.getValue() > motor.getStock()) {
				a.setContentText("Quantity Cannot Be Exceed The Motorcycle stock!");
				a.showAndWait();
			}
			else {
				String motorId = motor.getID();
				String userId = AuthController.tempUserID;
				Integer quantity = motorQuantityBuySpinner.getValue();
								
				CartController cc = new CartController();
				
				cc.addToCart(motorId, userId, quantity);
				
				
				b.setContentText("Succesfully Add To Cart!");
				b.showAndWait();
				refreshTable();
				refreshValues();
			}
		});
	}
	
	public BuyMotorcycle() {
		initialization();
		setPosition();
		refreshTable();
		setEventHandler();
		this.setCenter(buyMotorBorder);
	}
}
