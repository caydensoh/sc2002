import java.util.List;
import java.util.Scanner;

public class CompanyRepresentative extends User {

	final private String companyName;
	private String department;
	private String position;
	private InternshipRepo internships;
	private ApplicationRepo applications;
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
	/*public boolean addInternships(Internship internship) {
		/*• Able to create internship opportunities (up to 5) for their companies, 
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
		o Number of slots (max of 10) */

		//if length >= 5, cannot add anymore internship
		/*if (this.internships.size() >= 5) {
            System.out.println("You have already reached the maximum of 5 internships.");
            return false;
        }
        // auto assign company name
		Scanner sc = new Scanner(System.in);
		System.out.println("Please enter the Internship title");
		String title = sc.nextLine();
		System.out.println("Please enter the Internship description");
		String description = sc.nextLine();
		System.out.println("Please enter the Internship level");
		String level = sc.nextLine();
		System.out.println("Please enter the preffered majors for the internship");
		String major = sc.nextLine();
		System.out.println("Please enter the Internship application opening date");
		LocalDate openingDate = null;
		while (openingDate == null) {
			System.out.println("Please enter the opening date (yyyy-MM-dd):");
			String input = sc.nextLine().trim();
			try {
				openingDate = LocalDate.parse(input);
			} catch (DateTimeParseException e) {
				System.out.println("Invalid format. Use yyyy-MM-dd.");
			}
		}
		System.out.println("Please enter the Internship application closing date");
		LocalDate closingDate = null;
		while (closingDate == null) {
			System.out.println("Please enter the closing date (yyyy-MM-dd):");
			String input = sc.nextLine().trim();
			try {
				closingDate = LocalDate.parse(input);
				if (closingDate.isBefore(openingDate)) {
					System.out.println("Closing date must be on or after opening date.");
					closingDate = null;
				}
			} catch (DateTimeParseException e) {
				System.out.println("Invalid format. Use yyyy-MM-dd.");
			}
		}
		System.out.println("Please enter the number of slots for the Internship");
		int slots = sc.nextInt();
		Internship new_internship = new Internship(title, description, level, major, openingDate, closingDate, this.companyName, getUserID(), slots);
        // figure out how to make a internship ID
        this.internships.add(new_internship);
        return true;
	}*/

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
		this.internships.remove(index);
		return true;
	}

	
	public void setVisibility(int index) {
		/*• Able to toggle the visibility of the internship opportunity to “on” or “off”.
		This will be reflected in the internship list that will be visible to Students */ 
		if (index < 0 || index >= this.internships.size()) {
			System.out.println("Invalid internship index: " + index);
			return;
		}

		// Get the internship at that index
		Internship internship = this.internships.get(index);

		System.out.println("Would you like to set the visibility for internship '" + internship.getTitle() + "to ON (1) or OFF (0)?");
		Scanner sc = new Scanner(System.in);
		int choice = sc.nextInt();
		do{
			switch (choice){
				case 0:
					internship.setVisibility(false);
					break;
				case 1:
					internship.setVisibility(true);
					break;
				default:
					System.out.println("Please type a valid choice 1 or 0");
					System.out.println("Would you like to set the visibility for internship '" + internship.getTitle() + "to ON (1) or OFF (0)?");
					choice = sc.nextInt();
					break;

			}
		}while (choice != 1 &&choice != 0);
	}
/*

// o Able to view application details and student details for each of their
// internship opportunities
// 	• May Approve or Reject the internship application
// 	o Once approved, student application status becomes "Successful"
// 	o Student can then accept the placement confirmation
// 	o Internship opportunity status becomes "Filled" only when all
// 	available slots are confirmed by students*/
// 	public void viewApplicationsForInternship(Internship intern) {
// 		if (!this.internships.contains(intern)) {
// 			System.out.println("You do not manage this internship.");
// 			return;
// 		}

// 		System.out.println("\n=== Applications for: " + intern.getTitle() + " ===");
// 		boolean found = false;
// 		Scanner sc = Menu.scanner;

// 		for (Application app : Menu.allApplications) {
// 			if (app.getInternship() == intern) {
// 				System.out.println("Application ID: " + app.getApplicationID());
// 				System.out.println("Student: " + app.getStudent().getName() + " (" + app.getStudent().getUserID() + ")");
// 				System.out.println("Status: " + app.getStatus());
// 				System.out.println("Withdrawal Status: " + app.getWithdrawalStatus());
// 				System.out.println("----------------------------------------");
// 				found = true;

// 				// Only allow action if application is still pending
// 				if ("Pending".equalsIgnoreCase(app.getStatus())) {
// 					System.out.println("Would you like to approve or reject this application?");
// 					System.out.println("1 = Approve");
// 					System.out.println("0 = Reject");
// 					System.out.println("-1 = Skip to next application");

// 					int choice = -2;
// 					while (choice != -1 && choice != 0 && choice != 1) {
// 						try {
// 							System.out.print("Enter your choice: ");
// 							choice = Integer.parseInt(sc.nextLine().trim());
// 							if (choice != -1 && choice != 0 && choice != 1) {
// 								System.out.println("Invalid choice. Please enter 1, 0, or -1.");
// 							}
// 						} catch (NumberFormatException e) {
// 							System.out.println("Please enter a valid number (1, 0, or -1).");
// 						}
// 					}

// 					if (choice == 1) {
// 						// Approve application
// 						app.setStatus("Successful");
// 						System.out.println("Application approved for student: " + app.getStudent().getName());

// 						// Check if internship is now filled
// 						long successfulCount = 0;
// 						for (Application a : Menu.allApplications) {
// 							if (a.getInternship() == intern && "Successful".equals(a.getStatus())) {
// 								successfulCount++;
// 							}
// 						}
// 						if (successfulCount >= intern.getSlots()) {
// 							intern.setStatus("Filled");
// 							System.out.println(" Internship '" + intern.getTitle() + "' is now FILLED!");
// 						}
// 					} else if (choice == 0) {
// 						// Reject application
// 						app.setStatus("Unsuccessful");
// 						System.out.println("Application rejected for student: " + app.getStudent().getName());
// 					}
// 					// If choice == -1, do nothing and continue
// 				} 
// 				System.out.println(); // blank line for readability
// 			}
// 		}

// 		if (!found) {
// 			System.out.println("No applications found.");
// 		}
// 	}
	

}