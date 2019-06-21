package com.bennyhuo.coroutines.sample;

import com.bennyhuo.coroutines.utils.LogKt;

/*在 java 中间接使用协程 */
public class Ex10_JavaInterop {
    public static void main(String... args) {
        try {
            LogKt.log(CoroutineInterop.loadString());
            LogKt.log(CoroutineInterop.loadString());
            LogKt.log(CoroutineInterop.loadFuture().get());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
