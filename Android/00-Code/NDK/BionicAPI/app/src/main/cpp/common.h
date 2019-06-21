#ifndef BIONICAPI_COMMON_H
#define BIONICAPI_COMMON_H

#include <jni.h>


void ThrowException(JNIEnv *env, const char *className, const char *message);


#endif
