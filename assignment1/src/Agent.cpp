#include <Agent.h>

Agent::Agent(){}


ContactTracer::ContactTracer():Agent() {}

void ContactTracer::act(Session &session) {
    Tree* t ;
    if(!session.getQinf().empty()){
      int HeadNode(session.dequeueInfected());
      Tree* h= t->createTree(session,HeadNode);
      int toRemove = h->traceTree();
      std::vector<std::vector<int>> mat;
      std::vector<int> v;
      int sizee= session.getGraph().getGraph().size();

      for (int i = 0; i < sizee; i++) {
          v.clear();
          for (int j = 0; j < sizee; j++) {
              v.push_back(session.getGraph().getGraph()[i][j]);
          }
          mat.push_back(v);
      }


    //detaches a node
      for (int i = 0; i < mat.size(); ++i) {
          mat[i][toRemove]=0;
          mat[toRemove][i]=0;
      }


        Graph G(mat);
        session.setGraph(G);
        session.setQinfVisited(HeadNode);
        delete h;
    }
}

Virus::Virus(int nodeInd): Agent(),nodeInd(nodeInd) {
}

void Virus::act(Session &session) {
    bool toAdd= std::find(session.getQinfVisited().begin(),session.getQinfVisited().end(), this->getNodeInd()) != session.getQinfVisited().end();//check we dont add virus to queue again

    if (!toAdd) {
        session.enqueueInfected(this->getNodeInd());
    }

    Graph gr = session.getGraph();
    bool infect= false;
    for (int i =0 ; i < gr.getGraph().size()  && !infect; i++){
        if (gr.getGraph()[this->nodeInd][i]==1 && !gr.isInfected(i)){
            infect=true;
            Virus* vir = new Virus(i);
            session.getGraph().infectNode(i);
            session.addAgent(*vir);

        }
    }
}

const int Virus::getNodeInd() const {
    return nodeInd;
}
