package Controller;

import java.sql.SQLException;

import Connect.Connect;
import Model.User;

public class AuthController {
	Connect connect = Connect.getInstance();
	public static User authUser = new User();
	public static String tempUserID;
	public static String tempUserRole;
	public static String tempUserName;
	
	public void Register(String id, String username, String password, String gender, String address, String role) {
		String query = "INSERT INTO User "
				+ "VALUES ('"+ id +"', '"+ username +"', '"+ password +"', '"+ gender +"',  '"+ address +"', '"+ role +"')";
		connect.execUpdate(query);
	}
	
	public boolean Login(String username, String password) {
		String query = "SELECT * FROM User WHERE UserName = '"+username+"' AND UserPassword = '"+password+"' ";
		
		connect.rs = connect.execQuery(query);
		
		String tempUsername, tempPassword;
		
		try {
			while(connect.rs.next()) {
				tempUsername = connect.rs.getString("UserName");
				tempPassword = connect.rs.getString("UserPassword"); 
				
				if (tempUsername.equals(username) && tempPassword.equals(password)) {
					authUser.setID(connect.rs.getString("UserID"));
					authUser.setRole(connect.rs.getString("UserRole"));
					return true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}

}
