#include <jni.h>

#ifndef _Included_com_ztiany_bionic_JniBridge
#define _Included_com_ztiany_bionic_JniBridge
#ifdef __cplusplus
extern "C" {
#endif

//---------------------------------------------------------------------------------------------
//                                                      Test
//---------------------------------------------------------------------------------------------

JNIEXPORT void JNICALL Java_com_ztiany_bionic_JniBridge_testJni(JNIEnv *, jobject);


//---------------------------------------------------------------------------------------------
//                                                      Pthread
//---------------------------------------------------------------------------------------------

JNIEXPORT void JNICALL Java_com_ztiany_bionic_JniBridge_nativeInit(JNIEnv *, jobject);
JNIEXPORT void JNICALL Java_com_ztiany_bionic_JniBridge_nativeFree(JNIEnv *, jobject);
JNIEXPORT void JNICALL Java_com_ztiany_bionic_JniBridge_nativeWorker(JNIEnv *, jobject, jint, jint);
JNIEXPORT void JNICALL Java_com_ztiany_bionic_JniBridge_posixThreads(JNIEnv *, jobject, jint, jint);


//---------------------------------------------------------------------------------------------
//                                                      TCP/UDP
//---------------------------------------------------------------------------------------------

JNIEXPORT void JNICALL Java_com_ztiany_bionic_tcpudp_EchoClientActivity_nativeStartTcpClient(JNIEnv *, jobject, jstring, jint, jstring);
JNIEXPORT void JNICALL Java_com_ztiany_bionic_tcpudp_EchoClientActivity_nativeStartUdpClient(JNIEnv *, jobject, jstring, jint, jstring);

JNIEXPORT void JNICALL Java_com_ztiany_bionic_tcpudp_EchoServerActivity_nativeStartTcpServer(JNIEnv *, jobject, jint);
JNIEXPORT void JNICALL Java_com_ztiany_bionic_tcpudp_EchoServerActivity_nativeStartUdpServer(JNIEnv *, jobject, jint);

JNIEXPORT void JNICALL Java_com_ztiany_bionic_tcpudp_LocalEchoActivity_nativeStartLocalServer(JNIEnv *, jobject, jstring);


//---------------------------------------------------------------------------------------------
//                                                      Bitmap/OpenGL/NativeWindow
//---------------------------------------------------------------------------------------------

JNIEXPORT jlong JNICALL Java_com_ztiany_bionic_player_AbstractPlayerActivity_open(JNIEnv *, jclass, jstring);
JNIEXPORT jint JNICALL Java_com_ztiany_bionic_player_AbstractPlayerActivity_getWidth(JNIEnv *, jclass, jlong);
JNIEXPORT jint JNICALL Java_com_ztiany_bionic_player_AbstractPlayerActivity_getHeight(JNIEnv *, jclass, jlong);
JNIEXPORT jdouble JNICALL Java_com_ztiany_bionic_player_AbstractPlayerActivity_getFrameRate(JNIEnv *, jclass, jlong);
JNIEXPORT void JNICALL Java_com_ztiany_bionic_player_AbstractPlayerActivity_close(JNIEnv *, jclass, jlong);

JNIEXPORT jboolean JNICALL Java_com_ztiany_bionic_player_BitmapPlayerActivity_render(JNIEnv *, jclass, jlong, jobject);

JNIEXPORT void JNICALL Java_com_ztiany_bionic_player_NativeWindowPlayerActivity_init(JNIEnv *, jclass, jlong, jobject);
JNIEXPORT jboolean JNICALL Java_com_ztiany_bionic_player_NativeWindowPlayerActivity_render(JNIEnv *, jclass, jlong, jobject);


JNIEXPORT jlong JNICALL Java_com_ztiany_bionic_player_OpenGLPlayerActivity_init(JNIEnv *, jclass, jlong);
JNIEXPORT void JNICALL Java_com_ztiany_bionic_player_OpenGLPlayerActivity_initSurface(JNIEnv *, jclass, jlong, jlong);
JNIEXPORT jboolean JNICALL Java_com_ztiany_bionic_player_OpenGLPlayerActivity_render(JNIEnv *, jclass, jlong, jlong);
JNIEXPORT void JNICALL Java_com_ztiany_bionic_player_OpenGLPlayerActivity_free(JNIEnv *, jclass, jlong);

#ifdef __cplusplus
}
#endif
#endif
