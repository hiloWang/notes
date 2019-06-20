package feature;

import io.reactivex.Flowable;


/**
 * 直接获取流中的数据
 *
 * @author ztiany
 *         Email: ztiany3@gmail.com
 */
public class BlockGetMain {

    public static void main(String... args) {
        Integer integer = Flowable.fromArray(new Integer[]{1, 3, 4, 5, 56})
                .last(1)
                .blockingGet();
        System.out.println(integer);
    }
}
