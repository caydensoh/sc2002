import java.io.*;
import java.nio.file.*;
import java.util.*;

public class FilterSettingsLoader implements Loader {
    private final UserRepo userRepo;

    public FilterSettingsLoader(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public void load() {
        try {
            List<String> lines = Files.readAllLines(Paths.get(CsvPaths.FILTERS));
            if (lines.isEmpty()) return;
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i).trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length < 9) continue;
                String studentID = parts[0].trim();
                User u = findUserByID(studentID);
                if (u instanceof Student s) {
                    FilterSetting fs = parseFilterSetting(parts);
                    s.setFilterSettings(fs);
                }
            }
        } catch (IOException e) {
            // no filter file - ignore
        }
    }

    @Override
    public void save() {
        try (FileWriter fw = new FileWriter(CsvPaths.FILTERS)) {
            fw.write("StudentID,TitleKeywords,DescriptionKeywords,InternshipLevels,PreferredMajors,Status,CompanyName,Avaliable,Visibility\n");
            for (User u : userRepo.getAll()) {
                if (u instanceof Student s) {
                    FilterSetting fs = s.getFilterSettings();
                    if (fs != null) {
                        String line = formatFilterSetting(s.getUserID(), fs);
                        fw.write(line);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error writing filter settings: " + e.getMessage());
        }
    }

    private FilterSetting parseFilterSetting(String[] parts) {
        String titleKeywords = parts[1].isEmpty() ? null : parts[1].trim();
        String descriptionKeywords = parts[2].isEmpty() ? null : parts[2].trim();
        String internshipLevelsStr = parts[3].trim();
        String preferredMajorsStr = parts[4].trim();
        String status = parts[5].isEmpty() ? null : parts[5].trim();
        String companyName = parts[6].isEmpty() ? null : parts[6].trim();
        boolean available = Boolean.parseBoolean(parts[7].trim());
        boolean visibility = Boolean.parseBoolean(parts[8].trim());

        List<String> levels = internshipLevelsStr.isEmpty() ? new ArrayList<>() : new ArrayList<>(Arrays.asList(internshipLevelsStr.split(";")));
        List<String> majors = preferredMajorsStr.isEmpty() ? new ArrayList<>() : new ArrayList<>(Arrays.asList(preferredMajorsStr.split(";")));

        return new FilterSetting(titleKeywords, descriptionKeywords, levels, majors, status, companyName, available, visibility);
    }

    private String formatFilterSetting(String studentID, FilterSetting fs) {
        String levels = fs.getInternshipLevels() != null ? String.join(";", fs.getInternshipLevels()) : "";
        String majors = fs.getPreferredMajors() != null ? String.join(";", fs.getPreferredMajors()) : "";
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s\n",
            studentID,
            safe(fs.getTitleKeywords()),
            safe(fs.getDescriptionKeywords()),
            levels,
            majors,
            safe(fs.getStatus()),
            safe(fs.getCompanyName()),
            fs.getAvailable(),
            fs.getVisibility()
        );
    }

    private User findUserByID(String userID) {
        for (User u : userRepo.getAll()) {
            if (u.getUserID().equals(userID)) {
                return u;
            }
        }
        return null;
    }

    private String safe(String s) {
        return s == null ? "" : s.replaceAll(",", " ");
    }
}
