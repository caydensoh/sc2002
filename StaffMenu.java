
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
        optionMap.put("3", this::approveRejectCompanyReps);
        optionLabels.put("3", "Approve/Reject Company Representatives");
        // Removed viewAllInternships, browseInternships is now the base method
        optionMap.put("4", this::approveRejectInternshipsOpportunities);
        optionLabels.put("4", "Approve/Reject Internships");
        optionMap.put("5", this::generateInternshipReport);
        optionLabels.put("5", "Generate internship report");
        optionMap.put("6", this::viewAllApplications);
        optionLabels.put("6", "View all applications");
        optionMap.put("7", this::approveRejectInternshipWithdrawals);
        optionLabels.put("7", "Approve/Reject Internship Withdrawals");
        optionMap.put("8", this::changeOwnPassword);
        optionLabels.put("8", "Change password");
    }

    // ...existing code...

    // ...existing code...

    @Override
    protected void viewOwnProfile() {
        displayUserHeader();
        System.out.println("Type: Career Center Staff");
        System.out.println("Department: " + staff.getStaffDepartment());
        System.out.println("================================\n");
    }

    @Override
    protected void browseInternships() {
        System.out.println("\n========== All Internships ==========");
        if (internships.isEmpty()) {
            System.out.println("No internships available.\n");
            return;
        }

        for (int i = 0; i < internships.size(); i++) {
            Internship in = internships.get(i);
            System.out.println((i + 1) + ". " + in.getTitle() + " (" + in.getInternshipLevel() +
                ") - " + in.getCompanyName() + " | Visibility: " + in.getVisibility());
        }
        System.out.println();
    }

    private void viewAllApplications() {
        System.out.println("\n========== All Applications ==========");

        if (applications.isEmpty()) {
            System.out.println("No applications yet.\n");
            return;
        }

        // List all applications with index, student, and internship basic info
        for (int i = 0; i < applications.size(); i++) {
            Application app = applications.get(i);
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

            if (choice < 1 || choice > applications.size()) {
                System.out.println("Number out of range. Try again.");
                continue;
            }

            Application selected = applications.get(choice - 1);
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
            CompanyRepresentative rep = (CompanyRepresentative) pendingReps.get(i);
            System.out.println((i + 1) + ". " + rep.getName() + " (" + rep.getCompanyName() + ")");
        }

        System.out.print("\nSelect representative (0 to cancel): ");
        try {
            int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (idx >= 0 && idx < pendingReps.size()) {
                CompanyRepresentative selected = (CompanyRepresentative) pendingReps.get(idx);
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

    private void approveRejectInternshipsOpportunities() {
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
                    selected.setStatus("Rejected");
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

    public void approveRejectInternshipWithdrawals() { 
        //• Able to approve or reject student withdrawal requests (both before and after placement confirmation)
        System.out.println("\n========== Manage Internship Withdrawals ==========");
        ApplicationRepo pendingWithdrawals = new ApplicationRepo();
        for (Application app : applications.getAll()) {
            if (app.getWithdrawalStatus() != null && app.getWithdrawalStatus().equals("Pending")) {
                pendingWithdrawals.add(app);
            }
        }
        if (pendingWithdrawals.isEmpty()) {
            System.out.println("No pending withdrawal requests.\n");
            return;
        }
        for (int i = 0; i < pendingWithdrawals.size(); i++) {
            Application app = pendingWithdrawals.get(i);
            Student student = app.getStudent();
            Internship internship = app.getInternship();
            System.out.println((i + 1) + ". " + student.getName() + " (" + student.getUserID() + ") - " + internship.getTitle());
        }
        System.out.print("\nSelect application (0 to cancel): ");
        try {
            int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (idx >= 0 && idx < pendingWithdrawals.size()) {
                Application selected = pendingWithdrawals.get(idx);
                System.out.println("\n1: Approve Withdrawal");
                System.out.println("2: Reject Withdrawal");
                System.out.print("Choose action: ");
                String action = scanner.nextLine().trim();
                if (action.equals("1")) {
                    selected.setWithdrawalStatus("Withdrawn");
                    System.out.println("Withdrawal approved!\n");
                } else if (action.equals("2")) {
                    selected.setWithdrawalStatus("Active");
                    System.out.println("Withdrawal rejected.\n");
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.\n");
        }
    }
}
