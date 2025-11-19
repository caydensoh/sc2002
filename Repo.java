import java.util.List;

public abstract interface Repo<T> {
    List<T> getAll();

    void add(T item);

    void remove(T item);
    
    boolean isEmpty();

    int size();
}