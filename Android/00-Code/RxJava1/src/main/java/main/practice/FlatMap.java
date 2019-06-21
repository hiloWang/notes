package main.practice;

import rx.Observable;
import rx.schedulers.Schedulers;

import java.io.File;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @author ztiany
 *         Email: ztiany3@gmail.com
 */
public class FlatMap {

    public static void main(String... args) {
        new OneToMany().run();
        new NestedAsync().run();
    }

    /**
     * 一对多的变换
     */
    static class OneToMany {
        //使用flatMap的from从原来的file变换出string[],并且单个发射这些string
        void run() {
            File source = new File(".");
            Observable.just(source)//file是单个对象
                    .flatMap(file -> {
                        if (file.isFile()) {
                            return Observable.just(file.getAbsolutePath());
                        } else {
                            String[] list = file.list();
                            return Observable.from(list);
                        }
                    })
                    .subscribe(System.out::println);
        }
    }

    /**
     * 嵌套的异步
     */
    static class NestedAsync {

        void run() {
            File source = new File(".");
            //这里返回的是一个异步操作的Observable，如果这里不是异步的，将会串行执行
            Observable.from(source.listFiles())
                    .flatMap(this::getFileDetail)
                    .subscribe(System.out::println);
        }

        //比如为我有一个异步操作，要去获取一个File里所有的文件与文件夹名
        private Observable<String> getFileDetail(File file) {
            return Observable.just(file)
                    .map(fileMap -> Arrays.toString(fileMap.list()))
                    .subscribeOn(Schedulers.newThread())//这里应用了异步操作
                    .delay(2, TimeUnit.SECONDS);
        }
    }
}
