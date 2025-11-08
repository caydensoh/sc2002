public abstract class User {

	private String userID;
	private String name;
	private String password;
	private filterSetting filterSettings;

	//do we need to add constructor 
	public User(String userID, String name, String password) {
		this.userID=userID;
		this.name=name;
		this.password=password;
	}

	public Boolean login() {
		// TODO - implement User.login
		throw new UnsupportedOperationException();
	}

	public void logout() {
		// TODO - implement User.logout
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param oldPass
	 * @param newPass
	 */
	public Boolean changePassword(String oldPass, String newPass) {
		// TODO - implement User.changePassword
		
		throw new UnsupportedOperationException();
	}

}