all: cTrace

cTrace: ./bin/Agent.o ./bin/main.o ./bin/Session.o ./bin/Graph.o ./bin/Tree.o ./bin/cTrace

./bin/Agent.o: ./src/Agent.cpp ./include/Agent.h
	echo compiling...
	g++ -g -Wall -Weffc++ -std=c++11 -I ./include -c ./src/Agent.cpp -o ./bin/Agent.o

./bin/Session.o: ./src/Session.cpp ./include/Session.h
	echo compiling...
	# g++ -g -Wall -Weffc++ -std=c++11 -I ./include -I ../json_test/json/include -c ./src/Session.cpp -o ./bin/Session.o
	g++ -g -Wall -Weffc++ -std=c++11 -I ./include -I ./include/json-3.7.0 -c ./src/Session.cpp -o ./bin/Session.o

./bin/Graph.o: ./src/Graph.cpp ./include/Graph.h
	echo compiling...
	g++ -g -Wall -Weffc++ -std=c++11 -I ./include -c ./src/Graph.cpp -o ./bin/Graph.o

./bin/Tree.o: ./src/Tree.cpp ./include/Tree.h
	echo compiling...
	g++ -g -Wall -Weffc++ -std=c++11 -I ./include -c ./src/Tree.cpp -o ./bin/Tree.o

./bin/main.o: ./src/main.cpp
	echo compiling...
	g++ -g -Wall -Weffc++ -std=c++11 -I ./include -c ./src/main.cpp -o ./bin/main.o

./bin/cTrace: ./bin/Agent.o ./bin/Session.o ./bin/Tree.o ./bin/main.o
	g++ ./bin/Agent.o ./bin/Session.o ./bin/Graph.o ./bin/Tree.o ./bin/main.o -o ./bin/cTrace

clean:
	rm -f ./bin/Agent.o
	rm -f ./bin/main.o
	rm -f ./bin/Session.o 
	rm -f ./bin/Graph.o
	rm -f ./bin/Tree.o 
	rm -f ./bin/cTrace
