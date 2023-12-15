package org.infobeyondtech.unifieddl.metadatamodel.ingestion;

import java.util.ArrayList;
import java.util.List;

public class SensitivityLevel {
    SensitivityLevelText level;
    String description;

    // bi-directional association 1 to 0..n
    List<SensitivityMark> associatedSensitivityMarks = new ArrayList<SensitivityMark>();
}