import java.util.List;

public interface Repo<T> {

    T get(int index);

    List<T> getAll();

    void add(T item);

    void remove(T item);
    
    boolean isEmpty();

    int size();
}