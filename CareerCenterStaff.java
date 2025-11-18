import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

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

	public void approveAccountCreation(CompanyRepresentative rep) { // Able to authorize or reject the account creation of Company Representatives
		System.out.println("Would you like to approve or reject this account's creation? (1 = Yes, 0 = No)");
		Scanner sc = new Scanner(System.in);
		int choice = sc.nextInt();
		do {
			switch (choice){
				case 0:
					rep.setApproval(false);
					break;
				case 1:
					rep.setApproval(true);
					break;
				default:
					System.out.println("Please type a valid choice");
					System.out.println("Would you like to approve or reject this account's creation? (1 = Yes, 0 = No)");
					choice = sc.nextInt();
					break;
					

			}
		} while (choice != 1 &&choice != 0);
	
	}

	public void approveInternship(Internship intern, CompanyRepresentative rep, int index) {
				Scanner sc = new Scanner(System.in);
		int choice;

		do {
			System.out.println("Would you like to approve or reject this Internship? (1 = Approve, 0 = Reject)");

			// Input validation to avoid crash on non-integer input
			while (!sc.hasNextInt()) {
				System.out.println("Invalid input. Please enter 1 or 0.");
				sc.next(); // discard invalid token
			}
			choice = sc.nextInt();
			sc.nextLine(); // consume newline

			switch (choice) {
				case 0:
					intern.setStatus("Rejected");
					if (rep.deleteInternship(index)) {
						System.out.println("Internship rejected and removed.");
					} else {
						System.out.println("Failed to remove internship (invalid index).");
					}
					break;
				case 1:
					intern.setStatus("Approved");
					System.out.println("Internship approved.");
					break;
				default:
					System.out.println("Please enter a valid choice (1 or 0).");
					// Loop continues
			}
		} while (choice != 0 && choice != 1);

		sc.close(); // optional: but be careful if other code uses System.in
	}

	public void approveWithdrawal(Application app, boolean choice) { //• Able to approve or reject student withdrawal requests (both before and after placement confirmation)
		
	if ("Withdrawn".equals(app.getWithdrawalStatus())){
			System.out.println("Application has already been withdrawn");
		}else if("Active".equals(app.getWithdrawalStatus())){
			System.out.println("Application has not been requested to be withdrawn");
		}else if ("Pending".equals(app.getWithdrawalStatus())){
			if (choice ==true){
				
				app.setWithdrawalStatus("Active");
			}else{
				app.setWithdrawalStatus("Withdrawn");
			}
		
		}

	}

	public void generateInternshipReport() { //Able to generate comprehensive reports regarding internshipopportunities created: o There should be filters to generate filter opportunities based on their Status, Preferred Majors, Internship Level, etc... 
		Scanner scanner = Menu.scanner; // use shared scanner from Menu
		List<Internship> allInternships = Menu.allInternships;

		System.out.println("\n========== Generate Internship Report ==========");

		// Get filter inputs
		System.out.print("Enter Status filter (press Enter to skip): ");
		String statusFilter = scanner.nextLine().trim();
		if (statusFilter.isEmpty()) statusFilter = null;

		System.out.print("Enter Preferred Major filter (press Enter to skip): ");
		String majorFilter = scanner.nextLine().trim();
		if (majorFilter.isEmpty()) majorFilter = null;

		System.out.print("Enter Internship Level filter (press Enter to skip): ");
		String levelFilter = scanner.nextLine().trim();
		if (levelFilter.isEmpty()) levelFilter = null;

		// Filter internships
		List<Internship> filtered = new ArrayList<>();
		for (Internship intern : allInternships) {
			boolean match = true;

			if (statusFilter != null && !intern.getStatus().equalsIgnoreCase(statusFilter)) {
				match = false;
			}
			if (majorFilter != null && intern.getPreferredMajor() != null &&
				!intern.getPreferredMajor().equalsIgnoreCase(majorFilter)) {
				match = false;
			}
			if (levelFilter != null && intern.getInternshipLevel() != null &&
				!intern.getInternshipLevel().equalsIgnoreCase(levelFilter)) {
				match = false;
			}

			if (match) {
				filtered.add(intern);
			}
		}

		// display results
		if (filtered.isEmpty()) {
			System.out.println("\nNo internships match the selected filters.\n");
		} else {
			System.out.println("\n========== Filtered Internship Report ==========");
			System.out.printf("%-15s %-25s %-15s %-20s %-10s %-10s%n",
				"ID", "Title", "Level", "Major", "Status", "Slots");
			System.out.println("------------------------------------------------------------------------------------------");
			for (Internship intern : filtered) {
				System.out.printf("%-15s %-25s %-15s %-20s %-10s %-10d%n",
					intern.getInternshipID().substring(0, Math.min(14, intern.getInternshipID().length())),
					intern.getTitle().length() > 24 ? intern.getTitle().substring(0, 24) : intern.getTitle(),
					intern.getInternshipLevel() == null ? "N/A" : intern.getInternshipLevel(),
					intern.getPreferredMajor() == null ? "N/A" : intern.getPreferredMajor(),
					intern.getStatus(),
					intern.getSlots() == null ? 0 : intern.getSlots()
				);
			}
			System.out.println("------------------------------------------------------------------------------------------");
			System.out.println("Total matching internships: " + filtered.size() + "\n");
		}
	}

	/*. Career Center Staff
• Registration is automatic by reading in from the staff list file. IDK if need to implement this or not

• */
	
}