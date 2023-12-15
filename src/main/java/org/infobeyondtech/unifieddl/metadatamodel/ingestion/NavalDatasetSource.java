package org.infobeyondtech.unifieddl.metadatamodel.ingestion;

import java.util.List;

public class NavalDatasetSource extends NavalDataset {
    int battleManagementAidId;
    String battleManagementAidName;
    BMAType battleManagementAidType;
    BMAHost battleManagementAidHost;
    String bmaDescription;

    // bi-directional association 0..1 to 0..1
    NavalDatasetIngestion associatedNavalDatasetIngestion;
}
