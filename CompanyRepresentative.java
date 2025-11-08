import java.util.List;

public class CompanyRepresentative extends User {

	private String companyName;
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
		// TODO - implement CompanyRepresentative.CompanyRepresentative
		super(userID, name, password);
		this.companyName=companyName;
		this.department=department;
		this.position=position;
		//throw new UnsupportedOperationException();
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

	/**
	 * 
	 * @param internship
	 */
	public void addInternships(Internship internship) {
		// TODO - implement CompanyRepresentative.addInternships


		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param index
	 * @param application
	 */
	public Boolean changeInternship(Integer index, Application application) {
		// TODO - implement CompanyRepresentative.changeInternship
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param index
	 */
	public Boolean deleteInternship(Integer index) {
		// TODO - implement CompanyRepresentative.deleteInternship
		throw new UnsupportedOperationException();
	}

	public Boolean getApproval() {
		// TODO - implement CompanyRepresentative.getApproval
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param approval
	 */
	public void setApproval(Boolean approval) {
		// TODO - implement CompanyRepresentative.setApproval
		throw new UnsupportedOperationException();
	}

}