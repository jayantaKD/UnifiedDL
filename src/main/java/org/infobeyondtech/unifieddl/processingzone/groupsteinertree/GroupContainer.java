package org.infobeyondtech.unifieddl.processingzone.groupsteinertree;

public class GroupContainer {
    Group[] container;
    int next_insert;
    int size_;
    int num_touched_;
    boolean[]reached;

    //constructor
    public GroupContainer(int number_of_groups)
    {
        container = new Group[number_of_groups];
        size_ = number_of_groups;
        num_touched_ = 0;
        next_insert = 0;
        reached = null;

    }

//    GroupContainer::~GroupContainer()
//    {
//        if(container != NULL)
//        {
//            for(int i = 0; i < size_; i++)
//            {
//                if(container[i] != NULL){
//                    delete container[i];
//                    container[i] = NULL;
//                }
//            }
//            delete [] container;
//            container = NULL;
//        }
//    }

    void insertGroup(int group_num, int group_size)
    {
        container[group_num] = new Group(group_size);
    }

    public void insertGroup(int group_num, Group group){
        container[group_num] = group;
    }

    int getSize()
    {
        return size_;
    }

    public Group getGroup(int group_num)
    {
        return container[group_num];
    }

    boolean allReached()
    {
        for(int i = 0; i < size_; i++)
        {
            if(container[i].isReached() == false)
                return false;
        }
        return true;
    }


    int setGroupReached(int group_num)
    {
        container[group_num].setReached();
        return group_num;
    }

    int num_groups_touched(PathNode path)
    {
        int numberGroupsReached = 0;
        PathNode n = path;
        boolean[] GroupArray = new boolean[size_];
        int num_initially_touched = 0;
        for(int i = 0; i < size_;i++){
            if(container[i].isReached() == false)
            GroupArray[i] = false;
	else{
                num_initially_touched += 1;
                GroupArray[i] = true;
            }
        }
        int num_finally_touched = num_initially_touched;
        while(n != null){
            for(int i = 0; i < size_; i++){
                if(GroupArray[i] == false)
                    if(container[i].contains(n.vert))
                {
                    num_finally_touched += 1;
                    GroupArray[i] = true;
                }
            }
            n = n.next;
        }
//        delete GroupArray;

        return num_finally_touched-num_initially_touched;
    }
    void printReached()
    {
        if(reached != null)
        {
            for(int z = 0; z < size_; z++) {
                if (reached[z] == true)
                    System.out.println(z);
//                    printf("%d\n",z);
            }
        }
        else return;

//                delete reached;
        reached = null;
    }

    void updateReached(int vertex)
    {
        reached = new boolean[size_];
        for(int y = 0; y <size_; y++)
            reached[y] = false;

        for(int i = 0; i < size_; i++)
        {
            if(container[i].isReached() == false)
            if(container[i].contains(vertex)){
            container[i].setReached();
            num_touched_ = num_touched_+1;
            reached[i] = true;
            }
        }
    }

    void updateReached(PathNode path)
    {
        reached = new boolean[size_];
        for(int y = 0; y <size_; y++)
            reached[y] = false;
        PathNode n = path;
        while( n != null)
        {
            for(int i = 0; i < size_; i++){
                if(container[i].isReached() == false)
                if(container[i].contains(n.vert)){
                    container[i].setReached();
                    num_touched_ = num_touched_+1;
                    reached[i] = true;
                }
            }
            n = n.next;
        }
    }
}