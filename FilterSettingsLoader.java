import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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
                String[] parts = line.split(",", -1); // Keep empty trailing fields
                if (parts.length < 10) continue; // Skip rows with incorrect column count (expect at least 10)
                String studentID = parts[0].trim();
                User u = findUserByID(studentID);
                if (u instanceof Student s) {
                    FilterSetting fs = parseFilterSetting(parts);
                    s.setFilterSettings(fs);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading filter settings: " + e.getMessage());
        }
    }

    @Override
    public void save() {
        try (FileWriter fw = new FileWriter(CsvPaths.FILTERS)) {
            fw.write("StudentID,TitleKeywords,DescriptionKeywords,InternshipLevels,PreferredMajors,Status,CompanyName,Available,Visibility,WithdrawalHidden,StartDate,EndDate\n");
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
        String status = parts.length > 5 && parts[5].isEmpty() ? null : (parts.length > 5 ? parts[5].trim() : null);
        String companyName = parts.length > 6 && parts[6].isEmpty() ? null : (parts.length > 6 ? parts[6].trim() : null);
        Boolean available = null;
        if (parts.length > 7 && !parts[7].isEmpty()) available = Boolean.parseBoolean(parts[7].trim());
        Boolean visibility = null;
        if (parts.length > 8 && !parts[8].isEmpty()) visibility = Boolean.parseBoolean(parts[8].trim());
        Boolean withdrawalHidden = false;
        if (parts.length > 9 && !parts[9].isEmpty()) withdrawalHidden = Boolean.parseBoolean(parts[9].trim());

        List<String> levels = internshipLevelsStr.isEmpty() ? new ArrayList<>() : new ArrayList<>(Arrays.asList(internshipLevelsStr.split(";")));
        List<String> majors = preferredMajorsStr.isEmpty() ? new ArrayList<>() : new ArrayList<>(Arrays.asList(preferredMajorsStr.split(";")));

        FilterSetting fs = new FilterSetting(titleKeywords, descriptionKeywords, levels, majors, status, companyName, available == null ? false : available, visibility == null ? false : visibility, withdrawalHidden);

        // optional start/end date columns
        if (parts.length > 10 && !parts[10].isEmpty()) {
            try { fs.setFilterStartDate(LocalDate.parse(parts[10].trim())); } catch (DateTimeParseException e) { /* ignore parse errors */ }
        }
        if (parts.length > 11 && !parts[11].isEmpty()) {
            try { fs.setFilterEndDate(LocalDate.parse(parts[11].trim())); } catch (DateTimeParseException e) { /* ignore parse errors */ }
        }

        return fs;
    }

    private String formatFilterSetting(String studentID, FilterSetting fs) {
        String levels = fs.getInternshipLevels() != null ? String.join(";", fs.getInternshipLevels()) : "";
        String majors = fs.getPreferredMajors() != null ? String.join(";", fs.getPreferredMajors()) : "";
        String start = fs.getFilterStartDate() == null ? "" : fs.getFilterStartDate().toString();
        String end = fs.getFilterEndDate() == null ? "" : fs.getFilterEndDate().toString();
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s\n",
            studentID,
            safe(fs.getTitleKeywords()),
            safe(fs.getDescriptionKeywords()),
            levels,
            majors,
            safe(fs.getStatus()),
            safe(fs.getCompanyName()),
            fs.getAvailable(),
            fs.getVisibility(),
            fs.getWithdrawalHidden(),
            start,
            end
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
