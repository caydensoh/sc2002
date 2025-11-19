import java.util.List;

public class CompanyRepresentative extends User {

	final private String companyName;
	private String department;
	private String position;
	private InternshipRepo internships;
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
		return this.internships.getAll();
	}

	public String getCompanyName() {
		return this.companyName;
	}

	public boolean getApproval() {
		return this.approval;
	}

	/** @param approval
	 */
	public void setApproval(boolean approval) {
		this.approval = approval;
	}

	/**
	 * 
	 * @param internship
	 */

	 public boolean addInternships(Internship internship) {
		if (internship == null) {
			System.out.println("Cannot add a null internship.");
			return false;
		}
		if (this.internships.size() >= 5) {
			System.out.println("You have already reached the maximum of 5 internships.");
			return false;
		}
		// Auto-assign company name (safety)
		internship.setCompanyName(this.companyName);
		this.internships.add(internship);
		return true;
	}

	/**
	 * Delete an internship 
	 * @param index
	 */
	public boolean deleteInternship(int index) {
		// Check if the index is within valid bounds
		if (index < 0 || index >= this.internships.size()) {
			System.out.println("Warning: Invalid internship index: " + index);
			return false;
		}

		// Remove the internship at the given index
		this.internships.getAll().remove(index);
		return true;
	}
}