package org.infobeyondtech.unifieddl.processingzone.groupsteinertree;

public class GSTGraph {
    private EdgeNode[] edgeList;
    private int listSize;

    GSTGraph()
    {
        edgeList = null;
        listSize = 0;
    }

    public GSTGraph(int initial_size)
    {
        edgeList = new EdgeNode[initial_size];
        listSize = initial_size;
        for(int i=0; i < initial_size; i++)
        {
            edgeList[i] = null;
        }
    }

//    Graph::~Graph()
//    {
//        if(edgeList != NULL){
//            EdgeNode *n = NULL;
//            EdgeNode *ln1 = NULL;
//            for(int i = 0; i < listSize; i++)
//            {
//                n = edgeList[i];
//                while(n != NULL)
//                {
//                    ln1 = n;
//                    n = n->next;
//                    delete ln1;
//                }
//            }
//            delete [] edgeList;
//        }
//
//    }

    public boolean insertEdge(int vert1, int vert2, int edgeWeight)
    {
        int tmpNode;
        EdgeNode n1 = edgeList[vert1];
        EdgeNode n2 = edgeList[vert2];
        EdgeNode ln1 = null , ln2 = null;

        if(n1 == null)
            edgeList[vert1] = new EdgeNode(vert1, vert2, edgeWeight);
        else{
//          EdgeNode ln1 = null;
            while (n1 != null){
                if(n1.dest_vert == vert2)
                    return false;
                ln1 = n1;
                n1 = n1.next;
            }
            ln1.next = new EdgeNode(vert1, vert2, edgeWeight);
        }

        if(n2 == null)
            edgeList[vert2] = new EdgeNode(vert1, vert1, edgeWeight);
        else{
//          EdgeNode *ln2 = NULL;
            while(n2 != null){
                if(n2.dest_vert == vert1)
                    return false;
                ln2 = n2;
                n2 = n2.next;
            }
            ln2.next = new EdgeNode(vert1, vert1, edgeWeight);
        }
        return true;
    }

    int getEdgeCost(int vert1, int vert2){
        EdgeNode n = edgeList[vert1];
        if(n != null){
            while(n != null){
                if(n.dest_vert == vert2)
                    return n.weight;
                n = n.next;
            }
        }
        return -1;
    }

    boolean deleteEdge(int vert1, int vert2){

        EdgeNode n = edgeList[vert1];
        EdgeNode lagging_pntr = null;
        boolean firstEdgeFound = false;
        while(n != null)
        {
            if(n.dest_vert == vert2)
            {
                if(lagging_pntr == null)
                    edgeList[vert1] = n.next;
                else
                    lagging_pntr.next = n.next;
//                delete n;
                firstEdgeFound = true;
                break;
            }
            lagging_pntr = n;
            n = n.next;
        }
        if(firstEdgeFound == true)
        {
            n = edgeList[vert2];
            lagging_pntr = null;
            while(n != null)
            {
                if(n.dest_vert == vert1)
                {
                    if(lagging_pntr == null)
                        edgeList[vert2] = n.next;
                    else
                        lagging_pntr.next = n.next;
//                    delete n;
                    return true;
                }
                lagging_pntr = n;
                n = n.next;
            }
        }
        return false;
    }

    int getSize()
    {
        return listSize;
    }

    EdgeNode getAdjList(int vertex)
    {
        return edgeList[vertex];
    }
}