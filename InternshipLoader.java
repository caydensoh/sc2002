import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;

public class InternshipLoader implements Loader {
    private final InternshipRepo internshipRepo;

    public InternshipLoader(InternshipRepo internshipRepo) {
        this.internshipRepo = internshipRepo;
    }

    @Override
    public void load() {
        internshipRepo.getAll().clear();
        try {
            List<String> lines = Files.readAllLines(Paths.get(CsvPaths.INTERNSHIPS));
            if (lines.isEmpty()) return;
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i).trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(",");
                if (parts.length < 12) continue;

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

                Internship intern = new Internship(internshipID, title, description, level, major, openDate, closeDate, company, compRepID, slots);
                intern.setStatus(status);
                intern.setVisibility(visibility);
                internshipRepo.add(intern);
            }
            System.out.println("Loaded " + internshipRepo.getAll().size() + " internships.");
        } catch (IOException e) {
            System.out.println("Note: Internships file not found or empty.");
        }
    }

    @Override
    public void save() {
        try (FileWriter fw = new FileWriter(CsvPaths.INTERNSHIPS)) {
            fw.write("InternshipID,Title,Description,Level,PreferredMajor,OpeningDate,ClosingDate,Status,CompanyName,CompanyRepIC,Slots,Visibility\n");
            for (Internship intern : internshipRepo.getAll()) {
                String line = String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%d,%s\n",
                    intern.getInternshipID(),
                    safe(intern.getTitle()),
                    safe(intern.getDescription()),
                    intern.getInternshipLevel(),
                    intern.getPreferredMajor(),
                    intern.getOpeningDate(),
                    intern.getClosingDate(),
                    intern.getStatus(),
                    safe(intern.getCompanyName()),
                    intern.getCompanyRepIC(),
                    intern.getSlots(),
                    intern.getVisibility()
                );
                fw.write(line);
            }
            System.out.println("Internships saved.");
        } catch (IOException e) {
            System.out.println("Error writing internships CSV: " + e.getMessage());
        }
    }

    private String safe(String s) {
        return s == null ? "" : s.replaceAll(",", " ");
    }
}
