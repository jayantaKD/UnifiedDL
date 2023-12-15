package org.infobeyondtech.unifieddl.metadatamodel.ingestion;

import java.util.List;

public class RelationshipBetweenDataset {
    public int id;
    public DatasetRelationship relationship;
    public String description;

    // bi-directional association 1 to 0..*
    public List<RelationshipBTWAttMeasurement> associatedRelationshipBTWAttMeasurement;
}
