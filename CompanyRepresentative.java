import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;

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
		//if lenght >= 5, cannot add anymore internship
		if (internships.size() >= 5) {
            System.out.println("You have already reached the maximum of 5 internships.");
            return;
        }
        // auto assign company name
        internship.setCompanyName(this.companyName); 
        internships.add(internship); 

		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param index
	 * @param application
	 */
	public Boolean updateInternship(Integer index, Internship internship) {
		// TODO - implement CompanyRepresentative.changeInternship
		if (index < 0 || index >= internships.size()) {
			System.out.println("Internship not found.");
            return false;
        }
         Internship target = internships.get(index); 

		// Only allow editing if status is "Pending" (before approval)
		if (!target.getStatus().equals("Pending")) {
			System.out.println("Cannot update internship after it has been approved/rejected/filled.");
			return false;
		}

		Scanner scanner = new Scanner(System.in);
		boolean editing = true;

		while (editing) {
			System.out.println("\n--- Update Internship Menu ---");
			System.out.println("1: Update Title");
			System.out.println("2: Update Description");
			System.out.println("3: Update Level (Basic/Intermediate/Advanced)");
			System.out.println("4: Update Preferred Major");
			System.out.println("5: Update Opening Date (YYYY-MM-DD)");
			System.out.println("6: Update Closing Date (YYYY-MM-DD)");
			System.out.println("7: Update Number of Slots (max 10)");
			System.out.println("8: Toggle Visibility");
			System.out.println("0: Save and Exit");
			System.out.print("Enter your choice: ");

			int choice = scanner.nextInt();
			scanner.nextLine(); // consume newline

			switch (choice) {
				case 1:
					System.out.print("Enter new title: ");
					target.setTitle(scanner.nextLine());
					break;
				case 2:
					System.out.print("Enter new description: ");
					target.setDescription(scanner.nextLine());
					break;
				case 3:
					System.out.print("Enter new level (Basic/Intermediate/Advanced): ");
					String level = scanner.nextLine();
					if (level.equals("Basic") || level.equals("Intermediate") || level.equals("Advanced")) {
						target.setInternshipLevel(level);
					} else {
						System.out.println("Invalid level. No change made.");
					}
					break;
				case 4:
					System.out.print("Enter new preferred major (e.g., CSC, EEE): ");
					target.setPreferredMajor(scanner.nextLine());
					break;
				case 5:
					System.out.print("Enter new opening date (YYYY-MM-DD): ");
					String dateStringOpen=scanner.nextLine();
					LocalDate openingDate=LocalDate.parse(dateStringOpen);
					target.setOpeningDate(openingDate);
					break;
				case 6:
					System.out.print("Enter new closing date (YYYY-MM-DD): ");
					String dateStringClose=scanner.nextLine();
					LocalDate closingDate=LocalDate.parse(dateStringClose);
					target.setClosingDate(closingDate);
					break;
				case 7:
					System.out.print("Enter number of slots (1-10): ");
					int slots = scanner.nextInt();
					if (slots >= 1 && slots <= 10) {
						target.setSlots(slots);
					} else {
						System.out.println("Invalid number of slots. Must be between 1 and 10.");
					}
					scanner.nextLine(); // consume newline
					break;
				case 8:
					target.setVisibility(!target.getVisibility());
					System.out.println("Visibility toggled to " + (target.getVisibility() ? "ON" : "OFF"));
					break;
				case 0:
					editing = false;
					System.out.println("Updates saved.");
					break;
				default:
					System.out.println("Invalid choice. Try again.");
			}

		}
			return true;
			//throw new UnsupportedOperationException();
	}	

	/**
	 * 
	 * @param index
	 */
	public Boolean deleteInternship(Integer index) {
		// TODO - implement CompanyRepresentative.deleteInternship
		if (index < 0 || index >= internships.size()) {
            return false;
        }
        internships.remove(index.intValue());
        return true;
		//throw new UnsupportedOperationException();
	}

	public Boolean getApproval() {
		// TODO - implement CompanyRepresentative.getApproval
		return this.approval;
		//throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param approval
	 */
	public void setApproval(Boolean approval) { //idk if they are allowed to do this??
		// TODO - implement CompanyRepresentative.setApproval 
		this.approval = approval;
		throw new UnsupportedOperationException();
	}

}