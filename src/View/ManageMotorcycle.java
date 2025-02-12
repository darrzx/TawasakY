package View;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

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
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ManageMotorcycle extends BorderPane{
	BorderPane manageMotorBorder;
	GridPane manageMotorGrid;

	VBox vertikalManageMotorTop;
	HBox horizontalManageMotor;
	TableView<Motorcycle> motorTable;
	ArrayList<Motorcycle> motors;
	
	Label motorNameManageLabel, motorPriceManageLabel, motorQuantityManageLabel;
	TextField motorNameManageTF, motorPriceManageTF;
	Spinner<Integer> motorStockManageSpinner;
	Button deleteButton, updateButton, addButton;
	
	Connect connect = Connect.getInstance();
	Random rand = new Random();

	String tempMotorId = "";
	
	public void initialization() {
		manageMotorGrid = new GridPane();
		manageMotorBorder = new BorderPane();

		vertikalManageMotorTop = new VBox();
		horizontalManageMotor = new HBox();
		
		motorTable = new TableView<Motorcycle>();
		motorTable.setMaxHeight(300);

		motorNameManageLabel = new Label("Motorcycle Name");
		motorPriceManageLabel = new Label("Motorcycle Price");
		motorQuantityManageLabel = new Label("Motorcycle Quantity");
		
		motorNameManageTF = new TextField();
		motorPriceManageTF = new TextField();
		motorStockManageSpinner = new Spinner<>();
		
		deleteButton = new Button("Delete");
		updateButton = new Button("Update");
		addButton = new Button("Add");
		
		SpinnerValueFactory<Integer> quantitySpinner = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0);
		motorStockManageSpinner.setValueFactory(quantitySpinner);
		
		motorNameManageTF.setMinWidth(250);
		motorPriceManageTF.setMinWidth(250);
		motorStockManageSpinner.setMinWidth(250);
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
	
		manageMotorGrid.setAlignment(Pos.CENTER);
		manageMotorGrid.setHgap(10);
		manageMotorGrid.setVgap(20);
		manageMotorGrid.setPadding(new Insets(35, 55, 35, 55));

		manageMotorGrid.add(motorNameManageLabel, 0, 0);
		manageMotorGrid.add(motorPriceManageLabel, 0, 1);
		manageMotorGrid.add(motorQuantityManageLabel, 0, 2);
		manageMotorGrid.add(motorNameManageTF, 1, 0);
		manageMotorGrid.add(motorPriceManageTF, 1, 1);
		manageMotorGrid.add(motorStockManageSpinner, 1, 2);
		horizontalManageMotor.getChildren().addAll(deleteButton, updateButton, addButton);
		
		vertikalManageMotorTop.getChildren().addAll(motorTable, manageMotorGrid);
		manageMotorBorder.setCenter(vertikalManageMotorTop);
		manageMotorBorder.setBottom(horizontalManageMotor);

		horizontalManageMotor.setSpacing(100);
		horizontalManageMotor.setAlignment(Pos.CENTER);
		horizontalManageMotor.setPadding(new Insets(0, 0, 50, 0));
			
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
				
				motorNameManageTF.setText(motor.getName().toString());	
				motorPriceManageTF.setText(motor.getPrice().toString());
				motorStockManageSpinner.getValueFactory().setValue(motor.getStock());
				
				tempMotorId = motor.getID();
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
		motorNameManageTF.setText("");	
		motorPriceManageTF.setText("");
		motorStockManageSpinner.getValueFactory().setValue(0);
	}
	
	public String randomID() {
		String ID = "";
		ID += "MT";
		ID += (char) (rand.nextInt(9) + '0');
		ID += (char) (rand.nextInt(9) + '0');
		ID += (char) (rand.nextInt(9) + '0');
		
		return ID;
	}
	
	public void setEventHandler() {
		deleteButton.setOnAction(e->{
			TableSelectionModel<Motorcycle> motorSelectionModel = motorTable.getSelectionModel();
			motorSelectionModel.setSelectionMode(SelectionMode.SINGLE);
			
			if (motorSelectionModel.isEmpty()) {
				Alert a = new Alert(AlertType.ERROR);
				a.setContentText("Choose Item First!");
				a.showAndWait();
			}
			else {								
				String query = "DELETE FROM Motorcycle WHERE MotorcycleID = '"+tempMotorId+"'";
				connect.execUpdate(query);
				
				Alert b = new Alert(AlertType.INFORMATION);
				b.setContentText("Succesfully Remove Selected Item!");
				b.showAndWait();
				refreshTable();
				refreshValues();
			}
		});
		
		updateButton.setOnAction(e->{
			Alert a = new Alert(AlertType.ERROR);
			Alert b = new Alert(AlertType.INFORMATION);
			
			TableSelectionModel<Motorcycle> motorSelectionModel = motorTable.getSelectionModel();
			motorSelectionModel.setSelectionMode(SelectionMode.SINGLE);
			
			if (motorSelectionModel.isEmpty()) {
				a.setContentText("Choose Item First!");
				a.showAndWait();
			}
			else if (motorNameManageTF.getText().isEmpty()) {
				a.setContentText("Motorcycle Name must be filled!");
				a.showAndWait();
			}
			else if (motorPriceManageTF.getText().isEmpty()) {
				a.setContentText("Motorcycle Price must be filled!");
				a.showAndWait();
			}
			else if (motorStockManageSpinner.getValue() <= 0) {
				a.setContentText("Motorcycle Stock must More Than 0!");
				a.showAndWait();
			}
			else {
				String name = motorNameManageTF.getText();
				String price = motorPriceManageTF.getText();
				Integer stock = motorStockManageSpinner.getValue();
				
				String query = "UPDATE `Motorcycle` SET `MotorcycleName`='"+name+"',`MotorcycleStock`='"+stock+"',`MotorcyclePrice`='"+price+"' WHERE MotorcycleID = '"+tempMotorId+"'";
				connect.execUpdate(query);
				
				b.setContentText("Motorcycle Succesfully Updated!");
				b.showAndWait();
				
				refreshTable();
				refreshValues();
			}
		});
		
		addButton.setOnAction(e->{
			Alert a = new Alert(AlertType.ERROR);
			Alert b = new Alert(AlertType.INFORMATION);

			if (motorNameManageTF.getText().isEmpty()) {
				a.setContentText("Motorcycle Name must be filled!");
				a.showAndWait();
			}
			else if (motorPriceManageTF.getText().isEmpty()) {
				a.setContentText("Motorcycle Price must be filled!");
				a.showAndWait();
			}
			else if (motorStockManageSpinner.getValue() <= 0) {
				a.setContentText("Motorcycle Stock must More Than 0!");
				a.showAndWait();
			}
			else {
				String id = randomID();
				String name = motorNameManageTF.getText();
				String price = motorPriceManageTF.getText();
				Integer stock = motorStockManageSpinner.getValue();
								
				String query = "INSERT INTO `Motorcycle`(`MotorcycleID`, `MotorcycleName`, `MotorcycleStock`, `MotorcyclePrice`) VALUES ('"+id+"','"+name+"','"+stock+"','"+price+"')";
				connect.execUpdate(query);
				
				b.setContentText("Motorcycle Succesfully Inserted!");
				b.showAndWait();
				
				refreshTable();
				refreshValues();
			}
		});
	}

	public ManageMotorcycle() {
		initialization();
		setPosition();
		setEventHandler();
		this.setCenter(manageMotorBorder);
	}

}
