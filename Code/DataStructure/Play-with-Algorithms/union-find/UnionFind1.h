/*
 ============================================================================
 
 Author      : Ztiany
 Description : 并查集：数组实现

 ============================================================================
 */


#ifndef PLAY_WITH_ALGORITHMS_UF1_H
#define PLAY_WITH_ALGORITHMS_UF1_H

#include <cassert>
#include "UnionFindBase.h"

namespace UF1 {

    class UnionFind1 : public UnionFindBase {
    private:
        int *parent;
        int count;
    public:

        explicit UnionFind1(int size) {
            this->count = size;
            parent = new int[size];
            for (int i = 0; i < size; ++i) {
                parent[i] = i;
            }
        }

        ~UnionFind1() {
            delete[] parent;
        }

        int find(int p) override {
            assert(p >= 0 && p <= count);
            return parent[p];
        }

        bool isConnected(int p, int q) override {
            return find(p) == find(q);
        }

        void unionElements(int p, int q) override {
            assert(p >= 0 && p <= count);
            assert(q >= 0 && q <= count);
            int idP = find(p);
            int idQ = find(q);
            if (idP != idQ) {
                for (int i = 0; i < count; ++i) {
                    if (parent[i] == idP) {
                        parent[i] = idQ;
                    }
                }
            }
        }

    };
}

#endif //PLAY_WITH_ALGORITHMS_UF1_H
