import java.util.*;

public class StudentMenu extends Menu {
    private Student student;

    public StudentMenu(Student s) {
        this.student = s;
    }

    public static void displayOptions(Student s) {
        System.out.println("\n========== Internship Management System (Student) ==========");
        System.out.println("Logged in as: " + s.getName() + " (" + s.getUserID() + ")");
        System.out.println("1: View my profile");
        System.out.println("2: Browse internships");
        System.out.println("3: View my applications");
        System.out.println("4: Change my password");
        System.out.println("5: Manage filter settings");
        System.out.println("0: Logout");
        System.out.println("====================================================");
    }

    public static void handleChoice(Student s, String choice) {
        StudentMenu menu = new StudentMenu(s);
        menu.handleChoice(choice);
    }

    @Override
    public void handleChoice(String choice) {
        switch (choice) {
            case "1":
                viewOwnProfile();
                break;
            case "2":
                browseInternships();
                break;
            case "3":
                viewStudentApplications();
                break;
            case "4":
                UserApp.changeOwnPassword();
                break;
            case "5":
                manageFilterSettings();
                break;
            case "0":
                UserApp.logoutCurrentUser();
                break;
            default:
                System.out.println("Invalid choice. Please try again.\n");
        }
    }

    private void viewOwnProfile() {
        displayUserHeader(student);
        System.out.println("Type: Student");
        System.out.println("Year of Study: " + student.getYearOfStudy());
        System.out.println("Major: " + student.getMajor());
        System.out.println("Applications: " + (student.getApplications() != null ? student.getApplications().size() : 0));
        System.out.println("Internships: " + (student.getAcceptedInternship() != null ? 1 : 0));
        System.out.println("================================\n");
    }

    private void browseInternships() {
        System.out.println("\n========== Browse Internships ==========");
        FilterSetting filter = student.getFilterSettings();

        List<Internship> filtered = new ArrayList<>();
        for (Internship intern : allInternships) {
            if (matchesFilter(intern, filter)) {
                filtered.add(intern);
            }
        }

        if (filtered.isEmpty()) {
            System.out.println("No internships match your filter criteria.\n");
            return;
        }

        for (int i = 0; i < filtered.size(); i++) {
            Internship in = filtered.get(i);
            System.out.println((i + 1) + ". " + in.getTitle() + " (" + in.getInternshipLevel() +
                ") - " + in.getCompanyName() + " | Slots: " + in.getSlots());
        }

        System.out.print("\nSelect internship to apply (0 to cancel): ");
        try {
            int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (idx >= 0 && idx < filtered.size()) {
                Internship selected = filtered.get(idx);
                Application app = new Application(selected);
                student.addApplication(app);
                allApplications.add(app);
                System.out.println("Application submitted!\n");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.\n");
        }
    }

    private void viewStudentApplications() {
        System.out.println("\n========== My Applications ==========");
        List<Application> apps = student.getApplications();

        if (apps == null || apps.isEmpty()) {
            System.out.println("No applications yet.\n");
            return;
        }

        for (int i = 0; i < apps.size(); i++) {
            Application app = apps.get(i);
            System.out.println((i + 1) + ". " + app.getInternship().getTitle() +
                " - Status: " + app.getStatus());
        }
        
        System.out.print("\nSelect application to remove (0 to cancel): ");
        try {
            int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (idx >= 0 && idx < apps.size()) {
                Boolean removed = student.removeApplication(idx);
                if (removed) {
                    System.out.println("Application removed successfully!\n");
                } else {
                    System.out.println("Failed to remove application.\n");
                }
            } else if (idx != -1) {
                System.out.println("Invalid selection.\n");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.\n");
        }
    }

    private void manageFilterSettings() {
        System.out.println("\n========== Filter Settings ==========");
        FilterSetting filter = student.getFilterSettings();

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
            System.out.println();
            System.out.println("1: Change title keywords filter");
            System.out.println("2: Change description keywords filter");
            System.out.println("3: Change company name filter");
            System.out.println("4: Clear all filters");
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
                    filter.setTitleKeywords(null);
                    filter.setDescriptionKeywords(null);
                    filter.setCompanyName(null);
                    System.out.println("All filters cleared.\n");
                }
                case "0" -> managing = false;
                default -> System.out.println("Invalid choice. Please try again.\n");
            }
        }
    }
}
