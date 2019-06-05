#include "JniBridge.h"
#include <jni.h>
#include <android/log.h>
#include <sys/system_properties.h>
#include "bionic-api.h"
#include <cstdlib>
#include <ostream>

using namespace std;

JavaVM *gVM;

jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    LOGE("JNI_OnLoad");
    gVM = vm;
    return JNI_VERSION_1_4;
}

JNIEXPORT void JNICALL Java_com_ztiany_bionic_JniBridge_testJni(JNIEnv *env, jobject thiz) {
    //与子进程交互
    shellPro("ls -l");
    char value[PROP_VALUE_MAX];
    //获取属性
    getSystemProperty((char *) "ro.product.model", value);
    //获取用户id等信息
    getUID();
}

