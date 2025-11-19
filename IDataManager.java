import java.util.List;

public interface IDataManager {

    void loadAllData(List<User> users, List<Internship> allInternships, List<Application> allApplications);
    void saveAllData(List<User> users, List<Internship> allInternships, List<Application> allApplications);
}