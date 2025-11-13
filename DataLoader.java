import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataLoader {
//Helper method to read CSV files and skip header
    private List<String[]> readCSV(String filePath) {
        List<String[]> data = new ArrayList<>();
        String line = "";
        String cvsSplitBy = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            // read and skip header
            br.readLine();

            while ((line=br.readLine()) != null) {
                String cleanLine = line;
                String[] fields = cleanLine.split(cvsSplitBy);
                data.add(fields);
            }
        } catch (IOException e) {
            System.err.println("Could not read file: " + filePath);
            e.printStackTrace();
        }
        return data;
    }
    
    public List<Student> loadStudents(String filePath) {
        List<Student> studentList = new ArrayList<>();
        List<String[]> data = readCSV(filePath);

        for (String[] fields : data) {
            if (fields.length >= 4) {
                String userID = fields[0].trim();
                String name = fields[1].trim();
                String major = fields[2].trim();
                Integer yearOfStudy = Integer.parseInt(fields[3].trim());
                String defaultPassword = "password";

                Student student = new Student(userID, name, defaultPassword, yearOfStudy, major);
                studentList.add(student);
            }
        }
        System.out.println("Loaded " + studentList.size() + " students from " + filePath);
        return studentList;
    }

    public List<CareerCenterStaff> loadStaff(String filePath) {
        List<CareerCenterStaff> staffList = new ArrayList<>();
        List<String[]> data = readCSV(filePath);

        for (String[] fields : data) {
            if (fields.length >= 4) {
                String userID = fields[0].trim();
                String name = fields[1].trim();
                String staffDepartment = fields[3].trim();
                String defaultPassword = "password";

                CareerCenterStaff staff = new CareerCenterStaff(userID, name, defaultPassword, staffDepartment);
                staffList.add(staff);
            }
        }
        System.out.println("Loaded " + staffList.size() + " staff from " + filePath);
        return staffList;
    }

    public List<CompanyRepresentative> loadCompanyRep(String filePath) {
        List<CompanyRepresentative> repList = new ArrayList<>();
        List<String[]> data = readCSV(filePath);

        for (String[] fields : data) {
            if (fields.length >= 6) {
                String userID = fields[0].trim();
                String name = fields[1].trim();
                String companyName = fields[3].trim();
                String department = fields[4].trim();
                String position = fields[5].trim();
                String defaultPassword = "password";

                CompanyRepresentative rep = new CompanyRepresentative(userID, name, defaultPassword, companyName, department, position);
                repList.add(rep);
            }
        }
        System.out.println("Loaded " + repList.size() + " Company Representatives from " + filePath);
        return repList;
    }
}