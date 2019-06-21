/*
 ============================================================================
 
 Author      : Ztiany
 Description : 从文件中加载图

 ============================================================================
 */


#ifndef PLAY_WITH_ALGORITHMS_READGRAPH_H
#define PLAY_WITH_ALGORITHMS_READGRAPH_H


#include <iostream>
#include <string>
#include <fstream>
#include <sstream>
#include <cassert>

using namespace std;

// 读取图算法
template<typename Graph>
class ReadGraph {

public:
    // 从文件filename中读取图的信息, 存储进图graph中
    ReadGraph(Graph &graph, const string &filename) {
        ifstream file(filename);

        string line;
        int V, E;
        // 第一行读取图中的节点个数和边的个数
        assert(getline(file, line));
        //读到字符流中
        stringstream ss(line);
        ss >> V >> E;
        assert(V == graph.V());

        // 读取每一条边的信息
        for (int i = 0; i < E; i++) {
            assert(getline(file, line));
            stringstream ssM(line);

            int a, b;
            ssM >> a >> b;
            assert(a >= 0 && a < V);
            assert(b >= 0 && b < V);
            graph.addEdge(a, b);
        }

    }

};

#endif //PLAY_WITH_ALGORITHMS_READGRAPH_H
