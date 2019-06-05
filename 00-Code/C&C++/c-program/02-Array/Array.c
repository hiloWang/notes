/*
 ============================================================================
 
 Author      : Ztiany
 Description :  数组

 ============================================================================
 */

int main() {
    //定义数组
    int array1[4] = {1, 2, 4, 5};
    int array2[] = {1, 2, 4, 5};

    //c99可以指定初始化某个元素
    int days[7] = {[6] = 6};

    //定义多维数组
    int multiArr1[][4] = {{4}, {4}};//直接初始化
    int multiArr2[3][4];//同时指定行列数
}

//使用数组作为参数，可以指定长度也可以不指定长度
//使用const修饰参数，可以防止array所指向的内容被修改
void arrayTest(const int array[]) {
    //array[0] = 3;不合法
}

//变长数组，c99之前，二维数组传参，必须指定数组的维度，c99开始可以使用变长数组，变长数组必须使用自动变量。
void variableLengthArray(int rows, int cols, int arr[rows][cols]) {
    for (int i = 0; i < cols; ++i) {

    }
}