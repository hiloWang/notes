package main.practice;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author ztiany
 *         Email: ztiany3@gmail.com
 */
public class MapTransform {

    public static void main(String... args) {
        Observable.just(new HttpResult<String>())
                .map(new HttpResultFunc<>())
                .subscribe(s -> {

                });
    }

    /**
     * 用来统一处理Http的resultCode，并将HttpResult的Data部分剥离出来返回给subscriber
     *
     * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    static class HttpResultFunc<T> implements Func1<HttpResult<T>, T> {

        @Override
        public T call(HttpResult<T> httpResult) {
            if (httpResult.getCode() != 0) {
                throw new ApiException(httpResult.getCode());
            }
            return httpResult.getSubjects();
        }
    }


    /**
     * 用于统一封装后台返回的数据的抽象
     *
     * @param <T> 数据实体
     */
    static class HttpResult<T> {
        //用来模仿resultCode和resultMessage
        private int code;
        private String message;
        //用来模仿Data
        private T subjects;

        int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        T getSubjects() {
            return subjects;
        }
    }

    /**
     * 模拟API异常
     */
    static class ApiException extends RuntimeException {
        ApiException(int resultCode) {

        }
    }

}
