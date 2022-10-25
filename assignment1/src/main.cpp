#include <iostream>
#include <Session.h>

using namespace std;

int tree_exp1();

int tree_exp2();

int tree_exp3();

int main(int argc, char** argv){
//    if(argc != 2){
//        cout << "usage ctrace <config_path>" << endl;
//        return 0;
//    }

    //Session ses("../include/config1.json");
    //ses.simulate();

//    Session sess(argv[1]);
//    sess.simulate();
//    return 0;
    int k = tree_exp1();
    cout<<"0"<<endl;
    std::cout<<k<<std::endl;
    cout<<"1"<<endl;
    k = tree_exp2();
    std::cout<<k<<std::endl;
    cout<<"2"<<endl;
    k = tree_exp3();
    std::cout<<k<<std::endl;
    return 0;

}

int tree_exp3() {
    CycleTree t(1,1);
    t.addChild(CycleTree(2,1));
    t.addChild(CycleTree(3,1));
    t.addChild(CycleTree(4,1));
    return t.traceTree();
}

int tree_exp2() {
    RootTree t(1);
    t.addChild(RootTree(2));
    t.addChild(RootTree(3));
    t.addChild(RootTree(4));
    return t.traceTree();
}

int tree_exp1() {
    cout<<"01"<<endl;
    MaxRankTree t(1);
    cout<<"02"<<endl;
    MaxRankTree t2(2);
    cout<<"03"<<endl;
    t2.addChild(MaxRankTree(3));
    t2.addChild(MaxRankTree(4));
    t2.addChild(MaxRankTree(5));
    t.addChild(t2);
    t.addChild(MaxRankTree(6));
    cout<<"04"<<endl;
    int n = t.traceTree();
    //cout<< "132" << endl;
    return n;
}

