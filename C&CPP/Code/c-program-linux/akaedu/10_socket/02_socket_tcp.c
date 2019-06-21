/*
 ============================================================================

 Author      : Ztiany
 Description : Linux socket编程：socket tcp 编程

 ============================================================================
 */

/*
具体参考：https://akaedu.github.io/book/ch37s02.html
*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <unistd.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>

#define MAXLINE 80
#define SERV_PORT 8000

void socketServer()
{
    struct sockaddr_in servaddr, cliaddr;
    socklen_t cliaddr_len;
    int listenfd, connfd;
    char buf[MAXLINE];
    char str[INET_ADDRSTRLEN];
    int i, n;

    //socket()打开一个网络通讯端口，如果成功的话，就像open()一样返回一个文件描述符，应用程序可以像读写文件一样用read/write在网络上收发数据
    //对于IPv4，family参数指定为AF_INET。对于TCP协议，type参数指定为SOCK_STREAM，表示面向流的传输协议。
    listenfd = socket(AF_INET, SOCK_STREAM, 0);

    bzero(&servaddr, sizeof(servaddr));

    //struct sockaddr *是一个通用指针类型，myaddr参数实际上可以接受多种协议的sockaddr结构体，而它们的长度各不相同，所以需要第三个参数addrlen指定结构体的长度。
    //首先将整个结构体清零，然后设置地址类型为AF_INET，网络地址为INADDR_ANY，这个宏表示本地的任意IP地址，因为服务器可能有多个网卡，每个网卡也可能绑定多个IP地址，
    //这样设置可以在所有的IP地址上监听，直到与某个客户端建立了连接时才确定下来到底用哪个IP地址，端口号为SERV_PORT，我们定义为8000。
    servaddr.sin_family = AF_INET;
    servaddr.sin_addr.s_addr = htonl(INADDR_ANY);
    servaddr.sin_port = htons(SERV_PORT);

    //服务器程序所监听的网络地址和端口号通常是固定不变的，客户端程序得知服务器程序的地址和端口号后就可以向服务器发起连接，
    //因此服务器需要调用bind绑定一个固定的网络地址和端口号。bind()成功返回0，失败返回-1。
    //bind()的作用是将参数sockfd和myaddr绑定在一起，使sockfd这个用于网络通讯的文件描述符监听myaddr所描述的地址和端口号。
    bind(listenfd, (struct sockaddr *)&servaddr, sizeof(servaddr));

    //典型的服务器程序可以同时服务于多个客户端，当有客户端发起连接时，服务器调用的accept()返回并接受这个连接，如果有大量的客户端发起连接而服务器来不及处理，
    //尚未accept的客户端就处于连接等待状态，listen()声明sockfd处于监听状态，并且最多允许有backlog个客户端处于连接待状态，如果接收到更多的连接请求就忽略。
    listen(listenfd, 20);

    printf("Accepting connections ...\n");

    //三方握手完成后，服务器调用accept()接受连接，如果服务器调用accept()时还没有客户端的连接请求，就阻塞等待直到有客户端连接上来。
    while (1)
    {
        //cliaddr是一个传出参数，accept()返回时传出客户端的地址和端口号。addrlen参数是一个传入传出参数（value-result argument），
        //传入的是调用者提供的缓冲区cliaddr的长度以避免缓冲区溢出问题，传出的是客户端地址结构体的实际长度（有可能没有占满调用者提供的缓冲区）。
        //如果给cliaddr参数传NULL，表示不关心客户端的地址。
        cliaddr_len = sizeof(cliaddr);

        //accept()的参数listenfd是先前的监听文件描述符，而accept()的返回值是另外一个文件描述符connfd，之后与客户端之间就通过这个connfd通讯，
        //最后关闭connfd断开连接，而不关闭listenfd，再次回到循环开头listenfd仍然用作accept的参数。accept()成功返回一个文件描述符，出错返回-1。
        connfd = accept(listenfd, (struct sockaddr *)&cliaddr, &cliaddr_len);

        n = read(connfd, buf, MAXLINE);

        printf("received from %s at PORT %d\n",
               inet_ntop(AF_INET, &cliaddr.sin_addr, str, sizeof(str)),
               ntohs(cliaddr.sin_port));

        for (i = 0; i < n; i++)
        {
            buf[i] = toupper(buf[i]);
        }
        write(connfd, buf, n);
        close(connfd);
    }
}

void socketClient(int argc, char const *argv[])
{
    struct sockaddr_in servaddr;
    char buf[MAXLINE];
    int sockfd, n;
    char *str;

    if (argc < 3)
    {
        fputs("usage: ./client 2 message\n", stderr);
        exit(1);
    }
    str = argv[2];

    //由于客户端不需要固定的端口号，因此不必调用bind()，客户端的端口号由内核自动分配。注意，客户端不是不允许调用bind()，
    //只是没有必要调用bind()固定一个端口号，服务器也不是必须调用bind()，但如果服务器不调用bind()，内核会自动给服务器分配监听端口，
    //每次启动服务器时端口号都不一样，客户端要连接服务器就会遇到麻烦。
    sockfd = socket(AF_INET, SOCK_STREAM, 0);

    bzero(&servaddr, sizeof(servaddr));

    servaddr.sin_family = AF_INET;
    inet_pton(AF_INET, "127.0.0.1", &servaddr.sin_addr);
    servaddr.sin_port = htons(SERV_PORT);

    //客户端需要调用connect()连接服务器，connect和bind的参数形式一致，区别在于bind的参数是自己的地址，而connect的参数是对方的地址。
    connect(sockfd, (struct sockaddr *)&servaddr, sizeof(servaddr));

    write(sockfd, str, strlen(str));

    n = read(sockfd, buf, MAXLINE);
    printf("Response from server:\n");
    write(STDOUT_FILENO, buf, n);

    close(sockfd);
    return 0;
}

int main(int argc, char const *argv[])
{
    if (argc < 2)
    {
        printf("specfiy which function that you want to call\n");
        return EXIT_FAILURE;
    }

    const char *target = argv[1];

    if (strcmp(target, "1") == 0)
    {
        socketServer();
    }
    else if (strcmp(target, "2") == 0)
    {
        socketClient(argc, argv);
    }
    else
    {
        printf("specfiy which function that you want to call\n");
    }

    return 0;
}