#include <Tree.h>
#include <Session.h>
#include <vector>
#include <list>
#include <stdexcept>

Tree::Tree(int rootLabel){
    this->setNode(rootLabel);
}

MaxRankTree::MaxRankTree(int rootLabel, int howDeep):Tree(rootLabel),depth(howDeep) {
//    this->depth=howDeep;
}
void Tree::addChild(const Tree& child) {
    Tree* temp = const_cast<Tree*>(&child);
    this->children.push_back(temp);
}



    Tree* Tree::createTree(const Session& session,int rootLabel) {
    Tree *tree, *t;
    int deep=0;
    //TreeType
    if(session.getTreeType()==Cycle){
        tree = new CycleTree(rootLabel,session.getCycleNum());
    }
    if(session.getTreeType()==MaxRank){
        tree = new MaxRankTree(rootLabel,deep);
    }
    if(session.getTreeType()==Root){
        tree = new RootTree(rootLabel);
    }

    Graph graph = session.getGraph();
    std::list<Tree*> queue;
    bool *visited =new bool[graph.getGraph().size()];
    for(int i=0; i < graph.getGraph().size(); i++){
        visited[i]=false;
    }
    visited[tree->getNode()]=true;
    queue.push_back(tree);
    while(!queue.empty()){
        deep++;
        Tree* temp = queue.front();
        queue.pop_front();
        visited[temp->getNode()] = true;
        for (int i = 0; i < graph.getGraph().size(); i++) {
            if (graph.getGraph()[temp->getNode()][i] == 1 && visited[i] != true) {
                if (session.getTreeType() == Cycle) {
                    t = new CycleTree(i, session.getCycleNum());
                }
                if (session.getTreeType() == MaxRank) {
                    t = new MaxRankTree(i, deep);
                }
                if (session.getTreeType() == Root) {
                    t = new RootTree(i);
                }
                queue.push_back(t);
                temp->addChild(*t);
                visited[i] = true;
//                delete t;
            }
        }

    }
    delete[] visited;
    return tree;
}

Tree::~Tree() {
    while ( children.size() > 0 ){
        Tree *t1 = children.front();
        //delete t1;
        //t1 = nullptr;
        children.erase(children.begin());
    }
}

CycleTree::CycleTree(int rootLabel, int currCycle): Tree(rootLabel){
    this->currCycle = currCycle;
};

int CycleTree::traceTree() {
    int cycleNum = currCycle-1;
    CycleTree* tree1 = this;
    while(cycleNum > 0){
        if (tree1->getChildren().size()>0){
            if(tree1->getChildren()[0]!= nullptr) {
                tree1 = dynamic_cast<CycleTree *>(tree1->getChildren()[0]);
                cycleNum--;
            }
        }
        else
            cycleNum=0;
    }
    return tree1->getNode();
}

CycleTree::~CycleTree() {

}

std::vector<MaxRankTree*> Merge(std::vector<MaxRankTree*> v1,std::vector<MaxRankTree*> v2){
    std::vector<MaxRankTree*> v;
    for(int i = 0; i < v1.size(); i++)
        v.push_back(v1[i]);
    for(int i = 0; i < v2.size(); i++)
        v.push_back(v2[i]);
    return v;
}

std::vector<MaxRankTree*> treeToArray(MaxRankTree* t,bool firstRound){
    std::vector<MaxRankTree*> v;
    if(firstRound)
        v.push_back(t);
    for(int i = 0; i < t->getChildren().size(); i++){
        v.push_back((MaxRankTree*)t->getChildren()[i]);
        v=Merge(v, treeToArray((MaxRankTree*)t->getChildren()[i],false));
    }
    return v;
}

MaxRankTree::MaxRankTree(int rootLabel): Tree(rootLabel), depth(0){
//    this->depth=0;
}

int MaxRankTree::traceTree() {
    std::cout<<"001"<<std::endl;
    std::vector<MaxRankTree*> v = treeToArray(this, true);
    int index = v[0]->getNode();
    if(v[0]->getDepth() == -1)
        throw std::invalid_argument("Depth can not be -1 here");
    std::cout<<"002"<<std::endl;
    int currDepth = v[0]->getDepth();
    int max = v[0]->getChildren().size();
    for (int i = 1; i < v.size(); ++i) {
        if(v[i]->getChildren().size() > max) {
            max = v[i]->getChildren().size();
            index = v[i]->getNode();
            currDepth = v[i]->getDepth();
        }
        else if(v[i]->getChildren().size() == max) {
            if(v[i]->getDepth() == -1)
                throw std::invalid_argument("Depth can not be -1 here");
            if (v[i]->getDepth() < currDepth){
                index = v[i]->getNode();
                currDepth = v[i]->getDepth();
            }
        }
    }
    std::cout<<"003"<<std::endl;
    std::cout<<index<<std::endl;
    return index;
}

MaxRankTree::~MaxRankTree() {

}

RootTree::RootTree(int rootLabel): Tree(rootLabel) {
}

int RootTree::traceTree(){
    return this->getNode();
}

RootTree::~RootTree() {

}
