# RxJava总结——视图篇

---
## buffer + debouch + share 连续的双击检测

关键代码如下：

```java
            Observable<Void> observable = RxView.clicks(clickView).share();
            observable.buffer(observable.debounce(300, TimeUnit.MILLISECONDS))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<List<Void>>() {
                        @Override
                        public void onCompleted() {
                            Log.d(TAG, "onCompleted() called with: " + "");
                        }
    
                        @Override
                        public void onError(Throwable e) {
                            Log.d(TAG, "onError() called with: " + "e = [" + e + "]");
                        }
    
                        @Override
                        public void onNext(List<Void> voids) {
                            Log.d(TAG, "onNext() called with: " + "voids = [" + voids + "]");
                            if (voids.size() >= 2) {
                                Toast.makeText(getContext(), "doubleClick", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
```

1. `share`操作符很重要，普通的 Observable 是单播的（Observable 发射的数据不被多个订阅者共享），而 `share`=`publish`+`refCount`，把普通的 Observable 变成 `CoonectableObservable`，这时事件源都是多播的了（多个订阅者共享一个 Observable 发射的数据）。
2. 上面的 `observable` 就被多次订阅了，`buffer` 用于统计点击的次数，而 `debounce` 用于过滤频率过快的点击和通知 `observable.buffer` 发射数据，一举两得。

---
## RxTextView + debounce + swtchMap 响应 EditText 的输入变化，进行查询

需求：

- 一个搜索界面。
- 搜索框没有任何输入时显示最近的搜索项。
- 如果搜索框有输入则随着用户的输入进行查询，然后展示结果。

代码如下：
```java
public abstract class BaseSearchFragment<T> extends BaseListFragment<T> {

    private TextView mRecentTv;
    protected SearchView mSearchView;
    private PublishSubject<CharSequence> mPublishSubject;

    @Inject
    protected ErrorHandler mErrorHandler;

    @Override
    protected boolean hasInjector() {
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    @CallSuper
    protected void onViewPrepared(@NonNull View view, Bundle savedInstanceState) {
        super.onViewPrepared(view, savedInstanceState);
        mRecentTv = findView(R.id.stores_tv_recent_search);
        RecyclerView recyclerView = findView(R.id.base_list_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL, 0, 1));
        RecyclerAdapter<T, ?> adapter = getAdapter();
        setDataManager(adapter);
        recyclerView.setAdapter(setupLoadMore(adapter));
        getLoadMore().setAutoHiddenWhenNoMore(true);
        getStateLayoutConfig().setStateRetryListener(this::processRetry);
        setupToolbar(view);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //开始加载
        showRecentResult();
    }

    protected abstract RecyclerAdapter<T, ?> getAdapter();

    private void showRecentSearchData(List<T> recentSearchList) {
        if (recentSearchList == null) {
            mRecentTv.setVisibility(View.GONE);
        } else {
            mRecentTv.setVisibility(View.VISIBLE);
            getView().post(() -> {
                replaceData(recentSearchList);
                if (!isEmpty()) {
                    showContentLayout();
                }
            });
        }
    }

    private void setupToolbar(View view) {
        ToolbarUtils toolbarUtils = ToolbarUtils.setupToolBar(this, view, getTitleRes());
        Toolbar toolbar = toolbarUtils.getToolbar();
        toolbar.inflateMenu(R.menu.stores_menu_search);
        Menu menu = toolbar.getMenu();
        MenuItem searchItem = menu.findItem(R.id.stores_menu_search);
        mSearchView = (SearchView) searchItem.getActionView();
        Observable<SearchViewQueryTextEvent> observable = RxSearchView.queryTextChangeEvents(mSearchView);
        mPublishSubject = PublishSubject.create();
        //分发输入事件
        observable.map(SearchViewQueryTextEvent::queryText).subscribe(mPublishSubject::onNext);
        handleSearchTextChangeEvent(mPublishSubject);
    }

    private void processRetry(int state) {
        if (state == EMPTY) {
            onEmptyRetry();
        } else {
            mPublishSubject.onNext(mSearchView.getQuery());
        }
    }

    @Override
    protected void onLoadMore() {
        super.onLoadMore();
        doLoadMore(mSearchView.getQuery());
    }

    void handleSearchTextChangeEvent(PublishSubject<CharSequence> textChangeEvents) {
        textChangeEvents
                .debounce(400, TimeUnit.MILLISECONDS)//定制输入400毫秒后开始响应
                .observeOn(AndroidSchedulers.mainThread())
                .filter(charSequence -> {
                    Timber.d(" search：" + charSequence);
                    boolean doSearch = charSequence.length() > 0;
                    if (!doSearch) {
                        showRecentResult();
                    }
                    return doSearch;//防止空的内容
                })
                .doOnNext(event -> {
                    showRecentSearchData(null);//隐藏最近选择的客户
                    showLoadingLayout();
                })
                //当原始Observable发射一个新的数据（Observable）时，它将取消订阅并停止监视产生执之前那个数据的Observable，只监视当前这一个。
                .switchMapDelayError((Func1<CharSequence, Observable<List<T>>>)
                        sequence -> getSearchDataObservable(sequence.toString(), getPager().getPageStart(), getPager().getPageSize())
                                .observeOn(AndroidSchedulers.mainThread())
                                .doOnError(throwable -> {
                                    showMessage(mErrorHandler.createMessage(throwable));
                                    showErrorLayout();
                                }))
                .subscribe(customerModels -> {
                    if (Checker.isEmpty(customerModels)) {
                        showEmptyLayout();
                    } else {
                        replaceData(customerModels);
                        showContentLayout();
                    }
                }, RxUtils.logErrorHandler());
    }


    private void showRecentResult() {
        loadRecentCustomer();
    }

    private void loadRecentCustomer() {
        getRecentDataObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::showRecentSearchData, RxUtils.logErrorHandler());
    }

    void doLoadMore(CharSequence search) {
        getSearchDataObservable(search.toString(), getPager().getLoadingPage(), getPager().getPageSize())
                .takeUntil(mPublishSubject)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        customerModels -> {
                            if (Checker.isEmpty(customerModels)) {
                                loadMoreCompleted(false);
                            } else {
                                addData(customerModels);
                            }
                        }, throwable -> {
                            showMessage(mErrorHandler.createMessage(throwable));
                            loadMoreFail();
                        });
    }


    protected abstract int getTitleRes();

    protected abstract void onEmptyRetry();

    protected abstract Observable<List<T>> getSearchDataObservable(String s, int pageStart, int pageSize);

    protected abstract Observable<List<T>> getRecentDataObservable();
}

```

1. 通过 `debounce` 限流
2. 通过`filter`过滤空字符串
3. 通过 `switchMapDelayError` 发出查询请求，注意 `switchMapDelayError` 的特性，当原始的 Observable 发射一个新的数据，`switchMapDelayError` 不在订阅之前生成的 `Observable`，而是订阅现在生成的`Observable`，
4. 根据情况还可以添加 `retryWhen`，参考[RxSerach](https://github.com/hanks-zyh/RxSerach)。
5. 这里使用的 switchMapDelayError 代替 switchMap，与 `switchMap` 相比，当 Observable 发生错误时 switchMap 将停止工作，而 switchMapDelayError 可以忽略错误。

---
## combineLatest 合并最近的几个点

```java
        //三个输入Observable
        _emailChangeObservable = RxTextView.textChanges(_email).skip(1);
        _passwordChangeObservable = RxTextView.textChanges(_password).skip(1);
        _numberChangeObservable = RxTextView.textChanges(_number).skip(1);

        //合并检查
        Observable.combineLatest(_emailChangeObservable,
                    _passwordChangeObservable,
                    _numberChangeObservable,
                    new Func3<CharSequence, CharSequence, CharSequence, Boolean>() {
                        @Override
                        public Boolean call(CharSequence newEmail,
                                            CharSequence newPassword,
                                            CharSequence newNumber) {

                            boolean emailValid = !isEmpty(newEmail) &&
                                                EMAIL_ADDRESS.matcher(newEmail).matches();
                            if (!emailValid) {
                                _email.setError("Invalid Email!");
                            }

                            boolean passValid = !isEmpty(newPassword) && newPassword.length() > 8;
                            if (!passValid) {
                                _password.setError("Invalid Password!");
                            }

                            boolean numValid = !isEmpty(newNumber);
                            if (numValid) {
                                int num = Integer.parseInt(newNumber.toString());
                                numValid = num > 0 && num <= 100;
                            }
                            if (!numValid) {
                                _number.setError("Invalid Number!");
                            }

                            return emailValid && passValid && numValid;

                        }
                    })
                    .subscribe(new Observer<Boolean>() {
                        @Override
                        public void onCompleted() {
                            Timber.d("completed");
                        }

                        @Override
                        public void onError(Throwable e) {
                            Timber.e(e, "there was an error");
                        }

                        @Override
                        public void onNext(Boolean formValid) {
                            if (formValid) {
                                _btnValidIndicator.setBackgroundColor(getResources().getColor(R.color.blue));
                            } else {
                                _btnValidIndicator.setBackgroundColor(getResources().getColor(R.color.gray));
                            }
                        }
                    });
```

---
## debounce 防止多次点击

```java
RxView.clicks(button)  
              .debounce(1, TimeUnit.SECONDS)
              .observeOn(AndroidSchedulers.mainThread());
              .subscribe(new Observer<Object>() {  
                  @Override  
                  public void onCompleted() {  
                        log.d ("completed");  
                  }  
  
                  @Override  
                  public void onError(Throwable e) {  
                        log.e("error");  
                  }  
  
                  @Override  
                  public void onNext(Object o) {  
                       log.d("button clicked");  
                  }  
              });  
```

---
## debounce + zipWith + startWith 优化点赞逻辑

需求：

- 一个点赞按钮，无论是取消点赞还是点赞都有比较好看的动画。
- 为了防止用户反复多次点击，需要多点赞事件进行防抖动处理。
- 在对点赞行为为了防抖动处理之后还需要进一步优化，比如防抖动处理之后的事件是需要点赞的，而现有的状态已经是点赞行为，就不需要进行接口调用了。

```java
boolean like;
PublishSubject<Boolean> likeAction = PublishSubject.create();

likeBtn.setOnClickListener(v -> {
    likeAction.onNext(like);
    like = !like;
});

Observable<Boolean> debounced = likeAction.debounce(1000, TimeUnit.MILLISECONDS);

debounced.zipWith(
    debounced.startWith(like),
    (last, current) -> last == current ? new Pair<>(false, false) : new Pair<>(true, current)
)
    .flatMap(pair -> pair.first ? Observable.just(pair.second) : Observable.empty())
    .subscribe(like -> {
        if (like) {
            sendCancelLikeRequest(postId);
        } else {
            sendLikeRequest(postId);
        }
    });
```

解释：

- debounced 是一个多播的数据源，这一点很重要。
- debounced 用于仿抖动，如果 1s 后用户没有再操作则对最后一次点赞行为做处理
- zipWith 组合了原始的 debounced 和 debounced.startWith(like) 两个数据源。这一点很巧妙。 startWith(like) 错开了两个数据源发射的数据。这样就可以在接下来的处理中对前一次操作的结果和后一次操作需要的结果进行比较，在时序上来看，debounced 总是发射之前那次操作的结果，debounced.startWith(like) 总是发射当前操作期望的结果。
- 最后 flatMap 就是用来进行对比的，如果现在的结果与期望的结果一样就返回 `Observable.empty())` ，相当于忽略此次操作。

这个场景来自 [社交软件上消息的点赞与取消点赞](https://juejin.im/post/5b8f5ea8f265da0a9223887e)












