/*
 ============================================================================
 
 Author      : Ztiany
 Description : 并查集

 ============================================================================
 */
#include <cstdlib>
#include <iostream>
#include <vector>
#include <string>
#include "union-find/UnionFind1.h"
#include "union-find/UnionFindTestHelper.h"

int main() {
    int n = 1000000;
    //UnionFIndTestHelper::testUF1(n);
    UnionFIndTestHelper::testUF2(n);
    UnionFIndTestHelper::testUF3(n);
    UnionFIndTestHelper::testUF4(n);
    UnionFIndTestHelper::testUF5(n);

    return EXIT_SUCCESS;
}