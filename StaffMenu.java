import java.util.*;

public class StaffMenu extends Menu {
    private CarrerCenterStaff staff;

    public StaffMenu(CarrerCenterStaff s) {
        this.staff = s;
    }

    public static void displayOptions(CarrerCenterStaff s) {
        System.out.println("\n========== Internship Management System (Staff) ==========");
        System.out.println("Logged in as: " + s.getName() + " (" + s.getUserID() + ")");
        System.out.println("1: View my profile");
        System.out.println("2: View all internships");
        System.out.println("3: View all applications");
        System.out.println("4: Change my password");
        System.out.println("0: Logout");
        System.out.println("====================================================");
    }

    public static void handleChoice(CarrerCenterStaff s, String choice) {
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
}
