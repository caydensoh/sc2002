import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FilterSetting {

	private String titleKeywords;
	private String descriptionKeywords;
	private List<String> internshipLevels;
	private List<String> preferredMajors;
	private String status;
	private String companyName;
	private Boolean available;
	private Boolean visibility;
	private Boolean withdrawalHidden; //True if withdrawn internships are to be hidden
	private LocalDate filterStartDate;
	private LocalDate filterEndDate;

	public String getTitleKeywords() {
		return this.titleKeywords;
	}

	/**
	 * 
	 * @param titleKeywords
	 */
	public void setTitleKeywords(String titleKeywords) {
		this.titleKeywords = titleKeywords;
	}

	public String getDescriptionKeywords() {
		return this.descriptionKeywords;
	}

	/**
	 * 
	 * @param descriptionKeywords
	 */
	public void setDescriptionKeywords(String descriptionKeywords) {
		this.descriptionKeywords = descriptionKeywords;
	}

	public List<String> getInternshipLevels() {
		return this.internshipLevels;
	}

	/**
	 * 
	 * @param internshipLevels
	 */
	public void setInternshipLevels(List<String> internshipLevels) {
		this.internshipLevels = internshipLevels;
	}

	public List<String> getPreferredMajors() {
		return this.preferredMajors;
	}

	/**
	 * 
	 * @param preferredMajors
	 */
	public void setPreferredMajors(List<String> preferredMajors) {
		this.preferredMajors = preferredMajors;
	}

	// dateRange removed to simplify filtering per request

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

	public Boolean getAvailable() {
		return this.available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public Boolean getVisibility() {
		return this.visibility;
	}

	public void setVisibility(boolean visibility) {
		this.visibility = visibility;
	}

	public Boolean getWithdrawalHidden() {
		return this.withdrawalHidden;
	}

	public void setWithdrawalHidden(Boolean withdrawalHidden) {
		this.withdrawalHidden = withdrawalHidden;
	}

	public LocalDate getFilterStartDate() {
		return this.filterStartDate;
	}

	public void setFilterStartDate(LocalDate d) {
		this.filterStartDate = d;
	}

	public LocalDate getFilterEndDate() {
		return this.filterEndDate;
	}

	public void setFilterEndDate(LocalDate d) {
		this.filterEndDate = d;
	}

	/**
	 * 
	 * @param titleKeywords
	 * @param descriptionKeywords
	 * @param internshipLevels
	 * @param preferredMajors
	 * @param status
	 * @param companyName
	 * @param available
	 * @param visibility
	 * @param withdrawalHidden
	 */
public FilterSetting(String titleKeywords, String descriptionKeywords, List<String> internshipLevels, List<String> preferredMajors, String status, String companyName, boolean available, boolean visibility, Boolean withdrawalHidden) {
		// Initialize fields defensively
		this.titleKeywords = titleKeywords;
		this.descriptionKeywords = descriptionKeywords;
		this.internshipLevels = (internshipLevels == null) ? new ArrayList<>() : internshipLevels;
		this.preferredMajors = (preferredMajors == null) ? new ArrayList<>() : preferredMajors;
		// defaults
		this.status = status;
		this.companyName = companyName;
		this.available = available;
		this.visibility = visibility;
		this.withdrawalHidden = withdrawalHidden;
		this.filterStartDate = null;
		this.filterEndDate = null;
	}

	/**
	 * Helper method to clear or modify filter settings
	 * @param code 0 = clear all filters, 1 = set to available only, 2 = set to invisible only
	 */
}