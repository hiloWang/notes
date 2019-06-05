package rx;

public class UseCustomObservable {

    public static void main(String[] args) {

        new CustomObservable<String>(
                subscriber -> {
                    for (int i = 0; i < 100; i++) {
                        subscriber.onNext(String.valueOf(i));
                    }
                    subscriber.onCompleted();
                })
                .customSubscribeOn(Thread::new)
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("onCompleted" + Thread.currentThread());
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("UseCustomObservable.onError");
                    }

                    @Override
                    public void onNext(String s) {
                        System.out.println(s);
                    }
                });


    }
}