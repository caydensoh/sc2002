public class Application {

	private Internship internship;
	private String status; //
	private boolean pending;

	public Internship getInternship() {
		return this.internship;
	}

	/**
	 * 
	 * @param internship
	 */
	public void setInternship(Internship internship) {
		this.internship = internship;
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

	public Boolean getPending(Boolean) {
		// TODO - implement Application.getPending, return pending status (to see if application is pending or not)
		return this.pending;
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param confirm
	 */
	public void setPending(Boolean confirm) {
		this.pending = confirm ;
		System.out.println("Pending status set to "+confirm);
		// TODO - implement Application.setPending
		throw new UnsupportedOperationException();
	}

}