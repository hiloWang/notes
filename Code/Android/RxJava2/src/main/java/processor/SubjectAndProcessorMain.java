package processor;

import io.reactivex.processors.BehaviorProcessor;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import rx.Observer;
import rx.subjects.AsyncSubject;

/**
 * Behavior与Processor
 *
 * @author ztiany
 *         Email: ztiany3@gmail.com
 */
public class SubjectAndProcessorMain {

    /*
    Subject在RxJava2中依然存在，但现在他们不支持backpressure。
    新出现的Processors支持backpressure，并且都设计为现成安全的
     */
    public static void main(String... args) {
        testAsyncSubject();
        testBehaviorProcessor();
    }

    private static void testBehaviorProcessor() {
        BehaviorProcessor<String> behaviorProcessor = BehaviorProcessor.create();
        behaviorProcessor.subscribe(new Subscriber<String>() {
            @Override
            public void onSubscribe(Subscription s) {

            }

            @Override
            public void onNext(String s) {

            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private static void testAsyncSubject() {
        AsyncSubject<String> asyncSubject = AsyncSubject.create();
        asyncSubject.subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {

            }
        });
    }
}
