public class CarrerCenterStaff extends User {

	private String staffDepartment;  //only attribute

	/**
	 * 
	 * @param userID from User class
	 * @param name from User class
	 * @param staffDepartment from User class
	 */
	public CarrerCenterStaff(String userID, String name, String staffDepartment) {//constructor
		// TODO - implement CarrerCenterStaff.CarrerCenterStaff
		super(userID, name, "password");
		this.staffDepartment = staffDepartment;
		throw new UnsupportedOperationException();
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