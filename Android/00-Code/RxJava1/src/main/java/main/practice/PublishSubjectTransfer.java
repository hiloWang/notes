package main.practice;

import main.utils.Pair;
import rx.Scheduler;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * @author ztiany
 * Email: ztiany3@gmail.com
 */
public class PublishSubjectTransfer {


    public static void main(String... args) {

        PublishSubjectTransfer publishSubjectTransfer = new PublishSubjectTransfer(System.out::println);

        for (int a = 0; a < 2; a++) {
            for (int i = 1; i < 3; i++) {
                for (int j = 0; j < 10; j++) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    publishSubjectTransfer
                            .calcAmount(j, String.valueOf(j * i));
                }
            }
        }

    }

    private final PublishSubject<Task> mPublishSubject = PublishSubject.create();
    private final Map<String, Result> mCache = new HashMap<>();
    private final Consumer<Result> resultConsumer;

    private PublishSubjectTransfer(Consumer<Result> resultConsumer) {
        this.resultConsumer = resultConsumer;

        Scheduler mScheduler = Schedulers.from(Executors.newSingleThreadExecutor());

        mPublishSubject
                .subscribeOn(Schedulers.computation())
                .map(task -> new Pair<>(getTaskID(task), task.calcAmount()))
                .observeOn(mScheduler)
                .doOnNext(result -> mCache.put(result.first, result.second))
                .map(result -> result.second)
                .subscribe(this.resultConsumer::accept);
    }


    private void calcAmount(int id, String name) {
        String taskID = getTaskID(id, name);
        if (mCache.containsKey(taskID)) {
            System.out.println(1);
            resultConsumer.accept(mCache.get(taskID));
        } else {
            System.out.println(2);
            Task task = new Task(id, name);
            mPublishSubject.onNext(task);
        }
    }

    private String getTaskID(Task task) {
        return getTaskID(task.id, task.name);
    }

    private String getTaskID(int id, String name) {
        return name + id;
    }

    private static class Task {
        private final int id;
        private final String name;

        private Task(int age, String name) {
            this.id = age;
            this.name = name;
        }


        Result calcAmount() {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return new Result(Float.parseFloat(name));
        }
    }

    public static class Result {

        final float totalAmount;

        private Result(float totalAmount) {
            this.totalAmount = totalAmount;
        }

        @Override
        public String toString() {
            return "Result{" +
                    "totalAmount=" + totalAmount +
                    '}';
        }
    }


}
