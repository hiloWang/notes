#include "JniBridge.h"
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

/**
 * Constructs a new UDP socket.
 *
 * @param env JNIEnv interface.
 * @param obj object instance.
 * @return socket descriptor.
 * @throws IOException
 */
static int NewUdpSocket(JNIEnv *env, jobject obj) {
    // Construct socket
    LogMessage(env, obj, "Constructing a new UDP socket...");
    int udpSocket = socket(PF_INET, SOCK_DGRAM, 0);

    // Check if socket is properly constructed
    if (-1 == udpSocket) {
        // Throw an exception with error number
        ThrowErrnoException(env, "java/io/IOException", errno);
    }

    return udpSocket;
}

/**
 * Block and receive datagram from the socket into
 * the buffer, and populate the client address.
 *
 * @param env JNIEnv interface.
 * @param obj object instance.
 * @param sd socket descriptor.
 * @param address client address.
 * @param buffer data buffer.
 * @param bufferSize buffer size.
 * @return receive size.
 * @throws IOException
 */
static ssize_t ReceiveDatagramFromSocket(
        JNIEnv *env,
        jobject obj,
        int sd,
        struct sockaddr_in *address,
        char *buffer,
        size_t bufferSize) {

    socklen_t addressLength = sizeof(struct sockaddr_in);

    // Receive datagram from socket
    LogMessage(env, obj, "Receiving from the socket...");

    ssize_t recvSize = recvfrom(sd, buffer, bufferSize, 0, (struct sockaddr *) address,
                                &addressLength);

    // If receive is failed
    if (-1 == recvSize) {
        // Throw an exception with error number
        ThrowErrnoException(env, "java/io/IOException", errno);
    } else {
        // Log address
        LogAddress(env, obj, "Received from", address);

        // NULL terminate the buffer to make it a string
        buffer[recvSize] = NULL;

        // If data is received
        if (recvSize > 0) {
            LogMessage(env, obj, "Received %d bytes: %s", recvSize, buffer);
        }
    }

    return recvSize;
}

/**
 * Sends datagram to the given address using the given socket.
 *
 * @param env JNIEnv interface.
 * @param obj object instance.
 * @param sd socket descriptor.
 * @param address remote address.
 * @param buffer data buffer.
 * @param bufferSize buffer size.
 * @return sent size.
 * @throws IOException
 */
static ssize_t
SendDatagramToSocket(JNIEnv *env, jobject obj, int sd, const struct sockaddr_in *address,
                     const char *buffer, size_t bufferSize) {

    // Send data buffer to the socket
    LogAddress(env, obj, "Sending to", address);

    ssize_t sentSize = sendto(sd, buffer, bufferSize, 0, (const sockaddr *) address,
                              sizeof(struct sockaddr_in));

    // If send is failed
    if (-1 == sentSize) {
        // Throw an exception with error number
        ThrowErrnoException(env, "java/io/IOException", errno);
    } else if (sentSize > 0) {
        LogMessage(env, obj, "Sent %d bytes: %s", sentSize, buffer);
    }

    return sentSize;
}


void Java_com_ztiany_bionic_tcpudp_EchoClientActivity_nativeStartUdpClient(
        JNIEnv *env,
        jobject obj,
        jstring ip,
        jint port,
        jstring message) {
    // Construct a new UDP socket.
    int clientSocket = NewUdpSocket(env, obj);
    if (NULL == env->ExceptionOccurred()) {
        struct sockaddr_in address;

        memset(&address, 0, sizeof(address));
        address.sin_family = PF_INET;

        // Get IP address as C string
        const char *ipAddress = env->GetStringUTFChars(ip, NULL);
        if (NULL == ipAddress)
            goto exit;

        // Convert IP address string to Internet address
        int result = inet_aton(ipAddress, &(address.sin_addr));

        // Release the IP address
        env->ReleaseStringUTFChars(ip, ipAddress);

        // If conversion is failed
        if (0 == result) {
            // Throw an exception with error number
            ThrowErrnoException(env, "java/io/IOException", errno);
            goto exit;
        }

        // Convert port to network byte order
        address.sin_port = htons(port);

        // Get message as C string
        const char *messageText = env->GetStringUTFChars(message, NULL);
        if (NULL == messageText)
            goto exit;

        // Get the message size
        jsize messageSize = env->GetStringUTFLength(message);

        // Send message to socket
        SendDatagramToSocket(env, obj, clientSocket, &address, messageText, messageSize);

        // Release the message text
        env->ReleaseStringUTFChars(message, messageText);

        // If send was not successful
        if (NULL != env->ExceptionOccurred())
            goto exit;

        char buffer[MAX_BUFFER_SIZE];

        // Clear address
        memset(&address, 0, sizeof(address));

        // Receive from the socket
        ReceiveDatagramFromSocket(env, obj, clientSocket, &address, buffer, MAX_BUFFER_SIZE);
    }

    exit:
    if (clientSocket > 0) {
        close(clientSocket);
    }
}

void Java_com_ztiany_bionic_tcpudp_EchoServerActivity_nativeStartUdpServer(
        JNIEnv *env,
        jobject obj,
        jint port) {
    // Construct a new UDP socket.
    int serverSocket = NewUdpSocket(env, obj);
    if (NULL == env->ExceptionOccurred()) {
        // Bind socket to a port number
        BindSocketToPort(env, obj, serverSocket, (unsigned short) port);
        if (NULL != env->ExceptionOccurred())
            goto exit;

        // If random port number is requested
        if (0 == port) {
            // Get the port number socket is currently binded
            GetSocketPort(env, obj, serverSocket);
            if (NULL != env->ExceptionOccurred())
                goto exit;
        }

        // Client address
        struct sockaddr_in address;
        memset(&address, 0, sizeof(address));

        char buffer[MAX_BUFFER_SIZE];
        ssize_t recvSize;
        ssize_t sentSize;

        // Receive from the socket
        recvSize = ReceiveDatagramFromSocket(env, obj, serverSocket,
                                             &address, buffer, MAX_BUFFER_SIZE);

        if ((0 == recvSize) || (NULL != env->ExceptionOccurred()))
            goto exit;

        // Send to the socket
        sentSize = SendDatagramToSocket(env, obj, serverSocket,
                                        &address, buffer, (size_t) recvSize);
    }

    exit:
    if (serverSocket > 0) {
        close(serverSocket);
    }
}
