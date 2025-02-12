package Controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import Connect.Connect;
import View.CartPage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class CartController {
	Connect connect = Connect.getInstance();
	Random rand = new Random();

	public void addToCart(String motorId, String userId, Integer quantity) {
		String query1 = "SELECT * FROM Cart WHERE UserID = '"+AuthController.tempUserID+"'";
		connect.rs = connect.execQuery(query1);

		int flag = 0;
		Integer newQty = 0;

		try {
			while(connect.rs.next()){
				if(connect.rs.getString("MotorcycleID").equals(motorId)) {
					newQty = quantity + connect.rs.getInt("Quantity");
					String query2 = "UPDATE Cart SET Quantity = ? WHERE MotorcycleID = ?";
					PreparedStatement ps1 = connect.prepareStatement(query2);

					ps1.setInt(1, newQty);
					ps1.setString(2, connect.rs.getString("MotorcycleID"));
					ps1.executeUpdate();
					flag = 1;
					break;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (flag != 1) {			
			String query = "INSERT INTO Cart "
					+ "VALUES ('"+ userId +"', '"+ motorId +"', '"+ quantity +"')";
			connect.execUpdate(query);	
		}
	}

	public void removeCart(String motorId) {
		String query = "DELETE FROM Cart where MotorcycleID = ?";
		PreparedStatement ps = connect.prepareStatement(query);

		try {
			ps.setString(1, motorId);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateCart(String motorId, String userId, Integer quantity) {
		String query1 = "SELECT * FROM Cart WHERE UserID = '"+AuthController.tempUserID+"'";
		connect.rs = connect.execQuery(query1);

		Integer newQty = 0;

		try {
			while(connect.rs.next()){
				if(connect.rs.getString("MotorcycleID").equals(motorId)) {
					newQty = quantity;
					String query2 = "UPDATE Cart SET Quantity = ? WHERE MotorcycleID = ?";
					PreparedStatement ps1 = connect.prepareStatement(query2);

					ps1.setInt(1, newQty);
					ps1.setString(2, connect.rs.getString("MotorcycleID"));
					ps1.executeUpdate();
					break;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public String randomID() {
		String ID = "";
		ID += "TR";
		ID += (char) (rand.nextInt(9) + '0');
		ID += (char) (rand.nextInt(9) + '0');
		ID += (char) (rand.nextInt(9) + '0');
		
		return ID;
	}
	
	public void checkout() {
		String id = randomID();
		String queryth = "INSERT INTO `TransactionHeader`(`TransactionID`, `UserID`, `TransactionDate`) VALUES ('"+id+"', '"+AuthController.tempUserID+"', now())";
		connect.execUpdate(queryth);

		for (int i = 0; i < CartPage.checkout.size(); i++) {
			String queryDetail = "INSERT INTO `TransactionDetail`(`TransactionID`, `MotorcycleID`, `Quantity`) VALUES ('"+id+"', '"+CartPage.checkout.get(i).getMotorID()+"', '"+CartPage.checkout.get(i).getQuantity()+"')";
			connect.execUpdate(queryDetail);
		}
		
		Alert a = new Alert(AlertType.INFORMATION);
		a.setContentText("Checkout Success!");
		a.showAndWait();
		
		CartPage.checkout.clear();
		CartPage bu = new CartPage();
		bu.refreshTable();
	}
}
