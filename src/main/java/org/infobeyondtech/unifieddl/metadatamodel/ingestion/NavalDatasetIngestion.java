package org.infobeyondtech.unifieddl.metadatamodel.ingestion;

public class NavalDatasetIngestion {
    DataIngestionMode ingestionMode;
    String ingestionStartTime;
    String ingestionEndTime;
    String ingestionMethodName;
    String ingestionSourceCodeUrl;
    String ingestionEnvironment;
    String ingestionResult;
    String ingestionOutputLog;
    String ingestionErrorLog;
    String ingestionComment;

    // bi-directional association 0..* to 1
    MissionPersonnel associatedMissionPersonnel;
    // bi-directional association 0..1 to 0..1
    UnifiedDLDataset associatedUnifiedDLDataset;
    // bi-directional association 0..1 to 0..1
    NavalDatasetSource associatedNavalDatasetSource;
}
