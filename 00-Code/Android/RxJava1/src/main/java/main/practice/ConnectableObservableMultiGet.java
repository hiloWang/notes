package main.practice;

import main.utils.RxLock;
import rx.Observable;
import rx.functions.Func1;
import rx.observables.ConnectableObservable;
import rx.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ztiany
 *         Email: ztiany3@gmail.com
 */
public class ConnectableObservableMultiGet {

    public static void main(String... args) {

        DataSource dataSource = new DataSource();
        dataSource.getClassAList().subscribe(System.out::println);
        dataSource.getClassBList().subscribe(System.out::println);

        RxLock.lock(4000);
    }

    static class DataSource {

        private ConnectableObservable<HttpResult<List<String>>> mPublish;//数据源

        Observable<List<String>> getClassAList() {
            loadOrderListInfo();//判断是否已经执行了请求
            return mPublish
                    .flatMap(new FuncExtract<>())
                    .flatMap(new Func1<List<String>, Observable<String>>() {
                        @Override
                        public Observable<String> call(List<String> list) {
                            return Observable.from(list);
                        }
                    })
                    .filter(new Func1<String, Boolean>() {
                        @Override
                        public Boolean call(String data) {
                            return BusinessUtils.isClassifyA(data);
                        }
                    })
                    .toList();
        }

        Observable<List<String>> getClassBList() {
            loadOrderListInfo();//判断是否已经执行了请求

            return mPublish.flatMap(new FuncExtract<List<String>>())
                    .flatMap(new Func1<List<String>, Observable<String>>() {
                        @Override
                        public Observable<String> call(List<String> Strings) {
                            return Observable.from(Strings);
                        }
                    })
                    .filter(new Func1<String, Boolean>() {
                        @Override
                        public Boolean call(String data) {
                            return BusinessUtils.isClassifyB(data);
                        }
                    })
                    .toList();
        }

        private void loadOrderListInfo() {
            if (mPublish == null) {
                Observable<HttpResult<List<String>>> httpResultObservable = loadOrderList();
                mPublish = httpResultObservable.replay();
                mPublish.connect();//这里才是去请求数据
            }
        }

        private Observable<HttpResult<List<String>>> loadOrderList() {
            return Observable.just(null)
                    .subscribeOn(Schedulers.newThread())
                    .flatMap(o -> {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        HttpResult<List<String>> httpResult = new HttpResult<>();
                        List<String> data = new ArrayList<>();
                        httpResult.data = data;

                        for (int i = 0; i < 100; i++) {
                            if (i % 2 == 0) {
                                data.add("A " + i);
                            } else {
                                data.add("B " + i);
                            }
                        }
                        return Observable.just(httpResult);
                    });
        }

        static class BusinessUtils {

            static boolean isClassifyA(String s) {
                return s.startsWith("A");
            }

            static boolean isClassifyB(String s) {
                return s.startsWith("B");
            }
        }

        static class HttpResult<T> {
            private T data;
            private int code;
            T getData() {
                return data;
            }
            int getCode() {
                return code;
            }
        }

        static class FuncExtract<T> implements Func1<HttpResult<T>, Observable<T>> {
            @Override
            public Observable<T> call(HttpResult<T> tHttpResult) {
                return Observable.just(tHttpResult.getData());
            }
        }
    }
}