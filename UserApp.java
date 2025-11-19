import java.util.*;

public class UserApp {

	private static final UserRepo users = new UserRepo();
	private static final InternshipRepo internships = new InternshipRepo();
	private static final ApplicationRepo applications = new ApplicationRepo();
	private static final Scanner scanner = new Scanner(System.in);

	public static void main(String[] args) {
		// Load all data on startup
		DataManager d = new DataManager(users, internships, applications);
		d.loadAllData();

		AppController appController = new AppController(scanner, users, internships, applications);
		appController.start();

		// Save all data before exit
		d.saveAllData();
		System.out.println("Exiting... All changes saved.");
		scanner.close();	
	}
}
