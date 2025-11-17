import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

public class CompanyRepMenu extends Menu {
    private CompanyRepresentative compRep;

    public CompanyRepMenu(CompanyRepresentative cr) {
        this.compRep = cr;
    }

    public static void displayOptions(CompanyRepresentative cr) {
        System.out.println("\n========== Internship Management System (Company Rep) ==========");
        System.out.println("Logged in as: " + cr.getName() + " (" + cr.getUserID() + ")");
        System.out.println("1: View my profile");
        System.out.println("2: Manage my internships");
        System.out.println("3: Create new internship");
        System.out.println("4: Change my password");
        System.out.println("0: Logout");
        System.out.println("====================================================");
    }

    public static void handleChoice(CompanyRepresentative cr, String choice) {
        CompanyRepMenu menu = new CompanyRepMenu(cr);
        menu.handleChoice(choice);
    }

    @Override
    public void handleChoice(String choice) {
        switch (choice) {
            case "1":
                viewOwnProfile();
                break;
            case "2":
                manageCompanyInternships();
                break;
            case "3":
                createNewInternship();
                break;
            case "4":
                UserApp.changeOwnPassword();
                break;
            case "0":
                UserApp.logoutCurrentUser();
                break;
            default:
                System.out.println("Invalid choice. Please try again.\n");
        }
    }

    private void viewOwnProfile() {
        displayUserHeader(compRep);
        System.out.println("Type: Company Representative");
        System.out.println("Company: " + compRep.getCompanyName());
        System.out.println("Department: " + compRep.getDepartment());
        System.out.println("Position: " + compRep.getPosition());
        System.out.println("================================\n");
    }

    private void createNewInternship() {
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
        int slots = Integer.parseInt(scanner.nextLine().trim());

        Internship intern = new Internship(title, description, level, major, openDate, closeDate,
            compRep.getCompanyName(), compRep.getUserID(), slots);
        intern.setVisibility(true);
        Boolean added = compRep.addInternships(intern);
        if (added) {
            allInternships.add(intern);
            System.out.println("Internship created successfully!\n");
        } else {
            System.out.println("Failed to create internship.\n");
        }
    }

    private void manageCompanyInternships() {
        System.out.println("\n========== My Internships ==========");
        List<Internship> interns = compRep.getInternships();

        if (interns == null || interns.isEmpty()) {
            System.out.println("No internships yet.\n");
            return;
        }

        for (int i = 0; i < interns.size(); i++) {
            System.out.println((i + 1) + ". " + interns.get(i).getTitle() +
                " - Status: " + interns.get(i).getStatus());
        }
        
        System.out.print("\nSelect internship to manage (0 to cancel): ");
        try {
            int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (idx >= 0 && idx < interns.size()) {
                updateInternshipMenu(idx);
            } else if (idx != -1) {
                System.out.println("Invalid selection.\n");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.\n");
        }
    }

    private void updateInternshipMenu(int index) {
        Internship target = compRep.getInternships().get(index);
        
        // Only allow editing if status is "Pending" (before approval)
        if (!target.getStatus().equals("Pending")) {
            System.out.println("Cannot update internship after it has been approved/rejected/filled.\n");
            return;
        }

        boolean editing = true;
        while (editing) {
            System.out.println("\n--- Update Internship Menu ---");
            System.out.println("1: Update Title");
            System.out.println("2: Update Description");
            System.out.println("3: Update Level (Basic/Intermediate/Advanced)");
            System.out.println("4: Update Preferred Major");
            System.out.println("5: Update Opening Date (YYYY-MM-DD)");
            System.out.println("6: Update Closing Date (YYYY-MM-DD)");
            System.out.println("7: Update Number of Slots (1-10)");
            System.out.println("8: Toggle Visibility");
            System.out.println("9: Delete Internship");
            System.out.println("10: Review Internship Application");
            System.out.println("0: Save and Exit");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> {
                    System.out.print("Enter new title: ");
                    target.setTitle(scanner.nextLine().trim());
                    System.out.println("Title updated.\n");
                }
                case "2" -> {
                    System.out.print("Enter new description: ");
                    target.setDescription(scanner.nextLine().trim());
                    System.out.println("Description updated.\n");
                }
                case "3" -> {
                    System.out.print("Enter new level (Basic/Intermediate/Advanced): ");
                    String level = scanner.nextLine().trim();
                    if (level.equals("Basic") || level.equals("Intermediate") || level.equals("Advanced")) {
                        target.setInternshipLevel(level);
                        System.out.println("Level updated.\n");
                    } else {
                        System.out.println("Invalid level. No change made.\n");
                    }
                }
                case "4" -> {
                    System.out.print("Enter new preferred major: ");
                    target.setPreferredMajor(scanner.nextLine().trim());
                    System.out.println("Preferred major updated.\n");
                }
                case "5" -> {
                    System.out.print("Enter new opening date (YYYY-MM-DD): ");
                    String openingInput = scanner.nextLine().trim();
                    try {
                        LocalDate openingDate = LocalDate.parse(openingInput);
                        target.setOpeningDate(openingDate);
                        System.out.println("Opening date updated.\n");
                    } catch (DateTimeParseException e) {
                        System.out.println("Invalid date format. Please use YYYY-MM-DD. No change made.\n");
                    }
                }
                case "6" -> {
                    System.out.print("Enter new closing date (YYYY-MM-DD): ");
                    String closingInput = scanner.nextLine().trim();
                    try {
                        LocalDate closingDate = LocalDate.parse(closingInput);
                        target.setClosingDate(closingDate);
                        System.out.println("Closing date updated.\n");
                    } catch (DateTimeParseException e) {
                        System.out.println("Invalid date format. Please use YYYY-MM-DD. No change made.\n");
                    }
                }
                case "7" -> {
                    System.out.print("Enter number of slots (1-10): ");
                    String slotsInput = scanner.nextLine().trim();
                    try {
                        int slots = Integer.parseInt(slotsInput);
                        if (slots >= 1 && slots <= 10) {
                            target.setSlots(slots);
                            System.out.println("Slots updated.\n");
                        } else {
                            System.out.println("Invalid number of slots. Must be between 1 and 10.\n");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid number entered. No change made.\n");
                    }
                }
                case "8" -> {
                    target.setVisibility(!target.getVisibility());
                    System.out.println("Visibility toggled to " + (target.getVisibility() ? "ON" : "OFF") + "\n");
                }
                case "9" -> {
                    System.out.print("Are you sure you want to delete this internship? (yes/no): ");
                    if (scanner.nextLine().trim().equalsIgnoreCase("yes")) {
                        Boolean deleted = compRep.deleteInternship(index);
                        if (deleted) {
                            System.out.println("Internship deleted successfully.\n");
                            editing = false;
                        }
                    } else {
                        System.out.println("Delete cancelled.\n");
                    }
                }
                case "10" -> {
                    compRep.viewApplicationsForInternship(target);
                }
                case "0" -> {
                    editing = false;
                    System.out.println("Updates saved.\n");
                }
                default -> System.out.println("Invalid choice. Try again.\n");
            }
        }
    }
}
