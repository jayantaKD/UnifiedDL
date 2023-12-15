package org.infobeyondtech.unifieddl.metadatamodel.processandaccess;

import java.util.ArrayList;
import java.util.List;

public class DataMappingOperation {
    int id;
    String description;
    CanonicalMappingAlg mappingAlg;

    // bi-directional association 0..1 to 0..*
    List<GHDIGraphFusingProcess> associatedGHDIGraphFusingProcess = new ArrayList<GHDIGraphFusingProcess>();
}