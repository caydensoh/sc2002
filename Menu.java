import java.util.*;

public class Menu {
    protected static Scanner scanner;
    protected static List<User> users;
    protected static List<Internship> allInternships;
    protected static List<Application> allApplications;

    public Menu() {
    }

    public static void initialize(Scanner s, List<User> u, List<Internship> i, List<Application> a) {
        scanner = s;
        users = u;
        allInternships = i;
        allApplications = a;
    }

    public void displayOptions() {
    }

    public void handleChoice(String choice) {
    }

    protected void displayUserHeader(User user) {
        System.out.println("\n========== My Profile ==========");
        System.out.println("ID: " + user.getUserID());
        System.out.println("Name: " + user.getName());
    }

    protected boolean matchesFilter(Internship intern, FilterSetting filter) {
        // Visible
        if (intern.getVisibility() != filter.getVisibility()) {
            return false;
        }

        // Avaliable
        if (filter.getAvailable() != null && filter.getAvailable()) {
            Integer slots = intern.getSlots();
            if (slots <= 0) {
                return false;
            }
        }

        // Match internship level(s) if filter specifies them
        if (filter.getInternshipLevels() != null && !filter.getInternshipLevels().isEmpty()) {
            String internLevel = intern.getInternshipLevel();
            boolean levelMatches = false;
            for (String level : filter.getInternshipLevels()) {
                if (internLevel.equalsIgnoreCase(level)) {
                    levelMatches = true;
                    break;
                }
            }
            if (!levelMatches) {
                return false;
            }
        }

        // Match company name if filter specifies it
        if (filter.getCompanyName() != null && !intern.getCompanyName().equalsIgnoreCase(filter.getCompanyName())) {
            return false;
        }

        // Match title keywords if filter specifies them
        if (filter.getTitleKeywords() != null && !intern.getTitle().toLowerCase().contains(filter.getTitleKeywords().toLowerCase())) {
            return false;
        }

        // Match description keywords if filter specifies them
        if (filter.getDescriptionKeywords() != null && !intern.getDescription().toLowerCase().contains(filter.getDescriptionKeywords().toLowerCase())) {
            return false;
        }

        // Match status if filter specifies it
        if (filter.getStatus() != null && !intern.getStatus().equals(filter.getStatus())) {
            return false;
        }

        // Match preferred majors if filter specifies them
        if (filter.getPreferredMajors() != null && !filter.getPreferredMajors().isEmpty()) {
            String internMajor = intern.getPreferredMajor();
            boolean majorMatches = false;
            for (String preferredMajor : filter.getPreferredMajors()) {
                if (internMajor.equalsIgnoreCase(preferredMajor)) {
                    majorMatches = true;
                    break;
                }
            }
            if (!majorMatches) {
                return false;
            }
        }
        return true;
    }
}