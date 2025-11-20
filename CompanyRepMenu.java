import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

public class CompanyRepMenu extends Menu {
    private final CompanyRepresentative compRep;
    private final Scanner scanner;
    

    public CompanyRepMenu(Scanner s, CompanyRepresentative cr) {
        super(s, cr);
        this.scanner = s;
        this.compRep = cr;
    }

    @Override
    public String displayGetChoices() {
        System.out.println("\n========== Internship Management System (Company Rep) ==========");
        System.out.println("Logged in as: " + compRep.getName() + " (" + compRep.getUserID() + ")");
        System.out.println("1: View my profile");
        System.out.println("2: Manage my Internships (iRepo)");
        System.out.println("3: Create new internship");
        System.out.println("4: Approve/reject internships");
        System.out.println("5: Change my password");
        System.out.println("0: Logout");
        System.out.println("====================================================");
        System.out.print("Enter your choice: ");
        String input = scanner.nextLine().trim();
        return input;
    }

    @Override
    public void handleChoices(String choice) {
        switch (choice) {
            case "1" -> compRep.displayProfile();
            case "2" -> manageInternships();
            case "3" -> createInternship();
            case "4" -> approveRejectApplications();
            case "5" -> changeOwnPassword();
            case "0" -> logoutCurrentUser();
            default -> System.out.println("Invalid choice.\n");
        }
    }

    /*private void viewOwnProfile() {
        displayUserHeader();
        System.out.println("Type: Company Representative");
        System.out.println("Company: " + compRep.getCompanyName());
        System.out.println("Department: " + compRep.getDepartment());
        System.out.println("Position: " + compRep.getPosition());
        System.out.println("================================\n");
    }*/

    private void createInternship() {
        System.out.println("\n========== Create New Internship ==========");
        System.out.print("Title: ");
        String title = scanner.nextLine().trim();
        System.out.print("Description: ");
        String description = scanner.nextLine().trim();
        System.out.print("Level (Basic/Intermediate/Advanced): ");
        String level = scanner.nextLine().trim();
        System.out.print("Preferred Major: ");
        String major = scanner.nextLine().trim();
        System.out.print("Opening Date (YYYY-MM-DD): ");
        LocalDate openDate = LocalDate.parse(scanner.nextLine().trim());
        System.out.print("Closing Date (YYYY-MM-DD): ");
        LocalDate closeDate = LocalDate.parse(scanner.nextLine().trim());
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
            compRep.getInternships().add(intern);
            System.out.println("Internship created successfully!\n");
        } else {
            System.out.println("Failed to create internship.\n");
        }
    }

    private LocalDate parseDate(String label) {
        try {
            return LocalDate.parse(scanner.nextLine().trim());
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
        List<Internship> interns = compRep.getInternships();
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
                    LocalDate d = parseDate("Opening");
                    if (d != null) intern.setOpeningDate(d);
                }
                case "6" -> {
                    System.out.print("New closing date (YYYY-MM-DD): ");
                    LocalDate d = parseDate("Closing");
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
                        // Find index and delete
                        List<Internship> all = compRep.getInternships();
                        int idx = all.indexOf(intern);
                        if (idx >= 0) {
                            compRep.deleteInternship(idx);
                            return;
                        }
                    }
                }
                case "10" -> compRep.viewAndProcessApplicationsForInternship(intern, scanner);
                case "0" -> {
                    System.out.println("Updates saved.\n");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void approveRejectApplications() {
        List<Internship> approvedInterns = compRep.getApprovedInternships();
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
                compRep.viewAndProcessApplicationsForInternship(approvedInterns.get(choice - 1), scanner);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
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
