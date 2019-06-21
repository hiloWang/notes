/*
 ============================================================================
 
 Author      : Ztiany
 Description : 稀疏图

 ============================================================================
 */


#ifndef PLAY_WITH_ALGORITHMS_SPARSEGRAPH_H
#define PLAY_WITH_ALGORITHMS_SPARSEGRAPH_H

#include <iostream>
#include <vector>
#include <cassert>

/**稀疏图，邻接表*/
class SparseGraph {
private:
    int n, m;       // 节点数和边数
    bool directed;  // 是否为有向图
    std::vector<std::vector<int>> g;  // 图的具体数据
public:
    SparseGraph(int n, bool directed) {
        assert(n >= 0);
        this->n = n;
        this->m = 0;    // 初始化没有任何边
        this->directed = directed;
        // g初始化为n个空的vector, 表示每一个g[i]都为空, 即没有任和边
        g = std::vector<std::vector<int >>(n, std::vector<int>());
    }

    int V() { return n; } // 返回节点个数
    int E() { return m; } // 返回边的个数

    // 向图中添加一个边
    void addEdge(int v, int w) {
        assert(v >= 0 && v < n);
        assert(w >= 0 && w < n);
        g[v].push_back(w);
        if (v != w && !directed) {
            g[w].push_back(v);
        }
        m++;
    }

    // 验证图中是否有从v到w的边
    bool hasEdge(int v, int w) {
        assert(v >= 0 && v < n);
        assert(w >= 0 && w < n);
        for (int i : g[v]) {
            if (i == w) {
                return true;
            }
        }
        return false;
    }

    // 显示图的信息
    void show() {

        for (int i = 0; i < n; i++) {
            std::cout << "vertex " << i << ":\t";
            for (int j = 0; j < g[i].size(); j++) {
                std::cout << g[i][j] << "\t";
            }
            std::cout << std::endl;
        }
    }

public:
    // 邻边迭代器, 传入一个图和一个顶点，迭代在这个图中和这个顶点相连的所有顶点
    class adjIterator {
    private:
        SparseGraph &G; // 图G的引用
        int v;
        int index;

    public:
        // 构造函数
        adjIterator(SparseGraph &graph, int v) : G(graph) {
            this->v = v;
            this->index = 0;
        }

        // 返回图G中与顶点v相连接的第一个顶点
        int begin() {
            index = 0;
            if (!G.g[v].empty()) {
                return G.g[v][index];
            }
            // 若没有顶点和v相连接, 则返回-1
            return -1;
        }

        // 返回图G中与顶点v相连接的下一个顶点
        int next() {
            index++;
            if (index < G.g[v].size()) {
                return G.g[v][index];
            }
            // 若没有顶点和v相连接, 则返回-1
            return -1;
        }

        // 查看是否已经迭代完了图G中与顶点v相连接的所有顶点
        bool end() {
            return index >= G.g[v].size();
        }
    };
};

#endif //PLAY_WITH_ALGORITHMS_SPARSEGRAPH_H
