/*
 * Copyright (C) 2016 Jake Wharton
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package retrofit2.adapter.rxjava;

import retrofit2.Response;
import rx.Observable.OnSubscribe;
import rx.Subscriber;
import rx.exceptions.CompositeException;
import rx.exceptions.Exceptions;
import rx.exceptions.OnCompletedFailedException;
import rx.exceptions.OnErrorFailedException;
import rx.exceptions.OnErrorNotImplementedException;
import rx.plugins.RxJavaPlugins;

final class GitHubListOnSubscribe<GitHubPagingBody> implements OnSubscribe<GitHubPagingBody> {

    private final OnSubscribe<Response<GitHubPagingBody>> upstream;

    GitHubListOnSubscribe(OnSubscribe<Response<GitHubPagingBody>> upstream) {
        this.upstream = upstream;
    }

    @Override
    public void call(Subscriber<? super GitHubPagingBody> subscriber) {
        upstream.call(new GitHubListSubscriber<GitHubPagingBody>(subscriber));
    }

    private static class GitHubListSubscriber<GitHubPagingBody> extends Subscriber<Response<GitHubPagingBody>> {
        private final Subscriber<? super GitHubPagingBody> subscriber;
        /**
         * Indicates whether a terminal event has been sent to {@link #subscriber}.
         */
        private boolean subscriberTerminated;

        GitHubListSubscriber(Subscriber<? super GitHubPagingBody> subscriber) {
            super(subscriber);
            this.subscriber = subscriber;
        }

        @Override
        public void onNext(Response<GitHubPagingBody> response) {
            if (response.isSuccessful()) {
                GitHubPaging<?> paging;
                if (response.body() instanceof GitHubPaging) {
                    paging = (GitHubPaging<?>) response.body();
                } else if (response.body() instanceof PagingWrapper) {
                    paging = ((PagingWrapper) response.body()).getPaging();
                } else {
                    throw new IllegalArgumentException("response.body type error: " + response.body().getClass());
                }
                String link = response.headers().get("link");
                if (link != null) {
                    paging.setupLinks(link);
                }
                subscriber.onNext(response.body());
            } else {
                subscriberTerminated = true;
                Throwable t = new HttpException(response);
                try {
                    subscriber.onError(t);
                } catch (OnCompletedFailedException
                        | OnErrorFailedException
                        | OnErrorNotImplementedException e) {
                    RxJavaPlugins.getInstance().getErrorHandler().handleError(e);
                } catch (Throwable inner) {
                    Exceptions.throwIfFatal(inner);
                    CompositeException composite = new CompositeException(t, inner);
                    RxJavaPlugins.getInstance().getErrorHandler().handleError(composite);
                }
            }
        }

        @Override
        public void onError(Throwable throwable) {
            if (!subscriberTerminated) {
                subscriber.onError(throwable);
            } else {
                // This should never happen! onNext handles and forwards errors automatically.
                Throwable broken = new AssertionError(
                        "This should never happen! Report as a Retrofit bug with the full stacktrace.");
                //noinspection UnnecessaryInitCause Two-arg AssertionError constructor is 1.7+ only.
                broken.initCause(throwable);
                RxJavaPlugins.getInstance().getErrorHandler().handleError(broken);
            }
        }

        @Override
        public void onCompleted() {
            if (!subscriberTerminated) {
                subscriber.onCompleted();
            }
        }
    }
}
