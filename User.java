public abstract class User {

	final private String userID;
	private String name;
	private String password;
	private FilterSetting FilterSettings;

	
	public User(String userID, String name, String password) {
		this.userID=userID;
		this.name=name;
		this.password= (password == null)? "password" : password; // password is password by default
	}

	public String getUserID() {
		return this.userID;
	}

	public String getName() {
		return this.name;
	}

	public String getPassword() {
		return this.password;
	}

	public boolean login(String password) {
		if (password.equals(this.password)){
			System.out.println("You are now logged in.");
			return true;
		}
		System.out.println("Wrong password. Please try again.");
		return false;
	}

	/**
	 * 
	 * @param oldPass
	 * @param newPass
	 */
	public boolean changePassword(String oldPass, String newPass) {
		if (this.password.equals(oldPass)){
			this.password = newPass;
			System.out.println("Password successfully updated.");
			return true;
		}else {
			System.out.println("Wrong password. Please try again.");
			return false;
		}
	}

	public FilterSetting getFilterSettings() {
		if (this.FilterSettings == null) {
			this.FilterSettings = new FilterSetting(null, null, null, null, null, null, false, false);
		}
		return this.FilterSettings;
	}

	public void setFilterSettings(FilterSetting filterSettings) {
		this.FilterSettings = filterSettings;
	}

}