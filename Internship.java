import java.time.*;

public class Internship {
	private String internshipID;
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
	private boolean visibility;

	/**
	 * @param internshipId
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
	public Internship(String internshipId, String title, String description, String internshipLevel, String preferredMajor, LocalDate openingDate, LocalDate closingDate, String companyName, String companyRepIC, Integer slots) {
		this.internshipID = internshipId == null? java.util.UUID.randomUUID().toString():internshipId;
		this.title=title;
		this.description=description;
		this.internshipLevel=internshipLevel;
		this.preferredMajor=preferredMajor;
		this.openingDate=openingDate;
		this.closingDate=closingDate;
		this.status="Pending";
		this.companyName=companyName;
		this.companyRepIC=companyRepIC;
		this.slots=slots;
		this.visibility=false;
	}

	public String getInternshipID() {
		return this.internshipID;
	}

	public void setInternshipID(String internshipID) {
		this.internshipID = internshipID;
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

	public boolean getVisibility() {
		return this.visibility;
	}

	/**
	 * 
	 * @param visibility
	 */
	public void setVisibility(boolean visibility) {
		this.visibility = visibility;
	}

	public boolean matchesFilter(FilterSetting filter) {
		// Visibility (only check if filter specifies a preference)
		if (filter.getVisibility() != null) {
			if (this.visibility != filter.getVisibility()) return false;
		}

		// Available (only check if requested)
		if (filter.getAvailable() != null && filter.getAvailable()) {
			if (this.slots == null || this.slots <= 0) return false;
		}

		// Date range filtering: if user set start/end, ensure internship overlaps range
		if (filter.getFilterStartDate() != null) {
			if (this.closingDate != null && this.closingDate.isBefore(filter.getFilterStartDate())) return false;
		}
		if (filter.getFilterEndDate() != null) {
			if (this.openingDate != null && this.openingDate.isAfter(filter.getFilterEndDate())) return false;
		}

        // Match internship level(s) if filter specifies them
        if (filter.getInternshipLevels() != null && !filter.getInternshipLevels().isEmpty()) {
            boolean levelMatches = false;
            for (String level : filter.getInternshipLevels()) {
                if (this.internshipLevel.equalsIgnoreCase(level)) {
                    levelMatches = true;
                    break;
                }
            }
            if (!levelMatches) {
                return false;
            }
        }

        // Match company name if filter specifies it
        if (filter.getCompanyName() != null && !this.companyName.equalsIgnoreCase(filter.getCompanyName())) {
            return false;
        }

        // Match title keywords if filter specifies them
        if (filter.getTitleKeywords() != null && !this.title.toLowerCase().contains(filter.getTitleKeywords().toLowerCase())) {
            return false;
        }

        // Match description keywords if filter specifies them
        if (filter.getDescriptionKeywords() != null && !this.description.toLowerCase().contains(filter.getDescriptionKeywords().toLowerCase())) {
            return false;
        }

        // Match status if filter specifies it
        if (filter.getStatus() != null && !this.status.equals(filter.getStatus())) {
            return false;
        }

        // Match preferred majors if filter specifies them
        if (filter.getPreferredMajors() != null && !filter.getPreferredMajors().isEmpty()) {
            String internMajor = this.preferredMajor;
            boolean majorMatches = false;
            for (String pMajor : filter.getPreferredMajors()) {
                if (internMajor.equalsIgnoreCase(pMajor)) {
                    majorMatches = true;
                    break;
                }
            }
            if (!majorMatches) {
                return false;
            }
        }
        return true;
    }
}// If “Filled” or after the Closing Date, students will not be able toapply for them anymore