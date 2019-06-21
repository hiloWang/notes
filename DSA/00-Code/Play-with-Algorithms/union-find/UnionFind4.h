/*
 ============================================================================
 
 Author      : Ztiany
 Description : 并查集：树实现，基于 rank 优化。

 ============================================================================
 */

#ifndef PLAY_WITH_ALGORITHMS_UNIONFIND4_H
#define PLAY_WITH_ALGORITHMS_UNIONFIND4_H

#include <cassert>
#include "UnionFindBase.h"

namespace UF4 {
    class UnionFind : public UnionFindBase {
    private:
        int *parent;// parent[i]表示第i个元素所指向的父节点
        int count;
        int *rank;// rank[i]表示以i为根的集合所表示的树的层数

    public:

        explicit UnionFind(int size) {
            count = size;
            parent = new int[size];
            rank = new int[size];
            for (int i = 0; i < size; ++i) {
                parent[i] = i;
                rank[i] = 1;
            }
        }

        ~UnionFind() {
            delete[] parent;
            delete[] rank;
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
                if (rank[p] > rank[q]) {
                    parent[q] = idP;
                } else if (rank[p] < rank[q]) {
                    parent[p] = idQ;
                } else {//rank[p] = rank[q]
                    parent[q] = idP;
                    rank[idP]++;
                }
            }
        }

    };

}

#endif //PLAY_WITH_ALGORITHMS_UNIONFIND4_H
