
public class CareerCenterStaff extends User {

	private String staffDepartment;
	

	/**
	 * 
	 * @param userID 
	 * @param name 
	 * @param password 
	 * @param staffDepartment 
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