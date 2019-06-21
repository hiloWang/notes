#ifndef BIONICAPI_SOCKET_API_H
#define BIONICAPI_SOCKET_API_H

#include <jni.h>
#include <stdint.h>
#include "common.h"

int AcceptOnSocket(JNIEnv *env, jobject obj, int sd);

void BindSocketToPort(JNIEnv *env, jobject obj, int sd, unsigned short port);

void ConnectToAddress(JNIEnv *env, jobject obj, int sd, const char *ip, unsigned short port);

unsigned short GetSocketPort(JNIEnv *env, jobject obj, int sd);

void ListenOnSocket(JNIEnv *env, jobject obj, int sd, int backlog);

void LogAddress(JNIEnv *env, jobject obj, const char *message, const struct sockaddr_in *address);

void LogMessage(JNIEnv *env, jobject obj, const char *format, ...);

ssize_t ReceiveFromSocket(JNIEnv *env, jobject obj, int sd, char *buffer, size_t bufferSize);

ssize_t SendToSocket(JNIEnv *env, jobject obj, int sd, const char *buffer, size_t bufferSize);

void ThrowErrnoException(JNIEnv *env, const char *className, int errnum);

// Max log message length
#define MAX_LOG_MESSAGE_LENGTH 256
// Max data buffer size
#define MAX_BUFFER_SIZE 80

#endif
