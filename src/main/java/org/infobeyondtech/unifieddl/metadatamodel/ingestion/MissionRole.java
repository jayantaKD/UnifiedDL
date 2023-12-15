package org.infobeyondtech.unifieddl.metadatamodel.ingestion;

import java.util.List;

public class MissionRole {

    int id;
    String description;
    MissionJobTitle jobTitle;

    // bi-directional association 0..* to 0..*
    List<MissionPersonnel> associatedMissionPersonnel;
}
