package org.infobeyondtech.unifieddl.metadatamodel.ingestion;

public class QualityMetricMeasurement {
    public int id;
    public String measurementName;
    public String measurementFormula;
    public float value;
    public String unit;

    // bi-directional association 0..* to 1
    public UnifiedDLDataset associatedDataset;

    // bi-directional association 0..* to 1
    public QualityMetric associatedQualityMetric;
}
