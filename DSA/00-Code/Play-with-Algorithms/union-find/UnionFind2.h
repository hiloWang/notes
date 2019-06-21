/*
 ============================================================================
 
 Author      : Ztiany
 Description : 并查集：树实现，无优化。

 ============================================================================
 */

#ifndef PLAY_WITH_ALGORITHMS_UNIONFIND2_H
#define PLAY_WITH_ALGORITHMS_UNIONFIND2_H

#include <cassert>
#include "UnionFindBase.h"

namespace UF2 {
    class UnionFind : public UnionFindBase {
    private:
        int *parent;
        int count;

    public:

        explicit UnionFind(int size) {
            this->count = size;
            parent = new int[size];
            for (int i = 0; i < size; ++i) {
                parent[i] = i;
            }
        }

        ~UnionFind() {
            delete[] parent;
        }

        int find(int p) override {
            while (p != parent[p]) {
                p = parent[p];
            }
            return p;
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
                parent[p] = idQ;
            }
        }

    };

}

#endif //PLAY_WITH_ALGORITHMS_UNIONFIND2_H
