package org.infobeyondtech.unifieddl.processingzone.groupsteinertree;

public class Group {

    private int[] verts;
    boolean visited;
    int size_;
    int iterator_;
    int current_insert_pos;

    public Group(int group_size)
    {
        verts = new int[group_size];
        size_ = group_size;
        visited = false;
        iterator_ = -1;
        current_insert_pos = 0;
    }


    /*Group::~Group()
    {
        if(verts != NULL){
            delete[] verts;
            verts = NULL;
        }
    }*/

    //used for inserting vertexs into groups
    public void insertVertex(int new_vert)
    {
        if(current_insert_pos < size_){
            verts[current_insert_pos] = new_vert;
            current_insert_pos = current_insert_pos + 1;
        }
    }

    //iterator functions
    void resetIterator(){
        iterator_ = -1;
    }

    boolean isIteratorFinished()
    {
        if(iterator_ < current_insert_pos-1)
            return false;
        return true;
    }

    int advanceIterator()
    {
        if(iterator_ < (size_ - 1))
        {
            iterator_ = iterator_ + 1;
            return verts[iterator_];
        }
        return -1;
    }
//end iterator functions

//accessor methods

    int getGroupSize()
    {
        return size_;
    }

    boolean isReached()
    {
        return visited;
    }

    void setReached()
    {
        visited = true;
    }
    //check whether a group contains a vertex
    boolean contains(int vertex)
    {
        for(int i = 0; i < size_; i++)
        {
            if(verts[i] == vertex)
                return true;
        }
        return false;
    }
}