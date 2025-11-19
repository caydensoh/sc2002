import java.util.Scanner;

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
				case 0 -> rep.setApproval(false);
				case 1 -> rep.setApproval(true);
				default -> {
                                    System.out.println("Please type a valid choice");
                                    System.out.println("Would you like to approve or reject this account's creation? (1 = Yes, 0 = No)");
                                    choice = sc.nextInt();
                        }
					

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
				case 0 -> {
                                    intern.setStatus("Rejected");
                                    if (rep.deleteInternship(index)) {
                                        System.out.println("Internship rejected and removed.");
                                    } else {
                                        System.out.println("Failed to remove internship (invalid index).");
                                    }
                                        }
				case 1 -> {
                                    intern.setStatus("Approved");
                                    System.out.println("Internship approved.");
                                        }
				default -> System.out.println("Please enter a valid choice (1 or 0).");
					// Loop continues
			}
		} while (choice != 0 && choice != 1);

		sc.close(); // optional: but be careful if other code uses System.in
	}

	public void approveWithdrawal(Application app, boolean choice) { 		
	if (null != app.getWithdrawalStatus()) //• Able to approve or reject student withdrawal requests (both before and after placement confirmation)
            switch (app.getWithdrawalStatus()) {
                case "Withdrawn" -> System.out.println("Application has already been withdrawn");
                case "Active" -> System.out.println("Application has not been requested to be withdrawn");
                case "Pending" -> {
                    if (choice ==true){
                        
                        app.setWithdrawalStatus("Active");
                    }else{
                        app.setWithdrawalStatus("Withdrawn");
                    }
            }
                default -> {
            }
            }

	}

	/*. Career Center Staff
• Registration is automatic by reading in from the staff list file. IDK if need to implement this or not

• */
	
}