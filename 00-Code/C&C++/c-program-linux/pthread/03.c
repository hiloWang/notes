#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>
#include <pthread.h>

//消费者数量
#define CONSUMER_NUM 2
//生产者数量
#define PRODUCER_NUM 1

pthread_t pids[CONSUMER_NUM + PRODUCER_NUM];

//产品队列
int ready = 0;

//互斥锁
pthread_mutex_t mutex;
//条件变量
pthread_cond_t has_product;

//生产
void *producer(void *arg) {
    int no = (int) arg;
    printf("create producer %d\n", no);
    //条件变量
    for (;;) {
        pthread_mutex_lock(&mutex);
        //往队列中添加产品
        ready++;
        printf("producer %d, produce product\n", no);
        //通知消费者，有新的产品可以消费了
        //会阻塞输出
        pthread_cond_signal(&has_product);
        printf("producer %d, singal\n", no);
        pthread_mutex_unlock(&mutex);
        sleep(1);
    }
}

//消费者
void *consumer(void *arg) {
    int num = (int) arg;
    printf("create consumer %d\n", num);
    for (;;) {
        pthread_mutex_lock(&mutex);
        //while?
        //superious wake ‘惊群效应’
        while (ready == 0) {
            //没有产品，继续等待
            //1.阻塞等待has_product被唤醒
            //2.释放互斥锁，pthread_mutex_unlock
            //3.被唤醒时，解除阻塞，重新申请获得互斥锁pthread_mutex_lock
            printf("%d consumer wait\n", num);
            pthread_cond_wait(&has_product, &mutex);
        }
        //有产品，消费产品
        ready--;
        printf("%d consume product\n", num);
        pthread_mutex_unlock(&mutex);
        sleep(1);
    }
}


void main() {
    //初始化互斥锁和条件变量
    pthread_mutex_init(&mutex, NULL);
    pthread_cond_init(&has_product, NULL);
    printf("init\n");

    int i;
    for (i = 0; i < PRODUCER_NUM; i++) {
        //生产者线程
        printf("%d\n", i);
        pthread_create(&pids[i], NULL, producer, (void *) i);
    }

    for (i = 0; i < CONSUMER_NUM; i++) {
        //消费者线程
        pthread_create(&pids[PRODUCER_NUM + i], NULL, consumer, (void *) i);
    }

    //等待
    sleep(10);
    for (i = 0; i < PRODUCER_NUM + CONSUMER_NUM; i++) {
        pthread_join(pids[i], NULL);
    }

    //销毁互斥锁和条件变量
    pthread_mutex_destroy(&mutex);
    pthread_cond_destroy(&has_product);

}
