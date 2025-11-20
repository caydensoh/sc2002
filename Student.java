import java.util.ArrayList;
import java.util.List;

public class Student extends User {

	private Integer yearOfStudy;
	private String major;
	private final ApplicationRepo applicationsList;
	private Application internship;

	/**
	 * all from User
	 * @param userID
	 * @param name
	 * @param yearOfStudy
	 * @param major
	 */
	public Student(String userID, String name, String password, Integer yearOfStudy, String major, FilterSetting filterSettings) {
		super(userID, name, password);
		this.yearOfStudy = yearOfStudy;
		this.major = major;
		this.applicationsList = new ApplicationRepo();
		this.internship = null;
		if (filterSettings != null) {
			this.setFilterSettings(filterSettings);
		}
		else {
			List<String> allowedLevels = new ArrayList<>();
			List<String> majors = new ArrayList<>();
			majors.add(this.major);
			if (this.yearOfStudy != null && this.yearOfStudy <= 2) {
				allowedLevels.add("Basic");
			} else {
				// years 3+ see all levels
				allowedLevels.add("Basic");
				allowedLevels.add("Intermediate");
				allowedLevels.add("Advanced");
			}
			FilterSetting fs = new FilterSetting(null, null, allowedLevels, majors, null, null, true, true, false);
			this.setFilterSettings(fs);
		}
	}

	public Integer getYearOfStudy() {
		return this.yearOfStudy;
	}

	/**
	 * 
	 * @param yearOfStudy
	 */
	public void setYearOfStudy(Integer yearOfStudy) {
		this.yearOfStudy = yearOfStudy;
	}

	public String getMajor() {
		return this.major;
	}

	/**
	 * 
	 * @param major
	 */
	public void setMajor(String major) {
		this.major = major;
	}

	public ApplicationRepo getApplications() {
		return this.applicationsList;
	}

	public boolean addApplication(Application application) {
		// Check max applications limit
		int activeCount = 0;
		for (Application app : this.applicationsList.getAll()) {
			String ws = app.getWithdrawalStatus();
			if (ws == null || !ws.equalsIgnoreCase("Withdrawn")) {
				activeCount++;
			}
		}
		if (activeCount >= 3) {
			throw new IllegalStateException("Cannot apply to more than 3 internships.");
		}
		if (this.yearOfStudy <= 2 && !"Basic".equals(application.getInternship().getInternshipLevel())) {
			throw new IllegalArgumentException("Students in year 2 or below can only apply to Basic level internships.");
		}
		// ensure bidirectional link
		if (application.getStudent() == null) {
			application.setStudent(this);
		}
		this.applicationsList.add(application);
		return true;
	}

	/**
	 * Remove an application by index
	 * @param index
	 */
	public boolean removeApplication(Integer index) {
		if (index < 0 || index >= this.applicationsList.size()) {
			return false;
		}
		Application app = this.applicationsList.getAll().get(index);
		applicationsList.remove(app);
		return true;
	}

	public Application getAcceptedInternship() {
		return this.internship;
	}

	public void setAcceptedInternship(Application internship) {
		this.internship = internship;
	}

}