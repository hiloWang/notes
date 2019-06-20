/*
 ============================================================================
 
 Author      : Ztiany
 Description : 希尔排序

 ============================================================================
 */

#ifndef PLAY_WITH_ALGORITHMS_SHELLSORT_H
#define PLAY_WITH_ALGORITHMS_SHELLSORT_H

template<typename T>
void shellSort(T *arr, int size) {

    int increment = size;//步长
    int i, j, k;

    do {
        /* 组数 =  increment，每次分组排序完成后，分组数量减少  */
        increment = increment / 3 + 1;

        for (i = 0; i < increment; i++) {/*有多少组，就要遍历几次*/

            /*插入排序开始*/
            /*每一组，从第 2  个元素开始遍历，每组的第二个元素索引是 i + increment */
            for (j = i + increment; j < size; j += increment/*increment是每组元素的步长*/) {//这个for循环的次数是就是组元素的个数-1

                if (arr[j] < arr[j - increment]) {/*是否需要进行插入：如果后面的元素比前面的还小，就开始进行插入排序*/

                    T temp = arr[j];//存储当前需要插入的值
                    for (k = j - increment; k >= i  && (temp < arr[k]); k -= increment) {
                        arr[k + increment] = arr[k];// 第一次时：k + increment = j
                    }
                    arr[k + increment] = temp;//找到了需要插入的位置

                }

            }//插入排序 end

        }

    } while (increment > 1);/*继续进行排序的条件*/

}

#endif
