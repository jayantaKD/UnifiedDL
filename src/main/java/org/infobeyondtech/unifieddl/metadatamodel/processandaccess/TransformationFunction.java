package org.infobeyondtech.unifieddl.metadatamodel.processandaccess;

import java.util.List;

public class TransformationFunction {
    int id;
    ETLFunction name;
    String description;

    // bi-directional association 1 to 0..*
    List<TransformationOperation> associatedTransformationOperations;
}
