package View;


import java.sql.SQLException;
import java.util.ArrayList;

import Connect.Connect;
import Controller.AuthController;
import Model.TransactionDetail;
import Model.TransactionHeader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;


public class TransactionPage extends BorderPane {
	BorderPane borderContainer; 
	TableView<TransactionHeader> trHeaderTable;
	TableView<TransactionDetail> trDetailTable;
	ArrayList<TransactionHeader> th;
	ArrayList<TransactionDetail> td;
	ScrollPane sp, sp1;
	
	Connect connect = Connect.getInstance();
	
	public void initialization() {
		borderContainer = new BorderPane();
		trHeaderTable = new TableView<>();
		trHeaderTable.setMaxHeight(250);

		trDetailTable = new TableView<>();
		trDetailTable.setMaxHeight(280);
		
		th = new ArrayList<>();
		td = new ArrayList<>();
		sp = new ScrollPane();
		sp1 = new ScrollPane();
	}
	
	public void setPosition() {
		TableColumn<TransactionHeader, String> thIdCol = new TableColumn<>("Transaction ID");
		thIdCol.setCellValueFactory(new PropertyValueFactory<>("transactionId"));
		
		TableColumn<TransactionHeader, String> trDateCol = new TableColumn<>("Transaction Date");
		trDateCol.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));

		
		TableColumn<TransactionDetail, String> motorIdCol = new TableColumn<>("Motorcycle ID");
		motorIdCol.setCellValueFactory(new PropertyValueFactory<>("motorId"));
		
		TableColumn<TransactionDetail, String> motorNameCol = new TableColumn<>("Motorcycle Name");
		motorNameCol.setCellValueFactory(new PropertyValueFactory<>("motorName"));
		
		TableColumn<TransactionDetail, String> motorPriceCol = new TableColumn<>("Motorcycle Price");
		motorPriceCol.setCellValueFactory(new PropertyValueFactory<>("motorPrice"));
		
		TableColumn<TransactionDetail, Integer> quantityCol = new TableColumn<>("Quantity");
		quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		
	
		trHeaderTable.getColumns().addAll(thIdCol, trDateCol);
		trDetailTable.getColumns().addAll(motorIdCol, motorNameCol, motorPriceCol, quantityCol);
		
		trHeaderTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		trDetailTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		
		TableViewSelectionModel<TransactionHeader> selectionModel = trHeaderTable.getSelectionModel();
		
		selectionModel.setSelectionMode(SelectionMode.SINGLE);
		
		selectionModel.selectedItemProperty().addListener((obs, oldSelection, newSelection)->{
			getTrDetail(newSelection.getTransactionId());
			ObservableList<TransactionDetail> regObs1 = FXCollections.observableArrayList(td);
			trDetailTable.setItems(regObs1);
		});
		
		borderContainer.setTop(trHeaderTable);
		borderContainer.setMargin(trDetailTable, new Insets(50, 0, 0, 0));
		borderContainer.setCenter(trDetailTable);
		
		refreshTable();
	}
	
	private void getTrHeader() {
		th.clear();
		
		String userId = AuthController.tempUserID;
		String query = "";
		
		query = "SELECT * FROM TransactionHeader th JOIN User u ON th.UserID = u.UserID WHERE u.UserID = '"+userId+"'";
		connect.rs = connect.execQuery(query);
		
		try {
			while(connect.rs.next()) {
				TransactionHeader th1 = new TransactionHeader();
				th1.setTransactionId(connect.rs.getString("TransactionID"));
				th1.setTransactionDate(connect.rs.getString("TransactionDate"));
				th1.setUserName(connect.rs.getString("u.UserName"));
				th1.setUserId(connect.rs.getString("u.UserID"));
				th.add(th1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
//	
	private void getTrDetail(String thId) {
		td.clear();
		
		String query = "SELECT * FROM TransactionDetail td JOIN TransactionHeader th ON th.TransactionID = td.TransactionID JOIN Motorcycle m ON m.MotorcycleID = td.MotorcycleID WHERE th.TransactionID = '"+thId+"'";
		connect.rs = connect.execQuery(query);
		
		try {
			while(connect.rs.next()) {
				TransactionDetail td1 = new TransactionDetail();
				td1.setTransactionId(connect.rs.getString("TransactionID"));
				td1.setMotorId(connect.rs.getString("MotorcycleID"));
				td1.setMotorName(connect.rs.getString("m.MotorcycleName"));
				td1.setQuantity(connect.rs.getInt("Quantity"));
				String motorPrice = connect.rs.getString("m.MotorcyclePrice");
				td1.setMotorPrice(motorPrice);
				td.add(td1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void refreshTable() {
		getTrHeader();
		ObservableList<TransactionHeader> regObs = FXCollections.observableArrayList(th);
		trHeaderTable.setItems(regObs);
	}
	
	public TransactionPage() {
		initialization();
		setPosition();
		this.setCenter(borderContainer);
	}
	
}
