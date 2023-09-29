package users;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import shopPackage.BasketItem;
import shopPackage.Product;
import shopPackage.Shop;

public class Customer extends User{

	private HashMap<Integer, BasketItem> basket;
	
	public Customer(int userId, String username, String surname, int houseNumber, String postcode, String city) {
		super(userId, username, surname, houseNumber, postcode, city);
		
		basket=new HashMap<Integer, BasketItem>();
	}
	
	public String toString() {
		return super.toString()+", customer";
	}
	
	public void addToBasket(Shop shop,int barcode,int quantity) throws Exception{
		//if a product if in the shop and it is avalable in the desired quantity then add it to the basket
		Product product=shop.getProduct(barcode);
		if(product==null) {
			throw new Exception("No such product found");
		}
		
		if(product.getQuantityInStock()<quantity) {
			throw new Exception("The product is not available in desired quantity");
		}
		
		basket.put(barcode, new BasketItem(product, quantity));
			
	}
	
	public boolean isAvailable(int Barcode) {
		BasketItem item=basket.get(Barcode);
		if(item==null) {
			return false;
		}
		
		return item.isAvailable();
	}
	
	public void saveBasket(Shop shop) {
		//Saving the basket items. The basket is now empty
		shop.logBasketOperations(basket.values(), this, true);
		basket=new HashMap<Integer, BasketItem>();
	}
	
	public void cancelBasket(Shop shop) {
		//saving the operation. The basket is now empty
		shop.logBasketOperations(basket.values(), this, false);
		basket=new HashMap<Integer, BasketItem>();
		
	}
	
	public HashMap<Integer, BasketItem> buyAllWithPaypal(Shop shop,String emailAddress)throws Exception {
		//buying the items from the shop by paypal and making the basket empty. In the returning all the bought items so the customer can see them if needed.
		HashMap<Integer, BasketItem> items=shop.buyProductsByPaypal(this, basket.values(), emailAddress);
		basket=new HashMap<Integer, BasketItem>();
		
		return items;
	}
	
	public HashMap<Integer, BasketItem> buyAllWithCreditCard(Shop shop,String cardNo,String securityCode)throws Exception{
		//buying the items from the shop by credit and making the basket empty. In the returning all the bought items so the customer can see them if needed.
		HashMap<Integer, BasketItem> items=shop.buyProductsByCreditCard(this, basket.values(), cardNo, securityCode);
		basket=new HashMap<Integer, BasketItem>();
		return items;
	}
	
	public Collection<BasketItem> cloneBasketItems(){
		return basket.values();
	}
	
	public int getBasketSize() {
		return basket.size();
	}
	
	public double calculateBill() {
		double total=0;
		//if the product is available then add it's price to the total because otherwise the user 
		for(BasketItem item:basket.values()) {
			if(item.getProduct().getQuantityInStock()>=item.getQuantity()) {
				total+=item.getPrice();
			}
		}
		
		return total;
	}
	
}
