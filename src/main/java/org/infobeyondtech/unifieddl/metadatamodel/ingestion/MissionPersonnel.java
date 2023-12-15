package org.infobeyondtech.unifieddl.metadatamodel.ingestion;

import org.infobeyondtech.unifieddl.metadatamodel.processandaccess.UnifiedDLAccess;

import java.util.List;

public class MissionPersonnel {
    int id;
    String lastName;
    String firstName;
    NavalRank rank;
    ClearanceLevel clearance;
    int jobExperience;
    String contactInformation;


    // bi-directional association 0..* to 0..*
    List<MissionRole> associatedMissionRole;
    // bi-directional association 1 to 0..*
    List<NavalDatasetIngestion> associatedNavalDatasetIngestion;
    // bi-directional association 1 to 0..*
    List<UnifiedDLAccess> associatedUnifiedDLAccess;
}
