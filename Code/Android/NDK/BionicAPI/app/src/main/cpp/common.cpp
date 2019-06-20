#include "common.h"
#include <cstdlib>

//Throws a new exception using the given exception classand exception message
void ThrowException(JNIEnv *env, const char *className, const char *message) {
    jclass clazz = env->FindClass(className);
    if (NULL != clazz) {
        env->ThrowNew(clazz, message);
        env->DeleteLocalRef(clazz);
    }
}

