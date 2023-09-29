package users;

public class Address {
	private int houseNumber;
	private String postcode;
	private String city;
	
	public Address(int houseNumber,String postCode,String city) {
		this.houseNumber=houseNumber;
		this.postcode=postCode;
		this.city=city;
	}
	
	public int getHouseNumber() {
		return houseNumber;
	}
	
	public String getPostcode() {
		return postcode;
	}
	
	public String getCity() {
		return city;
	}
	
	public Address clone() {
		return new Address(this.houseNumber,this.postcode,this.city);
	}
	
}
