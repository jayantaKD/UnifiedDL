package org.infobeyondtech.unifieddl.guimodel;

import java.util.Hashtable;

public class ProjectTreeObject extends Hashtable<String, Object> {
    ProjectTreeObjectType type;
    String label;

    public ProjectTreeObject(ProjectTreeObjectType type, String label){
        this.type = type;
        this.label = label;
    }

    public ProjectTreeObjectType getType() {
        return type;
    }

    public String getLabel() {
        return label;
    }

    public String toString(){
        return label;
    }
}