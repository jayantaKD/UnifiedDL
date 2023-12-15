package org.infobeyondtech.unifieddl.metadatamodel.processandaccess;

import org.infobeyondtech.unifieddl.metadatamodel.ingestion.MissionPersonnel;
import org.infobeyondtech.unifieddl.metadatamodel.ingestion.UnifiedDLDataset;

import java.util.Date;

public class UnifiedDLAccess {

    Date accessTime;
    String objective;
    String comment;
    String[] accessKeywordQuery;
    String accessSPARQLSubQuery;

    // bi-directional association 0..* to 1
    MissionPersonnel associatedMissionPersonnel;
    // bi-directional association 0..1 to 0..1
    UnifiedDLDataset associatedUnifiedDLDataset;
}
