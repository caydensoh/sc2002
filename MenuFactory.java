import java.util.Scanner;
public class MenuFactory {
    public static Menu getMenuForUser(Scanner scanner, User user, InternshipRepo internships, ApplicationRepo applications, UserRepo users) {
        if (user instanceof Student)
            return new StudentMenu(scanner, (Student) user, internships, applications);
        if (user instanceof CompanyRepresentative)
            return new CompanyRepMenu(scanner, (CompanyRepresentative) user);
        if (user instanceof CareerCenterStaff)
            return new StaffMenu(scanner, (CareerCenterStaff) user, internships, applications, users);

        throw new IllegalArgumentException("Unknown user type");
    }
}