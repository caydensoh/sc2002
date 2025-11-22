import java.util.Scanner;
public class LoginScreen {
	private final Scanner scanner;
	private final UserRepo userRepo;

	public LoginScreen(Scanner scanner, UserRepo userRepo) {
		this.scanner = scanner;
		this.userRepo = userRepo;
	}
	public User login() {
		while (true) {
			System.out.println("\n========== Welcome to User Management System ==========");
			System.out.println("1: Log in.");
			System.out.println("2: Exit.");
			System.out.print("Choice: ");

			String input = scanner.nextLine().trim();
			switch (input) {
				case "1" -> {
					User user = validate();
					if (user != null) return user;
				}
				case "2" -> { return null; }
				default -> System.out.println("Invalid choice. Please try again.");
			}
		}
	}

	public User validate() {
		System.out.print("Enter User ID: ");
		String userID = scanner.nextLine().trim();
		
		// Find user by ID
		User user = userRepo.find(userID);
		if (user == null) {
			System.out.println("User not found. Please try again.");
			return null;
		}
		
		// Prompt for password
		System.out.print("Enter Password: ");
		String password = scanner.nextLine();
		
		// Attempt login
		if (user.login(password)) {
			// Check if company rep needs approval
			if (user instanceof CompanyRepresentative rep) {
				if (!rep.getApproval()) {
					System.out.println("Your account is pending approval from Career Center Staff.");
					System.out.println("Login aborted. You have been logged out.\n");
					return null;
				}
			}
			System.out.println("Welcome, " + user.getName() + "!\n");
			return user;
		} else {
			System.out.println("Login failed.\n");
			return null;
		}
	}
}