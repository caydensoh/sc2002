public class CarrerCenterStaff extends User {

	private String staffDepartment;

	/**
	 * 
	 * @param userID 
	 * @param name 
	 * @param password 
	 * @param staffDepartment 
	 */
	public CarrerCenterStaff(String userID, String name, String password, String staffDepartment) {//constructor
		// TODO - implement CarrerCenterStaff.CarrerCenterStaff
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