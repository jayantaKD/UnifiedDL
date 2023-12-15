package org.infobeyondtech.unifieddl.metadatamodel.ingestion;

import java.util.LinkedList;
import java.util.List;

public class UnifiedDLStructuredDataset extends UnifiedDLDataset {

    // 1..* composite aggregation relation
    List<EntityClass> hasEntityClass = new LinkedList<EntityClass>();

    public int getNbOfEntityClasses(){
        return this.hasEntityClass.size();
    }
}
