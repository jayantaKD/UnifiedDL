package org.infobeyondtech.unifieddl.metadatamodel.ingestion;

import org.infobeyondtech.unifieddl.metadatamodel.processandaccess.UnifiedDLProcess;

import java.util.Date;
import java.util.List;

public class UnifiedDLDataset extends NavalDataset {
    String connectionURL;
    String size;
    String administrator;
    String databaseFilenameExtension;
    Date databaseCreationDate;

    // bi-directional association 0..1 to 0..1
    SensitivityMark associatedSensitivityMark;
    // bi-directional association 1 to 0..*
    List<QualityMetricMeasurement> associatedQualityMeasurements;
    // bi-directional association 1 to 0..*
    List<RelationshipBTWDsMeasurement> associatedRelationshipBTWDsMeasurements;

    // bi-directional association 0..1 to 0..1
    NavalDatasetIngestion associatedNavalDatasetIngestion;
    // bi-directional association 0..1 to 0..1
    UnifiedDLProcess associatedSourceUnifiedDLProcess;
    // bi-directional association 0..1 to 0..1
    UnifiedDLProcess associatedTargetUnifiedDLProcess;
}
