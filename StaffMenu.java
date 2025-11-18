import java.util.ArrayList;
import java.util.List;

public class StaffMenu extends Menu {
    private CareerCenterStaff staff;

    public StaffMenu(CareerCenterStaff s) {
        this.staff = s;
    }

    public static void displayOptions(CareerCenterStaff s) {
        System.out.println("\n========== Internship Management System (Staff) ==========");
        System.out.println("Logged in as: " + s.getName() + " (" + s.getUserID() + ")");
        System.out.println("1: View my profile");
        System.out.println("2: View all internships");
        System.out.println("3: View all applications");
        System.out.println("4: Change my password");
        System.out.println("5: Generate Internship Report");
        System.out.println("6: Review Pending Company Reps");
        System.out.println("7: Review Pending Internships");
        System.out.println("8: Review Withdrawal Requests");
        System.out.println("0: Logout");
        System.out.println("====================================================");
    }

    public static void handleChoice(CareerCenterStaff s, String choice) {
        StaffMenu menu = new StaffMenu(s);
        menu.handleChoice(choice);
    }

    @Override
    public void handleChoice(String choice) {
        switch (choice) {
            case "1":
                viewOwnProfile();
                break;
            case "2":
                viewAllInternships();
                break;
            case "3":
                viewAllApplications();
                break;
            case "4":
                UserApp.changeOwnPassword();
                break;
            case "5":
                staff.generateInternshipReport();
                break;
            case "6": reviewPendingCompanyReps(); break;      
            case "7": reviewPendingInternships(); break;      
            case "8": reviewWithdrawalRequests(); break;
            case "0":
                UserApp.logoutCurrentUser();
                break;
            default:
                System.out.println("Invalid choice. Please try again.\n");
        }
    }

    private void viewOwnProfile() {
        displayUserHeader(staff);
        System.out.println("Type: Career Center Staff");
        System.out.println("Department: " + staff.getStaffDepartment());
        System.out.println("================================\n");
    }

    private void viewAllInternships() {
        System.out.println("\n========== All Internships ==========");
        if (allInternships.isEmpty()) {
            System.out.println("No internships available.\n");
            return;
        }

        for (int i = 0; i < allInternships.size(); i++) {
            Internship in = allInternships.get(i);
            System.out.println((i + 1) + ". " + in.getTitle() + " (" + in.getInternshipLevel() +
                ") - " + in.getCompanyName() + " | Visibility: " + in.getVisibility());
        }
        System.out.println();
    }

    private void viewAllApplications() {
        System.out.println("\n========== All Applications ==========");
        if (allApplications.isEmpty()) {
            System.out.println("No applications yet.\n");
            return;
        }

        for (int i = 0; i < allApplications.size(); i++) {
            Application app = allApplications.get(i);
            System.out.println((i + 1) + ". " + app.getInternship().getTitle() +
                " - Status: " + app.getStatus());
        }
        System.out.println();
    }

    private void reviewPendingCompanyReps() {
        List<CompanyRepresentative> pendingReps = new ArrayList<>();
        for (User user : Menu.users) {
            if (user instanceof CompanyRepresentative cr && !cr.getApproval()) {
                pendingReps.add(cr);
            }
        }

        if (pendingReps.isEmpty()) {
            System.out.println("\nNo pending company representative registrations.\n");
            return;
        }

        System.out.println("\n========== Pending Company Reps ==========");
        for (int i = 0; i < pendingReps.size(); i++) {
            CompanyRepresentative rep = pendingReps.get(i);
            System.out.println((i + 1) + ". " + rep.getName() + " (" + rep.getUserID() + ") - " + rep.getCompanyName());
        }

        System.out.print("\nSelect a rep to review (0 to cancel): ");
        try {
            int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (idx >= 0 && idx < pendingReps.size()) {
                staff.approveAccountCreation(pendingReps.get(idx));
            } else if (idx != -1) {
                System.out.println("Invalid selection.\n");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.\n");
        }
    }

    private void reviewPendingInternships() {
        List<Internship> pendingInternships = new ArrayList<>();
        List<CompanyRepresentative> owners = new ArrayList<>();

        // Find all pending internships and their owners
        for (Internship intern : Menu.allInternships) {
            if ("Pending".equals(intern.getStatus())) {
                pendingInternships.add(intern);
                // Find owner
                CompanyRepresentative owner = null;
                for (User user : Menu.users) {
                    if (user instanceof CompanyRepresentative cr &&
                        cr.getUserID().equals(intern.getCompanyRepIC())) {
                        owner = cr;
                        break;
                    }
                }
                owners.add(owner);
            }
        }

        if (pendingInternships.isEmpty()) {
            System.out.println("\nNo pending internships.\n");
            return;
        }

        System.out.println("\n========== Pending Internships ==========");
        for (int i = 0; i < pendingInternships.size(); i++) {
            Internship intern = pendingInternships.get(i);
            System.out.println((i + 1) + ". " + intern.getTitle() + " at " + intern.getCompanyName());
        }

        System.out.print("\nSelect an internship to review (0 to cancel): ");
        try {
            int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (idx >= 0 && idx < pendingInternships.size()) {
                Internship intern = pendingInternships.get(idx);
                CompanyRepresentative owner = owners.get(idx);
                if (owner != null) {
                    // Find index in owner's list (needed for deletion on reject)
                    int ownerIndex = owner.getInternships().indexOf(intern);
                    staff.approveInternship(intern, owner, ownerIndex);
                } else {
                    System.out.println("Owner not found. Approving without deletion capability.");
                    // Fallback: approve without delete
                    System.out.print("Approve? (1=Yes, 0=No): ");
                    int choice = Integer.parseInt(scanner.nextLine().trim());
                    if (choice == 1) {
                        intern.setStatus("Approved");
                        System.out.println("Approved.");
                    } else {
                        intern.setStatus("Rejected");
                        System.out.println("Rejected.");
                    }
                }
            } else if (idx != -1) {
                System.out.println("Invalid selection.\n");
            }
        } catch (Exception e) {
            System.out.println("Error processing selection: " + e.getMessage());
        }
    }


    private void reviewWithdrawalRequests() {
        List<Application> pendingWithdrawals = new ArrayList<>();
        for (Application app : Menu.allApplications) {
            if ("Pending".equals(app.getWithdrawalStatus())) {
                pendingWithdrawals.add(app);
            }
        }

        if (pendingWithdrawals.isEmpty()) {
            System.out.println("\nNo pending withdrawal requests.\n");
            return;
        }

        System.out.println("\n========== Pending Withdrawal Requests ==========");
        for (int i = 0; i < pendingWithdrawals.size(); i++) {
            Application app = pendingWithdrawals.get(i);
            System.out.println((i + 1) + ". " + app.getStudent().getName() + 
                " - " + app.getInternship().getTitle() + " (App ID: " + app.getApplicationID() + ")");
        }

        System.out.print("\nSelect a request to process (0 to cancel): ");
        try {
            int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (idx >= 0 && idx < pendingWithdrawals.size()) {
                Application app = pendingWithdrawals.get(idx);
                System.out.print("Approve withdrawal? (1 = Approve, 0 = Reject): ");
                int choice = Integer.parseInt(scanner.nextLine().trim());
                staff.approveWithdrawal(app, choice == 0); // Note: your method uses 'true' to keep active
                // Clarify logic: if choice==1 (approve withdrawal), then set to Withdrawn
                // But your current method: if(choice==true) -> set to Active (keep)
                // So: approve withdrawal => choice = false
            } else if (idx != -1) {
                System.out.println("Invalid selection.\n");
            }
        } catch (Exception e) {
            System.out.println("Invalid input.\n");
        }
    }

    /*when applying for an internship, can add this also:
    Application app = new Application(student, internship);
        Menu.allApplications.add(app);
     */
}
