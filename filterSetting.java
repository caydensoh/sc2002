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

	public String getTitleKeywords() {
		return this.titleKeywords;
	}

	/**
	 * 
	 * @param attribute
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

	/**
	 * 
	 * @param titleKeywords
	 * @param descriptionKeywords
	 * @param internshipLevel
	 * @param preferredMajors
	 * @param dateRange
	 */
	public FilterSetting(String titleKeywords, String descriptionKeywords, List<String> internshipLevels, List<String> preferredMajors, String status, String companyName, boolean available, boolean visibility) {
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
	}

	/**
	 * Helper method to clear or modify filter settings
	 * @param code 0 = clear all filters, 1 = set to available only, 2 = set to invisible only
	 */
	public void setAttribute(int code) {
		switch (code) {
			case 0: // Clear all filters
				this.titleKeywords = null;
				this.descriptionKeywords = null;
				if (this.internshipLevels != null) this.internshipLevels.clear();
				this.status = null;
				this.companyName = null;
				if (this.preferredMajors != null) this.preferredMajors.clear();
				break;
			case 1: // Set to available
				this.available = true;
				break;
			case 2: // Set to invisible
				this.visibility = false;
				break;
		}
	}

}