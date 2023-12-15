package org.infobeyondtech.unifieddl.metadatamodel.processandaccess;

import org.infobeyondtech.unifieddl.metadatamodel.ingestion.MissionPersonnel;
import org.infobeyondtech.unifieddl.metadatamodel.ingestion.UnifiedDLDataset;

import java.util.Date;
import java.util.List;

public abstract class UnifiedDLProcess {

    public int id;
    public String name;
    public Date creationTime;
    public Date lastModifTime;
    public String description;
    public Date executionDate;
    public String executionOutputLog;
    public String executionErrorLog;
    public String executionComment;

    // bi-directional association 0..1 to 0..1
    UnifiedDLDataset associatedSourceDataset;
    // bi-directional association 0..1 to 0..1
    UnifiedDLDataset associatedTargetDataset;
    // bi-directional association 0..* to 1
    MissionPersonnel associatedMissionPersonnel;
}
