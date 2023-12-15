package org.infobeyondtech.unifieddl.processingzone.groupsteinertree;

public class PathNode {

    public int vert;
    public PathNode next;
    public int p_len;
    public int p_cost;
    public boolean seen;

    PathNode(int vertex){
        vert = vertex;
        next = null;
        seen = false;
        p_len = 0;
        p_cost = 0;
    }

    PathNode(int vertex, int edge_cost, PathNode vPtr){
        vert = vertex;
        next = vPtr;
        seen = false;
        if(next == null){
            p_cost = 0;
            p_len = 0;
        }
        else{
            p_cost = next.p_cost + edge_cost;
            p_len = next.p_len + 1;
        }
    }



    boolean is_lexicographic(PathNode node2){
        PathNode n1 = this;
        PathNode n2 = node2;

//        System.out.println(n1 + "<------>" + n2);
//        System.out.println(n1 + "<------>"+ (n1!=null?n1.vert:null));
//        System.out.println(n2 + "<------>"+ (n2!=null?n2.vert:null));
//        System.out.println("------------------------------------");

        while(n1 != null && n2 != null)
        {
            if(n1.vert < n2.vert)
                return true;
            else if (n1.vert == n2.vert)
            {
                n1 = n1.next;
                 n2 = n2.next;
            }
            else if(n1.vert > n2.vert)
                return false;
        }

        if(n1 == null && n2 == null)
            return false;
        else if(n1 != null)
            return false;
        else if(n2 != null)
            return true;
        else
            return false;
    }

    void printPath()
    {
        System.out.println();
        System.out.println("PathCost: "+p_cost+" Path_Len: "+p_len);
        PathNode n = this;
        while (n != null)
        {
            System.out.println(n.vert);
//            printf("%d -> ",n->vert);
            n = n.next;
        }
//        printf("\n");
    }
}
