/*
 ============================================================================
 
 Author      : Ztiany
 Description : 快速排序，3 ways 法

 ============================================================================
 */


#ifndef PLAY_WITH_ALGORITHMS_QUICKSORT3WAYS_H
#define PLAY_WITH_ALGORITHMS_QUICKSORT3WAYS_H

template<typename T>
static void partition3Ways(T *arr, int left, int right) {

    // 对于小规模数组, 使用插入排序进行优化
    if (right - left <= 15) {
        insertionSort(arr, left, right);
        return;
    }

    swap(arr[left], arr[rand() % (right - left + 1) + left]);

    T v = arr[left];

    int lt = left;     // arr[l+1, lt] < v
    int gt = right + 1; // arr[gt, r] > v
    int i = left + 1;    // arr[lt+1, i) == v
    /*
     过程演示：
      lt  i                                                 gt
      5  1  2  8  7  5  5  5  1   9  9  2  3
          lt  i                                             gt
      5  1  2  8  7  5  5  5  1   9  9  2  3
          lt  i                                             gt
      5  1  2  8  7  5  5  5  1   9  9  2  3
              lt  i                                         gt
      5  1  2  8  7  5  5  5  1   9  9  2  3
              lt  i                                     gt
      5  1  2  3  7  5  5  5  1   9  9  2  8
                   lt  i                                gt
      5  1  2  3  7  5  5  5  1   9  9  2  8
                   lt  i                            gt
      5  1  2  3  2  5  5  5  1   9  9  7  8
                       lt  i                        gt
      5  1  2  3  2  5  5  5  1   9  9  7  8
                       lt              i            gt
      5  1  2  3  2  5  5  5  1   9  9  7  8
                           lt               i       gt
      5  1  2  3  2  1  5  5   5  9  9  7  8
                           lt               i   gt
      5  1  2  3  2  1  5  5   5  9  9  7  8
                                            gt
                           lt               i
      5  1  2  3  2  1  5  5   5  9  9  7  8
                                            gt
                           lt               i
      1  1  2  3  2  5  5  5   5  9  9  7  8
      然后：1  1  2  3  2  和 9  9  7  8 分别继续 partition
     */
    /*判断基准的arr[i]，i是分界点*/
    while (i < gt) {
        if (arr[i] < v) {
            std::swap(arr[i], arr[lt + 1]);
            lt++;
            i++;
        } else if (arr[i] > v) {
            std::swap(arr[i], arr[gt - 1]);
            gt--;
        } else {//arr[i] == v
            i++;
        }
    }

    std::swap(arr[left], arr[lt]);

    partition3Ways(arr, left, lt - 1);
    partition3Ways(arr, gt, right);
}

template<typename T>
void quickSort3Ways(T *arr, int size) {
    std::srand(NULL);//随机种子
    partition3Ways(arr, 0, size - 1);
}

#endif //PLAY_WITH_ALGORITHMS_QUICKSORT3WAYS_H
