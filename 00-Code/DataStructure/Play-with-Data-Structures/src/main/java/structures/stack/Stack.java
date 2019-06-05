package structures.stack;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.7.22 11:11
 */
public interface Stack<E> {

    void push(E e);

    E pop();

    E peek();

    boolean isEmpty();

    int size();

}
