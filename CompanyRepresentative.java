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
	public boolean deleteInternship(Internship internship) {
		if (internship == null) {
			System.out.println("Warning: Cannot delete a null internship.");
			return false;
		}
		if (this.internships.contains(internship)) {
			this.internships.remove(internship);
			return true;
		} else {
			System.out.println("Warning: The specified internship is not associated with this company representative.");
			return false;
		}
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

/*Company Representatives
• Company Representative list is empty at very beginning.
• Company Representatives must register as a representative of a
specific company, and they can only log in once approved by the Career
Center Staff.
• Able to create internship opportunities (up to 5) for their companies,
which should include the following details:
o Internship Title
o Description
o Internship Level (Basic, Intermediate, Advanced)
o Preferred Majors (Assume 1 preferred major will do)
Application opening date
o Application closing date
o Status (“Pending”, “Approved”, “Rejected”, “Filled”)
o Company Name
o Company Representatives in charge (automatically assigned)
o Number of slots (max of 10)
• Internship opportunities created must be approved by the career center
staff
o Once status is “Approved”, students may apply for them
o If “Filled” or after the Closing Date, students will not be able to
apply for them anymore
o Able to view application details and student details for each of their
internship opportunities
• May Approve or Reject the internship application
o Once approved, student application status becomes "Successful"
o Student can then accept the placement confirmation
o Internship opportunity status becomes "Filled" only when all
available slots are confirmed by students
• Able to toggle the visibility of the internship opportunity to “on” or “off”.
This will be reflected in the internship list that will be visible to Students */

}