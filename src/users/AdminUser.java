package users;

public class AdminUser extends User{

	public AdminUser(int userId, String username, String surname, int houseNumber, String postcode, String city) {
		super(userId, username, surname, houseNumber, postcode, city);
	}
	
	public String toString() {
		return super.toString()+", admin";
	}
	
}
