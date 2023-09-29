package shopPackage;

public class Mouse extends Product{
	
	private MouseType type;
	private int buttons;
	
	public Mouse(int barcode, String brand, String colour, Connectivity connectivity, int quantityInStock,
			double originalPrice, double retailPrice, MouseType type, int numberOfButtons) {
		super(barcode, brand, colour, connectivity, quantityInStock, originalPrice, retailPrice);
		this.type=type;
		this.buttons=numberOfButtons;
	}

	public MouseType getType() {
		return type;
	}

	public int getButtons() {
		return buttons;
	}
	
	//returns a copy of this object
	public Mouse clone() {
		return new Mouse(this.getBarcode(), this.getBrand(), this.getColour(), this.getConnectivity(), this.getQuantityInStock(), this.getOriginalPrice(), this.getRetailPrice(), this.type, this.buttons);
	}
	
	public String toString() {
		return String.format("%06d",super.getBarcode()) + ", mouse, " + this.type + ", " + super.toString() + ", " + this.buttons;
	}
}
