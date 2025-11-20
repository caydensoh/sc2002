import java.util.ArrayList;
import java.util.List;
public class InternshipRepo implements Repo<Internship> {
    private final List<Internship> internships;
    private int size;

    public InternshipRepo() {
        this.internships = new ArrayList<>();
        this.size = 0;
    }

    @Override
    public Internship get(int index) {
        return this.internships.get(index);
    }

    @Override
    public List<Internship> getAll() {
        return this.internships;
    }

    @Override
    public void add(Internship internship) {
        this.internships.add(internship);
        this.size++;
    }

    @Override
    public void remove(Internship internship) {
        this.internships.remove(internship);
        this.size--;
    }

    @Override
    public boolean isEmpty() {
        return this.internships.isEmpty();
    }

    @Override
    public int size() {
        return this.size;
    }

    public Internship find(String internshipID) {
        for (Internship intern : this.internships) {
            if (intern.getInternshipID().equals(internshipID)) {
                return intern;
            }
        }
        return null;
    }
}