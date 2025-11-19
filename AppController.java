import java.util.*;
public class AppController {

    private final Scanner scanner;
    private final UserRepo users;
    private final InternshipRepo internships;
    private final ApplicationRepo applications;

    private User activeUser;

    public AppController(Scanner scanner, UserRepo users, InternshipRepo internships, ApplicationRepo applications) {
        this.scanner = scanner;
        this.users = users;
        this.internships = internships;
        this.applications = applications;
    }

    public void start() {
        loginLoop();
        if (activeUser != null) {
            System.out.println("Login successful. Entering main menu...\n");
            menuLoop();
        }
    }
    
    private void loginLoop() {
        LoginScreen loginMenu = new LoginScreen(scanner, users);
        activeUser = loginMenu.login();
    }

    private void menuLoop() {
        Menu menu = MenuFactory.getMenuForUser(scanner, activeUser, internships, applications, users);
        while (activeUser != null) {
            System.out.println("\n========== Main Menu ==========");
            String choice = menu.displayGetChoices();
            menu.handleChoices(choice);

            if (choice.equals("0")) {
                activeUser = null;
            }
        }
    }
}
