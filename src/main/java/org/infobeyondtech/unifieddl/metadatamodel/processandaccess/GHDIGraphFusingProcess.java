package org.infobeyondtech.unifieddl.metadatamodel.processandaccess;

import java.util.List;

public class GHDIGraphFusingProcess extends UnifiedDLProcess{

    int numberOfMatchedEntities;
    int getNumberOfBridges;

    // bi-directional association 0..* to 0..1
    DataMappingOperation associatedDataMappingOperation;

}
