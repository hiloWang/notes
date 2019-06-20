package sample;

import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author ztiany
 *         Email: ztiany3@gmail.com
 */
public class MultiFileUploadSample {

    private static AtomicInteger taskCount = new AtomicInteger(0);
    private static AtomicInteger failTaskCount = new AtomicInteger(0);

    private static Executor executor = Executors.newSingleThreadExecutor();

    private static class Task {
        private final String name;

        private Task(String name) {
            this.name = name;
        }
    }

    private static List<Task> taskList = new ArrayList<>();

    public static void main(String... args) {
        executor.execute(() -> {
            for (int i = 0; i < 100; i++) {
                taskList.add(new Task(String.valueOf(i)));
            }
            uploadTask();
            System.out.println("executor  done...");
        });
    }

    //使用Subscriber进行订阅，是需要主动调用request方法的
    private static Subscriber subscriber = new Subscriber<Boolean>() {
        @Override
        public void onSubscribe(Subscription s) {
            s.request(100);
        }

        @Override
        public void onNext(Boolean aBoolean) {
            System.out.println("MultiFileUploadSample.onNext-------------->taskCount = " + taskCount + " failTaskCount = " + failTaskCount);
        }

        @Override
        public void onError(Throwable t) {
            System.out.println("MultiFileUploadSample.onError:" + t);
            System.out.println("MultiFileUploadSample.onNext-------------->taskCount = " + taskCount + " failTaskCount = " + failTaskCount);
        }

        @Override
        public void onComplete() {
            System.out.println("MultiFileUploadSample.onComplete");
        }
    };

    /*使用consumer订阅，默认request Long.MAX_VALUE个*/
    private static Consumer consumer = new Consumer<Boolean>() {
        @Override
        public void accept(Boolean aBoolean) throws Exception {
            System.out.println("MultiFileUploadSample.onNext -------------->" + taskCount.get());
        }
    };

    /*使用consumer订阅，默认request Long.MAX_VALUE个*/
    private static Consumer errorConsumer = new Consumer<Throwable>() {
        @Override
        public void accept(Throwable throwable) throws Exception {
            System.out.println("------------------throwable = [" + throwable + "]");
        }
    };

    //批量执行
    private static void uploadTask() {
        Flowable.fromIterable(taskList)
                .onBackpressureBuffer(true)
                .flatMap(MultiFileUploadSample::doUpload, true, 3)
                .doOnNext(aBoolean -> taskCount.addAndGet(1))
                .observeOn(Schedulers.from(executor))
                .doOnCancel(() -> System.out.println("MultiFileUploadSample.doOnCancel run"))
                .subscribe(subscriber);
    }

    //模拟上传文件
    private static Publisher<Boolean> doUpload(Task task) {
        return Flowable.just(task)
                .subscribeOn(Schedulers.io())
                .flatMap(task1 -> {
                    Thread.sleep(300);
                    System.out.println("MultiFileUploadSample.apply-->Task =" + task1.name);
                    int i = Integer.parseInt(task1.name);
                    int result = i % 3;
                    if (result == 0) {
                        System.out.println("success " + i);
                        return Flowable.just(true);//上传成功
                    } else if (result == 1) {
                        return Flowable.error(new RuntimeException("服务器错误"));//网络错误
                    } else {
                        return Flowable.error(new RuntimeException("网络错误"));//网络错误
                    }
                })
                .doOnError(throwable -> {
                    failTaskCount.addAndGet(1);
                    System.out.println("doOnError throwable = [" + throwable + "]--" + failTaskCount.get());
                });
    }
}
/*
MultiFileUploadSample.onNext-------------->taskCount = 48 failTaskCount = 47
MultiFileUploadSample.apply-->Task =97
MultiFileUploadSample.apply-->Task =98
MultiFileUploadSample.apply-->Task =96
success 98
success 96
doOnError throwable = [java.lang.RuntimeException: 服务器错误]--48
MultiFileUploadSample.onNext-------------->taskCount = 49 failTaskCount = 48
MultiFileUploadSample.onNext-------------->taskCount = 50 failTaskCount = 48
MultiFileUploadSample.apply-->Task =99
doOnError throwable = [java.lang.RuntimeException: 服务器错误]--49
MultiFileUploadSample.onError:io.reactivex.exceptions.CompositeException: 50 exceptions occurred.
MultiFileUploadSample.onNext-------------->taskCount = 50 failTaskCount = 49

success 92
doOnError throwable = [java.lang.RuntimeException: 服务器错误]--46
MultiFileUploadSample.onNext-------------->taskCount = 47 failTaskCount = 46
MultiFileUploadSample.apply-->Task =93
doOnError throwable = [java.lang.RuntimeException: 服务器错误]--47
MultiFileUploadSample.apply-->Task =94
MultiFileUploadSample.apply-->Task =95
success 94
doOnError throwable = [java.lang.RuntimeException: 服务器错误]--48
MultiFileUploadSample.onNext-------------->taskCount = 48 failTaskCount = 48
MultiFileUploadSample.apply-->Task =96
success 96
MultiFileUploadSample.onNext-------------->taskCount = 49 failTaskCount = 48
MultiFileUploadSample.apply-->Task =98
MultiFileUploadSample.apply-->Task =97
success 98
doOnError throwable = [java.lang.RuntimeException: 服务器错误]--49
MultiFileUploadSample.onNext-------------->taskCount = 50 failTaskCount = 49
MultiFileUploadSample.apply-->Task =99
doOnError throwable = [java.lang.RuntimeException: 服务器错误]--50
MultiFileUploadSample.onError:io.reactivex.exceptions.CompositeException: 50 exceptions occurred.
MultiFileUploadSample.onNext-------------->taskCount = 50 failTaskCount = 50
 */