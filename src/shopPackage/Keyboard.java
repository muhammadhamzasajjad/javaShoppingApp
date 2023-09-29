package shopPackage;

public class Keyboard extends Product{
	
	private KeyboardType type;
	private KeyboardLayout layout;
	
	
	public Keyboard(int barcode, String brand, String colour, Connectivity connectivity, int quantityInStock,
			double originalPrice, double retailPrice, KeyboardType type, KeyboardLayout layout) {
		super(barcode, brand, colour, connectivity, quantityInStock, originalPrice, retailPrice);
		this.type=type;
		this.layout=layout;
		
	}


	public KeyboardType getType() {
		return type;
	}


	public KeyboardLayout getLayout() {
		return layout;
	}
	
	public Keyboard clone() {
		return new Keyboard(this.getBarcode(), this.getBrand(), this.getColour(), this.getConnectivity(), this.getQuantityInStock(), this.getOriginalPrice(), this.getRetailPrice(), this.type, this.layout);
	}
	
	public String toString() {
		return String.format("%06d",super.getBarcode()) + ", keyboard, " + this.type + ", " + super.toString() + ", " + this.layout;
	}
	
	
}
