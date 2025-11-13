import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;

/**
 * Handles all CSV file I/O operations for users, internships, and applications.
 */
public class DataManager {
	private static final String USERS_CSV_PATH = "sample_student_list.csv";
	private static final String INTERNSHIPS_CSV_PATH = "internships.csv";
	private static final String APPLICATIONS_CSV_PATH = "applications.csv";
    private static final String FILTERS_CSV_PATH = "filter_settings.csv";

	/**
	 * Load all data from CSV files
	 */
	public static void loadAllData(List<User> users, List<Internship> allInternships, List<Application> allApplications) {
		loadInternshipsFromCSV(allInternships);
		loadApplicationsFromCSV(allApplications, allInternships);
		loadUsersFromCSV(users, allApplications);
		// Load per-student saved filter settings (optional file)
		loadFilterSettings(users);
	}

	/**
	 * Save all data to CSV files
	 */
	public static void saveAllData(List<User> users, List<Internship> allInternships, List<Application> allApplications) {
		saveUsersToCSV(users);
		saveInternshipsToCSV(allInternships);
		saveApplicationsToCSV(allApplications);
		// Persist per-student filter settings
		saveFilterSettings(users);
	}

	private static void loadFilterSettings(List<User> users) {
		try {
			List<String> lines = Files.readAllLines(Paths.get(FILTERS_CSV_PATH));
			if (lines.isEmpty()) return;
			for (int i = 1; i < lines.size(); i++) {
				String line = lines.get(i).trim();
				if (line.isEmpty()) continue;
				String[] parts = line.split(",");
				if (parts.length < 9) continue;
				String studentID = parts[0].trim();
				String titleKeywords = parts[1].trim();
				String descriptionKeywords = parts[2].trim();
				String internshipLevelsStr = parts[3].trim();
				String preferredMajorsStr = parts[4].trim();
				String status = parts[5].trim();
				String companyName = parts[6].trim();
				boolean avaliable = Boolean.parseBoolean(parts[7].trim());
				boolean visibility = Boolean.parseBoolean(parts[8].trim());

				User u = findUserByIDInList(studentID, users);
				if (u instanceof Student s) {
					List<String> preferredMajors = new ArrayList<>();
					if (!preferredMajorsStr.isEmpty()) {
						for (String m : preferredMajorsStr.split(";")) preferredMajors.add(m.trim());
					}
					List<String> internshipLevels = new ArrayList<>();
					if (!internshipLevelsStr.isEmpty()) {
						for (String l : internshipLevelsStr.split(";")) internshipLevels.add(l.trim());
					}
					FilterSetting fs = new FilterSetting(
						titleKeywords.isEmpty() ? null : titleKeywords,
						descriptionKeywords.isEmpty() ? null : descriptionKeywords,
						internshipLevels,
						preferredMajors,
						status.isEmpty() ? null : status,
						companyName.isEmpty() ? null : companyName,
						avaliable,
						visibility
					);
					s.setFilterSettings(fs);
				}
			}
		} catch (IOException e) {
			// no filter file - ignore
		}
	}

	private static User findUserByIDInList(String userID, List<User> users) {
		for (User u : users) if (u.getUserID().equals(userID)) return u;
		return null;
	}

	private static void saveFilterSettings(List<User> users) {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("StudentID,TitleKeywords,DescriptionKeywords,InternshipLevels,PreferredMajors,Status,CompanyName,Avaliable,Visibility\n");
			for (User u : users) {
				if (u instanceof Student s) {
					FilterSetting fs = s.getFilterSettings();
					String internshipLevelsStr = String.join(";", fs.getInternshipLevels() == null ? new ArrayList<>() : fs.getInternshipLevels());
					String preferredMajorsStr = String.join(";", fs.getPreferredMajors() == null ? new ArrayList<>() : fs.getPreferredMajors());
					String line = String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s\n",
						s.getUserID(),
						safe(fs.getTitleKeywords()),
						safe(fs.getDescriptionKeywords()),
						internshipLevelsStr,
						preferredMajorsStr,
						safe(fs.getStatus()),
						safe(fs.getCompanyName()),
						fs.getAvailable() == true ? "true" : fs.getAvailable().toString(),
						fs.getVisibility() == null ? "true" : fs.getVisibility().toString()
					);
					sb.append(line);
				}
			}
			Files.write(Paths.get(FILTERS_CSV_PATH), sb.toString().getBytes());
		} catch (IOException e) {
			System.out.println("Error writing filter settings: " + e.getMessage());
		}
	}

	private static String safe(String s) { return s == null ? "" : s.replaceAll(",", " "); }

	private static void loadUsersFromCSV(List<User> users, List<Application> allApplications) {
		users.clear();
		try {
			List<String> lines = Files.readAllLines(Paths.get(USERS_CSV_PATH));
			if (lines.isEmpty()) {
				System.out.println("Users CSV is empty.");
				return;
			}

			for (int i = 1; i < lines.size(); i++) {
				String line = lines.get(i).trim();
				if (line.isEmpty()) {
					continue;
				}

				String[] parts = line.split(",");
				if (parts.length < 4) {
					continue;
				}

				String studentID = parts[0].trim();
				String name = parts[1].trim();
				String major = parts[2].trim();
				String yearStr = parts[3].trim();
				String password = parts.length > 5 && !parts[5].trim().isEmpty() ? parts[5].trim() : "password";

				try {
					int year = Integer.parseInt(yearStr);
					Student student = new Student(studentID, name, password, year, major, null);

					// Populate applications if they exist (parts[6] contains semicolon-separated application IDs)
					if (parts.length > 6 && !parts[6].trim().isEmpty()) {
						String appIDsStr = parts[6].trim();
						String[] appIDs = appIDsStr.split(";");
						for (String appID : appIDs) {
							appID = appID.trim();
							// Find application by ID and add to student
							Application app = findApplicationByID(appID, allApplications);
							if (app != null) {
								student.addApplication(app);
							}
						}
					}

					// Set accepted internship if present (parts[7] contains application ID of accepted internship)
					if (parts.length > 7 && !parts[7].trim().isEmpty()) {
						String acceptedAppID = parts[7].trim();
						Application acceptedApp = findApplicationByID(acceptedAppID, allApplications);
						if (acceptedApp != null) {
							acceptedApp.setStatus("Accepted");
							setStudentAcceptedInternship(student, acceptedApp);
						}
					}

					users.add(student);
				} catch (NumberFormatException e) {
					System.out.println("Skipping line " + (i + 1) + ": invalid year format.");
				}
			}

			System.out.println("Loaded " + users.size() + " users.");
		} catch (IOException e) {
			System.out.println("Note: Users file not found or empty.");
		}
	}

	private static Application findApplicationByID(String appID, List<Application> allApplications) {
		for (Application app : allApplications) {
			if (app.getApplicationID().equals(appID)) {
				return app;
			}
		}
		return null;
	}

	private static void setStudentAcceptedInternship(Student student, Application app) {
		student.setAcceptedInternship(app);
	}

	private static void saveUsersToCSV(List<User> users) {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("StudentID,Name,Major,Year,Email,Password,ApplicationIDs,AcceptedInternshipID\n");

			for (User user : users) {
				if (user instanceof Student s) {
					// Collect application IDs
					StringBuilder appIDsBuilder = new StringBuilder();
					List<Application> apps = s.getApplications();
					if (apps != null && !apps.isEmpty()) {
						for (int i = 0; i < apps.size(); i++) {
							appIDsBuilder.append(apps.get(i).getApplicationID());
							if (i < apps.size() - 1) {
								appIDsBuilder.append(";");
							}
						}
					}

					// Get accepted internship ID if present
					String acceptedInternshipID = "";
					Application acceptedInternship = s.getAcceptedInternship();
					if (acceptedInternship != null) {
						acceptedInternshipID = acceptedInternship.getApplicationID();
					}

				String line = String.format("%s,%s,%s,%d,%s,%s,%s,%s\n",
					s.getUserID(), s.getName(), s.getMajor(), s.getYearOfStudy(),
					"", s.getPassword(), appIDsBuilder.toString(), acceptedInternshipID);
				sb.append(line);
				}
			}

			Files.write(Paths.get(USERS_CSV_PATH), sb.toString().getBytes());
			System.out.println("Users saved.");
		} catch (IOException e) {
			System.out.println("Error writing users CSV: " + e.getMessage());
		}
	}

	private static void loadInternshipsFromCSV(List<Internship> allInternships) {
		allInternships.clear();
		try {
			List<String> lines = Files.readAllLines(Paths.get(INTERNSHIPS_CSV_PATH));
			if (lines.isEmpty()) {
				return;
			}

			for (int i = 1; i < lines.size(); i++) {
				String line = lines.get(i).trim();
				if (line.isEmpty()) {
					continue;
				}

				String[] parts = line.split(",");
				if (parts.length < 12) {
					continue;
				}

				String internshipID = parts[0].trim();
				String title = parts[1].trim();
				String description = parts[2].trim();
				String level = parts[3].trim();
				String major = parts[4].trim();
				LocalDate openDate = LocalDate.parse(parts[5].trim());
				LocalDate closeDate = LocalDate.parse(parts[6].trim());
				String status = parts[7].trim();
				String company = parts[8].trim();
				String compRepID = parts[9].trim();
				int slots = Integer.parseInt(parts[10].trim());
				boolean visibility = Boolean.parseBoolean(parts[11].trim());

				Internship intern = new Internship(internshipID, title, description, level, major, openDate, closeDate,
					company, compRepID, slots);
				// Override the auto-generated UUID with the one from CSV
				intern.setInternshipID(internshipID);
				intern.setStatus(status);
				intern.setVisibility(visibility);
				allInternships.add(intern);
			}

			System.out.println("Loaded " + allInternships.size() + " internships.");
		} catch (IOException e) {
			System.out.println("Note: Internships file not found or empty.");
		}
	}

	private static void saveInternshipsToCSV(List<Internship> allInternships) {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("InternshipID,Title,Description,Level,PreferredMajor,OpeningDate,ClosingDate,Status,CompanyName,CompanyRepIC,Slots,Visibility\n");

			for (Internship intern : allInternships) {
				String line = String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%d,%s\n",
					intern.getInternshipID(),
					intern.getTitle(), intern.getDescription(), intern.getInternshipLevel(),
					intern.getPreferredMajor(), intern.getOpeningDate(), intern.getClosingDate(),
					intern.getStatus(), intern.getCompanyName(), intern.getCompanyRepIC(),
					intern.getSlots(), intern.getVisibility());
				sb.append(line);
			}

			Files.write(Paths.get(INTERNSHIPS_CSV_PATH), sb.toString().getBytes());
			System.out.println("Internships saved.");
		} catch (IOException e) {
			System.out.println("Error writing internships CSV: " + e.getMessage());
		}
	}

	private static void loadApplicationsFromCSV(List<Application> allApplications, List<Internship> allInternships) {
		allApplications.clear();
		try {
			List<String> lines = Files.readAllLines(Paths.get(APPLICATIONS_CSV_PATH));
			if (lines.isEmpty()) {
				return;
			}

			for (int i = 1; i < lines.size(); i++) {
				String line = lines.get(i).trim();
				if (line.isEmpty()) {
					continue;
				}

				String[] parts = line.split(",");
				if (parts.length < 3) {
					continue;
				}

				String applicationID = parts[0].trim();
				String internshipID = parts[1].trim();
				String status = parts[2].trim();

				// Find internship by ID
				Internship internship = findInternshipByID(internshipID, allInternships);
				if (internship != null) {
					Application app = new Application(applicationID, internship);
					app.setStatus(status);
					allApplications.add(app);
				}
			}

			System.out.println("Loaded " + allApplications.size() + " applications.");
		} catch (IOException e) {
			System.out.println("Note: Applications file not found or empty.");
		}
	}

	private static Internship findInternshipByID(String internshipID, List<Internship> allInternships) {
		for (Internship internship : allInternships) {
			if (internship.getInternshipID().equals(internshipID)) {
				return internship;
			}
		}
		return null;
	}

	private static void saveApplicationsToCSV(List<Application> allApplications) {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("ApplicationID,InternshipID,Status\n");

			for (Application app : allApplications) {
				String line = String.format("%s,%s,%s\n",
					app.getApplicationID(), app.getInternship().getInternshipID(), app.getStatus());
				sb.append(line);
			}

			Files.write(Paths.get(APPLICATIONS_CSV_PATH), sb.toString().getBytes());
			System.out.println("Applications saved.");
		} catch (IOException e) {
			System.out.println("Error writing applications CSV: " + e.getMessage());
		}
	}
}
