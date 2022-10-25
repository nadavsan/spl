#include <Graph.h>


Graph::Graph(std::vector<std::vector<int>> matrix) {
    std::vector<int> vec;
    for (int i = 0; i < matrix.size(); ++i) {
        vec.clear();
        for (int j = 0; j < matrix[i].size(); j++) {
            vec.push_back(matrix[i][j]);
        }
        this->edges.push_back(vec);
    }
    infectedList=new bool[edges.size()];
    for (int i = 0; i < edges.size(); ++i) {
        infectedList[i]=false;
    }
}

    void Graph::setGraph(std::vector<std::vector<int>> graph) {
    std::vector<int> v;
    this->edges.clear();

        for (int i = 0; i < graph.size(); i++) {
            v.clear();
            for (int j = 0; j < graph.size(); j++) {
                v.push_back(graph[i][j]);
            }
            this->edges.push_back(v);
        }
    }


    void Graph::infectNode(int nodeInd){
        this->infectedList[nodeInd] = true;
    };

    bool Graph::isInfected(int nodeInd){
        return this->infectedList[nodeInd];
    }

void Graph::setInfectedList(bool *infectedList) {
    Graph::infectedList = infectedList;
}
bool *Graph::getInfectedList() const {
    return infectedList;
}
