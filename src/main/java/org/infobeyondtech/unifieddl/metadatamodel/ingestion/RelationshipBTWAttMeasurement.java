package org.infobeyondtech.unifieddl.metadatamodel.ingestion;

import java.util.ArrayList;
import java.util.List;

public class RelationshipBTWAttMeasurement {
    int id;
    float value;

    // bi-directional association 0..* to 1
    public RelationshipBetweenAttributes associatedRelationshipBetweenAttributes;
    // bi-directional association 0..* to 1
    public Attribute associateAttribute1;
    // bi-directional association 0..* to 1
    public Attribute associateAttribute2;
}
