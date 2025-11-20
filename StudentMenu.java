import java.time.LocalDate;
import java.util.*;

public class StudentMenu extends Menu {
    private final Student student;
    private final InternshipRepo iRepo;
    private final ApplicationRepo aRepo;

    public StudentMenu(Scanner sc, Student s, InternshipRepo r, ApplicationRepo a) {
        super(sc, s);
        this.student = s;
        this.iRepo = r;
        this.aRepo = a;
        optionMap.put("3", this::viewStudentApplications);
        optionLabels.put("3", "View my applications");
        optionMap.put("4", this::changeOwnPassword);
        optionLabels.put("4", "Change password");
        optionMap.put("5", this::manageFilterSettings);
        optionLabels.put("5", "Manage filter settings");
    }

    @Override
    protected void viewOwnProfile() {
        displayUserHeader();
        System.out.println("Type: Student");
        System.out.println("Year of Study: " + student.getYearOfStudy());
        System.out.println("Major: " + student.getMajor());
        System.out.println("Applications: " + (student.getApplications() != null ? student.getApplications().size() : 0));
        System.out.println("Internships: " + (student.getAcceptedInternship() != null ? 1 : 0));
        System.out.println("================================\n");
    }

    @Override
    protected void browseInternships() {
        System.out.println("\n========== Browse Internships ==========");
        FilterSetting filter = student.getFilterSettings();

        InternshipRepo filtered = new InternshipRepo();
        for (Internship intern : iRepo.getAll()) {
            // Students should only see Approved internships that match their filters
            if (!"Approved".equals(intern.getStatus())) continue;
            if (!intern.getVisibility()) continue;
            if (intern.matchesFilter(filter)) {
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

                // Basic application eligibility checks
                if (!"Approved".equals(selected.getStatus())) {
                    System.out.println("Cannot apply: internship not approved.");
                    return;
                }
                if (selected.getSlots() == null || selected.getSlots() <= 0) {
                    System.out.println("Cannot apply: no available slots.");
                    return;
                }
                if (selected.getClosingDate() != null && selected.getClosingDate().isBefore(LocalDate.now())) {
                    System.out.println("Cannot apply: application period has closed.");
                    return;
                }
                // Already applied check
                for (Application existing : student.getApplications().getAll()) {
                    if (existing.getInternship() != null && selected.getInternshipID().equals(existing.getInternship().getInternshipID())) {
                        System.out.println("You have already applied to this internship.");
                        return;
                    }
                }
                // Accepted placement check
                if (student.getAcceptedInternship() != null) {
                    System.out.println("You have already accepted an internship and cannot apply to more.");
                    return;
                }

                Application app = new Application(selected, null, null);
                app.setStudent(student);
                try {
                    student.addApplication(app);
                    aRepo.add(app);
                    System.out.println("Application submitted!\n");
                } catch (IllegalArgumentException | IllegalStateException e) {
                    System.out.println("Failed to submit application: " + e.getMessage());
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.\n");
        }
    }

    private void viewStudentApplications() {
        System.out.println("\n========== My Applications ==========");
        ApplicationRepo apps = student.getApplications();

        if (apps == null || apps.isEmpty()) {
            System.out.println("No applications yet.\n");
            return;
        }

        for (int i = 0; i < apps.size(); i++) {
            Application app = apps.get(i);
            System.out.println((i + 1) + ". " + app.getInternship().getTitle() +
                " - Status: " + app.getStatus());
        }
        System.out.print("Select application: (1 to " + apps.size() + ", 0 to cancel): ");
        try {
            int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (idx >= 0 && idx < apps.size()) {
                Application selected = apps.get(idx);
                System.out.println("\n--- Application Details ---");
                System.out.println("Internship: " + selected.getInternship().getTitle());
                System.out.println("Company: " + selected.getInternship().getCompanyName());
                System.out.println("Level: " + selected.getInternship().getInternshipLevel());
                System.out.println("Status: " + selected.getStatus());
                System.out.println("---------------------------\n");
                System.out.println("1: Accept Internship (if Successful)");
                System.out.println("2: Withdraw Application");
                System.out.print("Enter choice (0 to cancel): ");
                String action = scanner.nextLine().trim();
                switch (action) {
                    case "1" -> {
                        if (!"Successful".equals(selected.getStatus())) {
                            System.out.println("Can only accept applications with 'Successful' status.");
                            return;
                        }
                        if (student.getAcceptedInternship() != null) {
                            System.out.println("You have already accepted an internship.");
                            return;
                        }
                        // Accept placement: record accepted application and withdraw others
                        student.setAcceptedInternship(selected);
                        Internship intern = selected.getInternship();
                        // decrement slots and mark filled if needed
                        if (intern.getSlots() != null) {
                            intern.setSlots(intern.getSlots() - 1);
                            if (intern.getSlots() <= 0) {
                                intern.setStatus("Filled");
                            }
                        }
                        // Withdraw other applications
                        for (Application other : apps.getAll()) {
                            if (other != selected) {
                                other.setWithdrawalStatus("Withdrawn");
                                other.setStatus("Unsuccessful");
                            }
                        }
                        System.out.println("Internship accepted. Congratulations!\n");
                    }
                    case "2" -> {
                        selected.setWithdrawalStatus("Pending");
                        System.out.println("Your withdrawal request has been sent to the Career Center Staff");
                    }
                    case "0" -> {
                        // Do nothing, just return
                    }
                    default -> System.out.println("Invalid choice.\n");
                }
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
            System.out.println(" Withdrawn Internships Hidden: " + filter.getWithdrawalHidden());
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
