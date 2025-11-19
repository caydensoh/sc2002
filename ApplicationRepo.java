import java.util.ArrayList;
import java.util.List;
public class ApplicationRepo implements Repo<Application> {
    private final List<Application> applications;

    public ApplicationRepo() {
        this.applications = new ArrayList<>();
    }

    @Override
    public List<Application> getAll() {
        return this.applications;
    }

    @Override
    public void add(Application application) {
        this.applications.add(application);
    }

    @Override
    public void remove(Application application) {
        this.applications.remove(application);
    }

    public Application find(String applicationID) {
        for (Application a : applications) {
            if (a.getApplicationID().equals(applicationID)) {
                return a;
            }
        }
        return null;
    }

    @Override
    public boolean isEmpty() {
        return this.applications.isEmpty();
    }

    @Override
    public int size() {
        return this.applications.size();
    }
}