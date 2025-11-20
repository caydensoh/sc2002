public class Application {
	private final String applicationID;
	private final Internship internship;
	private String status; //Pending, Successful, Unsuccessful
	private String withdrawalStatus; //Withdrawn, Active, Pending
	private Student student;

	public Application(Internship internship, String applicationID, String status) {
		this(internship, applicationID, status, "Active");
	}

	public Application(Internship internship, String applicationID, String status, String withdrawalStatus) {
		// Constructor for loading from CSV with existing ID
		this.applicationID = applicationID == null? java.util.UUID.randomUUID().toString():applicationID;
		this.internship = internship;
		this.status = status == null? "Pending":status;
		this.withdrawalStatus = withdrawalStatus == null? "Active" : withdrawalStatus;
	}

	public String getApplicationID() {
		return this.applicationID;
	}

	public Internship getInternship() {
		return this.internship;
	}

	public String getStatus() {
		return this.status;
	}
	
	public void setStudent(Student student){
		this.student = student;
	}

	public Student getStudent(){
		return this.student;
	}

	/**
	 * 
	 * @param status
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	public void setWithdrawalStatus(String status) {
		this.withdrawalStatus = status;
	}

	public String getWithdrawalStatus() {
		return this.withdrawalStatus;
	}


}