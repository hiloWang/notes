#include "JniBridge.h"
#include <pthread.h>
#include <unistd.h>
#include <cstdio>
#include <semaphore.h>


void test(){
    sem_t sem;
    int pshared = 5;
    sem_init(&sem, pshared, 3);

}

//线程参数
struct NativeWorkerArgs {
    jint id;
    jint iterations;
};

// 用来调用Java的方法id，不要在线程中获取
static jmethodID gOnNativeMessage = NULL;

// Java VM interface pointer
extern JavaVM *gVM;

// Global reference to object
static jobject gObj = NULL;

// 互斥锁
static pthread_mutex_t mutex;

JNIEXPORT void JNICALL Java_com_ztiany_bionic_JniBridge_nativeInit(JNIEnv *env, jobject obj) {
    //step 1 初始化互斥锁
    if (0 != pthread_mutex_init(&mutex, NULL)) {
        jclass exceptionClazz = env->FindClass("java/lang/RuntimeException");
        env->ThrowNew(exceptionClazz, "Unable to initialize mutex");
        goto exit;
    }

    //step 2 初始化全局引用
    if (NULL == gObj) {
        gObj = env->NewGlobalRef(obj);
        if (NULL == gObj) {
            goto exit;
        }
    }

    // step 3 获取回调方法id
    if (NULL == gOnNativeMessage) {
        jclass clazz = env->GetObjectClass(obj);
        gOnNativeMessage = env->GetMethodID(clazz, "onNativeMessage", "(Ljava/lang/String;)V");

        if (NULL == gOnNativeMessage) {
            jclass exceptionClazz = env->FindClass("java/lang/RuntimeException");
            env->ThrowNew(exceptionClazz, "Unable to find method");
        }
    }

    exit:
    return;
}

JNIEXPORT void JNICALL Java_com_ztiany_bionic_JniBridge_nativeFree(JNIEnv *env, jobject obj) {
    //删除全局引用
    if (NULL != gObj) {
        env->DeleteGlobalRef(gObj);
        gObj = NULL;
    }
    //销毁锁
    if (0 != pthread_mutex_destroy(&mutex)) {
        // Get the exception class
        jclass exceptionClazz = env->FindClass("java/lang/RuntimeException");
        env->ThrowNew(exceptionClazz, "Unable to destroy mutex");
    }
}


//线程任务
void nativeWorker(JNIEnv *env, jobject obj, jint id, jint iterations) {
    // 上锁
    if (0 != pthread_mutex_lock(&mutex)) {
        jclass exceptionClazz = env->FindClass("java/lang/RuntimeException");
        env->ThrowNew(exceptionClazz, "Unable to lock mutex");
        goto exit;
    }

    //模拟任务
    for (jint i = 0; i < iterations; i++) {
        char message[26];
        sprintf(message, "Worker %d: Iteration %d", id, i);

        jstring messageString = env->NewStringUTF(message);
        env->CallVoidMethod(obj, gOnNativeMessage, messageString);

        // 如果有异常发送则退出
        if (NULL != env->ExceptionOccurred()) {
            break;
        }

        //睡一秒
        sleep(1);
    }

    // 释放锁
    if (0 != pthread_mutex_unlock(&mutex)) {
        jclass exceptionClazz = env->FindClass("java/lang/RuntimeException");
        env->ThrowNew(exceptionClazz, "Unable to unlock mutex");
    }

    exit:
    return;
}


static void *nativeWorkerThread(void *args) {
    JNIEnv *env = NULL;

    //如果需要和Java进行交互，原生线程必须先attach到JVM上
    if (0 == gVM->AttachCurrentThread(&env, NULL)) {
        // Get the native worker thread arguments
        NativeWorkerArgs *nativeWorkerArgs = (NativeWorkerArgs *) args;

        //执行模拟任务
        nativeWorker(env, gObj, nativeWorkerArgs->id, nativeWorkerArgs->iterations);

        delete nativeWorkerArgs;

        //必须detach
        gVM->DetachCurrentThread();
    }
    return (void *) 1;
}

JNIEXPORT void JNICALL
Java_com_ztiany_bionic_JniBridge_posixThreads(JNIEnv *env, jobject obj,
                                              jint threads, jint iterations) {

    // 创建线程
    for (jint i = 0; i < threads; i++) {
        //创建一个线程空间
        pthread_t thread;

        // Native worker thread arguments
        NativeWorkerArgs *nativeWorkerArgs = new NativeWorkerArgs();
        nativeWorkerArgs->id = i;
        nativeWorkerArgs->iterations = iterations;

        // 线程实例
        int result = pthread_create(
                &thread,//线程引用
                NULL,//用于设置线程的属性，传NULL则使用默认属性
                nativeWorkerThread,//线程创建后执行的方法，必须是 void * method(void *args)类型
                (void *) nativeWorkerArgs);//传递给nativeWorkerThread的参数

        if (0 != result) {
            jclass exceptionClazz = env->FindClass("java/lang/RuntimeException");
            env->ThrowNew(exceptionClazz, "Unable to create thread");
        }

    }

}