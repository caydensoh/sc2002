import java.util.List;

public class Main {
    private static List<Student> students;
    private static List<CareerCenterStaff> staff;
    private static List<CompanyRepresentative> companyReps;

    private static final String STUDENT_FILE_PATH = "test_cases/sample_student_list.csv";
    private static final String STAFF_FILE_PATH  = "test_cases/sample_staff_list.csv";
    private static final String COMPANY_REP_FILE_PATH = "test_cases/sample_company_representative_list.csv";

    public static void main(String[] args) {
        DataLoader dataLoader = new DataLoader();

        students = dataLoader.loadStudents(STUDENT_FILE_PATH);
        staff = dataLoader.loadStaff(STAFF_FILE_PATH);
        companyReps = dataLoader.loadCompanyRep(COMPANY_REP_FILE_PATH);
        System.out.println("Data loading complete");
    }
}