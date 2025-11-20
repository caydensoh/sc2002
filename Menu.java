
import java.util.*;

public class Menu {
	protected Scanner scanner;
	protected User currentUser;
	protected Map<String, Runnable> optionMap = new LinkedHashMap<>();
    protected Map<String, String> optionLabels = new LinkedHashMap<>();

	public Menu(Scanner s, User u) {
		this.scanner = s;
		this.currentUser = u;
		optionMap.put("1", this::viewOwnProfile);
		optionLabels.put("1", "View my profile");
		optionMap.put("2", this::browseInternships);
		optionLabels.put("2", "Browse internships");
		optionMap.put("0", this::logoutCurrentUser);
		optionLabels.put("0", "Logout");
	}

	public String displayGetChoices() {
		ensureLogoutLast();
		System.out.println("\n========== Internship Management System ==========");
		System.out.println("Logged in as: " + currentUser.getName() + " (" + currentUser.getUserID() + ")");
		for (Map.Entry<String, Runnable> entry : optionMap.entrySet()) {
			String key = entry.getKey();
			String label = optionLabels.get(key);
			if (label == null) {
				label = switch (key) {
					default -> "Option " + key;
				};
			}
			System.out.println(key + ": " + label);
		}
		System.out.println("====================================================");
		System.out.print("Enter your choice: ");
		return scanner.nextLine().trim();
	}

	// ts pmo frfr holy shit
	private void ensureLogoutLast() {
		if (optionMap.containsKey("0")) {
			Runnable logout = optionMap.remove("0");
			String lbl = optionLabels.remove("0");
			optionMap.put("0", logout);
			if (lbl != null) optionLabels.put("0", lbl);
		}
	}

	public void handleChoices(String choice) {
		Runnable action = optionMap.get(choice);
		if (action != null) {
			action.run();
		} else {
			System.out.println("Invalid choice. Please try again.\n");
		}
	}

	protected void displayUserHeader() {
		System.out.println("\n========== My Profile ==========");
		System.out.println("ID: " + currentUser.getUserID());
		System.out.println("Name: " + currentUser.getName());
	}

	protected void changeOwnPassword() {
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

		if (currentUser.changePassword(oldPass, newPass)) {
			System.out.println("Password changed successfully.\n");
		} else {
			System.out.println("Password change failed.\n");
		}
	}

	protected void logoutCurrentUser() {
		System.out.println("You have been logged out.\n");
		currentUser = null;
	}

	protected void viewOwnProfile() {
		displayUserHeader();
		System.out.println("Type: " + currentUser.getClass().getSimpleName());
		System.out.println("================================\n");
	}

	protected void browseInternships() {
		System.out.println("\n========== Browse Internships ==========");
		System.out.println("(Generic view: internships would be listed here. Override for role-specific logic.)\n");
	}
}