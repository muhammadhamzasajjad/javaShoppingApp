package users;

import java.util.List;

import shopPackage.BasketItem;

public abstract class User {
	private int userId;
	private String username;
	private String surname;
	private Address address;
	
	public User(int userId,String username,String surname,int houseNumber,String postcode,String city) {
		this.userId=userId;
		this.username=username;
		this.surname=surname;
		this.address=new Address(houseNumber, postcode, city);
		
	}

	public int getUserId() {
		return userId;
	}

	public String getUsername() {
		return username;
	}

	public String getSurname() {
		return surname;
	}

	public Address getAddress() {
		return address.clone();
	}
	
	public String toString() {
		String separator=", ";
		return userId+separator+username+separator+surname+address.getHouseNumber()+separator+address.getPostcode();
	}
	
	
	
	
	
}
