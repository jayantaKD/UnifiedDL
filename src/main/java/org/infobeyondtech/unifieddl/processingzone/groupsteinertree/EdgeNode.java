package org.infobeyondtech.unifieddl.processingzone.groupsteinertree;

public class EdgeNode {

    public int dest_vert;
    public int weight;
    EdgeNode next;

    public int current_vert;

    EdgeNode(int c_vert, int d_vert, int edge_weight)
    {
        dest_vert = d_vert;
        weight = edge_weight;
        next = null;
        current_vert = c_vert;
    }

}
