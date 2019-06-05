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
 * Constructs a new Local UNIX socket.
 *
 * @param env JNIEnv interface.
 * @param obj object instance.
 * @return socket descriptor.
 * @throws IOException
 */
static int NewLocalSocket(JNIEnv *env, jobject obj) {
    // Construct socket
    LogMessage(env, obj, "Constructing a new Local UNIX socket...");
    int localSocket = socket(PF_LOCAL, SOCK_STREAM, 0);

    // Check if socket is properly constructed
    if (-1 == localSocket) {
        // Throw an exception with error number
        ThrowErrnoException(env, "java/io/IOException", errno);
    }

    return localSocket;
}

/**
 * Binds a local UNIX socket to a name.
 *
 * @param env JNIEnv interface.
 * @param obj object instance.
 * @param sd socket descriptor.
 * @param name socket name.
 * @throws IOException
 */
static void BindLocalSocketToName(
        JNIEnv *env,
        jobject obj,
        int sd,
        const char *name) {
    struct sockaddr_un address;

    // Name length
    const size_t nameLength = strlen(name);

    // Path length is initiall equal to name length
    size_t pathLength = nameLength;

    // If name is not starting with a slash it is
    // in the abstract namespace
    bool abstractNamespace = ('/' != name[0]);

    // Abstract namespace requires having the first
    // byte of the path to be the zero byte, update
    // the path length to include the zero byte
    if (abstractNamespace) {
        pathLength++;
    }

    // Check the path length
    if (pathLength > sizeof(address.sun_path)) {
        // Throw an exception with error number
        ThrowException(env, "java/io/IOException", "Name is too big.");
    } else {
        // Clear the address bytes
        memset(&address, 0, sizeof(address));
        address.sun_family = PF_LOCAL;

        // Socket path
        char *sunPath = address.sun_path;

        // First byte must be zero to use the abstract namespace
        if (abstractNamespace) {
            *sunPath++ = NULL;
        }

        // Append the local name
        strcpy(sunPath, name);

        // Address length
        socklen_t addressLength =
                (offsetof(struct sockaddr_un, sun_path))
                + pathLength;

        // Unlink if the socket name is already binded
        unlink(address.sun_path);

        // Bind socket
        LogMessage(env, obj, "Binding to local name %s%s.", (abstractNamespace) ? "(null)" : "",
                   name);

        if (-1 == bind(sd, (struct sockaddr *) &address, addressLength)) {
            // Throw an exception with error number
            ThrowErrnoException(env, "java/io/IOException", errno);
        }
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
static int AcceptOnLocalSocket(JNIEnv *env, jobject obj, int sd) {
    // Blocks and waits for an incoming client connection
    // and accepts it
    LogMessage(env, obj, "Waiting for a client connection...");
    int clientSocket = accept(sd, NULL, NULL);

    // If client socket is not valid
    if (-1 == clientSocket) {
        // Throw an exception with error number
        ThrowErrnoException(env, "java/io/IOException", errno);
    }

    return clientSocket;
}


void
Java_com_ztiany_bionic_tcpudp_LocalEchoActivity_nativeStartLocalServer(JNIEnv *env, jobject obj,
                                                                       jstring name) {
    // Construct a new local UNIX socket.
    int serverSocket = NewLocalSocket(env, obj);
    if (NULL == env->ExceptionOccurred()) {
        // Get name as C string
        const char *nameText = env->GetStringUTFChars(name, NULL);
        if (NULL == nameText)
            goto exit;

        // Bind socket to a port number
        BindLocalSocketToName(env, obj, serverSocket, nameText);

        // Release the name text
        env->ReleaseStringUTFChars(name, nameText);

        // If bind is failed
        if (NULL != env->ExceptionOccurred())
            goto exit;

        // Listen on socket with a backlog of 4 pending connections
        ListenOnSocket(env, obj, serverSocket, 4);
        if (NULL != env->ExceptionOccurred())
            goto exit;

        // Accept a client connection on socket
        int clientSocket = AcceptOnLocalSocket(env, obj, serverSocket);
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
