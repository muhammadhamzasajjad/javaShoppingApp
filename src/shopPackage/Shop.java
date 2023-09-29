package shopPackage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;


import users.AdminUser;
import users.Customer;
import users.User;

public class Shop {
	private HashMap<Integer, Product> products;
	
	public Shop() {
		products=new HashMap<Integer, Product>();
		populateProducts();
		
	}
	
	public Product getProduct(int barcode) {
		return products.get(barcode);
	}
	
	public void addProduct(Product product,User user)throws Exception {
		//if a product with the same barcode is found or the user is not an Admin then I can't add the product in the shop
		if (products.get(product.getBarcode())!=null) {
			throw new Exception("A product with the same barcode already exists");
		}
		
		if(!(user instanceof AdminUser)) {
			throw new Exception("Only a user admin can add a product in the shop");
		}
		
		products.put(new Integer(product.getBarcode()), product);
		writeFile("files/Stock.txt", product.toString()+"\n",true);
	}
	
	private HashMap<Integer,BasketItem> buyProducts(Customer customer, Collection<BasketItem> basketItems,String paymentMethod) {
		
		HashMap<Integer,BasketItem> boughtItems=new HashMap<Integer, BasketItem>();
		String lines="";
		
		for(BasketItem basketItem : basketItems) {
			//if a product (namely originalProduct) is found in the products list that has the same barcode as the product in the basket item and is available then buy it
			Product originalProduct=this.products.get(basketItem.getProduct().getBarcode());
			
			if (originalProduct != null && originalProduct.getQuantityInStock() >= basketItem.getQuantity()) {
				originalProduct.decrementQuantityInStock(basketItem.getQuantity());
				boughtItems.put(originalProduct.getBarcode(), basketItem.clone());
				
				//concatenating a new log
				lines+=customer.getUserId()+", "+customer.getAddress().getPostcode()+", "+originalProduct.getBarcode()+", "+originalProduct.retailPriceToString()+", "+basketItem.getQuantity()+", purchased, "+paymentMethod+", "+ getDateToString()+"\n";
			}
		}
		
		
		writeFile("files/ActivityLog.txt", lines,true);
		//saving the products with new stockQuantity in file
		writeFile("files/Stock.txt", productsToString(), false);
		return boughtItems;
	}
	
	public HashMap<Integer, BasketItem> buyProductsByPaypal(Customer customer, Collection<BasketItem> basketItems, String email)throws Exception{
		//validating email
		if(!email.contains("@")||!email.contains(".")||email.lastIndexOf(".")<email.indexOf("@")) {
			throw new Exception("The email is invalid");
		}
		
		return buyProducts(customer,basketItems,"PayPal");
	}
	
	public HashMap<Integer, BasketItem> buyProductsByCreditCard(Customer customer, Collection<BasketItem> basketItems, String cardNo,String securityCode)throws Exception{
		//I am using string type for cardNo because card numbers can start from 0 digit the same goes for securityCode
		if(cardNo.length()!=16||securityCode.length()!=3) {
			throw new Exception("The security code and/or card no. are invalid");
		}
		
		return buyProducts(customer, basketItems, "Credit Card");
	}
	
	public List<Product> cloneProducts(User user){
		List<Product> prodlist=new ArrayList<Product>();
		
		for(Product product:this.products.values()) {
			//if onlyAvalableProds is set to false i will be returning all products
			//and if it is true then I only want the available products so I check if the quantity in stock is >0
			if(!(user instanceof Customer)|| product.getQuantityInStock()>0)
				prodlist.add(product.clone());
		}
		Collections.sort(prodlist);
		return prodlist;
	}
	
	public List<Product> searchProducts(User user,String brand, boolean matchBrand, KeyboardLayout layout, boolean matchLayout){
		List<Product> foundProducts=new ArrayList<Product>();
		for (Product product : this.products.values()) {
			//using propositional logic I simply translate the following formula into code
			//(matchBrand->productBrand==brand) AND (matchlayout->product is a Keyboard AND productLayout==layout) AND (user is a Customer -> productQuantity>0)
			if( (!matchBrand||product.getBrand().equals(brand))&&(!matchLayout||(product instanceof Keyboard && (((Keyboard) product).getLayout().equals(layout) ) ) ) &&(!(user instanceof Customer)||product.getQuantityInStock()>0) ) {
				foundProducts.add(product.clone());
			}
		}
		
		Collections.sort(foundProducts);
		
		return foundProducts;
	}
	
	public String productsToString() {
		String productString="";
		
		for(Product product:products.values()) {
			productString+=product.toString()+"\n";
		}
		
		return productString;
	}
	
	public Collection<String> getBrandsCollection() {
		HashSet<String> brandSet=new HashSet<String>();
		
		for (Product product : products.values()) {
			brandSet.add(product.getBrand());
		}
		
		return brandSet;
	}
	
	public static List<User> getUsersList(){
		List<User> users= new ArrayList<User>();
		List<String[]> userRecords=readfile("files/userAccounts.txt");
		for (String[] userRecord : userRecords) {
			
			//if when reading a record from the file, an excpetion is launched which means that the data format is not respected for that record then I simply ignore that record.
			try {
				if(userRecord[6].trim().equals("admin")) {
					users.add(new AdminUser(Integer.parseInt(userRecord[0].trim()), userRecord[1].trim(), userRecord[2].trim(), Integer.parseInt(userRecord[3].trim()), userRecord[4].trim(), userRecord[5].trim()));
				}
				else {
					users.add(new Customer(Integer.parseInt(userRecord[0].trim()), userRecord[1].trim(), userRecord[2].trim(), Integer.parseInt(userRecord[3].trim()), userRecord[4].trim(), userRecord[5].trim()));
				}
				
			} catch (Exception e) {
				
			}
		}
		
		
		return users;
	}
	
	public void logBasketOperations(Collection<BasketItem> basketItems,Customer customer,boolean isSaveOperation) {
		String operation="cancelled";
		
		if (isSaveOperation) {
			operation="saved";
		}
		
		String lines="";
		
		for(BasketItem basketItem : basketItems) {
			Product product=basketItem.getProduct();
			lines+=customer.getUserId()+", "+customer.getAddress().getPostcode()+", "+product.getBarcode()+", "+product.retailPriceToString()+", "+basketItem.getQuantity()+", "+operation+", , "+getDateToString()+"\n";
			
		}
		
		writeFile("files/ActivityLog.txt", lines, true);
	}
	
	
	
	
	private static List<String[]> readfile(String filepath){
		File file=new File(filepath);
		List<String[]> records=new ArrayList<String[]>();
		
		try {
			Scanner fileScanner=new Scanner(file);
			//reading every line and splitting it in array of strings by ','
			while(fileScanner.hasNext()) {
				records.add(fileScanner.nextLine().split(", "));
			}
			
		} catch (Exception e) {
			System.err.println("file" + filepath + " not found");
		}
		
		return records;
	}
	
	private static void writeFile(String filename,String arg, boolean append) {
		FileWriter fileWriter;
		
		try {
			fileWriter = new FileWriter(new File(filename),append);
			//if the argument append is false then the function overwrites the file otherwise it appends the file.
			fileWriter.append(arg);
			fileWriter.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	private String getDateToString() {
		return new SimpleDateFormat("dd-MM-yyyy").format(new Date());
	}
	
	
	private static boolean isInteger(String arg) {
		//returning true if the parseInt Converts the string to int successfully, false otherwise
		try {
			Integer.parseInt(arg);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	
	
	
	private void populateProducts() {
		List<String[]> productrecords=readfile("files/Stock.txt");
		
		for (String[] prodrecord : productrecords) {
			
			//if when reading a record from the file, an excpetion is launched which means that the data format is not respected for that record then I simply ignore that record.
			//for this reason the catch clause is empty
			try {
				
				if(prodrecord[1].trim().toLowerCase().equals("keyboard")) {
					
					Keyboard keyboard=new Keyboard(Integer.parseInt(prodrecord[0]), prodrecord[3].trim(), prodrecord[4].trim(), Connectivity.valueOf(prodrecord[5].trim()), Integer.parseInt(prodrecord[6].trim()), Double.parseDouble(prodrecord[7].trim()), Double.parseDouble(prodrecord[8].trim()), KeyboardType.valueOf(prodrecord[2].trim()), KeyboardLayout.valueOf(prodrecord[9].trim()));
					products.put(keyboard.getBarcode(),keyboard);
				}
				else {
					Mouse mouse=new Mouse(Integer.parseInt(prodrecord[0]), prodrecord[3].trim(), prodrecord[4].trim(), Connectivity.valueOf(prodrecord[5].trim()), Integer.parseInt(prodrecord[6].trim()), Double.parseDouble(prodrecord[7].trim()), Double.parseDouble(prodrecord[8].trim()), MouseType.valueOf(prodrecord[2].trim()), Integer.parseInt(prodrecord[9].trim()));
					products.put(mouse.getBarcode(), mouse);
				}
				
			} catch (Exception e) {
				
			}
		}
		
	}
	
	
}
