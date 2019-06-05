#include "jni.h"
#include <stdio.h>
#include <pthread.h>
#include <android/log.h>
#include <unistd.h>

#define LOGI(FORMAT, ...) __android_log_print(ANDROID_LOG_INFO,"jason",FORMAT,##__VA_ARGS__);

JavaVM *javaVM;
jobject uuidutils_class_global;
jmethodID uuidutils_get_mid;

//动态库加载时会执行
//兼容Android SDK 2.2之后，2.2没有这个函数
JNIEXPORT jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    LOGI("%s", "JNI_OnLoad");
    javaVM = vm;
    return JNI_VERSION_1_4;
}

void *th_fun(void *arg) {
    int i;
    for (i = 0; i < 5; i++) {
        JNIEnv *env;
        //关联参数
        //JavaVMAttachArgs args = {JNI_VERSION_1_4, "my_thread", NULL};
        //(*javaVM)->AttachCurrentThread(javaVM,&env,&args);
        (*javaVM)->AttachCurrentThread(javaVM, &env, NULL);
        jobject uuid_jstr = (*env)->CallStaticObjectMethod(env, uuidutils_class_global,
                                                           uuidutils_get_mid);
        const char *uuid_cstr = (*env)->GetStringUTFChars(env, uuid_jstr, NULL);
        LOGI("uuid:%s", uuid_cstr);
        //退出线程
        if (i == 4) {
            goto end;
        }
        sleep(1);
    }
    end:
    //取消关联
    (*javaVM)->DetachCurrentThread(javaVM);
    pthread_exit((void *) 0);

}

//JavaVM 代表的是Java虚拟机，所有的工作都是从JavaVM开始，可以通过JavaVM获取到每个线程关联的JNIEnv(每个线程都有独立的JNIEnv)

//如何获取JavaVM？
//1.在JNI_OnLoad函数中获取
//2.(*env)->GetJavaVM(env,&javaVM);

//初始化
JNIEXPORT void JNICALL Java_com_ztiany_android_pthread_PosixThread_init
        (JNIEnv *env, jobject jobj) {
//获取class必须要在主线程中
    jclass uuidutils_class_tmp = (*env)->FindClass(env, "com/ztiany/android/pthread/UUIDUtils");
//创建全局引用
    uuidutils_class_global = (*env)->NewGlobalRef(env, uuidutils_class_tmp);
//获取jmethodId也可以在子线程中
    uuidutils_get_mid = (*env)->GetStaticMethodID(env, uuidutils_class_global, "get",
                                                  "()Ljava/lang/String;");
}

//销毁
JNIEXPORT void JNICALL Java_com_ztiany_android_pthread_PosixThread_destroy
        (JNIEnv *env, jobject jobj) {
//释放全局引用
    (*env)->DeleteGlobalRef(env, uuidutils_class_global);
}

JNIEXPORT void JNICALL Java_com_ztiany_android_pthread_PosixThread_pthread
        (JNIEnv *env, jobject jobj) {
//(*env)->GetJavaVM(env,&javaVM);
//创建多线程
    pthread_t tid;
    pthread_create(&tid, NULL, th_fun, (void *) "NO1");
}


