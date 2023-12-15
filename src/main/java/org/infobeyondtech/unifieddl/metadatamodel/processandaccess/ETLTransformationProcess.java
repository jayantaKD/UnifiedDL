package org.infobeyondtech.unifieddl.metadatamodel.processandaccess;

import org.infobeyondtech.unifieddl.metadatamodel.ingestion.RelationshipBTWAttMeasurement;

import java.util.List;

public class ETLTransformationProcess extends UnifiedDLProcess{
    int numberOfABACPolicies;
    int numberOfTransformationFunction;

    // bi-directional association 1 to 0..*
    List<TransformationOperation> associatedTransformationOperation;
}
