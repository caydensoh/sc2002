import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

public class CompanyRepMenu extends Menu {
    private final CompanyRepresentative compRep;
    private final InternshipRepo iRepo;
    private final ApplicationRepo aRepo;
    private final UserRepo userRepo;

    public CompanyRepMenu(Scanner s, CompanyRepresentative cr, InternshipRepo iRepo, ApplicationRepo aRepo, UserRepo userRepo) {
        super(s, cr);
        this.compRep = cr;
        this.iRepo = iRepo;
        this.aRepo = aRepo;
        this.userRepo = userRepo;
        optionMap.put("3", this::manageInternships);
        optionLabels.put("3", "Manage internships");
        optionMap.put("4", this::createInternship);
        optionLabels.put("4", "Create internship");
        optionMap.put("5", this::approveRejectApplications);
        optionLabels.put("5", "Approve/Reject applications");
        optionMap.put("6", this::changeOwnPassword);
        optionLabels.put("6", "Change password");
    }

    @Override
    protected void browseInternships() {
        System.out.println("\n========== Browse Internships ==========");
        InternshipRepo interns = compRep.getInternships();
        if (interns.isEmpty()) {
            System.out.println("No internships available.\n");
            return;
        }
        for (int i = 0; i < interns.size(); i++) {
            Internship in = interns.get(i);
            System.out.println((i + 1) + ". " + in.getTitle() + " (" + in.getInternshipLevel() +
                ") - " + in.getCompanyName() + " | Slots: " + in.getSlots());
        }
        System.out.println();
    }

    @Override
    protected void viewOwnProfile() {
        displayUserHeader();
        System.out.println("Type: Company Representative");
        System.out.println("Company: " + compRep.getCompanyName());
        System.out.println("Department: " + compRep.getDepartment());
        System.out.println("Position: " + compRep.getPosition());
        System.out.println("================================\n");
    }

    private void createInternship() {
        System.out.println("\n========== Create New Internship ==========");
        System.out.print("Title: ");
        String title = scanner.nextLine().trim();
        while (title.isEmpty()) {
            System.out.println("Title cannot be empty.");
            System.out.print("Title: ");
            title = scanner.nextLine().trim();
        }
        System.out.print("Description: ");
        String description = scanner.nextLine().trim();
        while (description.isEmpty()) {
            System.out.println("Description cannot be empty.");
            System.out.print("Description: ");
            description = scanner.nextLine().trim();
        }
        String level = "";
        boolean validLevel = false;
        while (!validLevel) {
            System.out.print("Level (Basic/Intermediate/Advanced): ");
            level = scanner.nextLine().trim();
            String lowerLevel = level.toLowerCase();
            if (lowerLevel.equals("basic") || lowerLevel.equals("intermediate") || lowerLevel.equals("advanced")) {
                // Normalize capitalization for consistency
                level = Character.toUpperCase(lowerLevel.charAt(0)) + lowerLevel.substring(1);
                validLevel = true;
            } else {
                System.out.println("Level must be 'Basic', 'Intermediate', or 'Advanced'.");
            }
        }
        System.out.print("Preferred Major: ");
        String major = scanner.nextLine().trim();
        while (major.isEmpty()) {
            System.out.println("Preferred Major cannot be empty.");
            System.out.print("Preferred Major: ");
            major = scanner.nextLine().trim();
        }
        LocalDate openDate = null;
        while (openDate == null) {
            System.out.print("Opening Date (YYYY-MM-DD): ");
            String dateInput = scanner.nextLine().trim();
            try {
                openDate = LocalDate.parse(dateInput);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use YYYY-MM-DD.");
            }
        }

        LocalDate closeDate = null;
        while (closeDate == null) {
            System.out.print("Closing Date (YYYY-MM-DD): ");
            String dateInput = scanner.nextLine().trim();
            try {
                closeDate = LocalDate.parse(dateInput);
                // Optional: ensure closeDate >= openDate
                if (closeDate.isBefore(openDate)) {
                    System.out.println("Closing date cannot be before opening date.");
                    closeDate = null;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use YYYY-MM-DD.");
            }
        }
        System.out.print("Number of Slots (1-10): ");
        Integer slots = null;
        while (slots == null || slots < 1 || slots > 10) {
            System.out.print("Number of Slots (1-10): ");
            try {
                slots = Integer.valueOf(scanner.nextLine().trim());
                if (slots < 1 || slots > 10) {
                    System.out.println("Slots must be between 1 and 10.");
                    slots = null;
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }

        String internshipId = "INT-" + String.format("%03d", compRep.getInternships().size() + 1);

        Internship intern = new Internship(internshipId, title, description, level, major, openDate, closeDate,
            compRep.getCompanyName(), compRep.getUserID(), slots);
        intern.setVisibility(true);
        Boolean added = compRep.addInternships(intern);
        if (added) {
            // keep central internship repo in sync
            if (iRepo != null) iRepo.add(intern); //hererer
            System.out.println("Internship created successfully!\n");
        } else {
            System.out.println("Failed to create internship.\n");
        }
    }

    private LocalDate parseDate(String date) {
        try {
            return LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Use YYYY-MM-DD.");
            return null;
        }
    }

    private Integer readSlots() {
        while (true) {
            System.out.print("Number of Slots (1-10): ");
            try {
                int slots = Integer.parseInt(scanner.nextLine().trim());
                if (slots >= 1 && slots <= 10) return slots;
                System.out.println("Slots must be between 1 and 10.");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private void manageInternships() {
        InternshipRepo interns = compRep.getInternships();
        if (interns.isEmpty()) {
            System.out.println("\nNo internships yet.\n");
            return;
        }

        System.out.println("\n========== My Internships ==========");
        for (int i = 0; i < interns.size(); i++) {
            System.out.println((i + 1) + ". " + interns.get(i).getTitle() +
                " - Status: " + interns.get(i).getStatus());
        }

        System.out.print("\nSelect internship to manage (0 to cancel): ");
        try {
            int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (idx >= 0 && idx < interns.size()) {
                updateInternshipMenu(interns.get(idx));
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.\n");
        }
    }

    private void updateInternshipMenu(Internship intern) {
        if (!"Pending".equals(intern.getStatus())) {
            System.out.println("Cannot update: status is '" + intern.getStatus() + "'.\n");
            return;
        }

        while (true) {
            System.out.println("\n--- Update Internship Menu ---");
            System.out.println("1: Title | 2: Description | 3: Level | 4: Major");
            System.out.println("5: Open Date | 6: Close Date | 7: Slots | 8: Toggle Visibility");
            System.out.println("9: Delete | 10: View Apps | 0: Save & Exit");
            System.out.print("Choice: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> {
                    System.out.print("New title: ");
                    intern.setTitle(scanner.nextLine().trim());
                }
                case "2" -> {
                    System.out.print("New description: ");
                    intern.setDescription(scanner.nextLine().trim());
                }
                case "3" -> {
                    System.out.print("New level (Basic/Intermediate/Advanced): ");
                    String level = scanner.nextLine().trim();
                    if (List.of("Basic", "Intermediate", "Advanced").contains(level)) {
                        intern.setInternshipLevel(level);
                    } else {
                        System.out.println("Invalid level.");
                    }
                }
                case "4" -> {
                    System.out.print("New major: ");
                    intern.setPreferredMajor(scanner.nextLine().trim());
                }
                case "5" -> {
                    System.out.print("New opening date (YYYY-MM-DD): ");
                    LocalDate d = parseDate(scanner.nextLine().trim());
                    if (d != null) intern.setOpeningDate(d);
                }
                case "6" -> {
                    System.out.print("New closing date (YYYY-MM-DD): ");
                    LocalDate d = parseDate(scanner.nextLine().trim());
                    if (d != null) intern.setClosingDate(d);
                }
                case "7" -> {
                    Integer slots = readSlots();
                    if (slots != null) intern.setSlots(slots);
                }
                case "8" -> {
                    intern.setVisibility(!intern.getVisibility());
                    System.out.println("Visibility: " + (intern.getVisibility() ? "ON" : "OFF"));
                }
                case "9" -> {
                    System.out.print("Delete? (yes/no): ");
                    if ("yes".equalsIgnoreCase(scanner.nextLine().trim())) {
                        compRep.getInternships().remove(intern);
                        if (iRepo != null) iRepo.remove(intern);
                    }
                }
                case "10" -> processApplicationsForInternship(intern);
                case "0" -> {
                    System.out.println("Updates saved.\n");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void approveRejectApplications() {
        InternshipRepo approvedInterns = compRep.getApprovedInternships();
        if (approvedInterns.isEmpty()) {
            System.out.println("No approved internships with applications.");
            return;
        }

        for (int i = 0; i < approvedInterns.size(); i++) {
            System.out.println((i + 1) + ". " + approvedInterns.get(i).getTitle());
        }
        System.out.print("Select internship (0 to cancel): ");
        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            if (choice >= 1 && choice <= approvedInterns.size()) {
                processApplicationsForInternship(approvedInterns.get(choice - 1));
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }
    
    
    private void processApplicationsForInternship(Internship intern) {
        ApplicationRepo appRepoLocal = this.aRepo != null ? this.aRepo : compRep.getAppRepo();
        // userRepo is available via constructor or compRep, not used directly here
        if (appRepoLocal == null) {
            System.out.println("No application repository available.");
            return;
        }


        System.out.println("\n=== Applications for: " + intern.getTitle() + " ===");
        boolean found = false;

        for (Application app : appRepoLocal.getAll()) {
            if (intern.getInternshipID().equals(app.getInternship().getInternshipID())) {
                Student student = null;
                if (userRepo != null && app.getStudent() != null) {
                    student = (Student) userRepo.find(app.getStudent().getUserID());
                }
                if (student == null) {
                    try { student = app.getStudent(); } catch (Exception e) { /* ignore */ }
                }

                System.out.println("Application ID: " + app.getApplicationID());
                System.out.println("Student: " + (student != null ? student.getName() + " (" + student.getUserID() + ")" : "Unknown"));
                System.out.println("Status: " + app.getStatus());
                System.out.println("Withdrawal: " + app.getWithdrawalStatus());
                System.out.println("----------------------------------------");
                found = true;

                if ("Pending".equalsIgnoreCase(app.getStatus())) {
                    System.out.print("Approve (A), Reject (R), or Skip (S)? ");
                    String action = scanner.nextLine().trim().toLowerCase();
                    if ("a".equals(action)) {
                        app.setStatus("Successful");
                        System.out.println("Application approved.");

                        // decrement slots and update status if needed
                        if (intern.getSlots() != null) {
                            intern.setSlots(intern.getSlots() - 1);
                            if (intern.getSlots() <= 0) {
                                intern.setStatus("Filled");
                                System.out.println("Internship '" + intern.getTitle() + "' is now filled!");

                                // mark other pending applications for this internship as unsuccessful
                                for (Application other : appRepoLocal.getAll()) {
                                    if (other != app && intern.getInternshipID().equals(other.getInternship().getInternshipID())
                                            && "Pending".equalsIgnoreCase(other.getStatus())) {
                                        other.setStatus("Unsuccessful");
                                    }
                                }
                            } else {
                                // not yet filled, still may be more accepts later
                                compRep.checkIfInternshipFilled(intern); // keep helper up-to-date
                            }
                        } else {
                            // if slots not set, still call helper to keep behavior consistent
                            compRep.checkIfInternshipFilled(intern);
                        }
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

    /*
    public void viewApplicationsForInternship(Internship intern) {
		if (this.internships.find(intern.getInternshipID()) != null) {
			System.out.println("You do not manage this internship.");
			return;
		}

		System.out.println("\n=== Applications for: " + intern.getTitle() + " ===");
		boolean found = false;

		for (Application app : Menu.allApplications) {
			if (app.getInternship() == intern) {
				System.out.println("Application ID: " + app.getApplicationID());
				System.out.println("Student: " + app.getStudent().getName() + " (" + app.getStudent().getUserID() + ")");
				System.out.println("Status: " + app.getStatus());
				System.out.println("Withdrawal Status: " + app.getWithdrawalStatus());
				System.out.println("----------------------------------------");
				found = true;

				// Only allow action if application is still pending
				if ("Pending".equalsIgnoreCase(app.getStatus())) {
					System.out.println("Would you like to approve or reject this application?");
					System.out.println("1 = Approve");
					System.out.println("0 = Reject");
					System.out.println("-1 = Skip to next application");

					int choice = -2;
					while (choice != -1 && choice != 0 && choice != 1) {
						try {
							System.out.print("Enter your choice: ");
							choice = Integer.parseInt(sc.nextLine().trim());
							if (choice != -1 && choice != 0 && choice != 1) {
								System.out.println("Invalid choice. Please enter 1, 0, or -1.");
							}
						} catch (NumberFormatException e) {
							System.out.println("Please enter a valid number (1, 0, or -1).");
						}
					}

					if (choice == 1) {
						// Approve application
						app.setStatus("Successful");
						System.out.println("Application approved for student: " + app.getStudent().getName());

						// Check if internship is now filled
						long successfulCount = 0;
						for (Application a : Menu.allApplications) {
							if (a.getInternship() == intern && "Successful".equals(a.getStatus())) {
								successfulCount++;
							}
						}
						if (successfulCount >= intern.getSlots()) {
							intern.setStatus("Filled");
							System.out.println(" Internship '" + intern.getTitle() + "' is now FILLED!");
						}
					} else if (choice == 0) {
						// Reject application
						app.setStatus("Unsuccessful");
						System.out.println("Application rejected for student: " + app.getStudent().getName());
					}
					// If choice == -1, do nothing and continue
				} 
				System.out.println(); // blank line for readability
			}
		}

		if (!found) {
			System.out.println("No applications found.");
		}
	}*/
}
