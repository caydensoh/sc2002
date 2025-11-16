public class Application {
	private String applicationID;
	private Internship internship;
	private String status; //Pending, Successful, Unsuccessful
	private String withdrawalStatus; //Withdrawn, Active, Pending

	public Application(Internship internship) {
		// TODO - implement Application.Application
		this.applicationID = java.util.UUID.randomUUID().toString();
		this.internship = internship;
		this.status = "Pending";
	}

	public Application(String applicationID, Internship internship) {
		// Constructor for loading from CSV with existing ID
		this.applicationID = applicationID;
		this.internship = internship;
		this.status = "Pending";
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