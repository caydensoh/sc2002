public class DataManager {
	private final UserLoader userLoader;
	private final InternshipLoader internshipLoader;
	private final ApplicationLoader applicationLoader;
	private final FilterSettingsLoader filterSettingsLoader;

	public DataManager(UserRepo userRepo, InternshipRepo internshipRepo, ApplicationRepo applicationRepo) {
		this.internshipLoader = new InternshipLoader(internshipRepo);
		this.applicationLoader = new ApplicationLoader(applicationRepo, internshipRepo);
		this.userLoader = new UserLoader(userRepo, applicationRepo, internshipRepo);
		this.filterSettingsLoader = new FilterSettingsLoader(userRepo);
	}

	public void loadAllData() {
		internshipLoader.load();
		applicationLoader.load();
		userLoader.load();
		filterSettingsLoader.load();
	}

	public void saveAllData() {
		userLoader.save();
		internshipLoader.save();
		applicationLoader.save();
		filterSettingsLoader.save();
	}
}
