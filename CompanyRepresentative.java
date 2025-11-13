import java.util.List;

public class CompanyRepresentative extends User {

	final private String companyName;
	private String department;
	private String position;
	private List<Internship> internships;
	private boolean approval; 

	/**
	 * 
	 * @param userID
	 * @param name
	 * @param companyName
	 * @param department
	 * @param position
	 */
	public CompanyRepresentative(String userID, String name, String password, String companyName, String department, String position) {
		super(userID, name, password);
		this.companyName=companyName;
		this.department=department;
		this.position=position;
		this.internships = new java.util.ArrayList<>();
		this.approval = false;
	}

	public String getDepartment() {
		return this.department;
	}

	/**
	 * 
	 * @param Department
	 */
	public void setDepartment(String Department) {
		this.department = Department;
	}

	public String getPosition() {
		return this.position;
	}

	/**
	 * 
	 * @param Position
	 */
	public void setPosition(String Position) {
		this.position = Position;
	}

	public List<Internship> getInternships() {
		return this.internships;
	}

	public String getCompanyName() {
		return this.companyName;
	}

	/**
	 * 
	 * @param internship
	 */
	public boolean addInternships(Internship internship) {
		//if length >= 5, cannot add anymore internship
		if (this.internships.size() >= 5) {
            System.out.println("You have already reached the maximum of 5 internships.");
            return false;
        }
        // auto assign company name
        internship.setCompanyName(this.companyName); 
        this.internships.add(internship);
        return true;
	}

	/**
	 * Delete an internship by index
	 * @param index
	 */
	public boolean deleteInternship(Integer index) {
		if (index < 0 || index >= internships.size()) {
            return false;
        }
        internships.remove(index.intValue());
        return true;
	}

	public boolean getApproval() {
		return this.approval;
	}

	/**
	 * 
	 * @param approval
	 */
	public void setApproval(boolean approval) {
		this.approval = approval;
	}

}