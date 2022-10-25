#ifndef GRAPH_H_
#define GRAPH_H_
#include <iostream>
#include <vector>

class Graph{
public:

    Graph(std::vector<std::vector<int>> matrix);
    std::vector<std::vector<int>> getGraph()const{ return edges;}
    Graph() {}
    void setGraph(std::vector<std::vector<int>> graph);
    void infectNode(int nodeInd);
    bool isInfected(int nodeInd);
    void setInfectedList(bool *infectedList);
    bool *getInfectedList() const;
    //void clear();
    //virtual ~Graph();

private:
    std::vector<std::vector<int>> edges;
    bool* infectedList;
};

#endif
