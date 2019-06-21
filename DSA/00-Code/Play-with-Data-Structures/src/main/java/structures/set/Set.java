package structures.set;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/10/13 15:25
 */
public interface Set<E> {

    boolean isEmpty();

    int size();

    boolean contains(E e);

    void add(E e);

    void remove(E e);

}
