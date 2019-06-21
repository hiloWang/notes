package processor;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.processors.BehaviorProcessor;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 17.7.13 0:16
 */
public class ProcessorMain {

    public static void main(String... args) {
        testBehaviorProcessor();
    }

    @SuppressWarnings("all")
    private static void testBehaviorProcessor() {

        BehaviorProcessor<String> behaviorProcessor = BehaviorProcessor.create();

        behaviorProcessor.subscribe(new Subscriber<String>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(Integer.MAX_VALUE);
                System.out.println("ProcessorMain.onSubscribe");
            }

            @Override
            public void onNext(String s) {
                System.out.println("ProcessorMain.onNext -- " + s);
            }

            @Override
            public void onError(Throwable t) {
                System.out.println("ProcessorMain.onError -- " + t);
            }

            @Override
            public void onComplete() {
                System.out.println("ProcessorMain.onComplete");
            }
        });

        behaviorProcessor.onNext("A");
        behaviorProcessor.onNext("B");
        behaviorProcessor.onNext("C");
        behaviorProcessor.onNext("D");
//        behaviorProcessor.onError(new RuntimeException("test error"));
        behaviorProcessor.onNext("E");

        //之后的订阅者在订阅了发生了错误的behaviorProcessor后，只会收到一个Error事件
        behaviorProcessor.subscribe(
                s -> {
                    System.out.println("Second onNext -- " + s);
                    int a = 3 / 0;
                },
                throwable -> System.out.println("Second onError -- " + throwable),
                () -> System.out.println("Second onComplete")
        );

        behaviorProcessor.subscribe(
                s -> System.out.println("Third onNext -- " + s),
                throwable -> System.out.println("Third onError -- " + throwable),
                () -> System.out.println("Third onComplete")
        );

        behaviorProcessor.subscribe(
                s -> System.out.println("Four onNext -- " + s),
                throwable -> System.out.println("Four onError -- " + throwable),
                () -> System.out.println("Four onComplete")
        );
    }

}
