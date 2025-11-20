import java.io.*;
import java.nio.file.*;
import java.util.*;

public class UserLoader implements Loader {
    private final UserRepo users;
    private final ApplicationRepo applications;
    private final InternshipRepo internships;

    public UserLoader(UserRepo users, ApplicationRepo applications, InternshipRepo internships) {
        this.users = users;
        this.applications = applications;
        this.internships = internships;
    }

    @Override
    public void load() {
        users.getAll().clear();
        try {
            List<String> lines = Files.readAllLines(Paths.get(CsvPaths.STUDENT));
            if (lines.isEmpty()) return;
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i).trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length < 4) continue;

                String studentID = parts[0].trim();
                String name = parts[1].trim();
                String major = parts[2].trim();
                int year = Integer.parseInt(parts[3].trim());
                String password = parts.length > 5 && !parts[5].isEmpty() ? parts[5].trim() : "password";

                Student student = new Student(studentID, name, password, year, major, null);
                users.add(student);

                // Parse application IDs if present (parts[6])
                if (parts.length > 6 && !parts[6].isEmpty()) {
                    String[] appIDs = parts[6].split(";");
                    for (String appID : appIDs) {
                        appID = appID.trim();
                        Application app = applications.find(appID);
                        if (app != null) {
                            student.addApplication(app);
                        }
                    }
                }

                // Set accepted internship if present (parts[7])
                if (parts.length > 7 && !parts[7].isEmpty()) {
                    String acceptedAppID = parts[7].trim();
                    Application acceptedApp = applications.find(acceptedAppID);
                    if (acceptedApp != null) {
                        student.setAcceptedInternship(acceptedApp);
                    }
                }
            }
            System.out.println("Loaded " + users.getAll().size() + " users.");
        } catch (IOException e) {
            System.out.println("Note: Students file not found or empty.");
        }

        try {
            List<String> staffLines = Files.readAllLines(Paths.get(CsvPaths.STAFF));
            if (!staffLines.isEmpty()) {
                for (int i = 1; i < staffLines.size(); i++) {
                    String line = staffLines.get(i).trim();
                    if (line.isEmpty()) continue;

                    String[] parts = line.split(",");
                    if (parts.length < 3) continue;

                    String staffID = parts[0].trim();
                    String name = parts[1].trim();
                    String department = parts[2].trim();
                    String password = parts.length > 3 && !parts[3].isEmpty() ? parts[3].trim() : "password";

                    CareerCenterStaff staff = new CareerCenterStaff(staffID, name, password, department);
                    users.add(staff);
                }
            }
        } catch (IOException e) {
            System.out.println("Note: Staff file not found or empty.");
        }

        try {
            List<String> repLines = Files.readAllLines(Paths.get(CsvPaths.COMPANY_REP));
            if (!repLines.isEmpty()) {
                for (int i = 1; i < repLines.size(); i++) {
                    String line = repLines.get(i).trim();
                    if (line.isEmpty()) continue;

                    String[] parts = line.split(",");
                    if (parts.length < 6) continue;

                    String userID = parts[0].trim();
                    String name = parts[1].trim();
                    String password = !parts[2].isEmpty() ? parts[2].trim() : "password";
                    String companyName = parts[3].trim();
                    String department = parts[4].trim();
                    String position = parts[5].trim();
                    boolean approval = !parts[6].isEmpty() ? Boolean.parseBoolean(parts[6].trim()) : false;

                    CompanyRepresentative rep = new CompanyRepresentative(userID, name, password, companyName, department, position);
                    rep.setApproval(approval); // set approval separately since constructor defaults to false
                    users.add(rep);

                    // attach any internships that belong to this rep into their local repo
                    if (internships != null) {
                        for (Internship intern : internships.getAll()) {
                            if (intern.getCompanyRepIC() != null && intern.getCompanyRepIC().equals(userID)) {
                                rep.getInternships().add(intern);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Note: Company Representatives file not found or empty.");
        }

    }

    @Override
    public void save() {
        try (FileWriter fw = new FileWriter(CsvPaths.STUDENT)) {
            fw.write("StudentID,Name,Major,Year,Email,Password,ApplicationIDs,AcceptedInternshipID\n");
            for (User user : users.getAll()) {
                if (user instanceof Student s) {
                    StringBuilder appIDsBuilder = new StringBuilder();
                    ApplicationRepo apps = s.getApplications();
                    if (apps != null && !apps.isEmpty()) {
                        for (int i = 0; i < apps.size(); i++) {
                            appIDsBuilder.append(apps.get(i).getApplicationID());
                            if (i < apps.size() - 1) {
                                appIDsBuilder.append(";");
                            }
                        }
                    }

                    String acceptedInternshipID = "";
                    Application acceptedInternship = s.getAcceptedInternship();
                    if (acceptedInternship != null) {
                        acceptedInternshipID = acceptedInternship.getApplicationID();
                    }

                    String line = String.format("%s,%s,%s,%d,%s,%s,%s,%s\n",
                        s.getUserID(), s.getName(), s.getMajor(), s.getYearOfStudy(),
                        "", s.getPassword(), appIDsBuilder.toString(), acceptedInternshipID);
                    fw.write(line);
                }
            }
            System.out.println("Users saved.");
        } catch (IOException e) {
            System.out.println("Error writing users CSV: " + e.getMessage());
        }
        try (FileWriter fw = new FileWriter(CsvPaths.STAFF)) {
            fw.write("StaffID,Name,Department,Password\n");
            for (User user : users.getAll()) {
                if (user instanceof CareerCenterStaff staff) {
                    String line = String.format("%s,%s,%s,%s\n",
                            staff.getUserID(),
                            staff.getName(),
                            staff.getStaffDepartment(),
                            staff.getPassword());
                    fw.write(line);
                }
            }
            System.out.println("Staff saved.");
        } catch (IOException e) {
            System.out.println("Error writing staff CSV: " + e.getMessage());
        }

        try (FileWriter fw = new FileWriter(CsvPaths.COMPANY_REP)) {
            fw.write("UserID,Name,Password,CompanyName,Department,Position,Approval\n");
            for (User user : users.getAll()) {
                if (user instanceof CompanyRepresentative rep) {
                    String line = String.format("%s,%s,%s,%s,%s,%s,%s\n",
                            rep.getUserID(),
                            rep.getName(),
                            rep.getPassword(),
                            rep.getCompanyName(),
                            rep.getDepartment(),
                            rep.getPosition(),
                            rep.getApproval());
                    fw.write(line);
                }
            }
            System.out.println("Company representatives saved.");
        } catch (IOException e) {
            System.out.println("Error writing company representatives CSV: " + e.getMessage());
        }
    }
}
