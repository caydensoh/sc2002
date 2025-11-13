public abstract class User {

	private String userID;
	private String name;
	private String password;
	private filterSetting filterSettings;
	private boolean isLoggedIn = false; 

	
	public User(String userID, String name, String password) {
		this.userID=userID;
		this.name=name;
		this.password=password; // password is password by default
	}

	public Boolean login(String in) { //need to update class diagram
		// TODO - implement User.login
		if (in == this.password){
			isLoggedIn = true;
			System.out.println("You are now logged in.");
			return true;
		}
		System.out.println("Wrong password. Please try again.");
		
		throw new UnsupportedOperationException();
	}

	public void logout() {
		// TODO - implement User.logout
		isLoggedIn = false;
		System.out.println("You are now logged out.");
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param oldPass
	 * @param newPass
	 */
	public Boolean changePassword(String oldPass, String newPass) {
		// TODO - implement User.changePassword
		if (this.password == oldPass){
			this.password = newPass;
			System.out.println("Password succesfully updated.");
		}else if (this.password != oldPass){
			System.out.println("Wrong password. Please  try again.");
		}

		
		throw new UnsupportedOperationException();
	}

}