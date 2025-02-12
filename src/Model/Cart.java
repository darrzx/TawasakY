package Model;

public class Cart {
	private String motorID;
	private String userID;
	private String motorName;
	private String motorPrice;
	private Integer quantity;
	
	public Cart(String motorID, String userID, String motorName, String motorPrice, Integer quantity) {
		super();
		this.motorID = motorID;
		this.userID = userID;
		this.motorName = motorName;
		this.motorPrice = motorPrice;
		this.quantity = quantity;
	}

	public String getMotorID() {
		return motorID;
	}

	public void setMotorID(String motorID) {
		this.motorID = motorID;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getMotorName() {
		return motorName;
	}

	public void setMotorName(String motorName) {
		this.motorName = motorName;
	}

	public String getMotorPrice() {
		return motorPrice;
	}

	public void setMotorPrice(String motorPrice) {
		this.motorPrice = motorPrice;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
}
