
public class StaffMenu extends Menu {
    private final CareerCenterStaff staff;
    private final InternshipRepo internships;
    private final ApplicationRepo applications;
    private final UserRepo users;

    public StaffMenu(java.util.Scanner s, CareerCenterStaff st, InternshipRepo internshipRepo, ApplicationRepo applicationRepo, UserRepo users) {
        super(s, st);
        this.staff = st;
        this.internships = internshipRepo;
        this.applications = applicationRepo;
        this.users = users;
    }

    @Override
    public String displayGetChoices() {
        System.out.println("\n========== Internship Management System (Staff) ==========");
        System.out.println("Logged in as: " + staff.getName() + " (" + staff.getUserID() + ")");
        System.out.println("1: View my profile");
        System.out.println("2: Approve/Reject company representatives");
        System.out.println("3: View all internships");
        System.out.println("4: Approve/Reject internship opportunities");
        System.out.println("5: Generate internship report");
        System.out.println("6: View all applications");
        System.out.println("7: Change my password");
        System.out.println("0: Logout");
        System.out.println("====================================================");
        System.out.print("Enter your choice: ");
        String input = scanner.nextLine().trim();
        return input;
    }

    @Override
    public void handleChoices(String choice) {
        switch (choice) {
            case "1" -> viewOwnProfile();
            case "2" -> approveRejectCompanyReps();
            case "3" -> viewAllInternships();
            case "4" -> approveRejectInternships();
            case "5" -> generateInternshipReport();
            case "6" -> viewAllApplications();
            case "7" -> changeOwnPassword();
            case "0" -> logoutCurrentUser();
            default -> System.out.println("Invalid choice. Please try again.\n");
        }
    }

    private void viewOwnProfile() {
        displayUserHeader();
        System.out.println("Type: Career Center Staff");
        System.out.println("Department: " + staff.getStaffDepartment());
        System.out.println("================================\n");
    }

    private void viewAllInternships() {
        System.out.println("\n========== All Internships ==========");
        if (internships.getAll().isEmpty()) {
            System.out.println("No internships available.\n");
            return;
        }

        for (int i = 0; i < internships.getAll().size(); i++) {
            Internship in = internships.getAll().get(i);
            System.out.println((i + 1) + ". " + in.getTitle() + " (" + in.getInternshipLevel() +
                ") - " + in.getCompanyName() + " | Visibility: " + in.getVisibility());
        }
        System.out.println();
    }

    private void viewAllApplications() {
        System.out.println("\n========== All Applications ==========");

        if (applications.getAll().isEmpty()) {
            System.out.println("No applications yet.\n");
            return;
        }

        // List all applications with index, student, and internship basic info
        for (int i = 0; i < applications.getAll().size(); i++) {
            Application app = applications.getAll().get(i);
            Student student = app.getStudent();
            Internship internship = app.getInternship();

            String studentInfo = student != null ? student.getName() + " (" + student.getUserID() + ")" : "Unknown Student";
            String internshipInfo = internship != null ? internship.getTitle() : "Unknown Internship";

            System.out.println((i + 1) + ". " + studentInfo + " -> " + internshipInfo);
        }

        while (true) {
            System.out.print("\nEnter application number to view details (0 to return): ");
            String input = scanner.nextLine().trim();

            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            if (choice == 0) {
                break; // exit to previous menu
            }

            if (choice < 1 || choice > applications.getAll().size()) {
                System.out.println("Number out of range. Try again.");
                continue;
            }

            Application selected = applications.getAll().get(choice - 1);
            Student student = selected.getStudent();
            Internship internship = selected.getInternship();

            System.out.println("\n========== Application Details ==========");
            System.out.println("Application ID: " + selected.getApplicationID());
            System.out.println("Student: " + (student != null ? student.getName() + " (" + student.getUserID() + ")" : "Unknown"));
            System.out.println("Year / Major: " + (student != null ? student.getYearOfStudy() + " / " + student.getMajor() : "N/A"));
            System.out.println("Internship: " + (internship != null ? internship.getTitle() : "Unknown"));
            System.out.println("Application Status: " + selected.getStatus());
            System.out.println("Withdrawal Status: " + (selected.getWithdrawalStatus() != null ? selected.getWithdrawalStatus() : "N/A"));
            System.out.println("-------------------------------------------------");
        }
    }

    private void approveRejectCompanyReps() {
        System.out.println("\n========== Manage Company Representatives ==========");
        UserRepo pendingReps = new UserRepo();
        for (User u : users.getAll()) {
            if (u instanceof CompanyRepresentative rep) {
                if (!rep.getApproval()) {
                    pendingReps.add(rep);
                }
            }
        }

        if (pendingReps.isEmpty()) {
            System.out.println("No pending company representatives.\n");
            return;
        }

        for (int i = 0; i < pendingReps.size(); i++) {
            CompanyRepresentative rep = (CompanyRepresentative) pendingReps.getAll().get(i);
            System.out.println((i + 1) + ". " + rep.getName() + " (" + rep.getCompanyName() + ")");
        }

        System.out.print("\nSelect representative (0 to cancel): ");
        try {
            int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (idx >= 0 && idx < pendingReps.size()) {
                CompanyRepresentative selected = (CompanyRepresentative) pendingReps.getAll().get(idx);
                System.out.println("\n1: Approve");
                System.out.println("2: Reject");
                System.out.print("Choose action: ");
                String action = scanner.nextLine().trim();
                if (action.equals("1")) {
                    selected.setApproval(true);
                    System.out.println("Representative approved!\n");
                } else if (action.equals("2")) {
                    users.remove(selected);
                    System.out.println("Representative rejected and removed.\n");
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.\n");
        }
    }

    private void approveRejectInternships() {
        System.out.println("\n========== Approve/Reject Internships ==========");
        java.util.List<Internship> pendingInternships = new java.util.ArrayList<>();
        for (Internship intern : internships.getAll()) {
            if (intern.getStatus().equals("Pending")) {
                pendingInternships.add(intern);
            }
        }

        if (pendingInternships.isEmpty()) {
            System.out.println("No pending internships.\n");
            return;
        }

        for (int i = 0; i < pendingInternships.size(); i++) {
            Internship intern = pendingInternships.get(i);
            System.out.println((i + 1) + ". " + intern.getTitle() + " (" + intern.getInternshipLevel() +
                ") - " + intern.getCompanyName());
        }

        System.out.print("\nSelect internship (0 to cancel): ");
        try {
            int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (idx >= 0 && idx < pendingInternships.size()) {
                Internship selected = pendingInternships.get(idx);
                System.out.println("\n1: Approve");
                System.out.println("2: Reject");
                System.out.print("Choose action: ");
                String action = scanner.nextLine().trim();
                if (action.equals("1")) {
                    selected.setStatus("Approved");
                    System.out.println("Internship approved!\n");
                } else if (action.equals("2")) {
                    selected.setStatus("Approved");
                    System.out.println("Internship rejected.\n");
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.\n");
        }
    }

    public void generateInternshipReport() { 
        //Able to generate comprehensive reports regarding internshipopportunities created.
        //There should be filters to generate filter opportunities based on their Status, Preferred Majors, Internship Level, etc... 
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
        InternshipRepo filtered = new InternshipRepo();
		for (Internship intern : internships.getAll()) {
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
			for (Internship intern : filtered.getAll()) {
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
}
