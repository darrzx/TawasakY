package Model;

public class Motorcycle {
	
	private String ID;
	private String name;
	private Integer stock;
	private String price;
	
	public Motorcycle(String iD, String name, Integer stock, String price) {
		super();
		ID = iD;
		this.name = name;
		this.stock = stock;
		this.price = price;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}
}
