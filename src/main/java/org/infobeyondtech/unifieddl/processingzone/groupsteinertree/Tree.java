package org.infobeyondtech.unifieddl.processingzone.groupsteinertree;

import java.util.ArrayList;
import java.util.List;

public class Tree {
    private int[] array;
    private int tree_max_size;
    private int cost;
    private int root;
    private int tree_size;
    private int iterator;
    private int tree_current_size;
    private int last_;

    Tree(int i_root,int max_size)
    {
        array = new int[max_size];
        tree_max_size = max_size;
        cost = 0;
        root = i_root;
        for(int i = 0; i < tree_max_size; i++)
            array[i] = -1;
        array[root] = 1;
        tree_current_size = 1;
        //Last index of iterator
        iterator = -1;
        last_ = root;
    }

//    Tree::~Tree()
//    {
//        delete [] array;
//    }

    void insertEdge(int v1, int v2, int edge_cost)
    {
        int max_edge;
        if( v1 > v2)
            max_edge = v1;
        else
            max_edge = v2;

        if(max_edge > last_)
            last_ = max_edge;

        if(array[v1] < 0){
            array[v1] = 1;
            tree_current_size += 1;
        }
        if(array[v2] < 0){
            array[v2] = 1;
            tree_current_size += 1;
        }
        cost = cost + edge_cost;
    }

    void printVertices()
    {
        for(int i = 0; i <= last_; i++)
        {
            if(array[i] > 0)
                System.out.println("vertices-"+i);
//                printf("vertices-%d\n",i);
        }
    }

    public List<Integer> gstTreeVertices()
    {
        List<Integer> res = new ArrayList<Integer>();
        for(int i = 0; i <= last_; i++)
        {
            if(array[i] > 0)
                res.add(i);
//                printf("vertices-%d\n",i);
        }
        return  res;
    }

    void reset_iter()
    {
        iterator = -1;
    }

    int next_vertex()
    {
        while(iterator < tree_max_size - 1 )
        {
            iterator = iterator + 1;
            if(array[iterator] > 0)
                return iterator;
        }
        return -1;
    }

    boolean iter_not_finished()
    {
        if(iterator < tree_max_size - 1 && iterator < last_)
            return true;
        else
            return false;

    }
    void printCost()
    {
        System.out.println("cost - "+cost);
//        printf("cost - %d\n",cost);

    }
    public int getCost()
    {
        return cost;
    }

    void incrementCost(int inc_val)
    {
        cost = cost + inc_val;
    }

    void addPath(PathNode path)
    {
        PathNode n = path;
        int path_cost = path.p_cost;
        incrementCost(path_cost);

        while(n.next!= null){
            insertEdge(n.vert, n.next.vert, 0);
            n = n.next;
        }
    }
}