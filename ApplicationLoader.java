import java.io.*;
import java.nio.file.*;
import java.util.*;

public class ApplicationLoader implements Loader {
    private final ApplicationRepo applicationRepo;
    private final InternshipRepo internshipRepo;

    public ApplicationLoader(ApplicationRepo applicationRepo, InternshipRepo internshipRepo) {
        this.applicationRepo = applicationRepo;
        this.internshipRepo = internshipRepo;
    }

    @Override
    public void load() {
        applicationRepo.getAll().clear();
        try {
            List<String> lines = Files.readAllLines(Paths.get(CsvPaths.APPLICATIONS));
            if (lines.isEmpty()) return;
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i).trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(",");
                if (parts.length < 3) continue;

                String applicationID = parts[0].trim();
                String internshipID = parts[1].trim();
                String status = parts[2].trim();

                Internship internship = findInternshipByID(internshipID);
                if (internship != null) {
                    Application app = new Application(internship, applicationID, status);
                    applicationRepo.add(app);
                }
            }
            System.out.println("Loaded " + applicationRepo.getAll().size() + " applications.");
        } catch (IOException e) {
            System.out.println("Note: Applications file not found or empty.");
        }
    }

    @Override
    public void save() {
        try (FileWriter fw = new FileWriter(CsvPaths.APPLICATIONS)) {
            fw.write("ApplicationID,InternshipID,Status\n");
            for (Application app : applicationRepo.getAll()) {
                String line = String.format("%s,%s,%s\n",
                    app.getApplicationID(),
                    app.getInternship().getInternshipID(),
                    app.getStatus()
                );
                fw.write(line);
            }
            System.out.println("Applications saved.");
        } catch (IOException e) {
            System.out.println("Error writing applications CSV: " + e.getMessage());
        }
    }

    private Internship findInternshipByID(String internshipID) {
        for (Internship internship : internshipRepo.getAll()) {
            if (internship.getInternshipID().equals(internshipID)) {
                return internship;
            }
        }
        return null;
    }
}
