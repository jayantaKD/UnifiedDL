package org.infobeyondtech.unifieddl.processingzone.groupsteinertree;

import java.util.List;

public class GroupSteinerTree {
    GSTGraph myGSTGraph;
    GroupContainer myGroups;
    Dijkstra[] dijkstrasResults;



    public GroupSteinerTree(int n, List<List<Integer>> edges, List<List<Integer>> groups){
        myGSTGraph = new GSTGraph(n);
        processEdges(edges);

        myGroups = new GroupContainer(groups.size());
        processGroups(groups);

        dijkstrasResults = new Dijkstra [n];
    }

    private void processEdges(List<List<Integer>> edges){
        for(List<Integer> edge:edges){
            myGSTGraph.insertEdge(edge.get(0), edge.get(1), edge.get(2));
        }
    }

    private void processGroups(List<List<Integer>> groups){
        int index = 0;
        for(List<Integer> group:groups){
            myGroups.insertGroup(index,new Group(group.size()));
            for(Integer gstNodeCounter:group){
                myGroups.getGroup(index).insertVertex(gstNodeCounter);
            }
            index = index + 1;
        }
    }

    public Tree executeGroupSteinerTreeAlgorithm(){
        for(int i = 0; i < this.dijkstrasResults.length; i++){
            this.dijkstrasResults[i] = null;
        }
        return algorithmOne(this.dijkstrasResults, myGSTGraph, myGroups, this.dijkstrasResults.length, myGroups.getSize());
    }

//    int readInteger()
//    {
//        char c;
//        int y = 1;
//        char[] buff = new char[10];
//        boolean done = false;
////        c = getchar();
//        while ( !isdigit(c))
//            c = getchar();
//        buff[0] = c;
//        do{
////            c = getchar();
//            if(c != ' ' && c != '\n' && c != EOF)
//            {
//                buff[y] = c;
//                y += 1;
//            }
//            else
//                done = true;
//        }while(done == false);
//        buff[y] = '\0';
//        return atoi(buff);
//    }

//    void readGroups(GroupContainer *myGroups)
//    {
//        int numGroups = myGroups->getSize();
//        for(int i = 0; i < numGroups; i++){
//            int groupSize = readInteger();
//            myGroups->insertGroup(i,new Group(groupSize));
//            for(int y = 0; y < groupSize; y++){
//                int vertex_value = readInteger();
//                myGroups->getGroup(i)->insertVertex(vertex_value);
//            }
//        }
//    }

//    void readFirstLine(int * n, int * m, int * k, int * w){
//        char buffer[256];
//        cin.getline(buffer, 256);
//    *n = atoi(strtok(buffer," "));
//    *m = atoi(strtok(NULL," "));
//    *k = atoi(strtok(NULL," "));
//    *w = atoi(strtok(NULL,"\n"));
//    }
//
//    void readEdges(int m, Graph *myGraph)
//    {
//        char buffer[256];
//        for(int i = 0; i < m; i++){
//            int e1, e2, cost;
//            cin.getline(buffer,256);
//            e1 = atoi(strtok(buffer," "));
//            e2 = atoi(strtok(NULL," "));
//            cost = atoi(strtok(NULL,"\n"));
//            myGraph->insertEdge(e1, e2, cost);
//        }
//    }


    //initiate Dijkstra Result Storage
public void initializeResults(Dijkstra[] results, int number_of_vertices)
    {
        for(int i = 0; i < number_of_vertices; i++){
            results[i] = null;
        }
    }
    //clean Dijkstra Result Storage
    void deleteResults(Dijkstra[] results, int number_of_vertices)
    {
        for(int i = 0; i < number_of_vertices; i++)
        {
//            delete results[i];
            results[i] = null;
        }
//        delete [] results;
    }
    //clean up memory
    void cleanMemory(Dijkstra[] results, GSTGraph myGSTGraph, GroupContainer myGroups, int number_of_vertices)
    {
        deleteResults(results,number_of_vertices);
//        delete myGraph;
//        delete myGroups;
    }

    public Tree algorithmOne(Dijkstra[] results, GSTGraph myGSTGraph, GroupContainer myGroups, int number_of_vertices, int number_of_groups)
    {
        //Initialize everything
        int num_groups_reached = 0;
        int root = myGroups.getGroup(0).advanceIterator();
        Tree myTree = new Tree(root, number_of_vertices);
        myGroups.setGroupReached(0);

//        printf("0\n");
//        System.out.println("0");
        while(myGroups.allReached() == false){
            int min_path = Integer.MAX_VALUE;
            int min_path_group_num = Integer.MAX_VALUE; //numeric_limits<int>::max();
            PathNode ptr2min_path = null;
            for(int y = 0; y < number_of_groups; y++){         //for every group g_i that is not reached
                if(myGroups.getGroup(y).isReached() == false){
                    PathNode local_group_min_ptr = null;
                    int local_group_min_val = 32766;
                    int local_group_num;
                    myGroups.getGroup(y).resetIterator();
                    while(myGroups.getGroup(y).isIteratorFinished() == false){ //iterate through all untouched groups
                        Dijkstra our_result = null;
                        PathNode current_path = null;
                        int current_vertex = myGroups.getGroup(y).advanceIterator();
                        if(results[current_vertex] == null){
                            results[current_vertex] = new Dijkstra(current_vertex, number_of_vertices);
                            results[current_vertex].runOpenShortestPath(myGSTGraph);
                        }
                        myTree.reset_iter();
                        our_result = results[current_vertex];
                        while(myTree.iter_not_finished()){ //iterating through all local trees
                            int nextVertex = myTree.next_vertex();
                            current_path = our_result.getPathToVertex(nextVertex);
                            if(current_path.p_cost < local_group_min_val)//
                            {
                                local_group_min_val = current_path.p_cost;
                                local_group_min_ptr = current_path;
                                local_group_num = y;
                            } else if(current_path.p_cost == local_group_min_val && current_path.is_lexicographic(local_group_min_ptr)){
                                local_group_min_ptr = current_path;
                            }
                        }
                    }
                    if(local_group_min_val < min_path) //adjust inter-group max
                    {
                        min_path_group_num = y;
                        min_path = local_group_min_val;
                        ptr2min_path = local_group_min_ptr;
                    }
                    else if(local_group_min_val == min_path && y< min_path_group_num)
                    {
                        min_path_group_num = y;
                        ptr2min_path = local_group_min_ptr;
                    }
                }
            }
            myTree.addPath(ptr2min_path);
            myGroups.setGroupReached(min_path_group_num);
//            System.out.println(""+min_path_group_num);
//            printf("%d\n",min_path_group_num); //print i in one line
        }
//        myTree.printVertices();
//        myTree.printCost();
        return  myTree;
    }

    public void algorithmTwo(Dijkstra[] results, GSTGraph myGSTGraph, GroupContainer myGroups, int number_of_vertices, int number_of_groups)
    {
        //Initialize everything
        int root = myGroups.getGroup(0).advanceIterator();
        Tree myTree = new Tree(root, number_of_vertices);
        myGroups.setGroupReached(0);
        int i  = 0;
        System.out.println("0");
//        printf("0\n");
        while(myGroups.allReached() == false){ //begin main loop
            int min_path = 32766;
            float min_d_t_ratio = Float.MIN_VALUE;//numeric_limits<float>::max(); //set ratio to max possible flt val
            int min_path_group_num;
            PathNode ptr2min_path = null;
            for(int y = 0; y < number_of_groups; y++){         //for every group g_i that is not reached
                if(myGroups.getGroup(y).isReached() == false){
                    PathNode local_group_min_ptr = null;
                    int local_group_min_val = 32766;
                    int local_group_unseen_groups_touched;
                    float local_group_d_t = Float.MAX_VALUE;//numeric_limits<float>::max();
                    myGroups.getGroup(y).resetIterator();
                    while(myGroups.getGroup(y).isIteratorFinished() == false){ //begin iterating through all nodes in unreached groups
                        Dijkstra our_result = null;
                        PathNode current_path = null;
                        int current_vertex = myGroups.getGroup(y).advanceIterator();
                        if(results[current_vertex] == null){
                            results[current_vertex] = new Dijkstra(current_vertex, number_of_vertices);
                            results[current_vertex].runOpenShortestPath(myGSTGraph);
                        }
                        myTree.reset_iter();
                        our_result = results[current_vertex];
                        while(myTree.iter_not_finished()){ //iterate through all nodes in tree
                            int nextVertex = myTree.next_vertex();
                            current_path = our_result.getPathToVertex(nextVertex);
                            float current_ratio =(float) (current_path.p_cost)/(myGroups.num_groups_touched(current_path));
                            if(current_ratio < local_group_d_t) //set min ratio
                            {
                                local_group_min_val = current_path.p_cost;
                                local_group_min_ptr = current_path;
                                local_group_unseen_groups_touched = myGroups.num_groups_touched(local_group_min_ptr);
                                local_group_d_t = current_ratio;
                            } else if(current_path.p_cost == local_group_min_val && current_path.is_lexicographic(local_group_min_ptr)){
                                local_group_min_ptr = current_path;
                            }
                        }
                    }
                    if(local_group_d_t < min_d_t_ratio ) //adjust inter-group max
                    {
                        min_path_group_num = y;
                        min_path = local_group_min_val;
                        ptr2min_path = local_group_min_ptr;
                        min_d_t_ratio = local_group_d_t;
                    }
                }
            }
            myTree.addPath(ptr2min_path); //add path to tree
            myGroups.updateReached(ptr2min_path); //add groups that are touched to tree
            myGroups.printReached();
        }
        myTree.printVertices();
        myTree.printCost();
//        delete myTree;
    }
}
