package main.practice;

import main.utils.RxLock;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 使用compose处理网络操作返回的结果
 *
 * @author ztiany
 *         Email: ztiany3@gmail.com
 */
public class ComposeProcessNetResult {

    static class Network {

        static boolean isConnected() {
            return false;
            //return true;
        }
    }

    private static HttpResult<String> getStringHttpResult() {
        HttpResult<String> stringHttpResult = new HttpResult<>();
        stringHttpResult.data = "远程数据";
        stringHttpResult.code = 3;
        //return stringHttpResult;
        return null;
    }

    public static void main(String... args) {
        Observable.defer(new Func0<Observable<HttpResult<String>>>() {
            @Override
            public Observable<HttpResult<String>> call() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                HttpResult<String> stringHttpResult = getStringHttpResult();
                return Observable.just(stringHttpResult);
            }
        })
                .subscribeOn(Schedulers.io())
                .compose(DataComposer.normal())
                .observeOn(Schedulers.computation())
                .subscribe(
                        new Action1<String>() {
                            @Override
                            public void call(String s) {
                                System.out.println("正常接收数据");
                                System.out.println(s);
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                System.out.println("发生错误");
                                System.out.println(throwable);
                            }
                        });

        RxLock.lock(3000);
    }


    /**
     * 用于处理网络数据，把网络连接错误，不合法的数据等等转换为异常，发送到下游
     *
     * @param <T>
     */
    static class DataComposer<T> implements Observable.Transformer<HttpResult<T>, T> {

        private static final DataComposer INSTANCE = new DataComposer();

        @SuppressWarnings("unchecked")
        static <R> DataComposer<R> normal() {
            return (DataComposer<R>) INSTANCE;
        }

        @Override
        public Observable<T> call(Observable<HttpResult<T>> httpResultObservable) {
            return httpResultObservable
                    .map(new Func1<HttpResult<T>, T>() {
                        @Override
                        public T call(HttpResult<T> tHttpResult) {
                            return processData(tHttpResult);
                        }
                    });
        }

        private T processData(HttpResult<T> rHttpResult) {
            if (rHttpResult == null) {
                if (Network.isConnected()) {
                    throwAs(new ServerErrorException(ServerErrorException.UNKNOW_ERROR));//有连接无数据，服务器错误
                } else {
                    throw new NetworkErrorException();//无连接网络错误
                }
            } else if (ApiHelper.isDataError(rHttpResult)) {
                throwAs(new ServerErrorException(ServerErrorException.SERVER_DATA_ERROR));//服务器数据格式错误
            } else if (!ApiHelper.isSuccess(rHttpResult)) {
                if (ApiHelper.isLoginExpired(rHttpResult)) {//登录过期
                    System.out.println("登录过期");
                }
                throwAs(createException(rHttpResult));
            }
            return rHttpResult.getData();
        }

        protected Throwable createException(HttpResult<T> rHttpResult) {
            return new ApiErrorException(rHttpResult.getCode());//默认还是API Error
        }

        @SuppressWarnings("unchecked")
        public static <T extends Throwable> void throwAs(Throwable throwable) throws T {
            if (throwable == null) {
                throwable = new RuntimeException("null throwable");
            }
            throw (T) throwable;
        }
    }

    static class ApiErrorException extends Exception {

        private int mCode;

        public ApiErrorException(int code) {
            mCode = code;
        }

        public int getCode() {
            return mCode;
        }

        public void setCode(int code) {
            mCode = code;
        }

        @Override
        public String getMessage() {
            return super.getMessage();
        }
    }

    static class ApiHelper {

        private static final int DATA_ERROR = -1;
        private static final int SUCCESS = 0;
        private static final int NOT_LOGIN = 3;

        public static boolean isSuccess(HttpResult<?> httpResult) {
            return httpResult.getCode() == SUCCESS;
        }

        public static boolean isDataError(HttpResult httpResult) {
            return httpResult.getCode() == DATA_ERROR;
        }

        public static boolean isLoginExpired(HttpResult httpResult) {
            return httpResult != null && httpResult.getCode() == NOT_LOGIN;
        }

        public static HttpResult createErrorResult() {
            HttpResult httpResult = new HttpResult();
            httpResult.setCode(DATA_ERROR);
            return httpResult;
        }
    }

    static class NetworkErrorException extends RuntimeException {

    }

    static class ServerErrorException extends Exception {

        private final int mErrorType;
        public static final int UNKNOW_ERROR = 1;
        public static final int SERVER_DATA_ERROR = 2;


        /**
         * @param errorType {@link #UNKNOW_ERROR},{@link #SERVER_DATA_ERROR}
         */
        public ServerErrorException(int errorType) {
            mErrorType = errorType;
        }

        public int getErrorType() {
            return mErrorType;
        }

    }

    static class HttpResult<T> {

        private String callee;
        private String caller;
        private int code;
        private String msg;
        private long timestamp;
        private T data;

        public T getData() {
            return data;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public String getCallee() {
            return callee;
        }

        public String getCaller() {
            return caller;
        }

        public String getMsg() {
            return msg;
        }

    }

}
