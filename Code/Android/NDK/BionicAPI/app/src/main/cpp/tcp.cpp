#include "JniBridge.h"
#include "socket-api.h"

#include <jni.h>
#include <stdio.h>
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

//Constructs a new TCP socket, return socket descriptor.
static int NewTcpSocket(JNIEnv *env, jobject obj) {
    LogMessage(env, obj, "Constructing a new TCP socket...");
    int tcpSocket = socket(PF_INET, SOCK_STREAM, 0);

    if (-1 == tcpSocket) {
        ThrowErrnoException(env, "java/io/IOException", errno);
    }

    return tcpSocket;
}


void Java_com_ztiany_bionic_tcpudp_EchoClientActivity_nativeStartTcpClient(JNIEnv *env, jobject obj,
                                                                           jstring ip, jint port,
                                                                           jstring message) {

    // Construct a new TCP socket.
    int clientSocket = NewTcpSocket(env, obj);

    if (NULL == env->ExceptionOccurred()) {
        // Get IP address as C string
        const char *ipAddress = env->GetStringUTFChars(ip, NULL);
        if (NULL == ipAddress)
            goto exit;

        // Connect to IP address and port
        ConnectToAddress(env, obj, clientSocket, ipAddress, (unsigned short) port);

        // Release the IP address
        env->ReleaseStringUTFChars(ip, ipAddress);

        // If connection was successful
        if (NULL != env->ExceptionOccurred())
            goto exit;

        // Get message as C string
        const char *messageText = env->GetStringUTFChars(message, NULL);
        if (NULL == messageText)
            goto exit;

        // Get the message size
        jsize messageSize = env->GetStringUTFLength(message);

        // Send message to socket
        SendToSocket(env, obj, clientSocket, messageText, messageSize);

        // Release the message text
        env->ReleaseStringUTFChars(message, messageText);

        // If send was not successful
        if (NULL != env->ExceptionOccurred())
            goto exit;

        char buffer[MAX_BUFFER_SIZE];

        // Receive from the socket
        ReceiveFromSocket(env, obj, clientSocket, buffer, MAX_BUFFER_SIZE);
    }

    exit:
    if (clientSocket > 0) {
        close(clientSocket);
    }
}

void Java_com_ztiany_bionic_tcpudp_EchoServerActivity_nativeStartTcpServer(
        JNIEnv *env,
        jobject obj,
        jint port) {

    // Construct a new TCP socket.
    int serverSocket = NewTcpSocket(env, obj);

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

        // Listen on socket with a backlog of 4 pending connections
        ListenOnSocket(env, obj, serverSocket, 4);
        if (NULL != env->ExceptionOccurred())
            goto exit;

        // Accept a client connection on socket
        int clientSocket = AcceptOnSocket(env, obj, serverSocket);
        if (NULL != env->ExceptionOccurred())
            goto exit;

        char buffer[MAX_BUFFER_SIZE];
        ssize_t recvSize;
        ssize_t sentSize;

        // Receive and send back the data
        while (1) {
            // Receive from the socket
            recvSize = ReceiveFromSocket(env, obj, clientSocket, buffer, MAX_BUFFER_SIZE);

            if ((0 == recvSize) || (NULL != env->ExceptionOccurred()))
                break;

            // Send to the socket
            sentSize = SendToSocket(env, obj, clientSocket, buffer, (size_t) recvSize);

            if ((0 == sentSize) || (NULL != env->ExceptionOccurred()))
                break;
        }

        // Close the client socket
        close(clientSocket);
    }

    exit:
    if (serverSocket > 0) {
        close(serverSocket);
    }
}