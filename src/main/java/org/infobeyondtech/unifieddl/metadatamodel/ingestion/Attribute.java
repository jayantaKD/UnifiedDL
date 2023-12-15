package org.infobeyondtech.unifieddl.metadatamodel.ingestion;
import org.infobeyondtech.unifieddl.metadatamodel.processandaccess.ETLFunction;
import java.util.List;

abstract class Attribute {
    public int id;
    public String name;
    public AttributeType attributeType;
    public boolean isETLTransformationApplied;
    public ETLFunction trasformationFunction;
    int missingValuesCount;
    float entropy;
    int distinct;
    float averageClassCount;
    int mostFrequentClassCount;
    int leastFrequentClassCount;
    int modeClassCount;
    int medianClassCount;

    // bi-directional association 0..1 to 0..1
    SensitivityMark associatedSensitivityMarkAttr;
    // bi-directional association 1 to 0..*
    List<RelationshipBTWAttMeasurement> associatedRelationshipBTWAttMeasurements;
}