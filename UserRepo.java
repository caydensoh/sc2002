import java.util.ArrayList;
import java.util.List;

public class UserRepo implements Repo<User> {
    private final List<User> users;
    private int size;

    public UserRepo() {
        this.users = new ArrayList<>();
        this.size = 0;
    }

    @Override
    public List<User> getAll() {
        return this.users;
    }

    @Override
    public void add(User User) {
        this.users.add(User);
        this.size++;
    }

    @Override
    public void remove(User User) {
        this.users.remove(User);
        this.size--;
    }

    public User find(String userID) {
		for (User u : users) {
			if (u.getUserID().equals(userID)) {
				return u;
			}
		}
		return null;
	}
    @Override
    public boolean isEmpty() {
        return this.users.isEmpty();
    }
    
    @Override
    public int size() {
        return this.size;
    }
}