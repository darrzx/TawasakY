package Model;

public class TransactionDetail {
	private String transactionId;
	private String motorId;
	private String motorName;
	private String motorPrice;
	private Integer quantity;
	
	public TransactionDetail(String transactionId, String motorId, String motorName, String motorPrice,
			Integer quantity) {
		super();
		this.transactionId = transactionId;
		this.motorId = motorId;
		this.motorName = motorName;
		this.motorPrice = motorPrice;
		this.quantity = quantity;
	}
	
	public TransactionDetail() {
		
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getMotorId() {
		return motorId;
	}

	public void setMotorId(String motorId) {
		this.motorId = motorId;
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
