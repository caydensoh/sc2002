import java.util.ArrayList;
import java.util.List;

public class Student extends User {

	private Integer yearOfStudy;
	private String major;
	private List<Application> applicationsList;
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
		this.applicationsList = new ArrayList<>();
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
			FilterSetting fs = new FilterSetting(null, null, allowedLevels, majors, null, null, true, true);
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

	public List<Application> getApplications() {
		return this.applicationsList;
	}

	public boolean addApplication(Application application) {
		// Check max applications limit
		if (this.applicationsList.size() >= 3) {
			System.out.println("You have reached the maximum of 3 applications. Please remove an application to apply for more.");
			return false;
		}
		if (this.yearOfStudy <= 2 && !"Basic".equals(application.getInternship().getInternshipLevel())) {
			System.out.println("Not eligible for this internship level. Level is too high for Year 1 and 2 students.");
			return false;
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
		Application app = this.applicationsList.get(index);
		applicationsList.remove(app);
		return true;
	}

	/**
	 * 
	 * @param index
	 */
	public boolean acceptInternship(Integer index) {
		if (index < 0 || index >= this.applicationsList.size()) {
			return false;
		}

		Application selected = this.applicationsList.get(index);
		if (!"Successful".equals(selected.getStatus())) {
			System.out.println("Can only accept applications with 'Successful' status.");
			return false;
		}
		// accept if successful status
		selected.setStatus("Accepted");
		this.internship = selected;

		// withdraw all others by creating a new list
		List<Application> remaining = new ArrayList<>();
		remaining.add(selected);
		this.applicationsList = remaining;

		System.out.println("Internship accepted. Other applications withdrawn.");
		return true;
	}

	public boolean withdrawInternship() {
		if (this.internship == null) {
			System.out.println("No accepted internship to withdraw.");
			return false;
		}

		this.internship = null;

		System.out.println("Your withdrawal request has been sent to the Career Center Staff");
		return true;
	}

	public Application getAcceptedInternship() {
		return this.internship;
	}

	public void setAcceptedInternship(Application internship) {
		this.internship = internship;
	}

}