#include "socket-api.h"
#include <jni.h>
#include <stdio.h>
// va_list, vsnprintf
#include <stdarg.h>
// errno
#include <errno.h>
// strerror_r, memset
#include <string.h>
// socket, bind, getsockname, listen, accept, recv, send, connect
#include <sys/types.h>
#include <sys/socket.h>
// sockaddr_un
#include <sys/un.h>
// htons, sockaddr_in
#include <netinet/in.h>
// inet_ntop
#include <arpa/inet.h>
// close, unlink
#include <unistd.h>
// offsetof
#include <stddef.h>


//Logs the given message to the application
void LogMessage(JNIEnv *env, jobject obj, const char *format, ...) {

    static jmethodID methodID = NULL;

    if (NULL == methodID) {
        jclass clazz = env->GetObjectClass(obj);
        methodID = env->GetMethodID(clazz, "logMessage", "(Ljava/lang/String;)V");
        env->DeleteLocalRef(clazz);
    }

    if (NULL != methodID) {
        char buffer[MAX_LOG_MESSAGE_LENGTH];

        //可变参数
        va_list ap;
        va_start(ap, format);
        vsnprintf(buffer, MAX_LOG_MESSAGE_LENGTH, format, ap);
        va_end(ap);

        jstring message = env->NewStringUTF(buffer);

        if (NULL != message) {
            env->CallVoidMethod(obj, methodID, message);
            env->DeleteLocalRef(message);
        }
    }
}

//Throws a new exception using the given exception class and error message based on the error number.
void ThrowErrnoException(JNIEnv *env, const char *className, int errnum) {
    char buffer[MAX_LOG_MESSAGE_LENGTH];
    if (-1 == strerror_r(errnum, buffer, MAX_LOG_MESSAGE_LENGTH)) {
        strerror_r(errno, buffer, MAX_LOG_MESSAGE_LENGTH);
    }
    ThrowException(env, className, buffer);
}


/**
 * Binds socket to a port number.
 *
 * @param env JNIEnv interface.
 * @param obj object instance.
 * @param sd socket descriptor.
 * @param port port number or zero for random port.
 * @throws IOException
 */
void BindSocketToPort(JNIEnv *env, jobject obj, int sd, unsigned short port) {
    struct sockaddr_in address;

    // Address to bind socket
    memset(&address, 0, sizeof(address));
    address.sin_family = PF_INET;

    // Bind to all addresses
    address.sin_addr.s_addr = htonl(INADDR_ANY);

    // Convert port to network byte order
    address.sin_port = htons(port);

    // Bind socket
    LogMessage(env, obj, "Binding to port %hu.", port);
    if (-1 == bind(sd, (struct sockaddr *) &address, sizeof(address))) {
        ThrowErrnoException(env, "java/io/IOException", errno);
    }
}

/**
 * Gets the port number socket is currently binded.
 *
 * @param env JNIEnv interface.
 * @param obj object instance.
 * @param sd socket descriptor.
 * @return port number.
 * @throws IOException
 */
unsigned short GetSocketPort(JNIEnv *env, jobject obj, int sd) {
    unsigned short port = 0;

    struct sockaddr_in address;
    socklen_t addressLength = sizeof(address);

    // Get the socket address
    if (-1 == getsockname(sd, (struct sockaddr *) &address, &addressLength)) {
        // Throw an exception with error number
        ThrowErrnoException(env, "java/io/IOException", errno);
    } else {
        // Convert port to host byte order
        port = ntohs(address.sin_port);

        LogMessage(env, obj, "Binded to random port %hu.", port);
    }

    return port;
}

/**
 * Listens on given socket with the given backlog for
 * pending connections. When the backlog is full, the
 * new connections will be rejected.
 *
 * @param env JNIEnv interface.
 * @param obj object instance.
 * @param sd socket descriptor.
 * @param backlog backlog size.
 * @throws IOException
 */
void ListenOnSocket(JNIEnv *env, jobject obj, int sd, int backlog) {
    // Listen on socket with the given backlog
    LogMessage(env, obj, "Listening on socket with a backlog of %d pending connections.", backlog);
    if (-1 == listen(sd, backlog)) {
        // Throw an exception with error number
        ThrowErrnoException(env, "java/io/IOException", errno);
    }
}

/**
 * Logs the IP address and the port number from the
 * given address.
 *
 * @param env JNIEnv interface.
 * @param obj object instance.
 * @param message message text.
 * @param address adress instance.
 * @throws IOException
 */
void
LogAddress(JNIEnv *env, jobject obj, const char *message, const struct sockaddr_in *address) {
    char ip[INET_ADDRSTRLEN];

    // Convert the IP address to string
    if (NULL == inet_ntop(PF_INET, &(address->sin_addr), ip, INET_ADDRSTRLEN)) {
        // Throw an exception with error number
        ThrowErrnoException(env, "java/io/IOException", errno);
    } else {
        // Convert port to host byte order
        unsigned short port = ntohs(address->sin_port);
        // Log address
        LogMessage(env, obj, "%s %s:%hu.", message, ip, port);
    }
}

/**
 * Blocks and waits for incoming client connections on the
 * given socket.
 *
 * @param env JNIEnv interface.
 * @param obj object instance.
 * @param sd socket descriptor.
 * @return client socket.
 * @throws IOException
 */
int AcceptOnSocket(JNIEnv *env, jobject obj, int sd) {
    struct sockaddr_in address;
    socklen_t addressLength = sizeof(address);
    // Blocks and waits for an incoming client connection
    // and accepts it
    LogMessage(env, obj, "Waiting for a client connection...");

    int clientSocket = accept(sd, (struct sockaddr *) &address, &addressLength);

    // If client socket is not valid
    if (-1 == clientSocket) {
        // Throw an exception with error number
        ThrowErrnoException(env, "java/io/IOException", errno);
    } else {
        // Log address
        LogAddress(env, obj, "Client connection from ", &address);
    }

    return clientSocket;
}

/**
 * Block and receive data from the socket into the buffer.
 *
 * @param env JNIEnv interface.
 * @param obj object instance.
 * @param sd socket descriptor.
 * @param buffer data buffer.
 * @param bufferSize buffer size.
 * @return receive size.
 * @throws IOException
 */
ssize_t
ReceiveFromSocket(JNIEnv *env, jobject obj, int sd, char *buffer, size_t bufferSize) {
    // Block and receive data from the socket into the buffer
    LogMessage(env, obj, "Receiving from the socket...");
    ssize_t recvSize = recv(sd, buffer, bufferSize - 1, 0);

    // If receive is failed
    if (-1 == recvSize) {
        // Throw an exception with error number
        ThrowErrnoException(env, "java/io/IOException", errno);
    } else {
        // NULL terminate the buffer to make it a string
        buffer[recvSize] = NULL;
        // If data is received
        if (recvSize > 0) {
            LogMessage(env, obj, "Received %d bytes: %s", recvSize, buffer);
        } else {
            LogMessage(env, obj, "Client disconnected.");
        }
    }

    return recvSize;
}

/**
 * Send data buffer to the socket.
 *
 * @param env JNIEnv interface.
 * @param obj object instance.
 * @param sd socket descriptor.
 * @param buffer data buffer.
 * @param bufferSize buffer size.
 * @return sent size.
 * @throws IOException
 */
ssize_t
SendToSocket(JNIEnv *env, jobject obj, int sd, const char *buffer, size_t bufferSize) {
    // Send data buffer to the socket
    LogMessage(env, obj, "Sending to the socket...");
    ssize_t sentSize = send(sd, buffer, bufferSize, 0);

    // If send is failed
    if (-1 == sentSize) {
        // Throw an exception with error number
        ThrowErrnoException(env, "java/io/IOException", errno);
    } else {
        if (sentSize > 0) {
            LogMessage(env, obj, "Sent %d bytes: %s", sentSize, buffer);
        } else {
            LogMessage(env, obj, "Client disconnected.");
        }
    }

    return sentSize;
}

/**
 * Connects to given IP address and given port.
 *
 * @param env JNIEnv interface.
 * @param obj object instance.
 * @param sd socket descriptor.
 * @param ip IP address.
 * @param port port number.
 * @throws IOException
 */
void
ConnectToAddress(JNIEnv *env, jobject obj, int sd, const char *ip, unsigned short port) {
    // Connecting to given IP address and given port
    LogMessage(env, obj, "Connecting to %s:%uh...", ip, port);

    struct sockaddr_in address;

    memset(&address, 0, sizeof(address));
    address.sin_family = PF_INET;

    // Convert IP address string to Internet address
    if (0 == inet_aton(ip, &(address.sin_addr))) {
        // Throw an exception with error number
        ThrowErrnoException(env, "java/io/IOException", errno);
    } else {
        // Convert port to network byte order
        address.sin_port = htons(port);

        // Connect to address
        if (-1 == connect(sd, (const sockaddr *) &address, sizeof(address))) {
            // Throw an exception with error number
            ThrowErrnoException(env, "java/io/IOException", errno);
        } else {
            LogMessage(env, obj, "Connected.");
        }
    }
}

