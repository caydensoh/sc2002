
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CompanyRepresentative extends User {

	final private String companyName;
	private String department;
	private String position;
	private boolean approval; 
	private final InternshipRepo internships;
	private final ApplicationRepo appRepo; // ← we'll pass this in
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

	public List<Internship> getApprovedInternships() {
		List<Internship> approved = new ArrayList<>();
		for (Internship i : internships.getAll()) {
			if ("Approved".equals(i.getStatus())) {
				approved.add(i);
			}
		}
		return approved;
	}

	public void viewAndProcessApplicationsForInternship(Internship intern, Scanner scanner) {
		System.out.println("\n=== Applications for: " + intern.getTitle() + " ===");
		boolean found = false;

		for (Application app : appRepo.getAll()) {
			if (intern.getInternshipID().equals(app.getInternship().getInternshipID())) {
				Student student = (Student) userRepo.find(app.getStudent().getUserID());
				if (student == null) continue;

				System.out.println("Application ID: " + app.getApplicationID());
				System.out.println("Student: " + student.getName() + " (" + student.getUserID() + ")");
				System.out.println("Status: " + app.getStatus());
				System.out.println("----------------------------------------");
				found = true;

				if ("Pending".equalsIgnoreCase(app.getStatus())) {
					System.out.print("Approve (A), Reject (R), or Skip (S)? ");
					String action = scanner.nextLine().trim().toLowerCase();
					if ("a".equals(action)) {
						app.setStatus("Successful");
						System.out.println("Application approved.");
						checkIfInternshipFilled(intern);
					} else if ("r".equals(action)) {
						app.setStatus("Unsuccessful");
						System.out.println("Application rejected.");
					}
				}
			}
		}

		if (!found) {
			System.out.println("No applications found.");
		}
	}
	private void checkIfInternshipFilled(Internship intern) {
		int successfulCount = 0;
		for (Application app : appRepo.getAll()) {
			if (intern.getInternshipID().equals(app.getInternship().getInternshipID()) &&
				"Successful".equals(app.getStatus())) {
				successfulCount++;
			}
		}
		if (successfulCount >= intern.getSlots()) {
			intern.setStatus("Filled");
			System.out.println("Internship '" + intern.getTitle() + "' is now filled!");
		}
	}

	// --- Profile & Password ---
	public void displayProfile() {
		System.out.println("Type: Company Representative");
		System.out.println("Company: " + companyName);
		System.out.println("Department: " + department);
		System.out.println("Position: " + position);
	}
}




/*3. Company Representatives
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
This will be reflected in the internship list that will be visible to Students*/
