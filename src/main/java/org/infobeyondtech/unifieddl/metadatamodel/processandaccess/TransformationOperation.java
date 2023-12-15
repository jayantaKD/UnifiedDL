package org.infobeyondtech.unifieddl.metadatamodel.processandaccess;

import java.util.List;

public class TransformationOperation {
    int id;
    String description;
    String Policy;

    // bi-directional association 0..* to 1
    ETLTransformationProcess associatedETLTransformationProcess;

    // bi-directional association 0..* to 1
    TransformationFunction associatedTransformationFunction;
}
