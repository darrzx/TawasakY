package Model;

public class TransactionHeader {
	private String transactionId;
	private String userId;
	private String userName;
	private String transactionDate;
	
	public TransactionHeader(String transactionId, String userId, String userName, String transactionDate) {
		super();
		this.transactionId = transactionId;
		this.userId = userId;
		this.userName = userName;
		this.transactionDate = transactionDate;
	}
	
	public TransactionHeader() {
		
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}
}
