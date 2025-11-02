public class Student extends User {

	private Integer yearOfStudy;
	private String major;
	private List<Application> applications;
	private Appplication internship;

	/**
	 * 
	 * @param userID
	 * @param name
	 * @param yearOfStudy
	 * @param major
	 */
	public Student(String userID, String name, Integer yearOfStudy, String major) {
		// TODO - implement Student.Student
		throw new UnsupportedOperationException();
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
		return this.applications;
	}

	/**
	 * 
	 * @param application
	 */
	public void addApplication(Application application) {
		// TODO - implement Student.addApplication
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param index
	 * @param application
	 */
	public Boolean changeApplication(Integer index, Application application) {
		// TODO - implement Student.changeApplication
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param index
	 */
	public Boolean deleteApplication(Integer index) {
		// TODO - implement Student.deleteApplication
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param index
	 */
	public Boolean acceptInternship(Integer index) {
		// TODO - implement Student.acceptInternship
		throw new UnsupportedOperationException();
	}

	public Boolean withdrawInternship() {
		// TODO - implement Student.withdrawInternship
		throw new UnsupportedOperationException();
	}

}