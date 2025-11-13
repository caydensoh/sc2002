public class CareerCenterStaff extends User {

	private String staffDepartment;  //only attribute

	/**
	 * 
	 * @param userID from User class
	 * @param name from User class
	 * @param staffDepartment from User class
	 */
	public CareerCenterStaff(String userID, String name, String password, String staffDepartment) {//constructor
		super(userID, name, password);
		this.staffDepartment = staffDepartment;
	}

	public String getStaffDepartment() {
		return this.staffDepartment;
	}

	/**
	 * 
	 * @param staffDepartment
	 */
	public void setStaffDepartment(String staffDepartment) {
		this.staffDepartment = staffDepartment;
	}

}