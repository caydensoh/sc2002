import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

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
        optionMap.put("9", this::manageFilterSettings);
        optionLabels.put("9", "Manage filter settings");
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
        // Apply staff filter settings (if any)
        FilterSetting fs = staff.getFilterSettings();
        InternshipRepo toShow = new InternshipRepo();
        for (Internship in : internships.getAll()) {
            if (fs == null || in.matchesFilter(fs)) toShow.add(in);
        }
        // sort alphabetically
        toShow.getAll().sort(Comparator.comparing(Internship::getTitle, String.CASE_INSENSITIVE_ORDER));
        for (int i = 0; i < toShow.size(); i++) {
            Internship in = toShow.get(i);
            String closing = in.getClosingDate() == null ? "N/A" : in.getClosingDate().toString();
            System.out.println((i + 1) + ". " + in.getTitle() + " (" + in.getInternshipLevel() +
                ") - " + in.getCompanyName() + " | Visibility: " + in.getVisibility() + " | Closes: " + closing);
        }
        System.out.println();
    }

    private void viewAllApplications() {
        System.out.println("\n========== All Applications ==========");

        if (applications.isEmpty()) {
            System.out.println("No applications yet.\n");
            return;
        }

        // List all applications with index, student, and internship basic info (exclude withdrawn)
        java.util.List<Application> visibleApps = new ArrayList<>();
        for (Application app : applications.getAll()) {
            if (app.getWithdrawalStatus() == null || !"Withdrawn".equalsIgnoreCase(app.getWithdrawalStatus())) {
                visibleApps.add(app);
            }
        }
        for (int i = 0; i < visibleApps.size(); i++) {
            Application app = visibleApps.get(i);
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

    private void manageFilterSettings() {
        System.out.println("\n========== Filter Settings (Staff) ==========");
        FilterSetting filter = staff.getFilterSettings();

        boolean managing = true;
        while (managing) {
            System.out.println("Current filters:");
            System.out.println("  Internship Levels: " + (filter.getInternshipLevels() != null && !filter.getInternshipLevels().isEmpty() ? 
                String.join(", ", filter.getInternshipLevels()) : "None"));
            System.out.println("  Preferred Majors: " + (filter.getPreferredMajors() != null && !filter.getPreferredMajors().isEmpty() ? 
                String.join(", ", filter.getPreferredMajors()) : "None"));
            System.out.println("  Title Keywords: " + (filter.getTitleKeywords() != null ? filter.getTitleKeywords() : "None"));
            System.out.println("  Description Keywords: " + (filter.getDescriptionKeywords() != null ? filter.getDescriptionKeywords() : "None"));
            System.out.println("  Company: " + (filter.getCompanyName() != null ? filter.getCompanyName() : "None"));
            System.out.println("  Status: " + (filter.getStatus() != null ? filter.getStatus() : "Any"));
            System.out.println("  Available only: " + Boolean.TRUE.equals(filter.getAvailable()));
            System.out.println("  Visibility only: " + Boolean.TRUE.equals(filter.getVisibility()));
            System.out.println("  Start Date: " + (filter.getFilterStartDate() == null ? "None" : filter.getFilterStartDate()));
            System.out.println("  End Date: " + (filter.getFilterEndDate() == null ? "None" : filter.getFilterEndDate()));
            System.out.println();
            System.out.println("1: Change title keywords filter");
            System.out.println("2: Change description keywords filter");
            System.out.println("3: Change company name filter");
            System.out.println("4: Set status filter (Approved/Rejected/Pending/All)");
            System.out.println("5: Change internship levels (semicolon-separated)");
            System.out.println("6: Change preferred majors (semicolon-separated)");
            System.out.println("7: Toggle available-only");
            System.out.println("8: Toggle visibility-only");
            System.out.println("9: Set start date filter (YYYY-MM-DD)");
            System.out.println("10: Set end date filter (YYYY-MM-DD)");
            System.out.println("11: Clear all filters");
            System.out.println("0: Done");
            System.out.print("Enter choice: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> {
                    System.out.print("Enter title keywords or leave blank to clear: ");
                    String title = scanner.nextLine().trim();
                    filter.setTitleKeywords(title.isEmpty() ? null : title);
                    System.out.println("Title keywords filter updated.\n");
                }
                case "2" -> {
                    System.out.print("Enter description keywords or leave blank to clear: ");
                    String description = scanner.nextLine().trim();
                    filter.setDescriptionKeywords(description.isEmpty() ? null : description);
                    System.out.println("Description keywords filter updated.\n");
                }
                case "3" -> {
                    System.out.print("Enter company name or leave blank to clear: ");
                    String company = scanner.nextLine().trim();
                    filter.setCompanyName(company.isEmpty() ? null : company);
                    System.out.println("Company filter updated.\n");
                }
                case "4" -> {
                    System.out.print("Enter status (Approved/Rejected/Pending) or leave blank to clear: ");
                    String st = scanner.nextLine().trim();
                    filter.setStatus(st.isEmpty() ? null : st);
                    System.out.println("Status filter updated.\n");
                }
                case "5" -> {
                    System.out.print("Enter internship levels separated by semicolon (e.g. Basic;Advanced) or leave blank to clear: ");
                    String levels = scanner.nextLine().trim();
                    filter.setInternshipLevels(levels.isEmpty() ? new ArrayList<>() : Arrays.asList(levels.split(";")));
                    System.out.println("Internship levels updated.\n");
                }
                case "6" -> {
                    System.out.print("Enter preferred majors separated by semicolon or leave blank to clear: ");
                    String majors = scanner.nextLine().trim();
                    filter.setPreferredMajors(majors.isEmpty() ? new ArrayList<>() : Arrays.asList(majors.split(";")));
                    System.out.println("Preferred majors updated.\n");
                }
                case "7" -> {
                    Boolean cur = filter.getAvailable();
                    filter.setAvailable(cur == null ? true : !cur);
                    System.out.println("Available-only toggled.\n");
                }
                case "8" -> {
                    Boolean cur = filter.getVisibility();
                    filter.setVisibility(cur == null ? true : !cur);
                    System.out.println("Visibility-only toggled.\n");
                }
                case "9" -> {
                    System.out.print("Enter start date (YYYY-MM-DD) or blank to clear: ");
                    String d = scanner.nextLine().trim();
                    if (d.isEmpty()) {
                        filter.setFilterStartDate(null);
                    } else {
                        try { filter.setFilterStartDate(LocalDate.parse(d)); } catch (DateTimeParseException e) { System.out.println("Invalid date format."); }
                    }
                }
                case "10" -> {
                    System.out.print("Enter end date (YYYY-MM-DD) or blank to clear: ");
                    String d = scanner.nextLine().trim();
                    if (d.isEmpty()) {
                        filter.setFilterEndDate(null);
                    } else {
                        try { filter.setFilterEndDate(LocalDate.parse(d)); } catch (DateTimeParseException e) { System.out.println("Invalid date format."); }
                    }
                }
                case "11" -> {
                    filter.setTitleKeywords(null);
                    filter.setDescriptionKeywords(null);
                    filter.setCompanyName(null);
                    filter.setStatus(null);
                    filter.setInternshipLevels(new ArrayList<>());
                    filter.setPreferredMajors(new ArrayList<>());
                    filter.setAvailable(false);
                    filter.setVisibility(false);
                    filter.setFilterStartDate(null);
                    filter.setFilterEndDate(null);
                    System.out.println("All filters cleared.\n");
                }
                case "0" -> managing = false;
                default -> System.out.println("Invalid choice. Please try again.\n");
            }
        }
    }
}
