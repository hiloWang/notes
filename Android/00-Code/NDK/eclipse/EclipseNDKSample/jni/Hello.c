#include <jni.h>
#include <android/log.h>
#include "com_ztiany_ndksample_MainActivity.h"

#define LOG_TAG "System.out"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)

JNIEXPORT jstring JNICALL Java_com_ztiany_ndksample_MainActivity_callC
(JNIEnv* env, jobject onj){

	LOGI("执行C代码------------>");
	const char* str = "来自C的问候，你好Java！";
	return (*env)->NewStringUTF(env,str);
}
