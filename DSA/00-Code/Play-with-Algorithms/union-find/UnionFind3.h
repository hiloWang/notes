/*
 ============================================================================
 
 Author      : Ztiany
 Description : 并查集：树实现，基于 size 优化。

 ============================================================================
 */

#ifndef PLAY_WITH_ALGORITHMS_UNIONFIND3_H
#define PLAY_WITH_ALGORITHMS_UNIONFIND3_H

#include <cassert>
#include "UnionFindBase.h"

namespace UF3 {
    class UnionFind : public UnionFindBase {
    private:
        int *parent;// parent[i]表示第i个元素所指向的父节点
        int count;
        int *sz;// sz[i]表示以i为根的集合中元素个数

    public:

        explicit UnionFind(int size) {
            count = size;
            parent = new int[size];
            sz = new int[size];
            for (int i = 0; i < size; ++i) {
                parent[i] = i;
                sz[i] = 1;
            }
        }

        ~UnionFind() {
            delete[] parent;
            delete[] sz;
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
                if (sz[p] > sz[q]) {
                    parent[q] = idP;
                    sz[p] += sz[q];
                } else {
                    parent[p] = idQ;
                    sz[q] += sz[p];
                }
            }
        }

    };

}

#endif //PLAY_WITH_ALGORITHMS_UNIONFIND3_H
