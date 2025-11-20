import java.util.Scanner;
public class MenuFactory {
    public static Menu getMenuForUser(Scanner scanner, User user, InternshipRepo internships, ApplicationRepo applications, UserRepo users) {
        if (user instanceof Student student)
            return new StudentMenu(scanner, student, internships, applications);
        if (user instanceof CompanyRepresentative companyRepresentative)
            return new CompanyRepMenu(scanner, companyRepresentative, internships, applications, users);
        if (user instanceof CareerCenterStaff careerCenterStaff)
            return new StaffMenu(scanner, careerCenterStaff, internships, applications, users);

        throw new IllegalArgumentException("Unknown user type");
    }
}