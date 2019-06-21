package structures.map;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/10/13 17:54
 */
public interface Map<Key, Value> {

    void add(Key key, Value value);

    Value remove(Key key);

    Value get(Key key);

    boolean contains(Key key);

    int size();

    void set(Key key, Value value);

    boolean isEmpty();

}
