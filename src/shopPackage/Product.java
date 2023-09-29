package shopPackage;



public abstract class Product implements Comparable<Product>{
	private int barcode;
	private String brand;
	private String colour;
	private Connectivity connectivity;
	private int quantityInStock;
	private double originalPrice;
	private double retailPrice;
	
	public Product(int barcode, String brand, String colour, Connectivity connectivity, int quantityInStock, double originalPrice, double retailPrice) {
		
		this.barcode = barcode;
		this.brand = new String(brand);
		this.colour = new String(colour);
		this.connectivity=connectivity;
		this.quantityInStock = quantityInStock;
		this.originalPrice = roundDouble(originalPrice);
		this.retailPrice = roundDouble(retailPrice);
	}
	
	public abstract Product clone();
	
	public int getBarcode() {
		return barcode;
	}
	
	public String getBrand() {
		return brand;
	}
	
	public String getColour() {
		return colour;
	}
	
	public int getQuantityInStock() {
		return quantityInStock;
	}
	
	public double getOriginalPrice() {
		return originalPrice;
	}
	
	public double getRetailPrice() {
		return retailPrice;
	}
	
	public Connectivity getConnectivity() {
		return connectivity;
	}
	
	//only the classes in the same package (eg. Shop) will be able to change the quantity in stock 
	void decrementQuantityInStock(int decrementValue) {
		if(this.quantityInStock>=decrementValue) {
			quantityInStock-=decrementValue;
		}
	}

	private double roundDouble(double value) {
		//rouding to two decimal places
		return Math.round(value*100.0)/100.0;
	}
	
	public String retailPriceToString() {
		//price to string with two decimal places
		return String.format("%.2f", retailPrice);
	}
	
	public String originalPriceToString() {
		//original price to string with two decimal places
		return String.format("%.2f", originalPrice);
	}
	
	@Override
	public int compareTo(Product p) {
		return p.quantityInStock-this.quantityInStock;
	}
	 
	public boolean equals(Object obj) {
		return obj instanceof Product && ((Product) obj).barcode==this.barcode;
	}
	
	
	
	@Override
	public String toString() {
		String separator=", ";
		
		return brand + separator + colour + separator + connectivity + separator + 
				quantityInStock + separator + String.format("%.2f" ,originalPrice) + separator + String.format("%.2f", retailPrice);
	}
	
	
	
}
