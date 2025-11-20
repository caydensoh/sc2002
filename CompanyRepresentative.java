 public class CompanyRepresentative extends User {

	final private String companyName;
	private String department;
	private String position;
	private boolean approval; 
	private final InternshipRepo internships;
	private final ApplicationRepo appRepo; 
	private final UserRepo userRepo; 

	/**
	 * 
	 * @param userID
	 * @param name
	 * @param companyName
	 * @param department
	 * @param position
	 */

	
	public CompanyRepresentative(String userID, String name, String password, 
                             String companyName, String department, String position,
                             ApplicationRepo appRepo, UserRepo userRepo) {
		super(userID, name, password);
		this.companyName = companyName;
		this.department = department;
		this.position = position;
		this.approval = false;
		this.internships = new InternshipRepo();
		this.appRepo = appRepo;
		this.userRepo = userRepo;
	}

	public CompanyRepresentative(String userID, String name, String password, 
                             String companyName, String department, String position) {
		// constructor for loading from csv
		this(userID, name, password, companyName, department, position, null, null);
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

	public InternshipRepo getInternships() {
		return this.internships;
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

	

	 public boolean addInternships(Internship internship) {//• Able to create internship opportunities (up to 5) for their companies,
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

	public InternshipRepo getApprovedInternships() {
		InternshipRepo approved = new InternshipRepo();
		for (Internship i : internships.getAll()) {
			if ("Approved".equals(i.getStatus())) {
				approved.add(i);
			}
		}
		return approved;
	}

	public boolean checkIfInternshipFilled(Internship intern) {
		if (appRepo == null) return false;
		int successfulCount = 0;
		for (Application app : appRepo.getAll()) {
			if (intern.getInternshipID().equals(app.getInternship().getInternshipID()) &&
				"Successful".equalsIgnoreCase(app.getStatus())) {
				successfulCount++;
			}
		}
		if (successfulCount >= intern.getSlots()) {
			intern.setStatus("Filled");
			return true;
		}
		return false;
	}

	public ApplicationRepo getAppRepo() {
		return this.appRepo;
	}

	public UserRepo getUserRepo() {
		return this.userRepo;
	}
}