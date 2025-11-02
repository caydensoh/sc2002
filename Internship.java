public class Internship {

	private String title;
	private String description;
	private String internshipLevel;
	private String preferredMajor;
	private LocalDate openingDate;
	private LocalDate closingDate;
	private String status;
	private String companyName;
	private String companyRepIC;
	private Integer slots;
	private Boolean visibility;
	private Boolean approval;

	/**
	 * 
	 * @param title
	 * @param description
	 * @param internshipLevel
	 * @param preferredMajor
	 * @param openingDate
	 * @param closingDate
	 * @param status
	 * @param companyName
	 * @param companyRepIC
	 * @param slots
	 * @param visibility
	 */
	public Internship(String title, String description, String internshipLevel, String preferredMajor, LocalDate openingDate, LocalDate closingDate, String status, String companyName, String companyRepIC, Integer slots, boolean visibility) {
		// TODO - implement Internship.Internship
		throw new UnsupportedOperationException();
	}

	public String getTitle() {
		return this.title;
	}

	/**
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return this.description;
	}

	/**
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	public String getInternshipLevel() {
		return this.internshipLevel;
	}

	/**
	 * 
	 * @param internshipLevel
	 */
	public void setInternshipLevel(String internshipLevel) {
		this.internshipLevel = internshipLevel;
	}

	public String getPreferredMajor() {
		return this.preferredMajor;
	}

	/**
	 * 
	 * @param preferredMajor
	 */
	public void setPreferredMajor(String preferredMajor) {
		this.preferredMajor = preferredMajor;
	}

	public LocalDate getOpeningDate() {
		return this.openingDate;
	}

	/**
	 * 
	 * @param openingDate
	 */
	public void setOpeningDate(LocalDate openingDate) {
		this.openingDate = openingDate;
	}

	public LocalDate getClosingDate() {
		return this.closingDate;
	}

	/**
	 * 
	 * @param closingDate
	 */
	public void setClosingDate(LocalDate closingDate) {
		this.closingDate = closingDate;
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

	public String getCompanyName() {
		return this.companyName;
	}

	/**
	 * 
	 * @param companyName
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyRepIC() {
		return this.companyRepIC;
	}

	/**
	 * 
	 * @param companyRepIC
	 */
	public void setCompanyRepIC(String companyRepIC) {
		this.companyRepIC = companyRepIC;
	}

	public Integer getSlots() {
		return this.slots;
	}

	/**
	 * 
	 * @param slots
	 */
	public void setSlots(Integer slots) {
		this.slots = slots;
	}

	public Boolean getVisibility() {
		return this.visibility;
	}

	/**
	 * 
	 * @param visibility
	 */
	public void setVisibility(Boolean visibility) {
		this.visibility = visibility;
	}

	public Boolean getApproval() {
		return this.approval;
	}

	/**
	 * 
	 * @param approval
	 */
	public void setApproval(Boolean approval) {
		this.approval = approval;
	}

}