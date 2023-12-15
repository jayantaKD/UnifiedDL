package org.infobeyondtech.unifieddl.metadatamodel.ingestion;

import java.util.List;

public class QualityDimension {
    public int id;
    public DataQualityDimension dimension;
    public String description;

    // aggregation association 1..*
    public  List<QualityMetric> associatedQualityMetrics;
}
