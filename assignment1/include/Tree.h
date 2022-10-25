#ifndef TREE_H_
#define TREE_H_

#include <vector>
#include <Session.h>
class Session;

class Tree{
public:
    Tree(int rootLabel);
    void setNode(int node1){this->node=node1;}
    int getNode(){return node;}
    void addChild(const Tree& child);
    static Tree* createTree(const Session& session, int rootLabel);
    virtual int traceTree()=0;
    std::vector<Tree*> getChildren(){return children;}

    virtual ~Tree();

private:
    int node;
    std::vector<Tree*> children;
};

class CycleTree: public Tree{
public:
    virtual ~CycleTree();

    CycleTree(int rootLabel, int currCycle);
    virtual int traceTree();
private:
    int currCycle;
};

class MaxRankTree: public Tree{
public:
    virtual ~MaxRankTree();

    MaxRankTree(int rootLabel);
    MaxRankTree(int rootLabel, int howDeep);
    virtual int traceTree();
    int getDepth(){return depth;}

private:
    int depth;
};

class RootTree: public Tree{
public:
    virtual ~RootTree();

    RootTree(int rootLabel);
    virtual int traceTree();
};

#endif
