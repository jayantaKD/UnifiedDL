package org.infobeyondtech.unifieddl.metadatamodel.ingestion;

import java.util.List;

public class QualityMetric {
    public int id;
    public DataQualityMetric metric;
    public String formula;

    // bi-directional association 1 to 1..*
    public List<QualityMetricMeasurement> associatedQualityMetricMeasurements;
}