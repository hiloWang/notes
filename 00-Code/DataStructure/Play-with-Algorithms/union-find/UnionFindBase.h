/*
 ============================================================================
 
 Author      : Ztiany
 Description : 并查集抽象

 ============================================================================
 */

#ifndef PLAY_WITH_ALGORITHMS_UNIONFINDBASE_H
#define PLAY_WITH_ALGORITHMS_UNIONFINDBASE_H

class UnionFindBase {
public:
    virtual int find(int p) = 0;

    virtual bool isConnected(int p, int q) = 0;

    virtual void unionElements(int p, int q) = 0;
};

#endif //PLAY_WITH_ALGORITHMS_UNIONFINDBASE_H
