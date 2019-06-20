/*
 ============================================================================
 
 Description : queue

 ============================================================================
 */


#include <iostream>
#include<queue>

using namespace std;

void test01() {

    queue<int> q; //创建队列

    q.push(10);
    q.push(20);
    q.push(30);
    q.push(40);

    cout << "队尾：" << q.back() << endl;

    //输出顺序? 10 20 30 40
    while (q.size() > 0) {

        cout << q.front() << " "; //输出队头元素
        q.pop(); //删除队头

    }

    //作业1 queue容器存放对象指针
    //作业2 queue容器存放stack容器
}


int main(void) {
    test01();
    return 0;
}
