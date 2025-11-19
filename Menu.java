
import java.util.Scanner;

public class Menu {
    protected Scanner scanner;
    protected User currentUser;

    public Menu(Scanner s, User u) {
        this.scanner = s;
        this.currentUser = u;
    }

    public String displayGetChoices() {
        return "";
    }

    public void handleChoices(String choice) {
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
}