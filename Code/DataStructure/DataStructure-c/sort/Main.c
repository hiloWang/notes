/*
 ============================================================================

 Author      : Ztiany
 Description : 排序

 ============================================================================
 */


#include <stdio.h>
#include <stdlib.h>
#include <time.h>

#define  MAX 1000000

long getSystemTime() {
    struct timeb tb;
    ftime(&tb);
    return tb.time * 1000 + tb.millitm;
}

//创建数组
int *createArray() {
    srand((unsigned int) time(NULL));
    int *arr = (int *) malloc(sizeof(int) * MAX);
    for (int i = 0; i < MAX; i++) {
        arr[i] = rand() % MAX;
    }
    return arr;
}


void swap(int *a, int *b) {
    int temp = *a;
    *a = *b;
    *b = temp;
}

void printArray(int *arr, int count) {
    printf("print array begin \n");
    for (int i = 0; i < count; ++i) {
        printf("%d ", arr[i]);
    }
    printf("\n");
}

/**冒泡排序*/
void bubbleSort(int *arr, int size) {
    long start = getSystemTime();

    for (int i = 0; i < size; ++i) {
        for (int j = 0; j < size - i - 1; ++j) {
            if (arr[j] > arr[j + 1]) {
                swap(&arr[j], &arr[j + 1]);
            }
        }
    }

    printf("bubbleSort count %d use time: %ld \n", size, (getSystemTime() - start));
}

/**选择排序*/
void selectSort(int *arr, int size) {
    long start = getSystemTime();

    //每一次遍历的最小值索引
    int min;
    for (int i = 0; i < size; ++i) {

        min = i;

        for (int j = i + 1; j < size; ++j) {
            if (arr[j] < arr[min]) {
                min = j;
            }
        }

        if (min != i) {
            swap(&arr[min], &arr[i]);
        }
    }

    printf("selectSort count %d use time: %ld \n", size, (getSystemTime() - start));
}


/**插入排序：将无序序列插入到有序序列中，在基本有序的序列中非常高效，数据序列少的情况下也比较高效*/
void insertSort(int *arr, int size) {
    long start = getSystemTime();

    //第一次把第一个数当作是有序的序列
    int j;
    for (int i = 1; i < size; ++i) {

        //如果右边的无序序列的第一个数 小于左边的有序序列的最后一个数，开始插入
        if (arr[i] < arr[i - 1]) {
            //记住无序序列的第一个数
            int temp = arr[i];
            //直到遇到不必这个数小位置，插入到这个数之前
            for (j = i - 1; j >= 0 && temp < arr[j]; --j) {
                arr[j + 1] = arr[j];
            }
            arr[j + 1] = temp;
        }
    }

    printf("insertSort count %d use time: %ld \n", size, (getSystemTime() - start));
}

/**希尔排序，对数据进行分段，然后每段运用插入排序*/
void shellSort(int *arr, int size) {
    long start = getSystemTime();

    int increment = size;
    int i, j, k;

    do {
        increment = increment / 3 + 1;
        //分成step组，每组遍历

        for (i = 0; i < increment; i++) {
            printf("increment = %d \n", increment);
            for (j = i + increment; j < size; j += increment) {

                if (arr[j] < arr[j - increment]) {

                    int temp = arr[j];
                    for (k = j - increment; k >= i && temp < arr[k]; k -= increment) {
                        arr[k + increment] = arr[k];
                    }

                    arr[k + increment] = temp;
                }

            }

        }

    } while (increment > 1);


    printf("shellSort count %d use time: %ld \n", size, (getSystemTime() - start));

}


void quickSort(int *arr, int start, int end) {
    //一个坑+两个指针，再一次迭代中被选坑的数不变
    int i = start;
    int j = end;
    int temp;
    //递归条件
    if (i < j) {
        //移动条件
        while (i < j) {
            temp = arr[start];
            //从右边到左边：指针不重叠，且找到一个最右边的数比坑中的数小
            while (i < j && arr[j] >= temp) {
                j--;
            }

            //填坑：右边的那个数入坑，此时左边的指针可以往右一步
            if (i < j) {
                arr[i] = arr[j];
                i++;
            }
            //从左边到右边：指针不重叠，且找到一个最左边的数比坑中的数大
            while (i < j && arr[i] < temp) {
                i++;
            }
            //填坑，左边的那个数入坑，此时右边的指针可以往左一步
            if (i < j) {
                arr[j] = arr[i];
                j--;
            }
        }

        //i == j
        arr[i] = temp;
        quickSort(arr, start, i - 1);
        quickSort(arr, i + 1, end);
    }
}

void quickSortTest(int *arr, int start, int end) {
    long startTime = getSystemTime();
    quickSort(arr, start, end);
    printf("quickSort count %d use time: %ld \n", (end + 1), (getSystemTime() - startTime));
}


//合并算法：从小到大
void merge(int arr[], int start, int end, int mid, int *temp) {
    int i_start = start;
    int i_end = mid;
    int j_start = mid + 1;
    int j_end = end;
    //表示辅助空间有多少个元素
    int length = 0;
    //合并两个有序序列
    while (i_start <= i_end && j_start <= j_end) {
        if (arr[i_start] < arr[j_start]) {
            temp[length] = arr[i_start];
            length++;
            i_start++;
        } else {
            temp[length] = arr[j_start];
            j_start++;
            length++;
        }
    }
    //i这个序列
    while (i_start <= i_end) {
        temp[length] = arr[i_start];
        i_start++;
        length++;
    }
    //j序列
    while (j_start <= j_end) {
        temp[length] = arr[j_start];
        length++;
        j_start++;
    }
    //辅助空间数据覆盖原空间
    for (int i = 0; i < length; i++) {
        arr[start + i] = temp[i];
    }
}

//归并排序
void mergeSort(int arr[], int start, int end, int *temp) {

    if (start >= end) {
        return;
    }

    int mid = (start + end) / 2;
    //分组
    //左半边
    mergeSort(arr, start, mid, temp);
    //右半边
    mergeSort(arr, mid + 1, end, temp);
    //合并
    merge(arr, start, end, mid, temp);
}


void mergeSortTest() {
    int *myArr = createArray();
    //辅助空间
    int *temp = (int *) malloc(sizeof(int) * MAX);
    long startTime = getSystemTime();
    mergeSort(myArr, 0, MAX - 1, temp);
    long t_end = getSystemTime();
    //PrintArray(myArr, MAX);
    printf("mergeSort count %d use time: %ld \n", (MAX), (getSystemTime() - startTime));
    //释放空间
    free(temp);
    free(myArr);
}


/**
	@param myArr 待调整的数组
	@param index 待调整的结点的下标
	@param len 数组的长度
*/
void heapAdjust(int arr[], int index, int len) {

    //先保存当前结点的下标
    int max = index;
    //保存左右孩子的数组下标
    int lchild = index * 2 + 1;
    int rchild = index * 2 + 2;

    if (lchild < len && arr[lchild] < arr[max]) {
        max = lchild;
    }

    if (rchild < len && arr[rchild] < arr[max]) {
        max = rchild;
    }

    if (max != index) {
        //交换两个结点
        swap(&arr[max], &arr[index]);
        heapAdjust(arr, max, len);
    }
}

/**堆排序*/
void heapSort(int myArr[], int len) {
    //初始化堆
    for (int i = len / 2 - 1; i >= 0; i--) {
        heapAdjust(myArr, i, len);
    }

    //交换堆顶元素和最后一个元素
    for (int i = len - 1; i >= 0; i--) {
        swap(&myArr[0], &myArr[i]);
        heapAdjust(myArr, 0, i);
    }
}

void heapSortTest(int *arr, int size) {
    long startTime = getSystemTime();
    heapSort(arr, size);
    printf("heapSort count %d use time: %ld \n", (MAX), (getSystemTime() - startTime));
}

int main() {
    int *arr = createArray();

//    bubbleSort(arr, MAX); //冒泡排序
//    selectSort(arr, MAX); //选择排序
//    insertSort(arr, MAX); //插入排序
//    shellSort(arr, MAX);  //希尔排序
//    quickSortTest(arr, 0, MAX - 1);   //快速排序
//    mergeSortTest();  //归并排序
//    heapSortTest(arr, MAX);   //调用排序
//    printArray(arr, MAX);
    return EXIT_SUCCESS;
}