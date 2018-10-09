# RxJava总结——视图篇


---
## 1 buffer+debouch+share连续的双击检测

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

1. `share`操作符很重要，普通的Observable只能被单个订阅者订阅，而`share`=`publish`+`refCount`,把普通的Observable变成`CoonectableObservable`,这时就可以被多个订阅了
2. 上面的`observable`就被多次订阅了，`buffer`用于统计点击的次数，而`debounce`用于过滤频率过快的点击和通知`buffer`发射数据，一举两得

---
## 2 RxTextView+debounce+swtchMap响应EditText的输入变化，进行查询

代码如下：
```java
      private void checkEditTextInput(final View view) {
            EditText text = (EditText) view.findViewById(R.id.frag_rx_view_et);
            Observable<CharSequence> charSequenceObservable = RxTextView.textChanges(text);
            charSequenceObservable
                    .debounce(400, TimeUnit.MILLISECONDS)//停止输入400毫秒后开始响应
                    .filter(new Func1<CharSequence, Boolean>() {
                        @Override
                        public Boolean call(CharSequence charSequence) {
                            return charSequence != null && charSequence.length() > 0;//防止空的内容
                        }
                    })
                    .switchMap(new Func1<CharSequence, Observable<Integer>>() {//当原始Observable发射一个新的数据（Observable）时，它将取消订阅并停止监视产生执之前那个数据的Observable，只监视当前这一个。
                        @Override
                        public Observable<Integer> call(CharSequence charSequence) {
                            return update(charSequence);
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Integer>() {
                        @Override
                        public void onCompleted() {
                            Log.d(TAG, "onCompleted() called with: " + "");
                        }
    
                        @Override
                        public void onError(Throwable e) {
                            Log.d(TAG, "onError() called with: " + "e = [" + e + "]");
                        }
    
                        @Override
                        public void onNext(Integer charSequence) {
                            Log.d(TAG, "onNext() called with: " + "charSequence = [" + charSequence + "]");
                        }
                    });
        }
    
        private Observable<Integer> update(CharSequence charSequence) {
            Log.d(TAG, "update() called with: " + "charSequence = [" + charSequence + "]");
            return Observable.just(charSequence.length()).delay(3, TimeUnit.SECONDS);
        }
```

1. 通过`debounce`限流
2. 通过`filter`过滤空字符串
3. 通过`switchMap`发出查询请求，但是注意`switchMap`的特性，当原始的Observable发射一个新的数据，`switchMap`不在订阅之前生成的`Observable`,而是订阅现在生成的`Observable`
4. 根据情况还可以添加`retryWhen`，参考[RxSerach](https://github.com/hanks-zyh/RxSerach)
5. 根据需求可以使用switchMapDelayError代替switchMap，当Observable发生错误时switchMap将停止工作，而switchMapDelayError可以忽略错误。

---
## 3 combineLatest 合并最近的几个点


```java
三个输入Observable

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
## 4 debounce 防止多次点击

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















