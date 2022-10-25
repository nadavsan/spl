#include <Session.h>
#include <vector>
#include <list>

Session::Session(const std::string& path){
    std::vector<std::vector<int>> toGraph;
    cycleNum = 0;
    //reading json file
    std::ifstream json_file(path);
    json read_json;
    json_file >> read_json;
    //TreeType
    if(read_json["tree"]=="M"){
        this->treeType=MaxRank;
    }
    else if(read_json["tree"]=="C") {
        this->treeType = Cycle;
    } else if(read_json["tree"]=="R") {
        this->treeType=Root;
    }
    //Graph creation
    //Graph
    for(int i =0;i<read_json["graph"].size();i++) {
        std::vector<int> v;
        for (int j = 0; j < read_json["graph"][i].size(); ++j) {
            v.push_back(read_json["graph"][i][j]);
        }
        toGraph.push_back(v);
    }
    this->g.setGraph(toGraph);
    //InfectedList
    bool* infectedList=new bool[g.getGraph().size()];
    for (int i = 0; i < g.getGraph().size(); ++i) {
        infectedList[i] = false;
    }
    this->g.setInfectedList(infectedList);
    //Agents Creation
    for(int i=0;i<read_json["agents"].size();i++){
        if(read_json["agents"][i][0]=="V"){
            this->agents.push_back(new Virus(read_json["agents"][i][1]));
            this->getGraph().infectNode(read_json["agents"][i][1]);
      //      this->enqueueInfected(read_json["agents"][i][1]);   /// needed to change

        }else if (read_json["agents"][i][0]=="C"){

            this->agents.push_back(new ContactTracer());
        }
    }
}

void Session::addAgent(const Agent& agent){
    Agent* temp = const_cast<Agent*>(&agent);
    this->agents.push_back(temp);
}

void Session::setGraph(const Graph& graph){
    std::vector<std::vector<int>> temp;
    std::vector<int> v;
    for (int i = 0; i < graph.getGraph().size(); i++) {
        v.clear();
        for (int j = 0; j < graph.getGraph().size(); j++) {
            v.push_back(graph.getGraph()[i][j]);
        }
        temp.push_back(v);
    }
    this->g.setGraph(temp);
}



void Session::simulate() {
    bool finish = false;
    this->cycleNum=0;
    while (!finish) {

        this->cycleNum++;
        int temp = agents.size();
        for (int i = 0; i < temp; ++i) {
            agents[i]->act(*this);
        }
        if(temp==agents.size()){
            finish = true;
        }
    }
    output();
    //delete this;
}

int Session::dequeueInfected() {
    int i =this->Qinf.front();
    this->Qinf.pop_front();
    return i;
}

void Session::enqueueInfected(int inf) {
    this->Qinf.push_back(inf);
}

TreeType Session::getTreeType() const {
    return treeType;
}

int Session::getCycleNum() const {
    return cycleNum;
}

const std::list<int> &Session::getQinfVisited() const {
    return QinfVisited;
}

void Session::setQinfVisited(int toAdd) {
    this->QinfVisited.push_back(toAdd);
}

const std::list<int> &Session::getQinf() const {
    return Qinf;
}

void Session::output(){
    std::vector<int> v1;
    json j;
    for (int i = 0; i < g.getGraph().size(); ++i) {
        if(g.isInfected(i))
            v1.push_back(i);
    }
    j["graph"] = g.getGraph();
    j["infected"] = v1;

    std::string out = j.dump();
    std::ofstream outfile ("output.json");
    outfile << out << std::endl;
    outfile.close();
};

/*Session::~Session() {
    //this->QinfVisited.clear();
    /*this->Qinf.clear();
    g.clear();
    for (int i = 0; i < this->agents.size(); ++i) {
        delete agents[i];
    }
    agents.clear();
}*/

