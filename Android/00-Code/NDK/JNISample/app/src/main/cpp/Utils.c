//
// Created by Administrator on 17.11.23.
//

#include "Utils.h"

#include <malloc.h>
#include <string.h>

/**
 * Java String转换为C字符串，转换后的字符串是可以修改的
 */
char *Jstring2CString(JNIEnv *env, jstring jstr) {
    char *rtn = NULL;
    jclass clsstring = (*env)->FindClass(env, "java/lang/String");
    jstring strencode = (*env)->NewStringUTF(env, "GB2312");
    jmethodID mid = (*env)->GetMethodID(env, clsstring, "getBytes", "(Ljava/lang/String;)[B");
    // String .getByte("GB2312");
    jbyteArray barr = (jbyteArray) (*env)->CallObjectMethod(env, jstr, mid, strencode);
    jsize alen = (*env)->GetArrayLength(env, barr);
    //动态的获取内在堆内存中，需要被释放
    jbyte *ba = (*env)->GetByteArrayElements(env, barr, JNI_FALSE);
    if (alen > 0) {
        rtn = (char *) malloc(alen + 1);         //"\0" c中字符串以\0结尾
        memcpy(rtn, ba, alen);
        rtn[alen] = 0;//让最后一个字符='\0',表示是字符串的结尾
    }
    (*env)->ReleaseByteArrayElements(env, barr, ba, 0);  //释放内存
    return rtn;
}