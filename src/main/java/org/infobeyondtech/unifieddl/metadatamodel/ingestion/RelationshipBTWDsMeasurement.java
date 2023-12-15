package org.infobeyondtech.unifieddl.metadatamodel.ingestion;

public class RelationshipBTWDsMeasurement {
    public int id;
    public float value;
    public boolean conditionalValue;

    // bi-directional association 1 to 0..*
    public RelationshipBetweenDataset associatedRelationshipBetweenDataset;
    // bi-directional association 0..* to 1
    public UnifiedDLDataset associatedDataset1;
    // bi-directional association 0..* to 1
    public UnifiedDLDataset associatedDataset2;
}
