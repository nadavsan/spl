#ifndef SESSION_H_
#define SESSION_H_

#include <vector>
#include <string>
#include <Graph.h>
#include <Agent.h>
#include <fstream>
#include <json.hpp>
#include <iostream>
#include <Tree.h>
#include <list>

using json = nlohmann::json;
class Agent;


enum TreeType{
  Cycle,
  MaxRank,
  Root
};

class Session{
public:
    Session(const std::string& path);
    
    void simulate();
    void addAgent(const Agent& agent);
    void setGraph(const Graph& graph);
    Graph getGraph()const {return g;}//const before the function is for guarantee you dont change the vars of the class
    void enqueueInfected(int);
    int dequeueInfected();
    TreeType getTreeType() const;
    int getCycleNum() const ;

    void setQinfVisited(int toAdd);
    const std::list<int> &getQinfVisited() const;

    const std::list<int> &getQinf() const;

    //virtual ~Session();

private:
    std::list<int> QinfVisited;
    std::list<int> Qinf;
    int cycleNum;
    Graph g;
    TreeType treeType;
    std::vector<Agent*> agents;
    void output();
};

#endif
