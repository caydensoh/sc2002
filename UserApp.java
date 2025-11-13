import java.util.*;

public class UserApp {

	private static final List<User> users = new ArrayList<>();
	private static final List<Internship> allInternships = new ArrayList<>();
	private static final List<Application> allApplications = new ArrayList<>();
	private static final Scanner scanner = new Scanner(System.in);
	private static User activeUser = null;

	public static void main(String[] args) {
		// Initialize Menu base class with shared resources
		Menu.initialize(scanner, users, allInternships, allApplications);

		// Load all data on startup
		DataManager.loadAllData(users, allInternships, allApplications);
		loginScreen();
		if (activeUser != null) {
			mainMenuLoop();
		}

		// Save all data before exit
		DataManager.saveAllData(users, allInternships, allApplications);
		System.out.println("Exiting... All changes saved.");
		scanner.close();
	}

	private static void loginScreen() {
		System.out.println("\n========== Welcome to User Management System ==========");
		System.out.println("1: Log in.");
		System.out.println("2: Exit.");
		String input = scanner.next();
		scanner.nextLine(); // consume newline
		Boolean running = true;
		while (running) {
			switch (input) {
				case "1":
					System.out.print("Enter User ID: ");
					String userID = scanner.nextLine().trim();

					// Find user by ID
					User user = findUserByID(userID);
					if (user == null) {
						System.out.println("User not found. Please try again.");
						break;
					}

					// Prompt for password
					System.out.print("Enter Password: ");
					String password = scanner.nextLine();

					// Attempt login
					if (user.login(password)) {
						activeUser = user;
						System.out.println("Welcome, " + activeUser.getName() + "!\n");
						running = false;
					} else {
						System.out.println("Login failed.\n");
					}
					break;
				case "2":
					running = false;
					break;
				default:
					System.out.println("Invalid choice. Please try again.\n");
					break;
			}
		}
	}

	private static User findUserByID(String userID) {
		for (User u : users) {
			if (u.getUserID().equals(userID)) {
				return u;
			}
		}
		return null;
	}

	private static void mainMenuLoop() {
		boolean running = true;
		while (running && activeUser != null) {
			// Delegate menu display & handling to role-specific menu classes
			if (activeUser instanceof Student) {
				StudentMenu.displayOptions((Student) activeUser);
				System.out.print("Enter your choice: ");
				String choice = scanner.nextLine().trim();
				StudentMenu.handleChoice((Student) activeUser, choice);
				if (choice.equals("0")) {
					running = false;
				}
			} else if (activeUser instanceof CompanyRepresentative) {
				CompanyRepMenu.displayOptions((CompanyRepresentative) activeUser);
				System.out.print("Enter your choice: ");
				String choice = scanner.nextLine().trim();
				CompanyRepMenu.handleChoice((CompanyRepresentative) activeUser, choice);
				if (choice.equals("0")) {
					running = false;
				}
			} else if (activeUser instanceof CarrerCenterStaff) {
				StaffMenu.displayOptions((CarrerCenterStaff) activeUser);
				System.out.print("Enter your choice: ");
				String choice = scanner.nextLine().trim();
				StaffMenu.handleChoice((CarrerCenterStaff) activeUser, choice);
				if (choice.equals("0")) {
					running = false;
				}
			}
		}
	}

	static void changeOwnPassword() {
		System.out.print("Enter current password: ");
		String oldPass = scanner.nextLine();
		System.out.print("Enter new password: ");
		String newPass = scanner.nextLine();
		System.out.print("Confirm new password: ");
		String confirmPass = scanner.nextLine();

		if (!newPass.equals(confirmPass)) {
			System.out.println("Passwords do not match. Change cancelled.\n");
			return;
		}

		if (activeUser.changePassword(oldPass, newPass)) {
			System.out.println("Password changed successfully.\n");
		} else {
			System.out.println("Password change failed.\n");
		}
	}

	static void logoutCurrentUser() {
		System.out.println("You have been logged out.\n");
		activeUser = null;
	}
}
