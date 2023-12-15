package org.infobeyondtech.unifieddl.metadatamodel.ingestion;

import java.util.ArrayList;
import java.util.List;

public class RelationshipBetweenAttributes {
    int id;
    Correlation relationParameter;
    String description;

    // bi-directional association 1 to 0..*
    public List<RelationshipBTWAttMeasurement> associatedRelationshipBTWAttMeasurement =
            new ArrayList<RelationshipBTWAttMeasurement>();
}
