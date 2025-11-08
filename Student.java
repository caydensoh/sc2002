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
	public Student(String userID, String name, String password, Integer yearOfStudy, String major) {
		// TODO - implement Student.Student
		super(userID,  name, password);
		this.yearOfStudy = yearOfStudy;
		this.major = major;
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
		return this.applicationsList;
	}

	/**
	 * 
	 * @param application
	 */
	public void addApplication(Application application) {
		// TODO - implement Student.addApplication
		this.applicationsList.add(application);
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param index
	 * @param application
	 */
	public Boolean changeApplication(Integer index, Application application) {
		// TODO - implement Student.changeApplication
		if (index < 0 || index >= applicationsList.size()) {
			return false;
		}

		Internship opp = application.getInternship();

		//check eligibility
		if (yearOfStudy <= 2 && opp.getInternshipLevel()!="Basic") {
			System.out.println("Not eligible for this internship level. Level is too high for Year 1 and 2 students.");
			return false; //unsuccesful set
		}

		applicationsList.set(index,application);
		return true; //susccesful set
		
		
		//throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param index
	 */
	public Boolean deleteApplication(Integer index) {
		// TODO - implement Student.deleteApplication
		if (index < 0 || index >= applicationsList.size()) { //data validation
			return false;
		}

		Application app = applicationsList.get(index);
		//if approved dont delete
		if (app.getStatus()=="Approved") {
			System.out.println("This application has been approved and can only be withdrawn.");
			return false;
		}

		applicationsList.remove(index);
		return true;
		//applications.remove(index);
		// need to return boolean?
		//throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param index
	 */
	public Boolean acceptInternship(Integer index) {
		// TODO - implement Student.acceptInternship
		if (index < 0 || index >= applicationsList.size()) { //data validation
			return false;
		}

		Application selected = applicationsList.get(index);//cannot accept if not succesful
		if (selected.getStatus()!="Successful") {
			System.out.println("Can only accept applications with 'Successful' status.");
			return false;
		}
		// accept if succesful status
		selected.setStatus("Accepted");
		this.internship = selected;

		// withdraw all others
		for (int i = 0; i < applicationsList.size(); i++) {
			if (i != index) {
				applicationsList.get(index).setStatus("Withdrawn");
			}
		}

		System.out.println("Internship accepted. Other applications withdrawn.");
		return true;
		//throw new UnsupportedOperationException();
	}

	public Boolean withdrawInternship() {
		// TODO - implement Student.withdrawInternship
		if (this.internship == null) {
			System.out.println("No accepted internship to withdraw.");
			return false;
		}

		this.internship.setStatus("Withdrawn");
		this.internship = null;

		System.out.println("Your internship has been withdrawn.");
		return true;
		//throw new UnsupportedOperationException();
	}

}