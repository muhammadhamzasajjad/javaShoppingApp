/*
 *
 */

package shopPackage;

import java.util.Arrays;

public class BasketItem {
	
	private Product product;
	private int quantity;
	
	
	public BasketItem(Product product,int quantity) {
		this.product=product;
		this.quantity=quantity;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	public Product getProduct() {
		return product;
	}
	
	public double getPrice() {
		return product.getRetailPrice()*quantity;
	}
	
	public boolean isAvailable() {
		return product.getQuantityInStock()>=this.quantity;
	}
	
	public void setQuantity(int quantity) throws Exception {
		/*if(product.getQuantityInStock()<quantity) {
			throw new Exception("Selected product is not available in required quantity");
		}*/
		this.quantity=quantity;
	}
	
	public BasketItem clone() {
		return new BasketItem(product.clone(), quantity);
	}
	
	
	
	
	
}
